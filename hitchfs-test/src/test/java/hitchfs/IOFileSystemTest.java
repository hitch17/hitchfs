package hitchfs;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class IOFileSystemTest {

	@Test
	public void test() {
		IOFileSystem fs = new IOFileSystem() {
			@Override
			public String getCurrentDirectory() {
				return "/a/b";
			}
		};
		
		assertEquals("/a/b/c", fs.repath("c"));
		assertEquals("/a/b/c", fs.repath("c/"));
		assertEquals("/a/b/c", fs.repath("c/"));
		assertEquals("/a/b/c/d", fs.repath("c/d"));
		assertEquals("/a/b/c/d", fs.repath("c/d/"));
		assertEquals("/c", fs.repath("/c"));
		assertEquals("/c", fs.repath("/c/"));
		assertEquals("/c/d", fs.repath("/c/d"));
		assertEquals("/c/d", fs.repath("/c/d/"));
		assertEquals("/a/c", fs.repath("../c"));
		assertEquals("/a/b/c", fs.repath("./c/"));
		assertEquals("/a/b/~/d", fs.repath("~/d"));
		assertEquals("/d", fs.repath("/c/../d/"));

	}
	
	@Test
	public void testRegister() throws Exception {
		IOFileSystem fs = new IOFileSystem() {
			@Override
			public String getAbsolutePath(FakeFile fake) {
				AbsolutePathProp p = fake.getProperty(AbsolutePathProp.class);
				return p==null ? null : p.getPath();
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
	private void assertAbsolute(IOFileSystem fs, String expected, String ... path) {
		assertAbsolute(fs, expected, Arrays.asList(path));
	}

	private void assertAbsolute(IOFileSystem fs, String expected, List<String> path) {
		for (FakeFile f : tree(fs, path)) {
			assertEquals(expected, f.getAbsolutePath());
		}
	}

	private List<FakeFile> tree(final IOFileSystem fs, List<String> path) {
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

	private List<FakeFile> leaf(IOFileSystem fs, String path) {
		return Lists.newArrayList(
				fs.file(path),
				fs.file(fs.file(path)),
				fs.file(new File(path)));
	}
	
	
	
}
