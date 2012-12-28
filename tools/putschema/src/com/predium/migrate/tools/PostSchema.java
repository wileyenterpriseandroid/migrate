package com.migrate.migrate.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.migrate.dataModel.PropertyIndex;
import com.migrate.dataModel.Schema;
import com.migrate.util.JsonHelper;
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
    protected static final String URL = "http://localhost:8080/WebData/schema/{type}";

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
		return schema.toString();
	}

    private static void postSchema4Class(Class apiClass) throws IOException {
        String type = apiClass.getName();

        String jsonSchemaStr = getJsonSchema(apiClass);
        Schema schema = new Schema();
        TypeReference<HashMap<String,Object>> typeRef  = new TypeReference<HashMap<String,Object> >() {};
        Map<String, Object> jsonSchema =
                JsonHelper.readValue(jsonSchemaStr.getBytes(), typeRef);
        schema.setJsonSchema(jsonSchema);

        // do this with annotations
        schema.setIndexList(new ArrayList<PropertyIndex>());
//        schema.setIndexList(createIndexList());

        HttpHeaders header = new HttpHeaders();
        header.add("content-type", "application/json");
        String postUrl = URL;
        HttpEntity<Schema> requestEntity = new HttpEntity<Schema>(schema, header);

        ResponseEntity<String> response = restTemplate.exchange(postUrl, HttpMethod.POST,
                requestEntity, String.class, type);
        log.info(response.getBody());
    }
}
