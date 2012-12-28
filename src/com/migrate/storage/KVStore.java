package com.migrate.storage;

import java.io.IOException;
/**
 * @author Zane Pan
 */
public interface KVStore {
	KVObject get(String bucket, String key) throws IOException;
	//this is method is for the testing purpose for checking version number increase 
	// after each update.
	KVObject get(String bucket, String key, long version) throws IOException;
	void delete(String bucket, String key) throws IOException;	
	void update(KVObject data) throws IOException;
	void create(String bucket, String key, String className, byte[] data) throws IOException;
	void put(KVObject data) throws IOException;
}
