package com.migrate.rest;

/**
 * Enables integration of various notification providers like Apple push and Google Cloud Messaging
 */
public interface SyncNotification {
    void dataChanged(long time);
}
