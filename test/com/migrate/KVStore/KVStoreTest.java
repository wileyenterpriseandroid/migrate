package com.migrate.KVStore;
/**
 * @author Zane Pan
 */

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.migrate.storage.KVStore;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "/spring/applicationContext-*.xml" })
public abstract class KVStoreTest {
	@Autowired
	@Qualifier(value = "kvStore")
	protected KVStore store;
	protected String bucket;
	protected String key;
	protected String className = "fooClass";
	protected byte[] value = "KVStoreTest".getBytes();

	@Before
	public void setup() throws Exception {
		String time = new Long(System.currentTimeMillis()).toString();
		bucket = "bucket:" + time;
		key = "key:" + time;
		value = (bucket +":" + key + ":" + className).getBytes();
		store.delete(bucket, key);
	}

	@After
	public void tearDown() throws Exception {
		store.delete(bucket, key);
	}
}
