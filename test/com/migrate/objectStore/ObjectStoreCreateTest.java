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

	@Before 
	public void ensureDelete() throws IOException {
		setup();
		store.delete(namespace, key);
	}
	
	@Test
	public void createTest() throws Exception {
		store.create(testObj);
		TestClass tc = store.get(namespace, key, TestClass.class.getCanonicalName(), TestClass.class);
		System.out.println(tc.getIntValue());
		assertArrayEquals(tc.getBytes(), testObj.getBytes());
		assertArrayEquals(tc.getListValue().toArray(), testObj.getListValue().toArray());
		assertEquals(tc.getListValue(), testObj.getListValue());
		testObj.setWd_updateTime(tc.getWd_updateTime());
		assertEquals(tc.getFooProp(), foo);
	
		boolean bException = false;
		try {
			store.create(testObj);
		} catch (DuplicationKeyException e) {
			bException = true;
		}
		assertTrue(bException);
	}
	

	
}
