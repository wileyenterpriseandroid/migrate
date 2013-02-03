package com.migrate.storage;

import java.io.IOException;
import java.util.List;


import com.migrate.webdata.model.PersistentObject;

/**
 * @author Zane Pan
 */
public interface ObjectStore {
	<T extends PersistentObject> T get(String namespace, String key, Class<T> valueType) throws IOException;
	void update(PersistentObject bo) throws IOException;
	void create(PersistentObject bo) throws IOException;
	void delete(String bucket, String key) throws IOException;
	<T extends PersistentObject> List<T> findChanged(String namespace, Class<T> valueType, long time) throws IOException;
}
