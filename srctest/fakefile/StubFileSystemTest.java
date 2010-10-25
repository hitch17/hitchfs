package fakefile;
import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.junit.Test;

import fakefile.FakeFile;
import fakefile.MessageDigestOutputStream;
import fakefile.StubFileSystem;

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
			public long length(FakeFile fake) {
				return 20;
			}
		};
		File real = new File("fakedir/fakefile.txt");
		final FakeFile fake = fs.file(real);
		assertEquals(real.length(), fake.length());
	}
	
	@Test
	public void testGetReader() throws Exception {
		final String expected = "this is some text in memory.";
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public InputStream input(File file) throws FileNotFoundException {
				return new ByteArrayInputStream(expected.getBytes());
			}
		};
		FakeFile file = fs.file("fakedir/doesntexist.txt");
		Reader reader = fs.reader(file);
		char[] chars = new char[30];
		int len = reader.read(chars);
		reader.close();
		String result = new String(chars, 0, len);
		assertEquals(expected, result);
	}
	
	@Test
	public void testGetWriter() throws Exception {
		final MessageDigestOutputStream md5 = MessageDigestOutputStream.md5();
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public OutputStream output(File file) throws FileNotFoundException {
				return md5;
			}
		};
		FakeFile file = fs.file("fakedir/fakefile2.txt");
		Writer writer = fs.writer(file);
		writer.write("fake file with text.");
		writer.close();
		assertEquals("9d2110c9a94894f10cfee35afaf8ceb2", md5.getDigestAsHex());
	}
	
}
