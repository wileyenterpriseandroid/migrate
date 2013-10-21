package com.migrate.KVStore;

import static org.junit.Assert.*;
import org.apache.log4j.Logger;
import org.junit.Test;
import com.migrate.exception.DuplicationKeyException;
import com.migrate.storage.KVObject;

/**
 * @author Zane Pan
 */

public class KVStoreCreateTest extends KVStoreTest {
	private static org.apache.log4j.Logger log = Logger.getLogger(KVStoreCreateTest.class);
	
	@Test
	public void createTest() throws Exception {
		log.info(" store: " + store);
		store.create(namespace, key, className, value);
		KVObject kvo = store.get(namespace, key);
		assertNotNull(kvo);
		assertEquals(namespace, kvo.getNamespace());
		assertEquals(key, kvo.getKey());
		assertArrayEquals(value, kvo.getValue());
		boolean bException = false;
		try {
			store.create(namespace, key, className, value);
		} catch(DuplicationKeyException e ) {
			bException = true;
		}
		assertTrue(bException);
	}

}
