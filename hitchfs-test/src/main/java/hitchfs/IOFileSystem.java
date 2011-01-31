package hitchfs;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Lists.newLinkedList;
import static java.io.File.separator;
import static java.util.Arrays.asList;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.Maps;

public class IOFileSystem extends StubFileSystem {

	Map<String, FakeFile> registry = Maps.newConcurrentMap();
	
	@Override
	public FakeFile file(File parent, String child) {
		return register(super.file(parent, child));
	}

	@Override
	public FakeFile file(File regular) {
		return register(super.file(regular));
	}
	
	@Override
	public FakeFile file(String parent, String child) {
		return register(super.file(parent, child));
	}
	
	@Override
	public FakeFile file(String pathname) {
		return register(super.file(pathname));
	}
	
	@Override
	public FakeFile file(URI uri) {
		return register(super.file(uri));
	}
	
	public FakeFile register(FakeFile file) {
		String path = repath(file._getPath());
		registry.put(path, file);
		file.withProperty(new AbsolutePathProp(path));
		return file;
	}

	public String getCurrentDirectory() {
		return System.getProperty("user.dir");
	}

	public Iterable<String> getCurrentDirectorySplit() {
		return skip(asList(getCurrentDirectory().split("/")), 1);
	}
	
	public String makePath(Iterable<String> ps, String pathDelim, boolean absolute) {
		StringBuilder buffer = new StringBuilder();
		if (absolute) {
			buffer.append(pathDelim);
		}
		on(pathDelim).appendTo(buffer, ps);
		return buffer.toString();
	}
	
	public String repath(String path) {
		LinkedList<String> ps = newLinkedList();
		if (!path.startsWith(separator)) {
			for (String p : getCurrentDirectorySplit()) {
				ps.addLast(p);
			}
		}
		for (String p : path.split(separator)) {
			if ("..".equals(p) && ps.size() > 0) {
				ps.removeLast();
			} else if (!".".equals(p) && !"".equals(p)) {
				ps.addLast(p);
			}
		}
		return makePath(ps, separator, true);
	}

	static class AbsolutePathProp implements FileProp {
		private final String path;
		public AbsolutePathProp(String path) {
			this.path = path;
		}
		public String getPath() {
			return path;
		}
	}
	
}
