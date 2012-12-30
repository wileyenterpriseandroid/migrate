package com.migrate.storage;

import java.io.IOException;


import com.migrate.webdata.model.PersistentObject;

/**
 * @author Zane Pan
 */
public interface ObjectStore {
	<T extends PersistentObject> T get(String bucket, String key, Class<T> valueType) throws IOException;
	void update(PersistentObject bo) throws IOException;
	void create(PersistentObject bo) throws IOException;
	void delete(String bucket, String key) throws IOException;
}
