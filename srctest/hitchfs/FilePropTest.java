package hitchfs;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

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
