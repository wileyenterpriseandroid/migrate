package com.migrate.gcm;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.migrate.rest.SyncNotification;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;

public class GCMSyncNotification implements SyncNotification {
    private static org.apache.log4j.Logger log = Logger.getLogger(GCMSyncNotification.class);


    // TODO: obviously need to configure this
    public static final String GCM_KEY = "AIzaSyC9a3CuvPjvVbpoL2nl6eLm0EZlgtmgm4s";
    public static final int GCM_RETRIES = 5;

    private static List<String> DEVICE_REGISTRATIONS= Arrays.asList(new String[]{"foo"});

    @Override
    public void dataChanged(long syncTime, ServletContext ctx) {
        // iterate through attached notification systems (amazon, GCM, etc.)
        Sender s = new Sender(GCM_KEY);
        Message m = new Message.Builder().addData("lastSyncTime", String.valueOf(syncTime)).build();

        try {
            MulticastResult mr = s.send(m, DEVICE_REGISTRATIONS, GCM_RETRIES);
            System.out.println("canonical ids: " + mr.getCanonicalIds());
            System.out.println("string: " + mr.toString());
        } catch (IOException e) {
            ctx.log("exception from cloud gcm request", e);
        }
    }
}
