package com.migrate.KVStore;

/**
 * @author Zane Pan
 */
import static org.junit.Assert.*;

import java.io.IOException;

import com.migrate.rest.VersionMismatchException;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.migrate.storage.KVObject;

public class KVStoreUpdateTest extends KVStoreTest {
    public static final String TEST_TENANT_ID = "testTenant";

    private static org.apache.log4j.Logger log = Logger.getLogger(KVStoreUpdateTest.class);
	@Override
	public void setup() throws Exception {
		super.setup();
		store.create(namespace, key, className, value, TEST_TENANT_ID);
	}
	
	@Test 
	public void updateTest() throws IOException {
		long ver = 1;
		KVObject kvo = store.get(namespace, key, ver, TEST_TENANT_ID);
		kvo.setValue("new value".getBytes());
		store.update(kvo, TEST_TENANT_ID);
		KVObject updateKvo = store.get(namespace, key, ver+1, TEST_TENANT_ID);
		assertNotNull(updateKvo);
		assertEquals(ver+1, updateKvo.getVersion());
		assertArrayEquals(kvo.getValue(), updateKvo.getValue());
		assertTrue(kvo.getUpdateTime() < updateKvo.getUpdateTime());
		boolean bException = false;
		try {
			kvo.setVersion(1L);
			store.update(kvo, TEST_TENANT_ID);
		} catch (VersionMismatchException e ) {
			bException = true;
		}
		assertTrue(bException);
	}
	
	public void tearDown() throws Exception {
		store.delete(namespace, key, TEST_TENANT_ID);
	}
	
	
}
