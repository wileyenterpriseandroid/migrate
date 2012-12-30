package com.migrate.objectStore;
/**
 * @author Zane Pan
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.migrate.webdata.model.BasePersistentObject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.migrate.storage.ObjectStore;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "/spring/applicationContext-*.xml" })
public abstract class ObjectStoreTest {
	@Autowired
	@Qualifier(value = "objectStore")
	protected ObjectStore store;
	protected String bucket = "ObjectStoreTest";
	protected String key = "key:" + System.currentTimeMillis();
	protected TestClass testObj;
	protected Foo foo;
	@Before 
	public void setup() {
		testObj = new TestClass();
		testObj.setBucket(bucket);
		testObj.setId(key);
		testObj.stringValue = "stringValue1";
		testObj.longValue = System.currentTimeMillis();
		testObj.intValue = 100;
		testObj.bytes = "good morning".getBytes();
		foo = new Foo();
		foo.setName("fooname");
		foo.setValue("fooValue");
		testObj.setFooProp(foo);
		List<String> listValue = new ArrayList<String>();
		for (int i=0; i<5; i++) {
			listValue.add(new String("listValue_" + i));
		}
		testObj.listValue = listValue;
	}

	
	@After
	public void tearDown() throws IOException {
		store.delete(bucket, key);
	}
	
	public static class TestClass extends BasePersistentObject {
		private static final long serialVersionUID = 8235408424870289742L;
		private String stringValue;
		private long longValue;
		private int	intValue;
		private byte[] bytes;
		private List<String> listValue;
		private Foo fooProp;

		public String getStringValue() {
			return stringValue;
		}

		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}

		public long getLongValue() {
			return longValue;
		}

		public void setLongValue(long longValue) {
			this.longValue = longValue;
		}

		public int getIntValue() {
			return intValue;
		}

		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

		public List<String> getListValue() {
			return listValue;
		}

		public void setListValue(List<String> listValue) {
			this.listValue = listValue;
		}

		public Foo getFooProp() {
			return fooProp;
		}

		public void setFooProp(Foo fooProp) {
			this.fooProp = fooProp;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Arrays.hashCode(bytes);
			result = prime * result + intValue;
			result = prime * result
					+ ((listValue == null) ? 0 : listValue.hashCode());
			result = prime * result + (int) (longValue ^ (longValue >>> 32));
			result = prime * result
					+ ((stringValue == null) ? 0 : stringValue.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestClass other = (TestClass) obj;
			if (!Arrays.equals(bytes, other.bytes))
				return false;
			if (intValue != other.intValue)
				return false;
			if (listValue == null) {
				if (other.listValue != null)
					return false;
			} else if (!listValue.equals(other.listValue))
				return false;
			if (longValue != other.longValue)
				return false;
			if (stringValue == null) {
				if (other.stringValue != null)
					return false;
			} else if (!stringValue.equals(other.stringValue))
				return false;
			return true;
		}
	}
	
	public static class Foo {
		private String name;
		private String value;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Foo other = (Foo) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
		
		
	}
}
