package com.migrate.gcm;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.migrate.rest.SyncNotification;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GCMSyncNotification implements SyncNotification {
    protected static final Logger logger =
            Logger.getLogger(GCMSyncNotification.class.getName());

    // TODO: obviously need to configure this
    public static final String GCM_KEY = "AIzaSyC9a3CuvPjvVbpoL2nl6eLm0EZlgtmgm4s";
    public static final int GCM_RETRIES = 5;

    private static List<String> DEVICE_REGISTRATIONS= Arrays.asList(new String[]{"foo"});

    @Override
    public void dataChanged(long syncTime) {
        // iterate through attached notification systems (amazon, GCM, etc.)
        Sender s = new Sender(GCM_KEY);
        Message m = new Message.Builder().addData("lastSyncTime", String.valueOf(syncTime)).build();

        try {
            MulticastResult mr = s.send(m, DEVICE_REGISTRATIONS, GCM_RETRIES);
            System.out.println("canonical ids: " + mr.getCanonicalIds());
            System.out.println("string: " + mr.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception executing gcm cloud request.", e);
        }
    }
}
