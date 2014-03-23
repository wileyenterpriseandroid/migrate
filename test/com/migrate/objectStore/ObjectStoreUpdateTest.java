package com.migrate.objectStore;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


import com.migrate.exception.DuplicationKeyException;

/**
 * @author Zane Pan
 */
public class ObjectStoreUpdateTest extends ObjectStoreTest {
    public static final String TEST_TENANT_ID = "testTenant";


	@Before 
	public void ensureDelete() throws IOException {
		setup();
		store.create(testObj, TEST_TENANT_ID);
	}
	
	@Test
	public void createTest() throws Exception {
		TestClass tc = store.get(namespace, key, TestClass.class.getName(), TestClass.class, TEST_TENANT_ID);
		System.out.println(tc.getWd_namespace());
		System.out.println(tc.getIntValue());
		
		modify(tc);
		store.update(tc, TEST_TENANT_ID);
		
		TestClass tcUpdated = store.get(namespace, key, TestClass.class.getName(),  TestClass.class, TEST_TENANT_ID);
		System.out.println(tcUpdated.getIntValue());

		assertEquals(tc.getIntValue(), tcUpdated.getIntValue());
				assertArrayEquals(tc.getBytes(), tcUpdated.getBytes());
		assertArrayEquals(tc.getListValue().toArray(), tcUpdated.getListValue().toArray());
		assertEquals(tc.getListValue(), tcUpdated.getListValue());
		assertEquals(tc.getFooProp(), foo);
		tcUpdated.setWd_updateTime(tc.getWd_updateTime());
		//assertEquals(tc, tcUpdated);
		boolean bException = false;
		try {
			store.create(testObj, TEST_TENANT_ID);
		} catch (DuplicationKeyException e) {
			bException = true;
		}
		assertTrue(bException);
	}

	private void modify(TestClass tc) {
		tc.setStringValue("stringValue1:updated");
		tc.setLongValue(System.currentTimeMillis());
		tc.setIntValue(300);
		tc.setBytes("good morning: updated".getBytes());
		tc.setFooProp(foo);
		List<String> listValue = new ArrayList<String>();
		for (int i=0; i<8; i++) {
			listValue.add(new String("listValue_updated" + i));
		}
		tc.setListValue(listValue);		
	}
}
