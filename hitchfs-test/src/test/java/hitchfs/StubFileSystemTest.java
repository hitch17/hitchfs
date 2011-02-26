package hitchfs;

import static hitchfs.MessageDigestOutputStream.md5;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;

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
		assertFalse(real.exists());
		assertTrue(fake.exists());
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
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public InputStream getInputStream(FakeFile fake) throws IOException {
				StringProp p = fake.getProperty(StringProp.class);
				if (p == null) {
					throw new FileNotFoundException();
				}
				return new ByteArrayInputStream(p.data.getBytes());
			}
		};
		FakeFile file = fs.file("fakedir/doesntexist.txt")
			.withProperty(new StringProp(expected));
		Reader reader = fs.reader(file);
		char[] chars = new char[expected.length() + 5];
		int len = reader.read(chars);
		reader.close();
		assertEquals(expected, new String(chars, 0, len));
	}
	
	static class StringProp implements FileProp {
		private final String data;
		public StringProp(String data) {
			this.data = data;
		}
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testGetReader_FNF() throws Exception {
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public InputStream getInputStream(FakeFile fake) throws IOException {
				StringProp p = fake.getProperty(StringProp.class);
				if (p == null) {
					throw new FileNotFoundException();
				}
				return new ByteArrayInputStream(p.data.getBytes());
			}
		};
		FakeFile file = fs.file("fakedir/doesntexist.txt");
		fs.reader(file);
	}
	
	@Test
	public void testGetWriter() throws Exception {
		String message = "fake file with text.";
		ByteArrayOutputStream out = new ByteArrayOutputStream(message.length());
		final MessageDigestOutputStream md5 = md5().setOutput(out);
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public OutputStream getOutputStream(FakeFile fake, boolean append) throws IOException {
				return md5;
			}
		};
		FakeFile file = fs.file("fakedir/fakefile2.txt");
		Writer writer = fs.writer(file);
		writer.write(message);
		writer.close();
		assertEquals("9d2110c9a94894f10cfee35afaf8ceb2", md5.getDigestAsHex());
		assertEquals(message, new String(out.toByteArray()));
	}
	
	@Test
	public void testCombos() {
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public String getCurrentDirectory() {
				return "/a/b";
			}
		};

		assertEquals("/a/b/c", fs.canonical("c"));
		assertEquals("/a/b/c", fs.canonical("c/"));
		assertEquals("/a/b/c", fs.canonical("c/"));
		assertEquals("/a/b/c/d", fs.canonical("c/d"));
		assertEquals("/a/b/c/d", fs.canonical("c/d/"));
		assertEquals("/c", fs.canonical("/c"));
		assertEquals("/c", fs.canonical("/c/"));
		assertEquals("/c/d", fs.canonical("/c/d"));
		assertEquals("/c/d", fs.canonical("/c/d/"));
		assertEquals("/a/c", fs.canonical("../c"));
		assertEquals("/a/b/c", fs.canonical("./c/"));
		assertEquals("/a/b/~/d", fs.canonical("~/d"));
		assertEquals("/d", fs.canonical("/c/../d/"));
		assertEquals("/c", fs.canonical("../../c"));
		
		assertEquals("/a/b/c", fs.absolute("c"));
		assertEquals("/a/b/c", fs.absolute("c/"));
		assertEquals("/a/b/c", fs.absolute("c/"));
		assertEquals("/a/b/c/d", fs.absolute("c/d"));
		assertEquals("/a/b/c/d", fs.absolute("c/d/"));
		assertEquals("/c", fs.absolute("/c"));
		assertEquals("/c", fs.absolute("/c/"));
		assertEquals("/c/d", fs.absolute("/c/d"));
		assertEquals("/c/d", fs.absolute("/c/d/"));
		assertEquals("/a/b/../c", fs.absolute("../c"));
		assertEquals("/a/b/./c", fs.absolute("./c/"));
		assertEquals("/a/b/~/d", fs.absolute("~/d"));
		assertEquals("/c/../d", fs.absolute("/c/../d/"));

	}
	
	@Test
	public void testBackDirFromRoot() {
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public String getCurrentDirectory() {
				return "/";
			}
		};
		assertEquals("/a", fs.canonical("../a"));
	}
	
	@Test
	public void testRegister() throws Exception {
		StubFileSystem fs = new StubFileSystem() {
			@Override
			public String getAbsolutePath(FakeFile fake) {
				return fake.getKey();
			}
			@Override
			public String getPath(FakeFile fake) {
				return fake._getPath();
			}
			@Override
			public String getCurrentDirectory() {
				return "/a/b";
			}
		};

		assertAbsolute(fs, "/",     "/");
		assertAbsolute(fs, "/",     "../..");
		assertAbsolute(fs, "/a",     "/a");
		assertAbsolute(fs, "/a",     "../");
		assertAbsolute(fs, "/a/b",     "");
		assertAbsolute(fs, "/a/b",     ".");
		assertAbsolute(fs, "/a/b",     "../b");
		assertAbsolute(fs, "/a/b/c",   "c");
		assertAbsolute(fs, "/a/b/c",   "c/d/..");
		assertAbsolute(fs, "/a/b/c",   "c/../c");
		assertAbsolute(fs, "/a/b/c",   "c/");
		assertAbsolute(fs, "/a/b/c",   "./c/.");
		assertAbsolute(fs, "/a/b/c/d", "c/d");
		assertAbsolute(fs, "/a/b/c/d", "c/d/../d");
		assertAbsolute(fs, "/c/d",     "/c/d");
		assertAbsolute(fs, "/a/b/d",   "c/../d");
		assertAbsolute(fs, "/a/c/d",   "../c/./d");
		assertAbsolute(fs, "/c",       "/c");
		assertAbsolute(fs, "/c/d",     "/c/d");
		assertAbsolute(fs, "/c/d",     "/c","d");
		assertAbsolute(fs, "/c/d",     "/","c","d");
		assertAbsolute(fs, "/",     "..","..");
		assertAbsolute(fs, "/a/b",     "..","b");
		assertAbsolute(fs, "/a/b/c",   "c/d/..");
		assertAbsolute(fs, "/a/b/c",   "c","d/..");
		assertAbsolute(fs, "/a/b/c",   "c/d","..");
		assertAbsolute(fs, "/a/b/c",   "c","d","..");
		assertAbsolute(fs, "/a/b/c",   "c/../c");
		assertAbsolute(fs, "/a/b/c",   "c","..","c");
		assertAbsolute(fs, "/a/b/c",   "c","../c");
		assertAbsolute(fs, "/a/b/c",   "c/..","c");
		assertAbsolute(fs, "/a/b/c",   "./c/.");
		assertAbsolute(fs, "/a/b/c",   ".","c/.");
		assertAbsolute(fs, "/a/b/c",   "./c",".");
		assertAbsolute(fs, "/a/b/c",   ".","c",".");
		assertAbsolute(fs, "/a/b/c/d", "c/d/../d");
		assertAbsolute(fs, "/a/b/c/d", "c","d/../d");
		assertAbsolute(fs, "/a/b/c/d", "c/d","../d");
		assertAbsolute(fs, "/a/b/c/d", "c/d/..","d");
		assertAbsolute(fs, "/a/b/c/d", "c","d","../d");
		assertAbsolute(fs, "/a/b/c/d", "c","d/..","d");
		assertAbsolute(fs, "/a/b/c/d", "c/d","..","d");
		assertAbsolute(fs, "/a/b/c/d", "c","d","..","d");
		assertAbsolute(fs, "/a/b/d",   "c/../d");
		assertAbsolute(fs, "/a/b/d",   "c","../d");
		assertAbsolute(fs, "/a/b/d",   "c/..","d");
		assertAbsolute(fs, "/a/b/d",   "c","..","d");
		assertAbsolute(fs, "/a/c/d",   "../c/./d");
		assertAbsolute(fs, "/a/c/d",   "..","c/./d");
		assertAbsolute(fs, "/a/c/d",   "../c","./d");
		assertAbsolute(fs, "/a/c/d",   "../c/.","d");
		assertAbsolute(fs, "/a/c/d",   "..","c","./d");
		assertAbsolute(fs, "/a/c/d",   "..","c/.","d");
		assertAbsolute(fs, "/a/c/d",   "../c",".","d");
		assertAbsolute(fs, "/a/c/d",   "..","c",".","d");
	}

	// Tries all combinations of File, FakeFile and String as the parents of a File
	private void assertAbsolute(StubFileSystem fs, String expected, String ... path) {
		assertAbsolute(fs, expected, Arrays.asList(path));
	}

	private void assertAbsolute(StubFileSystem fs, String expected, List<String> path) {
		for (FakeFile f : tree(fs, path)) {
			assertEquals(expected, f.getAbsolutePath());
		}
	}

	private List<FakeFile> tree(final StubFileSystem fs, List<String> path) {
		if (path.isEmpty()) {
			throw new RuntimeException();
		} 
		
		List<FakeFile> current = leaf(fs, path.get(0));
		for (int i = 1; i < path.size(); i++) {
			List<FakeFile> r = Lists.newLinkedList();
			String p = path.get(i);
			for (FakeFile parent : current) {
				r.add(fs.file(parent, p));
				r.add(fs.file(parent.getPath(), p));
			}
			current = r;
		}
		
		return current;
	}

	private List<FakeFile> leaf(StubFileSystem fs, String path) {
		return Lists.newArrayList(
				fs.file(path),
				fs.file(fs.file(path)),
				fs.file(new File(path)));
	}
	
	@Test
	public void testRegistry() throws Exception {
		StubFileSystem fs = new StubFileSystem() {
			Map<String, FakeFile> registry = new HashMap<String, FakeFile>();
			@Override
			public FakeFile register(FakeFile file) {
				FakeFile f = registry.get(file.getKey());
				if (f == null) {
					f = file;
					registry.put(f.getKey(), f);
				}
				return f;
			}
			@Override
			public InputStream getInputStream(FakeFile fake) throws IOException {
				StringProp p = fake.getProperty(StringProp.class);
				if (p == null) {
					throw new FileNotFoundException();
				} else {
					return new ByteArrayInputStream(p.data.getBytes());
				}
			}
		};
		String msg = "the data";
		fs.file("somefile").withProperty(new StringProp(msg));
		Reader r = fs.reader("somefile");
		char[] buffer = new char[10];
		int len = r.read(buffer);
		assertEquals(msg, new String(buffer, 0, len));
		assertEquals(msg.length(), len);
	}
	
}
