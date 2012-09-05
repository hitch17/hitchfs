package gs.hitchin.hitchfs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gs.hitchin.hitchfs.FakeFile;
import gs.hitchin.hitchfs.FileProp;
import gs.hitchin.hitchfs.PropStore;
import gs.hitchin.hitchfs.StubFileSystem;
import gs.hitchin.hitchfs.PropStore.PropVisitor;

import org.junit.Test;

/*
 * Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
public class FilePropTest {

	@Test
	public void testProp() {
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public long length(FakeFile fake) {
				// If the file has a property, return its value, otherwise return -1
				LengthTestProp p = fake.getProperty(LengthTestProp.class);
				return (p!=null) ? p.len : -1;
			}
		};
		assertEquals(100, fs.file("one").withProperty(new LengthTestProp(100)).length());
		assertEquals(50, fs.file("two").withProperty(new LengthTestProp(50)).length());
		assertEquals(-1, fs.file("three").length());
	}
	
	static class LengthTestProp implements FileProp {
		final int len;
		public LengthTestProp(int len) {
			this.len = len;
		}
	}
	
	@Test
	public void testProp2() {
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public long length(FakeFile fake) {
				// If the file has a property, return its value, otherwise return -1
				LengthTestProp p = fake.getProperty(LengthTestProp.class);
				return (p!=null) ? p.len : -1;
			}
		};
		assertEquals(200, fs.file("one").withProperty(LengthTestProp.class, 
				new DoubleLengthTestProp(100)).length());
		assertEquals(100, fs.file("two").withProperty(LengthTestProp.class, 
				new DoubleLengthTestProp(50)).length());
		assertEquals(-1, fs.file("three").length());
		
		FakeFile file = fs.file("test");
		assertFalse(file.hasProperty(LengthTestProp.class));
		file.withProperty(new LengthTestProp(5));
		assertTrue(file.hasProperty(LengthTestProp.class));
		
		assertTrue(file.visitProperty(DoubleLengthTestProp.class, 
				new PropVisitor<DoubleLengthTestProp, Boolean>() {
			@Override
			public Boolean none(PropStore props) {
				return true;
			}
			@Override
			public Boolean some(DoubleLengthTestProp p) {
				return false;
			}}));
		
		assertTrue(file.visitProperty(LengthTestProp.class, 
				new PropVisitor<LengthTestProp, Boolean>() {
			@Override
			public Boolean none(PropStore props) {
				return false;
			}
			@Override
			public Boolean some(LengthTestProp p) {
				return true;
			}}));
		
	}
	
	static class DoubleLengthTestProp extends LengthTestProp {
		public DoubleLengthTestProp(int len) {
			super(len*2);
		}
	}

	
}
