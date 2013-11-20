package com.migrate.storage.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.migrate.webdata.model.GenericMap;
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
	public <T extends PersistentObject> T get(String namespace, String key, String className, Class<T> valueType) throws IOException {
		KVObject kvo = store.get(namespace, key);
		if (kvo == null) {
			return null;
		}
		if (!className.equals(kvo.getClassName())) {
			throw new IllegalArgumentException( valueType.getName() + " does not match " + kvo.getClassName() );
		}
		T t = JsonHelper.readValue(kvo.getValue(), valueType);
		t.setWd_namespace(kvo.getNamespace());
		t.setWd_classname(kvo.getClassName());
		t.setWd_id(kvo.getKey());
		t.setWd_version(kvo.getVersion());
		t.setWd_updateTime(kvo.getUpdateTime());
		return t;
	}

    @Override
	public void update(PersistentObject object)  throws IOException {
		String namespace = object.getWd_namespace();
		String key = object.getWd_id();
		KVObject existingObject = store.get(namespace, key);
		if (existingObject == null) {
			 create(object);
			 return;
		}
		cleanObject(object);
		byte[] data = JsonHelper.writeValueAsByte(object);

        // restore before update, which could fail due to conflict, and lose existing id
        restoreBaseObjectId(object, namespace, key);

        existingObject.setValue(data);
		existingObject.setVersion(object.getWd_version());
		store.update(existingObject);
	}

	@Override
	public void create(PersistentObject bo) throws IOException {
		String namespace = bo.getWd_namespace();
		String key = bo.getWd_id();
		cleanObject(bo);
		byte[] value = JsonHelper.writeValueAsByte(bo);
		store.create (namespace, key, bo.getWd_classname(), value);
		restoreBaseObjectId(bo, namespace, key);
	}

	private void restoreBaseObjectId(PersistentObject bo, String namespace, String key) {
		bo.setWd_namespace(namespace);
		bo.setWd_id(key);
	}

    private void cleanObject(PersistentObject bo) {
		bo.setWd_namespace(null);
		bo.setWd_id(null);	
	}

    @Override
    public void delete(String namespace, String key) throws IOException {
        delete(namespace, key, true, 0L);
    }

    @Override
	public void delete(String namespace, String key, boolean isPermament, long now) throws IOException {
		store.delete(namespace, key, isPermament, now);
	}
	
	@Override
	public List<GenericMap> findChanged(String namespace, String classname, long time,
                                        int start, int numMatches) throws IOException
    {
		List<KVObject> list = store.findChanged(namespace, classname, time, start, numMatches);

		List<GenericMap> result = new ArrayList<GenericMap>(list.size());
		for (KVObject kvo : list) {
            byte[] value = kvo.getValue();
			GenericMap map = (value == null ?
                    new GenericMap()
                    :
                    JsonHelper.readValue(kvo.getValue(), GenericMap.class));

			map.setWd_namespace(namespace);
			map.setWd_id(kvo.getKey());
			map.setWd_updateTime(kvo.getUpdateTime());
			map.setWd_version(kvo.getVersion());
            map.setWd_isDeleted("1".equals(kvo.getDeleted()));
			result.add(map);
		}
		return result;
	}
}
