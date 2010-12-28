package hitchfs;
import static junit.framework.Assert.assertEquals;

import hitchfs.FakeFile;
import hitchfs.StubFileSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.Writer;

import junit.framework.Assert;

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
public class StubFileSystemTest {

	@Test
	public void testFakeTheReal() {
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public boolean exists(FakeFile MockFile) {
				return true;
			}
		};
		File real = new File("fakedir/doesntexist.txt");
		FakeFile fake = fs.file(real);
		Assert.assertFalse(real.exists());
		Assert.assertTrue(fake.exists());
	}
	

	@Test(expected=RuntimeException.class)
	public void deleteExample() {
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public boolean delete(FakeFile MockFile) {
				throw new RuntimeException("deleting me is really bad!");
			}
		};
		FakeFile fake = fs.file("something-critical.txt");
		fake.delete();
	}
	
	@Test
	public void testGetReader() throws Exception {
		String expected = "this is some text in memory.";
		StubFileSystem fs = new StubFileSystem();
		FakeFile file = fs.file("fakedir/doesntexist.txt")
			.withInputStream(new ByteArrayInputStream(expected.getBytes()));
		Reader reader = fs.reader(file);
		char[] chars = new char[expected.length() + 5];
		int len = reader.read(chars);
		reader.close();
		assertEquals(expected, new String(chars, 0, len));
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testGetReader_FNF() throws Exception {
		StubFileSystem fs = new StubFileSystem();
		FakeFile file = fs.file("fakedir/doesntexist.txt");
		fs.reader(file);
	}
	
	@Test
	public void testGetWriter() throws Exception {
		StubFileSystem fs = new StubFileSystem();
		String message = "fake file with text.";
		ByteArrayOutputStream out = new ByteArrayOutputStream(message.length());
		FakeFile file = fs.file("fakedir/fakefile2.txt").withOutputStream(out);
		Writer writer = fs.writer(file);
		writer.write(message);
		writer.close();
		assertEquals("9d2110c9a94894f10cfee35afaf8ceb2", file.getOutputStream().getDigestAsHex());
		assertEquals(message, new String(out.toByteArray()));
	}
	
}
