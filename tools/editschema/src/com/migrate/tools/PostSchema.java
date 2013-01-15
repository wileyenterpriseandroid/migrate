package com.migrate.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.migrate.storage.impl.JsonHelper;
import com.migrate.webdata.model.PersistentSchema;
import com.migrate.webdata.model.PropertyIndex;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Takes a jar and a class, then generates json-schema, and puts it in a webdata
 * service instance.  The class should implement APISource and return an array
 * containing the APIs for which this tool will put schema.
 */
public class PostSchema {
    protected static org.apache.log4j.Logger log = Logger.getLogger(PostSchema.class);

    // TODO: needs to be an argument as well...
    protected static final String URL = "http://localhost:8080/migrate/schema/{type}";

    protected static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws Exception {
        if (!"-cp".equals(args[0]) || (args.length < 2)) {
            usage();
        }

        String cp = args[1];
        String[] jarElts = cp.split(String.valueOf(File.pathSeparatorChar));
        ArrayList jarUrls = new ArrayList();
        for (String elt: jarElts) {
            URL jarURL = new File(elt).toURL();
            jarUrls.add(jarURL);
        }

        URL[] urls = (URL[]) jarUrls.toArray(new URL[] {});

        URLClassLoader apiLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        String classes = args[2];
        String[] classElts = classes.split(",");
        for (String cl: classElts) {
            if (!"".equals(cl.trim())) {
                postSchema4Class(Class.forName(cl, true, apiLoader));
            }
        }
    }

    private static void usage() {
        System.err.println("Usage: -cp <classPath> <className>,[className]...");
        System.exit(1);
    }

	private static String getJsonSchema(Class cl) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonSchema schema = mapper.generateJsonSchema(cl);

        // TODO: wish I could use jackson JsonSerializableSchema annotation here, but that would require jackson on android side
        // Oh, and this actually is a "standard" json schema attribute that
        // you use as a unique identifier for the schema - in our case, the
        // class name, which we can use as the table name.
        schema.getSchemaNode().put("id", cl.getName());

		return schema.toString();
	}

    private static void postSchema4Class(Class apiClass) throws IOException {
        String type = apiClass.getName();

        String jsonSchemaStr = getJsonSchema(apiClass);
        PersistentSchema persistentSchema = new PersistentSchema();

        TypeReference<HashMap<String,Object>> typeRef  = new TypeReference<HashMap<String,Object> >() {};
        Map<String, Object> jsonSchema =
                JsonHelper.readValue(jsonSchemaStr.getBytes(), typeRef);

        persistentSchema.setJsonSchema(jsonSchema);

        // do this with annotations
        persistentSchema.setIndexList(new ArrayList<PropertyIndex>());
//        schema.setIndexList(createIndexList());

//        String json = new String(JsonHelper.writeValueAsByte(persistentSchema));

        HttpHeaders header = new HttpHeaders();
        header.add("content-type", "application/json");
        String postUrl = URL;
        HttpEntity<PersistentSchema> requestEntity = new HttpEntity<PersistentSchema>(persistentSchema, header);

        ResponseEntity<String> response = restTemplate.exchange(postUrl, HttpMethod.POST,
                requestEntity, String.class, type);
        log.info(response.getBody());
    }
}
