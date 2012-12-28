package com.migrate.storage;

import java.io.IOException;


import com.migrate.dataModel.PersistedObject;
/**
 * @author Zane Pan
 */
public interface ObjectStore {
	<T extends PersistedObject> T get(String bucket, String key, Class<T> valueType) throws IOException;
	void update(PersistedObject bo) throws IOException;
	void create(PersistedObject bo) throws IOException;
	void delete(String bucket, String key) throws IOException;
}
