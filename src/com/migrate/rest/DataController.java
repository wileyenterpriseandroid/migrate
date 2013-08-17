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
import com.migrate.webdata.model.SyncResult;
import com.migrate.exception.DuplicationKeyException;
import com.migrate.service.DataService;

/**
 * @author Zane Pan
 */
@Controller
@RequestMapping("/{context}/classes")
public class DataController {
	private static org.apache.log4j.Logger log =
            Logger.getLogger(DataController.class);

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
		data.setWd_classname(className);
		data.setWd_id(id);

		dataService.storeObject(data);
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("location", req.getRequestURL().toString());
		return map;
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
		System.out.println(" classname :" + className);
		data.setWd_classname(className);
		data.setWd_id(id);

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
		List<GenericMap> ret = dataService.luceneSearch(className, queryStr);
		if (ret == null || ret.size() == 0) {
			resp.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return ret;
	}

	// @RequestMapping(value = "{className}", method = RequestMethod.POST)
	// @ResponseBody
	// public SyncResult sync(@PathVariable String context,
	// @PathVariable String className,
	// @RequestBody List<GenericMap> clientChangedData,
	// @RequestParam(value = "syncTime", required = true) String syncTime,
	// HttpServletResponse resp) throws IOException, ParseException
	// {
	// // TODO: client data wont deserialize as a generic map :-(
	//
	// System.out.println(" sync time : " + syncTime);
	// String queryStr = "modified:[" + syncTime + " TO 9991517871585]";
	//
	// Long now = new Long(System.currentTimeMillis());
	// List<GenericMap> serverChangedData = dataService.find(className,
	// queryStr);
	//
	//
	// for (GenericMap clientData : clientChangedData) {
	// if ( clientData != null ) {
	// clientData.setWd_classname(className);
	// if (((Boolean) clientData.get("deleted")).booleanValue()) {
	// dataService.deleteObject(className, clientData.getWd_id());
	// } else {
	// dataService.storeObject(clientData);
	// }
	// }
	// }
	//
	// SyncResult result = new SyncResult();
	// result.setGenericMapList(serverChangedData);
	// result.setSynchTime(now);
	// return result;
	// }
	@RequestMapping(value = "{className}", method = RequestMethod.POST)
	@ResponseBody
	public SyncResult syncData(@PathVariable String context,
                               @PathVariable String className,
                               @RequestBody GenericMap[] clientChangedData,
                               @RequestParam(value = "syncTime", required = true) long syncTime,
                               HttpServletResponse resp) throws IOException, ParseException
    {
		// TODO: client data wont deserialize as a generic map :-(

		System.out.println(" *sync time : " + syncTime);
		//String queryStr = "modified:[" + syncTime + " TO 9991517871585]";

		Long now = new Long(System.currentTimeMillis());
		List<GenericMap> serverChangedData = dataService.find(className, syncTime);

		for (GenericMap clientData : clientChangedData) {
			if (clientData != null) {
				clientData.setWd_classname(className);
				System.out.println(" *** wd_id: " + clientData.getWd_id());
				Integer deleted = (Integer) clientData.get("wd_deleted");
				
				if (deleted != null && deleted.equals('1')) { // as represented by sqlite
					dataService.deleteObject(className, clientData.getWd_id());
				} else {

                    // TODO: need to better handle corrupt or incomplete data

					dataService.storeObject(clientData);
				}
			}
		}

		SyncResult result = new SyncResult();
		result.setGenericMapList(serverChangedData);
		result.setSynchTime(now);
		return result;
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
