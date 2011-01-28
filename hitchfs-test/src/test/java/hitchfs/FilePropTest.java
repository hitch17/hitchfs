package hitchfs;


import static org.junit.Assert.assertEquals;

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
	
}
