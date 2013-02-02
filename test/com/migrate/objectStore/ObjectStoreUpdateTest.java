package com.migrate.objectStore;
/**
 * @author Zane Pan
 */

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


import com.migrate.exception.DuplicationKeyException;

public class ObjectStoreUpdateTest extends ObjectStoreTest {

	@Before 
	public void ensureDelete() throws IOException {
		setup();
		store.create(testObj);
	}
	
	@Test
	public void createTest() throws Exception {
		TestClass tc = store.get(bucket, key, TestClass.class);
		System.out.println(tc.getWd_namespace());
		System.out.println(tc.getIntValue());
		
		modify(tc);
		store.update(tc);
		
		TestClass tcUpdated = store.get(bucket, key, TestClass.class);
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
			store.create(testObj);
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
