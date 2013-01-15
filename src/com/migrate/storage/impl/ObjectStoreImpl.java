package com.migrate.storage.impl;

import java.io.IOException;

import com.migrate.webdata.model.PersistentObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import com.migrate.storage.KVObject;
import com.migrate.storage.KVStore;
import com.migrate.storage.ObjectStore;
/**
 * @author Zane Pan
 */
@Transactional(isolation=Isolation.REPEATABLE_READ)
public class ObjectStoreImpl implements ObjectStore {
	
	@Autowired
	@Qualifier(value = "kvStore")
	private KVStore store;
	
	@Override
	public <T extends PersistentObject> T get(String bucket, String key, Class<T> valueType) throws IOException {
		KVObject kvo = store.get(bucket, key);
		if (kvo == null) {
			return null;
		}
		if (!valueType.getName().equals(kvo.getClassName())) {
			throw new IllegalArgumentException( valueType.getName() + " does not match " + kvo.getClassName() );
		}
		T t = JsonHelper.readValue(kvo.getValue(), valueType);
		t.setBucket(kvo.getBucket());
		t.setId(kvo.getKey());
		t.setVersion(kvo.getVersion());
		t.setUpdateTime(kvo.getUpdateTime());
		return t;
	}


	@Override
	public void update(PersistentObject bo)  throws IOException {
		/**
		 * turn off the version check by reading the object version from DB for now.
		 */
		String bucket = bo.getBucket();
		String key = bo.getId();
		KVObject kvo = store.get(bo.getBucket(), bo.getId());
		if (kvo == null) {
			 create(bo);
			 return;
		}
		cleanBaseObjectId(bo);
		byte[] data = JsonHelper.writeValueAsByte(bo);
		kvo.setValue(data);
		kvo.setVersion(bo.getVersion());
		store.update(kvo);
		restoreBaseObjetId(bo, bucket, key);
	}

	@Override
	public void create(PersistentObject bo) throws IOException {
		String bucket = bo.getBucket();
		String key = bo.getId();
		cleanBaseObjectId(bo);
		byte[] value = JsonHelper.writeValueAsByte(bo);
		store.create (bucket, key, bo.getClass().getName(), value);
		restoreBaseObjetId(bo, bucket, key);
		
	}

	private void restoreBaseObjetId(PersistentObject bo, String bucket, String key) {
		bo.setBucket(bucket);
		bo.setId(key);
	}
	private void cleanBaseObjectId(PersistentObject bo) {
		bo.setBucket(null);
		bo.setId(null);	
	}


	@Override
	public void delete(String bucket, String key) throws IOException {
		store.delete(bucket, key);
	}
}
