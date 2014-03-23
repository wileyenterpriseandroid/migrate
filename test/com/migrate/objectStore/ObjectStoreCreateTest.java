package com.migrate.objectStore;

/**
 * @author Zane Pan
 */
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.migrate.exception.DuplicationKeyException;

public class ObjectStoreCreateTest extends ObjectStoreTest {
    public static final String TEST_TENANT_ID = "testTenant";

    @Before
	public void ensureDelete() throws IOException {
		setup();
		store.delete(namespace, key, TEST_TENANT_ID);
	}
	
	@Test
	public void createTest() throws Exception {
		store.create(testObj, TEST_TENANT_ID);
		TestClass tc = store.get(namespace, key, TestClass.class.getCanonicalName(), TestClass.class, TEST_TENANT_ID);
		System.out.println(tc.getIntValue());
		assertArrayEquals(tc.getBytes(), testObj.getBytes());
		assertArrayEquals(tc.getListValue().toArray(), testObj.getListValue().toArray());
		assertEquals(tc.getListValue(), testObj.getListValue());
		testObj.setWd_updateTime(tc.getWd_updateTime());
		assertEquals(tc.getFooProp(), foo);
	
		boolean bException = false;
		try {
			store.create(testObj, TEST_TENANT_ID);
		} catch (DuplicationKeyException e) {
			bException = true;
		}
		assertTrue(bException);
	}
	

	
}
