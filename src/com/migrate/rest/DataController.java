package com.migrate.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.migrate.exception.VersionMismatchException;
import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.migrate.webdata.model.GenericMap;
import com.migrate.exception.DuplicationKeyException;
import com.migrate.service.DataService;

/**
 * @author Zane Pan
 */

@Controller
@RequestMapping("/{context}/classes")
public class DataController {
	private static org.apache.log4j.Logger log = Logger
			.getLogger(DataController.class);

	@Autowired
	@Qualifier(value = "dataService")
	private DataService dataService;

	/*
	 * Get the JSON object with the given type and id.
	 */
	@RequestMapping(value = "{className}/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getObject(@PathVariable String context,
			@PathVariable String className, @PathVariable String id,
			HttpServletResponse resp) throws IOException
    {

		GenericMap ret = dataService.getObject(className, id);
		if (ret == null) {
			resp.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return ret;
	}

	/*
	 * update the JSON object with the given type and id.
	 */
	@RequestMapping(value = "{className}/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, String> putObject(@PathVariable String context,
			@PathVariable String className, @PathVariable String id,
			@RequestBody GenericMap data, HttpServletRequest req,
			HttpServletResponse resp) throws IOException
    {
		data.setBucket(className);
		data.setId(id);
		dataService.storeObject(data);
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("location", req.getRequestURL().toString());
		return map;
	}

	private void putObjectDo(GenericMap data)
    {
		
	}
	
	/*
	 * create the JSON object with the given type and id.
	 */
	@RequestMapping(value = "{className}/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> createObject(@PathVariable String context,
			@PathVariable String className, @PathVariable String id,
			@RequestBody GenericMap data, HttpServletRequest req,
			HttpServletResponse resp) throws IOException
    {
		data.setBucket(className);
		data.setId(id);
		dataService.createObject(data);
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("location", req.getRequestURL().toString());
		return map;
	}

	@RequestMapping(value = "{className}/{id}", method = RequestMethod.DELETE)
	public void deleteObject(@PathVariable String context,
			@PathVariable String className, @PathVariable String id,
			HttpServletResponse resp) throws IOException
    {
		dataService.deleteObject(className, id);

	}

	@RequestMapping(value = "{className}", method = RequestMethod.GET)
	@ResponseBody
	public List<GenericMap> searchObject(@PathVariable String context,
			@PathVariable String className,
			@RequestParam(value = "query", required = true) String queryStr,
			HttpServletResponse resp) throws IOException, ParseException
    {
		// log.info("******** queryStr: " + queryStr);
		List<GenericMap> ret = dataService.find(className, queryStr);
		if (ret == null || ret.size() == 0) {
			resp.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return ret;
	}

	@RequestMapping(value = "{className}", method = RequestMethod.POST)
	@ResponseBody
	public GenericMap sync(@PathVariable String context,
			@PathVariable String className,
			@RequestBody List<GenericMap> dataList,
			@RequestParam(value = "syncTime", required = true) String syncTime,
			HttpServletResponse resp) throws IOException, ParseException
    {
		System.out.println(" sync time : " + syncTime);
		String queryStr = "modified:[" +  syncTime + " TO 9341517871585]";
		                                                   
		Long now = new Long (System.currentTimeMillis());
		List<GenericMap> changedData = dataService.find(className, queryStr);
		GenericMap ret = new GenericMap();
		ret.put("syncTime", now.toString());
		ret.put("result", changedData);
		System.out.println(" ***** now : " + now.toString() );
		System.out.println(" ***** dataList size: " + dataList.size());

		for ( GenericMap data : dataList) {
			data.setBucket(className);
			if ( ((Boolean)data.get("deleted")).booleanValue()) {
				dataService.deleteObject(className, data.getId());
			}else {
				dataService.storeObject(data);
			}
		}
		return ret;
	}
	
	
	@ExceptionHandler(DuplicationKeyException.class)
	@ResponseBody
	public String handleDuplicationKeyException(Throwable exception,
			HttpServletResponse response) throws IOException
    {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return "Duplication key";
	}

	@ExceptionHandler(VersionMismatchException.class)
	@ResponseBody
	public String handleVersionMissMatchException(Throwable exception,
			HttpServletResponse response) throws IOException
    {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return "Version MissMatch";
	}
}
