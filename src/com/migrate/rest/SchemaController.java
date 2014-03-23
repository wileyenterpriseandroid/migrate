package com.migrate.rest;

import com.migrate.service.SchemaService;
import com.migrate.webdata.model.GenericMap;
import com.migrate.webdata.model.PersistentSchema;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
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
//    @Qualifier(value = "migrateOAuthSignatureMethodFactory")
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
            HttpServletResponse resp,
            Principal principal)
            throws IOException
    {
        String userId = authorize(null /*oauth*/, principal);

        try {
            schema.setWd_id(schemaName);
            schemaService.updateSchema(schema, userId);
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
            @RequestParam(value = "syncTime", required = true) long syncTime,
            Principal principal) throws IOException
    {
        String userId = authorize(null/*oauth*/, principal);

        List<GenericMap> allSchema = schemaService.getAllSchema(syncTime, userId);
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
            @RequestParam(value = "syncTime", required = true) long syncTime,
            Principal principal) throws IOException
    {
        String userId = authorize(null/*oauth*/, principal);

        List<GenericMap> allSchema = schemaService.getAllSchema(syncTime, userId);
        return allSchema;
    }

    @RequestMapping(value = "/{schemaName}", method = RequestMethod.GET)
    @ResponseBody
    public PersistentSchema getSchema(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String schemaName,
            HttpServletRequest req,
            HttpServletResponse resp,
            Principal principal) throws IOException
    {
        String userId = authorize(null/*oauth*/, principal);

        PersistentSchema persistentSchema = schemaService.getSchema(schemaName, userId);
        return persistentSchema;
    }

    @RequestMapping(value = "/{schemaName}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteSchema(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String schemaName,
            Principal principal,
            HttpServletRequest req,
            HttpServletResponse resp) throws IOException
    {
        String userId = authorize(null /*oauth*/, principal);

        schemaService.deleteSchema(schemaName, userId);
    }

    /*
     * Validate the user using either 'o' or basic auth
     */
    public static String authorize(String authHeader, Principal principal) {
        if (null != principal) {
            // basic
            String pn = principal.getName();
            return pn;
        } else {
            throw new SecurityException("Unable to authorize request.");
        }

//        String userId = oAuthSignatureMethodFactory.authorize(oauth);
    }
}
