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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zane Pan
 */
@Controller
@RequestMapping("/schema")
public class SchemaController {
    private static org.apache.log4j.Logger log = Logger.getLogger(SchemaController.class);

    @Autowired
    @Qualifier(value = "schemaService")
    private SchemaService schemaService;

    @RequestMapping(value = "/{schemaName}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> createSchema(
            @PathVariable String schemaName,
            @RequestBody PersistentSchema schema, HttpServletRequest req,
            HttpServletResponse resp) throws IOException
    {
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<GenericMap> getSchema(
            HttpServletRequest req,
            HttpServletResponse resp,
            @RequestParam(value = "syncTime", required = true) long syncTime)
            throws IOException
    {
        List<GenericMap> allSchema = schemaService.getAllSchema(syncTime);
        return allSchema;
    }

    @RequestMapping(value = "/{schemaName}", method = RequestMethod.GET)
    @ResponseBody
    public PersistentSchema getSchema(@PathVariable String schemaName,
                                      HttpServletRequest req,
                                      HttpServletResponse resp) throws IOException
    {
        PersistentSchema persistentSchema = schemaService.getSchema(schemaName);
        return persistentSchema;
    }

    @RequestMapping(value = "/{schemaName}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteSchema(@PathVariable String schemaName,
                             HttpServletRequest req,
                             HttpServletResponse resp) throws IOException
    {
        schemaService.deleteSchema(schemaName);
    }
}
