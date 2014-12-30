package com.migrate.rest;

import javax.servlet.ServletContext;

/**
 * Enables integration of various notification providers like Apple push and Google Cloud Messaging
 */
public interface SyncNotification {
    void dataChanged(long time, ServletContext servletContext);
}
