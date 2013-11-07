package com.migrate.storage;

import java.io.IOException;
import java.util.List;
/**
 * @author Zane Pan
 */
public interface KVStore {
	KVObject get(String namespace, String key) throws IOException;
	//this is method is for the testing purpose for checking version number increase 
	// after each update.
	KVObject get(String namespace, String key, long version) throws IOException;
	void delete(String namespace, String key) throws IOException;
	void delete(String namespace, String key, boolean isPermanent, long now) throws IOException;

	void update(KVObject data) throws IOException;
	void create(String namespace, String key, String className, byte[] data) throws IOException;
	void put(KVObject data) throws IOException;
	List<KVObject> findChanged(String namespace, String className,
                               long time, int start, int numMatches);
}
