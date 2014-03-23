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
    public static final String TEST_TENANT_ID = "testTenant";

	@Autowired
	@Qualifier(value = "kvStore")
	protected KVStore store;
	protected String namespace;
	protected String key;
	protected String className = "fooClass";
	protected byte[] value = "KVStoreTest".getBytes();

	@Before
	public void setup() throws Exception {
		String time = new Long(System.currentTimeMillis()).toString();
		namespace = "namespace:" + time;
		key = "key:" + time;
		value = (namespace +":" + key + ":" + className).getBytes();
		store.delete(namespace, key, TEST_TENANT_ID);
	}

	@After
	public void tearDown() throws Exception {
		store.delete(namespace, key, TEST_TENANT_ID);
	}
}
