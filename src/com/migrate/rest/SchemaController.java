package com.migrate.rest;

import com.migrate.service.SchemaService;
import com.migrate.webdata.model.GenericMap;
import com.migrate.webdata.model.PersistentSchema;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth.common.signature.OAuthSignatureMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zane Pan
 */
@Controller
@RequestMapping("/schema")
public class SchemaController {
//    @Autowired
//    @Qualifier(value = "oAuthSignatureMethodFactory")
//    private OAuthSignatureMethodFactory oAuthSignatureMethodFactory;

    private static org.apache.log4j.Logger log = Logger.getLogger(SchemaController.class);

    @Autowired
    @Qualifier(value = "schemaService")
    private SchemaService schemaService;

    @RequestMapping(value = "/{schemaName}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> createSchema(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String schemaName,
            @RequestBody PersistentSchema schema, HttpServletRequest req,
            HttpServletResponse resp) throws IOException
    {

//        String userId = getUserId(oauth);

        try {
            schema.setWd_id(schemaName);
            schemaService.updateSchema(schema);
            Map<String, String> map = new HashMap<String, String>(1);
            map.put("location", req.getRequestURL().toString());
            resp.setStatus(HttpStatus.ACCEPTED.value());
            return map;
        } catch (Exception e) {
            // TODO: need to use logging for these
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    @ResponseBody
    public String[] getSchemaIDs(
            HttpServletRequest req,
            HttpServletResponse resp,
//            @RequestHeader("Authorization") String oauth,
            @RequestParam(value = "syncTime", required = true) long syncTime)
            throws IOException
    {
//        String userId = getUserId(oauth);

        List<GenericMap> allSchema = schemaService.getAllSchema(syncTime);
        String[] schemaIDs = new String[allSchema.size()];
        int count = 0;
        for (GenericMap schemaMap : allSchema) {
            schemaIDs[count] = schemaMap.getWd_id();
            count++;
        }

        return schemaIDs;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<GenericMap> getSchema(
            HttpServletRequest req,
            HttpServletResponse resp,
//            @RequestHeader("Authorization") String oauth,
            @RequestParam(value = "syncTime", required = true) long syncTime)
            throws IOException
    {
//        String userId = getUserId(oauth);

        List<GenericMap> allSchema = schemaService.getAllSchema(syncTime);
        return allSchema;
    }

    @RequestMapping(value = "/{schemaName}", method = RequestMethod.GET)
    @ResponseBody
    public PersistentSchema getSchema(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String schemaName,
            HttpServletRequest req,
            HttpServletResponse resp) throws IOException
    {
//        String userId = getUserId(oauth);

        PersistentSchema persistentSchema = schemaService.getSchema(schemaName);
        return persistentSchema;
    }

    @RequestMapping(value = "/{schemaName}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteSchema(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String schemaName,
            HttpServletRequest req,
            HttpServletResponse resp) throws IOException
    {
//        String userId = getUserId(oauth);

        schemaService.deleteSchema(schemaName);
    }

    private String getUserId(String oauth) {
        return "";

//        oauth = oauth.trim();
//        String[] temp = oauth.split(",");
//        String consumerKey = temp[1].replace("consumerKey=", "");
//        String signature = temp[2].replace("signature=", "");
//        OAuthSignatureMethod oauthMethod = oAuthSignatureMethodFactory.getOAuthSignatureMethod(consumerKey);
//        oauthMethod.verify(temp[0] + "," + temp[1], signature);
//        return consumerKey;
    }
}
