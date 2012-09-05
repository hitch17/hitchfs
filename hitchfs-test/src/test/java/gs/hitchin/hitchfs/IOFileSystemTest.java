package gs.hitchin.hitchfs;

import static gs.hitchin.hitchfs.ByteArrayContent.byteArrayContent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gs.hitchin.hitchfs.ByteArrayContent;
import gs.hitchin.hitchfs.FakeFile;
import gs.hitchin.hitchfs.IOFileSystem;
import gs.hitchin.hitchfs.IOFileSystem.Content;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicLong;

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
public class IOFileSystemTest {

	@Test
	public void test() throws IOException {
		final AtomicLong currentTime = new AtomicLong();
		IOFileSystem fs = new IOFileSystem() {
			@Override
			public long currentTimeMillis() {
				return currentTime.get();
			}
		};
		String pathname = "file";
		fs.file(pathname)
			.withProperty(byteArrayContent());
		Writer w = fs.writer(pathname);
		currentTime.set(12345);
		String msg = "hello, world.";
		w.write(msg);
		w.close();
		 
		FakeFile f = fs.file(pathname);
		assertTrue(f.exists());
		assertTrue(f.isFile());
		assertFalse(f.isDirectory());
		assertEquals(12345, f.lastModified());
		assertEquals(msg.length(), f.length());
		
		assertTrue(f.canRead());
		assertTrue(f.canWrite());
		assertTrue(f.canExecute());
		
		char[] buffer = new char[msg.length() + 5];
		Reader r = fs.reader(pathname);
		int len = r.read(buffer);
		r.close();
		assertEquals(msg.length(), len);
		assertEquals(msg, new String(buffer, 0, len));
	}
	
	@Test(expected=IOException.class)
	public void testPermissions() throws IOException {
		// Cant read a directory
		IOFileSystem fs = new IOFileSystem();
		FakeFile d = fs.file("dir");
		assertTrue(d.mkdir());
		fs.input(d);
	}
	
	@Test (expected=IOException.class)
	public void testPermissionsRead() throws IOException {
		IOFileSystem fs = new IOFileSystem();
		FakeFile f = fs.file("file");
		assertTrue(f.createNewFile());
		assertTrue(f.setReadable(false, false));
		fs.input(f);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testReadMissing() throws IOException {
		IOFileSystem fs = new IOFileSystem();
		FakeFile f = fs.file("file");
		assertFalse(f.exists());
		fs.input(f);
	}
	
	@Test (expected=IOException.class)
	public void testPermissionsWrite() throws IOException {
		IOFileSystem fs = new IOFileSystem();
		FakeFile f = fs.file("file");
		assertTrue(f.createNewFile());
		assertEquals(0, f.length());
		assertTrue(f.setWritable(false, false));
		fs.output(f);
	}
	
	@Test(expected=IOException.class)
	public void testDoubleOpen() throws IOException {
		IOFileSystem fs = new IOFileSystem();
		fs.writer("file");
		fs.writer("file");
	}
	
	@Test
	public void testClear() throws IOException {
		IOFileSystem fs = new IOFileSystem();
		FakeFile f = fs.file("file");
		Writer w = fs.writer(f);
		w.write("hello");
		w.close();
		assertEquals(5, f.length());
		assertTrue(f.delete());
		assertEquals(0, f.length());
		assertTrue(f.createNewFile());
		assertEquals(0, f.length());
		Reader r = fs.reader(f);
		assertEquals(-1, r.read());
		r.close();
	}
	
	@Test
	public void testAppend() throws IOException {
		IOFileSystem fs = new IOFileSystem();
		FakeFile f = fs.file("file")
			.withProperty(byteArrayContent());
		Writer w = fs.writer(f);
		w.write("hello, ");
		w.close();

		w = fs.writer(f, true);
		w.write("world.");
		w.close();

		ByteArrayContent c = (ByteArrayContent) f.getProperty(Content.class).getContent();
		assertEquals("hello, world.", new String(c.data));
	}
	
}
