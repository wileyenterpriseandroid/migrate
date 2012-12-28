package com.migrate.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.migrate.Constants;
import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.migrate.dataModel.GenericMap;
import com.migrate.service.DataService;

// TODO: is this even used? It looks like requests are just going to the DataController?
@Controller
@RequestMapping("/contacts")
public class ContactsController {
	private static final String className = "com.migrate.sample.contacts.dataModel.Contact";
	@Autowired
	@Qualifier(value = "dataService")
	private DataService dataService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listContacts(HttpServletResponse resp ) throws IOException, ParseException {
		System.out.println(" ***** calling notes");
		String queryStr =  "modified:[0 TO 9999817027]";
		List<GenericMap> mapList = dataService.find(className, queryStr);
//		if ( mapList == null || mapList.size() == 0) {
//			resp.setStatus(HttpStatus.NOT_FOUND.value());
//		}
		ModelAndView modelAndView = new ModelAndView("contactsList");
		modelAndView.addObject("contactsList", mapList);
		return modelAndView;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView editNotes(@PathVariable String id, 
			HttpServletResponse resp ) throws IOException, ParseException
    {
		GenericMap contactsObj = dataService.getObject(className, id);
		if ( contactsObj == null ) {
			resp.setStatus(HttpStatus.NOT_FOUND.value());
		}
		ModelAndView modelAndView = new ModelAndView("contactsEdit");
		modelAndView.addObject("contact", contactsObj);

        return modelAndView;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public ModelAndView updateNotes(@PathVariable String id, 
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="address", required=false) String address,			
			@RequestParam(value="phoneNumber", required=false) String phoneNumber,			
			@RequestParam(value="update", required=false) String update,			
			@RequestParam(value="delete", required=false) String delete,			
			HttpServletResponse resp ) throws IOException, ParseException
    {
		boolean bUpdate = (update != null );
		GenericMap contactObj = dataService.getObject(className, id);
		if ( contactObj == null ) {
			resp.setStatus(HttpStatus.NOT_FOUND.value());
		}
		if (!bUpdate) {
			//dataService.deleteObject("unit.test.Notes", id);
			contactObj.put("deleted", "true");
			dataService.storeObject(contactObj);
		} else {
			contactObj.put("name", name);
			contactObj.put("address", address);
			contactObj.put("phoneNumber", address);
			Long now = new Long(System.currentTimeMillis());
			contactObj.put("modified", now);
			dataService.storeObject(contactObj);
//			ModelAndView modelAndView = new ModelAndView("notesEdit");
//			modelAndView.addObject("notes", notesObj);
//			return modelAndView;
		}
		return listContacts(resp);
	}
}
