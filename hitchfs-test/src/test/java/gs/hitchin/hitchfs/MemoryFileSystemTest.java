package gs.hitchin.hitchfs;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gs.hitchin.hitchfs.FakeFile;
import gs.hitchin.hitchfs.FileProp;
import gs.hitchin.hitchfs.MemoryFileSystem;
import gs.hitchin.hitchfs.MemoryFileSystem.Metadata;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Ignore;
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
public class MemoryFileSystemTest {

	MemoryFileSystem fs;
	AtomicBoolean isOwner = new AtomicBoolean(true);
	
	@Before
	public void setup() {
		this.fs = new MemoryFileSystem() {
			@Override
			public String getCurrentDirectory() {
				return "/a/b";
			}
			@Override
			public long currentTimeMillis() {
				return 87654;
			}
			@Override
			public boolean isOwner(FakeFile fake) {
				return isOwner.get();
			}
		};
	}
	
	@Test
	public void testMkdirs() {
		FakeFile c = fs.file("c");
		assertFalse(c.exists());
		assertTrue(c.mkdirs());
		assertTrue(c.exists());
		fs.file("d/e").mkdirs();
		fs.file("/a/g/f").mkdirs();
		c = (FakeFile) c.getAbsoluteFile();
		assertTrue(c.exists());
		assertTrue(c.isDirectory());
		assertFalse(c.isFile());
		File b = c.getParentFile();
		assertTrue(b.exists());
		assertTrue(b.isDirectory());
		assertFalse(b.isFile());
		File a = b.getParentFile();
		assertTrue(a.exists());
		assertTrue(a.isDirectory());
		assertFalse(a.isFile());
	}
	
	@Test
	public void testMkdir() throws IOException {
		FakeFile c = fs.file("c/d");
		assertFalse(c.mkdir());
		assertTrue(fs.file("c").mkdir());
		assertTrue(fs.file("c").isDirectory());
		assertFalse(fs.file("c").isFile());
	}
	
	@Test
	public void testCreateNewFile() throws IOException {
		FakeFile c = fs.file("c");
		assertFalse(c.isDirectory());
		assertFalse(c.isFile());
		assertFalse(c.exists());
		assertTrue(c.createNewFile());
		assertFalse(c.isDirectory());
		assertTrue(c.isFile());
		assertTrue(c.exists());
	}
	
	@Test
	public void testDelete() throws IOException {
		fs.file("").mkdirs();
		FakeFile c = fs.file("c");
		assertFalse(c.exists());
		assertFalse(c.delete());
		assertFalse(c.exists());
		assertTrue(c.createNewFile());
		assertTrue(c.exists());
		assertTrue(c.delete());
		assertFalse(c.exists());
	}

	@Test
	public void testDeleteDir() throws IOException {
		fs.file("").mkdirs();
		FakeFile c = fs.file("c");
		assertTrue(c.mkdir());
		FakeFile d = fs.file(c, "d");
		assertTrue(d.createNewFile());

		assertTrue(c.exists());
		assertFalse(c.delete());
		
		assertTrue(d.delete());
		assertFalse(d.exists());
		assertTrue(c.delete());
		assertFalse(c.exists());
	}
	
	@Test
	public void testList() throws IOException {
		FakeFile dir = fs.file("");
		dir.mkdirs();
		assertTrue(fs.file("d").createNewFile());
		FakeFile e = fs.file("e");
		assertTrue(e.createNewFile());
		assertTrue(e.delete());
		assertTrue(fs.file("c").mkdir());
		String[] files = dir.list();
		assertEquals(2, files.length);
		assertEquals("c", files[0]);
		assertEquals("d", files[1]);
	}
	
	@Test
	public void testListFiles() throws IOException {
		FakeFile dir = fs.file(".");
		dir.mkdirs();
		assertTrue(fs.file("d").createNewFile());
		FakeFile e = fs.file("e");
		assertTrue(e.createNewFile());
		assertTrue(e.delete());
		assertTrue(fs.file("c").mkdir());
		File[] files = dir.listFiles();
		assertEquals(2, files.length);
		assertEquals(fs.file("./c"), files[0]);
		assertEquals(fs.file("./d"), files[1]);
	}
	
	@Test
	public void testList_FilenameFilter() throws IOException {
		final FakeFile dir = fs.file(".");
		dir.mkdirs();
		assertTrue(fs.file("d").createNewFile());
		FakeFile e = fs.file("e");
		assertTrue(e.createNewFile());
		assertTrue(e.delete());
		assertTrue(fs.file("f").createNewFile());
		assertTrue(fs.file("c").mkdir());
		String[] files = dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File d, String name) {
				assertEquals(dir, d);
				return !"d".equals(name);
			}
		});
		assertEquals(2, files.length);
		assertEquals("c", files[0]);
		assertEquals("f", files[1]);
	}
	
	@Test
	public void testListFiles_FileFilter() throws IOException {
		FakeFile dir = fs.file("");
		dir.mkdirs();
		assertTrue(fs.file("d").createNewFile());
		FakeFile e = fs.file("e");
		assertTrue(e.createNewFile());
		assertTrue(e.delete());
		assertTrue(fs.file("f").createNewFile());
		assertTrue(fs.file("c").mkdir());
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return !"d".equals(f.getName());
			}});
		assertEquals(2, files.length);
		assertEquals(fs.file("/c"), files[0]);
		assertEquals(fs.file("/f"), files[1]);
	}
		
	@Test
	public void testListFiles_FilenameFilter() throws IOException {
		final FakeFile dir = fs.file("x");
		dir.mkdirs();
		assertTrue(fs.file(dir, "d").createNewFile());
		FakeFile e = fs.file(dir, "e");
		assertTrue(e.createNewFile());
		assertTrue(e.delete());
		assertTrue(fs.file(dir, "f").createNewFile());
		assertTrue(fs.file(dir, "c").mkdir());
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File d, String name) {
				assertEquals(dir, d);
				return !"d".equals(name);
			}});
		assertEquals(2, files.length);
		assertEquals(fs.file("x/c"), files[0]);
		assertEquals(fs.file("x/f"), files[1]);
	}

	@Test
	public void testDeleteOnExit() {
		final FakeFile dir = fs.file("dir");
		dir.mkdirs();
		FakeFile f = fs.file(dir, "f");
		f.deleteOnExit(); 
		Metadata m = f.getProperty(Metadata.class);
		assertTrue(m.deleteOnExit);
	}

	@Test
	public void testRename() throws IOException {
		final FakeFile dir = fs.file("dir");
		dir.mkdirs();
		FakeFile f = fs.file(dir, "f");
		assertTrue(f.mkdir());
		fs.file(f, "j").createNewFile();
		FakeFile g = fs.file(dir, "g");
		g.createNewFile();
		FakeFile h = fs.file(dir, "h");

		assertTrue(f.exists());
		assertTrue(g.exists());
		assertFalse(h.exists());

		assertFalse(f.renameTo(g));

		assertTrue(f.exists());
		assertTrue(g.exists());
		assertFalse(h.exists());

		assertTrue(f.renameTo(h));

		assertFalse(f.exists());
		assertTrue(g.exists());
		assertTrue(h.exists());
		
		assertTrue(fs.file(h, "j").exists());
	}
	
	@Test
	public void testHidden() throws IOException {
		FakeFile f = fs.file("f");
		assertFalse(f.isHidden());
		f.setHidden(true);
		assertFalse(f.isHidden());
		f.createNewFile();
		f.setHidden(true);
		assertTrue(f.isHidden());
		f.setHidden(false);
		assertFalse(f.isHidden());
	}
	
	@Test
	public void testLastModified() throws IOException {
		FakeFile f = fs.file("/a/b/f");
		assertEquals(0, f.lastModified());
		assertTrue(f.createNewFile());
		assertEquals(87654, f.lastModified());
		f.setLastModified(12345);
		assertEquals(12345, f.lastModified());
		f.touch();
		assertEquals(87654, f.lastModified());
		f.delete();
		assertEquals(0, f.lastModified());
	}

	@Test
	public void setWrite() throws IOException {
		Metadata m = new Metadata();
		FakeFile f = fs.file("f").withProperty(m);
		assertFalse(f.setWritable(false));
		f.createNewFile();
		isOwner.set(false);
		assertFalse(f.setWritable(false));
		assertTrue(m.ownerWrite && m.allWrite);
		isOwner.set(true);

		m.ownerWrite = true; 
		m.allWrite = true;
		// d-w--w--w-
		assertTrue(f.setWritable(false, false));
		// d---------
		assertTrue(!m.ownerWrite && !m.allWrite);

		m.ownerWrite = true;
		m.allWrite = true;
		// d-w--w--w-
		assertTrue(f.setWritable(false, true));
		// d----w--w-
		assertTrue(!m.ownerWrite && m.allWrite);

		m.ownerWrite = false;
		m.allWrite = false;
		// d---------
		assertTrue(f.setWritable(true, false));
		// // d-w--w--w-
		assertTrue(m.ownerWrite && m.allWrite);

		m.ownerWrite = false;
		m.allWrite = false;
		// d---------
		assertTrue(f.setWritable(true, true));
		// // d-w-------
		assertTrue(m.ownerWrite && !m.allWrite);
		
		m.ownerWrite = false;
		m.allWrite = false;
		// d-w--w--w-
		assertTrue(f.setReadOnly());
		// dr--------
		assertTrue(!m.ownerWrite && !m.allWrite);
	}
	
	@Test
	public void setRead() throws IOException {
		Metadata m = new Metadata();
		FakeFile f = fs.file("f").withProperty(m);
		assertFalse(f.setReadable(false));
		f.createNewFile();
		isOwner.set(false);
		assertFalse(f.setReadable(false));
		assertTrue(m.ownerRead && m.allRead);
		isOwner.set(true);

		m.ownerRead = true; 
		m.allRead = true;
		// dr--r--r--
		assertTrue(f.setReadable(false, false));
		// d---------
		assertTrue(!m.ownerRead && !m.allRead);

		m.ownerRead = true;
		m.allRead = true;
		// dr--r--r--
		assertTrue(f.setReadable(false, true));
		// d---r--r--
		assertTrue(!m.ownerRead && m.allRead);

		m.ownerRead = false;
		m.allRead = false;
		// d---------
		assertTrue(f.setReadable(true, false));
		// dr--r--r--
		assertTrue(m.ownerRead && m.allRead);

		m.ownerRead = false;
		m.allRead = false;
		// d---------
		assertTrue(f.setReadable(true, true));
		// dr--------
		assertTrue(m.ownerRead && !m.allRead);
	}
	
	@Test
	public void setExecute() throws IOException {
		Metadata m = new Metadata();
		FakeFile f = fs.file("f").withProperty(m);
		assertFalse(f.setExecutable(false));
		f.createNewFile();
		isOwner.set(false);
		assertFalse(f.setExecutable(false));
		assertTrue(m.ownerExecute && m.allExecute);
		isOwner.set(true);

		m.ownerExecute = true; 
		m.allExecute = true;
		// d--x--x--x
		assertTrue(f.setExecutable(false, false));
		// d---------
		assertTrue(!m.ownerExecute && !m.allExecute);

		m.ownerExecute = true;
		m.allExecute = true;
		// d--x--x--x
		assertTrue(f.setExecutable(false, true));
		// d-----x--x
		assertTrue(!m.ownerExecute && m.allExecute);

		m.ownerExecute = false;
		m.allExecute = false;
		// d---------
		assertTrue(f.setExecutable(true, false));
		// d--x--x--x
		assertTrue(m.ownerExecute && m.allExecute);

		m.ownerExecute = false;
		m.allExecute = false;
		// d---------
		assertTrue(f.setExecutable(true, true));
		// d--x------
		assertTrue(m.ownerExecute && !m.allExecute);
	}
	
	@Test
	public void testCanWrite() throws IOException {
		Metadata m = new Metadata();
		FakeFile f = fs.file("f").withProperty(m);
		f.createNewFile();
		assertTrue(f.canWrite());

		isOwner.set(true);
		m.allWrite = true;
		m.ownerWrite = true;
		assertTrue(f.canWrite());

		isOwner.set(true);
		m.allWrite = true;
		m.ownerWrite = false;
		assertFalse(f.canWrite());

		isOwner.set(true);
		m.allWrite = false;
		m.ownerWrite = true;
		assertTrue(f.canWrite());

		isOwner.set(true);
		m.allWrite = false;
		m.ownerWrite = false;
		assertFalse(f.canWrite());

		// Not owner
		isOwner.set(false);
		m.allWrite = true;
		m.ownerWrite = true;
		assertTrue(f.canWrite());

		isOwner.set(false);
		m.allWrite = true;
		m.ownerWrite = false;
		assertTrue(f.canWrite());

		isOwner.set(false);
		m.allWrite = false;
		m.ownerWrite = true;
		assertFalse(f.canWrite());

		isOwner.set(false);
		m.allWrite = false;
		m.ownerWrite = false;
		assertFalse(f.canWrite());
	}
	
	@Test
	public void testCanRead() throws IOException {
		Metadata m = new Metadata();
		FakeFile f = fs.file("f").withProperty(m);
		f.createNewFile();
		assertTrue(f.canRead());

		isOwner.set(true);
		m.allRead = true;
		m.ownerRead = true;
		assertTrue(f.canRead());

		isOwner.set(true);
		m.allRead = true;
		m.ownerRead = false;
		assertFalse(f.canRead());

		isOwner.set(true);
		m.allRead = false;
		m.ownerRead = true;
		assertTrue(f.canRead());

		isOwner.set(true);
		m.allRead = false;
		m.ownerRead = false;
		assertFalse(f.canRead());

		// Not owner
		isOwner.set(false);
		m.allRead = true;
		m.ownerRead = true;
		assertTrue(f.canRead());

		isOwner.set(false);
		m.allRead = true;
		m.ownerRead = false;
		assertTrue(f.canRead());

		isOwner.set(false);
		m.allRead = false;
		m.ownerRead = true;
		assertFalse(f.canRead());

		isOwner.set(false);
		m.allRead = false;
		m.ownerRead = false;
		assertFalse(f.canRead());
	}

	@Test
	public void testCanExecute() throws IOException {
		Metadata m = new Metadata();
		FakeFile f = fs.file("f").withProperty(m);
		f.createNewFile();
		assertTrue(f.canExecute());

		isOwner.set(true);
		m.allExecute = true;
		m.ownerExecute = true;
		assertTrue(f.canExecute());

		isOwner.set(true);
		m.allExecute = true;
		m.ownerExecute = false;
		assertFalse(f.canExecute());

		isOwner.set(true);
		m.allExecute = false;
		m.ownerExecute = true;
		assertTrue(f.canExecute());

		isOwner.set(true);
		m.allExecute = false;
		m.ownerExecute = false;
		assertFalse(f.canExecute());

		// Not owner
		isOwner.set(false);
		m.allExecute = true;
		m.ownerExecute = true;
		assertTrue(f.canExecute());

		isOwner.set(false);
		m.allExecute = true;
		m.ownerExecute = false;
		assertTrue(f.canExecute());

		isOwner.set(false);
		m.allExecute = false;
		m.ownerExecute = true;
		assertFalse(f.canExecute());

		isOwner.set(false);
		m.allExecute = false;
		m.ownerExecute = false;
		assertFalse(f.canExecute());
	}

	@Test
	public void testListRoots() throws IOException {
		File[] roots = fs.listRoots();
		assertEquals(1, roots.length);
		assertEquals(fs.file("/"), roots[0]);
	}

	@Test @Ignore // TODO test list fs roots
	public void testCreateTempFile() throws IOException {
		String prefix = "prefix";
		String suffix = null;
		FakeFile dir = (FakeFile) fs.file("dir").getAbsoluteFile();
		fs.createTempFile(suffix, prefix);
		fs.createTempFile(suffix, prefix, dir);
		suffix = "suffix";
		fs.createTempFile(suffix, prefix);
		fs.createTempFile(suffix, prefix, dir);
	}
	
	@Test
	public void testListCantRead() throws IOException {
		FakeFile d  = fs.file("dir");
		d.mkdir();
		fs.file(d, "file1").createNewFile();
		fs.file(d, "file2").createNewFile();
		assertEquals(2, d.list().length);
		d.setReadable(false);
		assertNull(d.list());
	}
	
	@Test
	public void testCantCreatePermission() throws IOException {
		FakeFile d  = fs.file("dir");
		d.mkdir();
		assertTrue(fs.file(d, "file1").createNewFile());
		assertTrue(d.setWritable(false, false));
		assertFalse(fs.file(d, "file2").createNewFile());
		assertEquals(1, d.list().length);
	}
	
	@Test
	public void testCantMkdirPermission() throws IOException {
		FakeFile d  = fs.file("dir");
		d.mkdir();
		assertTrue(fs.file(d, "dir1").mkdir());
		assertTrue(d.setWritable(false, false));
		assertFalse(fs.file(d, "dir2").mkdir());
		assertEquals(1, d.list().length);
	}
	
	@Test
	public void testCantDeletePermission() throws IOException {
		FakeFile f = fs.file("file");
		assertTrue(f.createNewFile());
		assertTrue(f.setWritable(false, false));
		assertFalse(f.delete());
		assertTrue(f.setWritable(true, false));
		assertTrue(f.delete());
	}
	
	@Test
	public void testCantDeleteDirPermission() throws IOException {
		FakeFile f = fs.file("dir");
		assertTrue(f.mkdir());
		assertTrue(f.setWritable(false, false));
		assertFalse(f.delete());
		assertTrue(f.setWritable(true, false));
		assertTrue(f.delete());
	}
	
	@Test
	public void testCantDeleteFilePermission() throws IOException {
		FakeFile d  = fs.file("dir");
		d.mkdir();
		assertTrue(fs.file(d, "file1").createNewFile());
		assertTrue(fs.file(d, "file2").createNewFile());
		assertTrue(d.setWritable(false, false));
		assertFalse(fs.file(d, "file2").delete());
		assertTrue(d.setWritable(true, false));
		assertTrue(fs.file(d, "file2").delete());
		assertEquals(1, d.list().length);
	}

	@Test
	public void testCantDeleteDirectoryPermission() throws IOException {
		FakeFile d  = fs.file("dir");
		d.mkdir();
		FakeFile d2 = fs.file(d, "dir2");
		assertTrue(d2.mkdir());
		assertTrue(d.setWritable(false, false));
		assertEquals(1, d.list().length);
		assertFalse(d2.delete());
		assertTrue(d.setWritable(true, false));
		assertTrue(d2.delete());
		assertEquals(0, d.list().length);
	}

	@Test
	public void testRegister() {
		FakeFile c = fs.file("c");
		assertFalse(c.hasProperty(TestProp.class));
		c.withProperty(new TestProp());
		assertTrue(fs.file("c").hasProperty(TestProp.class));
	}

	
	static class TestProp implements FileProp {}
	
}
