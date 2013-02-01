package com.migrate.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrate.api.annotations.WebdataSchema;
import com.migrate.webdata.model.PersistentSchema;
import com.migrate.webdata.model.PropertyIndex;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Takes a jar and a class, then generates json-schema, and puts it in a webdata
 * service instance.  The class should implement APISource and return an array
 * containing the APIs for which this tool will put schema.
 */
public class PostSchema {
    protected static org.apache.log4j.Logger log = Logger.getLogger(PostSchema.class);
    protected static RestTemplate restTemplate = new RestTemplate();

    private static final String CP = "cp";
    private static final String DEST = "d";
    private static final String MIGRATE_URL = "migrateurl";
    private static final String API_CLASSES = "api";
    private static final String OUTPUT_DIR = "api";

    // TODO: constants that should live somewhere else
    private static final String TYPE = "type";
    private static final String OBJECT = "object";
    private static final String SCHEMA_ID = "id";
    private static final String SCHEMA_BUCKET = "__schema";
    public static final String GET = "get";
    public static final String STRING = "string";
    public static final String NUMBER = "number";
    public static final String INTEGER = "integer";

    public static final String UNIQUE = "unique";
    public static final String INDEX = "index";

    public static void main(String[] args) throws Exception {
        Map<String, String> argMap = parseArgs(args);

        String cp = argMap.get(CP);
        String dest = argMap.get(DEST);
        String migrateURL = argMap.get(MIGRATE_URL);
        String apis = argMap.get(API_CLASSES);

        if ((cp == null) || (migrateURL == null) || (apis == null) || (dest == null)) {
            usage();
            // does not happen
            return;
        }

        String[] classes = cp.split(String.valueOf(File.pathSeparatorChar));
        ArrayList<URL> jarUrls = new ArrayList<URL>();
        for (String elt: classes) {
            URL jarURL = new File(elt).toURL();
            jarUrls.add(jarURL);
        }

        URL[] urls = jarUrls.toArray(new URL[] {});

        URLClassLoader apiLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        createSchemasAndBuildContracts(apis, apiLoader);
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> argMap = new HashMap<String, String>();

        Iterator<String> argIter = Arrays.asList(args).iterator();
        while (argIter.hasNext()) {
            String arg = argIter.next().toLowerCase();
            if (arg.startsWith("-")) {
                if (argIter.hasNext()) {
                    String value = argIter.next();
                    // strip the '-'
                    argMap.put(arg.substring(1), value);
                } else {
                    usage();
                }
            }
        }
        return argMap;
    }

    private static void createSchemasAndBuildContracts(String api, URLClassLoader apiLoader)
            throws ClassNotFoundException, IOException, MalformedSchemaDeclarationException
    {
        String[] classElts = api.split(",");
        for (String clName : classElts) {
            clName = clName.trim();
            if (!"".equals(clName.trim())) {
                Class cl = Class.forName(clName, true, apiLoader);

                PersistentSchema schema = readSchemaBuildContract(cl);

                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(System.out, schema);
            }
        }
    }

    private static PersistentSchema readSchemaBuildContract(Class apiClass)
            throws IOException, MalformedSchemaDeclarationException
    {
        Map<String, Object> jsonSchema = new HashMap<String, Object>();
        jsonSchema.put(TYPE, OBJECT);
        jsonSchema.put(SCHEMA_ID, apiClass.getName());

        ContractBuilder contractBuilder = new ContractBuilder();
        String version = readVersion(apiClass, contractBuilder);

        PersistentSchema persistentSchema = new PersistentSchema();
        persistentSchema.setVersion(Long.valueOf(version));
        persistentSchema.setUpdateTime(System.currentTimeMillis());
        persistentSchema.setBucket(SCHEMA_BUCKET);

        Map<String, Object> properties = getProperties(apiClass, contractBuilder);
        jsonSchema.put("properties", properties);

        persistentSchema.setJsonSchema(jsonSchema);

        contractBuilder.end();
        System.out.println(contractBuilder.build());

        // do this with annotations
        List<PropertyIndex> indexList = creatIndexList();
        persistentSchema.setIndexList(indexList);

        return persistentSchema;
    }

    private static String readVersion(Class apiClass, ContractBuilder apiBuilder)
            throws MalformedSchemaDeclarationException
    {
        WebdataSchema schemaAnnotation = (WebdataSchema)
                apiClass.getAnnotation(WebdataSchema.class);
        if (schemaAnnotation == null) {
            throw new MalformedSchemaDeclarationException("Missing annotation: " +
                    WebdataSchema.class.getName());
        }
        String version = schemaAnnotation.version();
        if (version == null) {
            throw new MalformedSchemaDeclarationException("Missing schema version");
        }

        apiBuilder.start(apiClass, version);

        return version;
    }

    private static List<PropertyIndex> creatIndexList() {
        return null;
    }

    public static Map<String, Object> getProperties(Class apiClass, ContractBuilder apiBuilder) {

        Map<String, Object> properties = new HashMap<String, Object>();
        Method[] methods = apiClass.getDeclaredMethods();
        for (Method m : methods) {
            String mName = m.getName();
            String propertyName = propertyName(mName);

            if (propertyName != null) {

                Map<String, String> typeMap = new HashMap<String, String>();

                Class returnType = m.getReturnType();
                if (String.class.isAssignableFrom(returnType)) {
                    typeMap.put(TYPE, "string");
                } else if (int.class.isAssignableFrom(returnType)) {
                    typeMap.put(TYPE, "integer");
                } else if (Integer.class.isAssignableFrom(returnType)) {
                    typeMap.put(TYPE, "integer");
                } else if (Long.class.isAssignableFrom(returnType)) {
                    typeMap.put(TYPE, "number");
                } else if (Float.class.isAssignableFrom(returnType)) {
                    typeMap.put(TYPE, "number");
                } else if (Date.class.isAssignableFrom(returnType)) {
                    typeMap.put(TYPE, "number");
                } else if (Double.class.isAssignableFrom(returnType)) {
                    typeMap.put(TYPE, "number");
                } else {
                    throw new IllegalArgumentException("Unsupported property type: " +
                            propertyName + ": " + returnType.getName());
                }

//                Unique unique = m.getAnnotation(Unique.class);
//                if (unique != null) {
//                    typeMap.put(UNIQUE, "true");
//                }
//
//                Index index = m.getAnnotation(Index.class);
//                if (unique != null) {
//                    String indexType = index.type();
//                    typeMap.put(INDEX, indexType);
//                }

                properties.put(propertyName, typeMap);

                apiBuilder.addProperty(propertyName);
            }
        }
        return properties;
    }

    private static String propertyName(String mName) {
        if (mName.startsWith(GET)) {
            String name = mName.substring(GET.length());
            return String.valueOf(name.charAt(0)).toLowerCase() + name.substring(1);
        }
        return null;
    }

    private static void postSchema(PersistentSchema persistentSchema, Class apiClass, String dest)
            throws IOException,
            MalformedSchemaDeclarationException
    {
        HttpHeaders header = new HttpHeaders();
        header.add("content-type", "application/json");

        String migrateURL = dest + "/schema/{schemaId}";

        HttpEntity<PersistentSchema> requestEntity =
                new HttpEntity<PersistentSchema>(persistentSchema, header);

        String schemaId = apiClass.getName();
        ResponseEntity<String> response = restTemplate.exchange(migrateURL, HttpMethod.POST,
                requestEntity, String.class, schemaId);
        log.info(response.getBody());
    }

    private static void usage() {
        System.err.println("Usage: -cp <classPath> -dest <http://host:port/migratecontext> -api <className>,[className]...");
        System.exit(1);
    }
}
