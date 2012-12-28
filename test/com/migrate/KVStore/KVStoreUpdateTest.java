package com.migrate.KVStore;

/**
 * @author Zane Pan
 */
import static org.junit.Assert.*;

import java.io.IOException;

import com.migrate.exception.VersionMismatchException;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.migrate.storage.KVObject;

public class KVStoreUpdateTest extends KVStoreTest {
	private static org.apache.log4j.Logger log = Logger.getLogger(KVStoreUpdateTest.class);
	@Override
	public void setup() throws Exception {
		super.setup();
		store.create(bucket, key, className, value);
	}
	
	@Test 
	public void updataTest() throws IOException {
		long ver = 1;
		KVObject kvo = store.get(bucket, key, ver);
		kvo.setValue("new value".getBytes());
		store.update(kvo);
		KVObject updateKvo = store.get(bucket, key, ver+1);
		assertNotNull(updateKvo);
		assertEquals(ver+1, updateKvo.getVersion());
		assertArrayEquals(kvo.getValue(), updateKvo.getValue());
		assertTrue(kvo.getUpdateTime() < updateKvo.getUpdateTime());
		boolean bException = false;
		try {
			kvo.setVersion(1L);
			store.update(kvo);
		} catch (VersionMismatchException e ) {
			bException = true;
		}
		assertTrue(bException);
	}
	
	public void tearDown() throws Exception {
		store.delete(bucket, key);
	}
	
	
}
