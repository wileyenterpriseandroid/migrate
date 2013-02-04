package com.migrate.rest;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrate.webdata.model.PersistentSchema;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.migrate.service.SchemaService;

/**
 * @author Zane Pan
 */
@Controller
@RequestMapping("/{context}/schema")
public class SchemaController {
	private static org.apache.log4j.Logger log = Logger.getLogger(SchemaController.class);

	@Autowired
	@Qualifier(value = "schemaService")
	private SchemaService schemaService;

	@RequestMapping(value = "/{schemaName}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> createSchema(@PathVariable String context,
			@PathVariable String schemaName,
			@RequestBody PersistentSchema schema, HttpServletRequest req,
			HttpServletResponse resp) throws IOException
    {
		try {
			schema.setWd_id(schemaName);
			System.out.println(" namespace: " + schema.getWd_namespace());
			schemaService.updateSchema(schema);
			Map<String, String> map = new HashMap<String, String>(1);
			map.put("location", req.getRequestURL().toString());
			resp.setStatus(HttpStatus.ACCEPTED.value());
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/{schemaName}", method = RequestMethod.GET)
	@ResponseBody
	public PersistentSchema getSchema(@PathVariable String context,
			@PathVariable String schemaName, HttpServletRequest req,
			HttpServletResponse resp) throws IOException
    {
		PersistentSchema persistentSchema = schemaService.getSchema(schemaName);
		return persistentSchema;
	}

	@RequestMapping(value = "/{schemaName}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteSchema(@PathVariable String context,
			@PathVariable String schemaName, HttpServletRequest req,
			HttpServletResponse resp) throws IOException
    {
		schemaService.deleteSchema(schemaName);
	}
}
