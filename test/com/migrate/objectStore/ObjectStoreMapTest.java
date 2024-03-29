package com.migrate.objectStore;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.migrate.webdata.model.GenericMap;


public class ObjectStoreMapTest extends ObjectStoreTest {
	@Test
	public void test() throws IOException {
		long time = System.currentTimeMillis();
		GenericMap map = new GenericMap();
		map.setWd_classname(GenericMap.class.getName());
		map.setWd_namespace("map test name space");
		map.put("p1String", "xyz");
		map.put("p1Long", 200L);
		map.setWd_id("uuid4");
		map.setWd_updateTime(System.currentTimeMillis());
		this.store.create(map, TEST_TENANT_ID);
		List<GenericMap> result = this.store.findChanged("map test name space", GenericMap.class.getName(), 0, 0, 1000, TEST_TENANT_ID);
		for ( GenericMap m : result ) {
			for ( Map.Entry<String, Object> e : m.entrySet() ) {
				System.out.println( e.getKey() + " = " + e.getValue() );
			}
			
		}
	}
}
