package hitchfs;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Ordering.natural;
import static java.util.Collections.sort;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

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
// TODO listRoots and many others are not useful on windows
public class MemoryFileSystem extends BasicFileSystem {

	Node root = new Node();
	
	public MemoryFileSystem() {
		getMetadata(root)
			.setExists(true)
			.setFile(false)
			.setLastModified(currentTimeMillis());
		file(getCurrentDirectory()).mkdirs();
	}
	
	@Override
	public FakeFile register(FakeFile file) {
		Node current = lookup(file);
		if (current.props == null) {
			current.props = file.getProperties();
		} else {
			file.setProperties(current.props);
		}
		return file;
	}
	
	public Node lookup(FakeFile file) {
		Node current = root;
		for (String p : canonicalSplit(file.getPath())) {
			current = current.get(p);
		}
		return current;
	}
	
	@Override
	public boolean exists(FakeFile fake) {
		return getMetadata(fake).exists;
	}
	
	@Override
	public boolean mkdir(FakeFile fake) {
		try {
			File parent = fake.getCanonicalFile().getParentFile();
			if (parent == null) {
				return false;
			}
			if (!parent.exists()) {
				return false;
			}
			if (!parent.canWrite()) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		if (fake.exists()) {
			return false;
		}
		getMetadata(fake)
			.setExists(true)
			.setFile(false);
		touch(fake);
		return true;
	}
	
	/**
	 * http://www.docjar.com/html/api/java/io/File.java.html
	 */
	@Override
	public boolean mkdirs(FakeFile fake) {
		/* If the terminal directory already exists, answer false */
		if (fake.exists()) {
			return false;
		}

		/* If the receiver can be created, answer true */
		if (fake.mkdir()) {
			return true;
		}

		String parentDir = fake.getParent();
		/* If there is no parent and we were not created, answer false */
		if (parentDir == null) {
			return false;
		}

		/* Otherwise, try to create a parent directory and then this directory */
		return (file(parentDir).mkdirs() && fake.mkdir());
	}

	@Override
	public boolean isDirectory(FakeFile fake) {
		return fake.exists() && !getMetadata(fake).isFile;
	}

	@Override
	public boolean isFile(FakeFile fake) {
		return fake.exists() && getMetadata(fake).isFile;
	}
	
	@Override
	public boolean createNewFile(FakeFile fake) throws IOException {
		try {
			File f = fake.getCanonicalFile().getParentFile();
			if (!f.exists()) {
				return false;
			}
			if (!f.canWrite()) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		if (fake.exists()) {
			return false;
		}
		getMetadata(fake)
			.setExists(true)
			.setFile(true);
		touch(fake);
		return true;
	}

	@Override
	public boolean delete(FakeFile fake) {
		if (!fake.exists()) {
			return false;
		} 
		try {
			File f = fake.getCanonicalFile().getParentFile();
			if (!f.canWrite()) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		if (!fake.canWrite()) {
			return false;
		}
		if (fake.isFile()) {
			getMetadata(fake).setDefaults();
			return true;
		} else {
			String[] contents = fake.list();
			if (contents != null && contents.length == 0) {
				getMetadata(fake).setDefaults();
				return true;
			} else {
				return false;
			}
		}
	}

	public List<String> listAsList(FakeFile fake) {
		if (fake.isFile() || !fake.canRead()) {
			return null;
		}
		Node n = lookup(fake);
		List<String> rs = newArrayListWithExpectedSize(n.nodes.size());
		for (Entry<String, Node> e : n.nodes.entrySet()) {
			if (getMetadata(e.getValue()).exists) {
				rs.add(e.getKey());
			}
		}
		sort(rs);
		return rs;
	}
	
	@Override
	public String[] list(FakeFile fake) {
		List<String> rs = listAsList(fake);
		if (rs == null) {
			return null;
		}
		return rs.toArray(new String[rs.size()]);
	}
	
	@Override
	public File[] listFiles(FakeFile fake) {
		return toFiles(fake, fake.list());
	}
	
	@Override
	public String[] list(final FakeFile fake, final FilenameFilter filter) {
		List<String> list = listAsList(fake);
		if (list == null) {
			return null;
		}
		List<String> rs = copyOf(filter(list, 
				new Predicate<String>() {
			@Override
			public boolean apply(String name) {
				return filter.accept(fake, name);
			}}));
		return rs.toArray(new String[rs.size()]);
	}

	public File[] listFiles(final FakeFile dir, Predicate<File> filter) {
		List<String> list = listAsList(dir);
		if (list == null) {
			return null;
		}
		List<File> rs = copyOf(filter(transform(list, 
			new Function<String, File>() {
				@Override
				public File apply(String name) {
					return file(dir, name);
				}}), filter));
		return rs.toArray(new File[rs.size()]);
	}

	@Override
	public File[] listFiles(final FakeFile dir, final FileFilter filter) {
		return listFiles(dir, new Predicate<File>() {
			@Override
			public boolean apply(File f) {
				return filter.accept(f);
			}});
	}
	
	@Override
	public File[] listFiles(final FakeFile dir, final FilenameFilter filter) {
		return listFiles(dir, new Predicate<File>() {
			@Override
			public boolean apply(File f) {
				return filter.accept(dir, f.getName());
			}});
	}
	
	private File[] toFiles(FakeFile dir, String[] files) {
		if (files == null) {
			return null;
		}
		File[] result = new File[files.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = file(dir, files[i]);
		}
		return result;
	}
	
	@Override
	public void deleteOnExit(FakeFile fake) {
		// Id normally check file exists, but this doesnt do much
		// in a fake files system anyway, and you might just want to know
		// if it gets called.
		getMetadata(fake).setDeleteOnExit(true);
	}
	
	@Override
	public boolean renameTo(FakeFile fake, File dest) {
		if (!fake.exists()) {
			return false;
		}
		FakeFile d = file(dest);
		if (d.exists()) {
			return false;
		}
		
		PropStore oldProps = fake.getProperties();
		PropStore newProps = d.getProperties();
		Map<Class<? extends FileProp>, FileProp> newHashMap = newHashMap();
		newProps.setMap(oldProps.setMap(newHashMap));

		Node oNode = lookup(fake);
		Node nNode = lookup(d);
		nNode.nodes = oNode.nodes;
		oNode.nodes = newHashMap();
			
		return true;
	}
	
	@Override
	public void setHidden(FakeFile fake, final boolean hidden) {
		if (fake.exists()) {
			getMetadata(fake).setHidden(hidden);
		}
	}
	
	@Override
	public boolean isHidden(FakeFile fake) {
		return fake.exists() && getMetadata(fake).hidden;
	}
	
	@Override
	public long lastModified(FakeFile fake) {
		if (fake.exists()) {
			return getMetadata(fake).lastModified;
		}
		return 0;
	}
	
	public boolean setLastModified(FakeFile fake, final long millis) {
		if (!fake.exists()) {
			return false;
		}
		getMetadata(fake).setLastModified(millis);
		return true;
	}
	
	@Override
	public void touch(FakeFile fake) {
		fake.setLastModified(currentTimeMillis());
	}
	
	@Override
	public File[] listRoots() {
		return new File[] {file("/")};
	}
	
	@Override
	public boolean setWritable(FakeFile fake, boolean writable, boolean ownerOnly) {
		if (!fake.exists()) {
			return false;
		}
		if (isOwner(fake)) {
			Metadata meta = getMetadata(fake);
			if (ownerOnly) {
				meta.setWritable(writable, meta.allWrite);
			} else {
				meta.setWritable(writable, writable);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean setReadable(FakeFile fake, final boolean readable, boolean ownerOnly) {
		if (!fake.exists()) {
			return false;
		}
		if (isOwner(fake)) {
			Metadata meta = getMetadata(fake);
			if (ownerOnly) {
				meta.setReadable(readable, meta.allRead);
			} else {
				meta.setReadable(readable, readable);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean setReadOnly(FakeFile fake) {
		return setReadable(fake, false);
	}
	
	@Override
	public boolean setExecutable(FakeFile fake, boolean executable, boolean ownerOnly) {
		if (!fake.exists()) {
			return false;
		}
		if (isOwner(fake)) {
			Metadata meta = getMetadata(fake);
			if (ownerOnly) {
				meta.setExecutable(executable, meta.allExecute);
			} else {
				meta.setExecutable(executable, executable);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean canRead(FakeFile fake) {
		if (!fake.exists()) {
			return false;
		}
		Metadata meta = getMetadata(fake);
		if (isOwner(fake)) {
			return meta.ownerRead;
		} else {
			return meta.allRead;
		}
	}

	@Override
	public boolean canWrite(FakeFile fake) {
		if (!fake.exists()) {
			return false;
		}
		Metadata meta = getMetadata(fake);
		if (isOwner(fake)) {
			return meta.ownerWrite;
		} else {
			return meta.allWrite;
		}
	}
	
	@Override
	public boolean canExecute(FakeFile fake) {
		if (!fake.exists()) {
			return false;
		}
		Metadata meta = getMetadata(fake);
		if (isOwner(fake)) {
			return meta.ownerExecute;
		} else {
			return meta.allExecute;
		}
	}

	@Override
	public boolean setWritable(FakeFile fake, boolean writable) {
		return this.setWritable(fake, writable, true);
	}
	
	@Override
	public boolean setExecutable(FakeFile fake, boolean executable) {
		return setExecutable(fake, executable, true);
	}
	
	@Override
	public boolean setReadable(FakeFile fake, boolean readable) {
		return setReadable(fake, readable, true);
	}
	
	public boolean isOwner(FakeFile fake) {
		return true;
	}

	Metadata getMetadata(Node node) {
		PropStore props = node.props;
		if (props == null) {
			node.props = new PropStore();
			props = node.props;
		}
		Metadata meta = props.getProperty(Metadata.class);
		if (meta == null) {
			meta = new Metadata();
			props.withProperty(meta);
		}
		return meta;
	}
	
	Metadata getMetadata(FakeFile fake) {
		Metadata meta = fake.getProperty(Metadata.class);
		if (meta == null) {
			meta = getMetadata(lookup(fake));
			fake.withProperty(meta);
		}
		return meta;
	}

	public void printFS() {
		printFS(0, "/", root);
	}

	private void printFS(int indent, String path, Node node) {
		for (int i = 0; i < indent; i++) {
			System.out.print("  ");
		}
		System.out.print("- " + path);
		if (node.props != null && node.props.hasProperty(Metadata.class)) {
			Metadata meta = node.props.getProperty(Metadata.class);
			System.out.print(" [" + meta + "]");
		}
		System.out.println();
		List<String> files = natural().sortedCopy(node.nodes.keySet());
		for (String f : files) {
			printFS(indent+1, f, node.get(f));
		}
	}
	
	static class Node {
		PropStore props;
		Map<String, Node> nodes = Maps.newHashMap();
		Node get(String name) {
			Node n = nodes.get(name);
			if (n == null) {
				n = new Node();
				nodes.put(name, n);
			}
			return n;
		}
	}
	
	static class Metadata implements FileProp {

		boolean exists = false;
		boolean isFile = false;
		boolean ownerRead    = true;
		boolean ownerWrite   = true;
		boolean ownerExecute = true;
		boolean allRead      = true;
		boolean allWrite     = true;
		boolean allExecute   = true;
		boolean deleteOnExit = false;
		boolean hidden = false;
		long lastModified = 0;

		public Metadata setDefaults() {
			exists = false;
			isFile = false;
			ownerRead    = true;
			ownerWrite   = true;
			ownerExecute = true;
			allRead      = true;
			allWrite     = true;
			allExecute   = true;
			deleteOnExit = false;
			hidden = false;
			lastModified = 0;
			return this;
		}

		public Metadata setReadable(boolean ownerRead, boolean allRead) {
			this.ownerRead = ownerRead;
			this.allRead = allRead;
			return this;
		}

		public Metadata setWritable(boolean ownerWrite, boolean allWrite) {
			this.ownerWrite = ownerWrite;
			this.allWrite = allWrite;
			return this;
		}

		public Metadata setExecutable(boolean ownerExecute, boolean allExecute) {
			this.ownerExecute = ownerExecute;
			this.allExecute = allExecute;
			return this;
		}
		
		public Metadata setLastModified(long millis) {
			this.lastModified = millis;
			return this;
		}
		
		public Metadata setHidden(boolean hidden) {
			this.hidden = hidden;
			return this;
		}
		
		public Metadata setExists(boolean exists) {
			this.exists = exists;
			return this;
		}
		
		public Metadata setFile(boolean isFile) {
			this.isFile = isFile;
			return this;
		}
		
		public Metadata setDeleteOnExit(boolean deleteOnExit) {
			this.deleteOnExit = deleteOnExit;
			return this;
		}
		
	}
	
}
