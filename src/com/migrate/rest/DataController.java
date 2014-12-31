package com.migrate.rest;

import com.migrate.exception.DuplicationKeyException;
import com.migrate.service.DataService;
import com.migrate.webdata.model.GenericMap;
import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zane Pan
 */
@Controller
@RequestMapping("/classes")
public class DataController {
    private static org.apache.log4j.Logger log = Logger.getLogger(DataController.class);

//    @Autowired
//    @Qualifier(value = "oAuthSignatureMethodFactory")
//    private OAuthSignatureMethodFactory oAuthSignatureMethodFactory;

    private static final int MAX_NUM_DATA_TO_SYNCH = 1000;

    @Autowired
    @Qualifier(value = "dataService")
    private DataService dataService;

    @Autowired
    @Qualifier(value = "syncNotification")
    private SyncNotification notifier;

    /*
     * Get the JSON object with the given type and id.
     */
    @RequestMapping(value = "{className}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getObject(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String className,
            @PathVariable String id,
            Principal principal,
            HttpServletResponse resp) throws IOException
    {
        String userId = SchemaController.authorize(null /*oauth*/, principal);

        GenericMap ret = dataService.getObject(className, id, userId);
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
    public Map<String, String> putObject(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String className,
            @PathVariable String id,
            @RequestBody GenericMap data, HttpServletRequest req,
            Principal principal,
            HttpServletResponse resp) throws IOException
    {
        data.setWd_classname(className);
        data.setWd_id(id);

        String userId = SchemaController.authorize(null /*oauth*/, principal);
        dataService.storeObject(data, userId);
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("location", req.getRequestURL().toString());
        return map;
    }

    /*
     * create the JSON object with the given type and id.
     */
    @RequestMapping(value = "{className}/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> createObject(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String className,
            @PathVariable String id,
            @RequestBody GenericMap data,
            Principal principal,
            HttpServletRequest req,
            HttpServletResponse resp) throws IOException
    {
        System.out.println(" classname :" + className);
        data.setWd_classname(className);
        data.setWd_id(id);

        String userId = SchemaController.authorize(null /*oauth*/, principal);
        dataService.createObject(data, userId);
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("location", req.getRequestURL().toString());
        return map;
    }

    @RequestMapping(value = "{className}/{id}", method = RequestMethod.DELETE)
    public void deleteObject(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String className,
            @PathVariable String id,
            Principal principal,
            HttpServletResponse resp) throws IOException
    {
        String userId = SchemaController.authorize(null /*oauth*/, principal);
        dataService.deleteObject(className, id, userId);
    }

    @RequestMapping(value = "{className}", method = RequestMethod.GET)
    @ResponseBody
    public List<GenericMap> searchObject(
//            @RequestHeader("Authorization") String oauth,
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

    public List<GenericMap> findChanged(String classname, long time, int start, int numMatches, String userId)
            throws IOException, ParseException
    {
        return dataService.find(classname, time, start, numMatches, userId);
    }

    private GenericMap findData(List<GenericMap> list, GenericMap data) {
        for (GenericMap listData : list) {
            if (listData.getWd_id().equals(data.getWd_id())) {
                return listData;
            }
        }
        return null;
    }

    private void removeData(List<GenericMap> dataList, GenericMap data) {
        for (GenericMap c : dataList) {
            if (c.getWd_id().equals(data.getWd_id())) {
                dataList.remove(c);
                return;
            }
        }
    }

    private void removeData(List<GenericMap> srcList, List<GenericMap> removeList) {
        for (GenericMap data : removeList) {
            removeData(srcList, data);
        }
    }

    public GenericMap getData(String classname, String id, String tenantId) throws IOException {
//    public GenericMap getData(String classname, String id, String userid) throws IOException {
        return dataService.getObject(classname, id, tenantId);
    }

    @RequestMapping(value = "{classname}", method = RequestMethod.POST)
    @ResponseBody
    public SyncResult syncData(
//            @RequestHeader("Authorization") String oauth,
            @PathVariable String classname,
            @RequestBody SyncRequest syncRequest,
            Principal principal)
            throws IOException
    {
        try {
            String userId = SchemaController.authorize(null /*oauth*/, principal);

            List<GenericMap> clientModifiedData = syncRequest.getModifiedData();

            List<GenericMap> conflictData = new ArrayList<GenericMap>();

            /***
             * TODO: For now, max records is 1000
             *
             * Get server records changed since the client's last sync time.
             */
            long lastClientSyncTime = syncRequest.getSyncTime();
            List<GenericMap> serverModifiedData =
                    findChanged(classname, lastClientSyncTime, 0, MAX_NUM_DATA_TO_SYNCH, userId);

            /**
             * The current time becomes the client's next sync time.
             *
             * TODO: It is possible that another client can modify the data after the "now" timestamp before the call
             * to findChanged. In this case, the modified data is already included in the changedData set, however the
             * next sync, the modified data will be included in the changedData set again since its update time is
             * great than "now".
             */
            Long now = System.currentTimeMillis();
            SyncResult syncResult = new SyncResult(serverModifiedData, conflictData, now);

            // Set true if any client changes are accepted
            boolean isChanged = false;

            for (GenericMap clientObject : clientModifiedData) {
                try {
                    if (clientObject != null) {
                        clientObject.setWd_classname(classname);

                        if (clientObject.isWd_deleted()) {
                            // hold till all concerned clients know its gone?
                            // TODO: hold all deleted indefinitely ids in a single column?
                            dataService.softDeleteObject(classname, clientObject.getWd_id(), now, userId);
                        } else {
                            dataService.storeObject(clientObject, userId);
                        }
                    }

                    // sync resulted in at least one object modification
                    isChanged = true;

                } catch (VersionMismatchException e) {
                    /*
                     * The client's version does not match the server's version, the server must have updated
                     * since last sync - so find the conflicting server elt and let the client resolve it.
                     */
                    GenericMap conflictServerElt = findData(serverModifiedData, clientObject);

                    if (conflictServerElt != null) {
                        // Confirms that server did change the data since last sync.
                        conflictData.add(conflictServerElt);
                    } else {
                        // TODO: must conflict with some item changed *before* last sync?
                        String uuid = clientObject.getWd_id();
                        if (null == uuid) {
                            throw new IllegalArgumentException("Client data has null id");
                        }

                        conflictServerElt = getData(classname, uuid, userId);

                        if (conflictServerElt.getWd_version() > clientObject.getWd_version()) {
                            conflictData.add(conflictServerElt);
                        } else {
                            /*
                             * The client has a newer version than the server's, so the client must be sending a wrong
                             * data version.
                             */
                            throw new IllegalArgumentException("Client sent incorrect data version");
                        }
                    }
                }
            }

            /*
             * Dont send server data as "server modified" that the client also changed, since that data will no longer
             * be plain server modified - it will be a conflict, and thus present in the conflicts list. The client will
             * need to have kept its version of changes for resolution on arrival of the server conflict result list.
             */
            removeData(serverModifiedData, clientModifiedData);

            // For now tell all listeners about all changes
//            if (isChanged) {
//                notifier.dataChanged(now);
//            }

            return syncResult;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (ParseException e) {
            e.printStackTrace();
            // TODO: right thing to do?
            throw new IOException("Could not execute query", e);
        }
    }

    @ExceptionHandler(DuplicationKeyException.class)
    @ResponseBody
    public String handleDuplicationKeyException(Throwable exception,
                                                HttpServletResponse response) throws IOException
    {
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        // need to add return code here
        return "Duplicate key";
    }

    @ExceptionHandler(VersionMismatchException.class)
    @ResponseBody
    public String handleVersionMissMatchException(Throwable exception,
                                                  HttpServletResponse response) throws IOException
    {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        // need to add return code here
        return "Version Mismatch";
    }
}
