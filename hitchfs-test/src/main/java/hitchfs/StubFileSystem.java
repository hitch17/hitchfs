package hitchfs;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Lists.newLinkedList;
import static java.io.File.separator;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.LinkedList;

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
/**
 * A stub of a FileSystem for use in testing. 
 */
public class StubFileSystem extends StubFakeFileOperations implements FileSystem {
	
	FakeFileOperations operations = this;

	public StubFileSystem() {}
	
	public StubFileSystem(FakeFileOperations operations) {
		this.operations = operations;
	}
	
	
	public FakeFile file(File parent, String child) {
		return register(new FakeFile(getFileOperations(), parent, child));
	}
	
	public FakeFile file(String parent, String child) {
		return register(new FakeFile(getFileOperations(), parent, child));
	}
	
	public FakeFile file(String pathname) {
		return register(new FakeFile(getFileOperations(), pathname));
	}
	
	public FakeFile file(URI uri) {
		return register(new FakeFile(getFileOperations(), uri));
	}

	public FakeFile file(File regular) {
		if (regular instanceof FakeFile) {
			return register((FakeFile) regular);
		} else {
			return register(new FakeFile(getFileOperations(), regular));
		}
	}
	
	public FakeFileOperations getFileOperations() {
		return operations ;
	}
	
	public FakeFile register(FakeFile file) {
		return file.setAbsoluteKey(repath(file._getPath()));
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
	
	public InputStream input(File file) throws IOException {
		return getInputStream(file(file));
	}

	public InputStream input(String file) throws IOException {
		return input(file(file));
	}

	public OutputStream output(File file) throws IOException {
		return output(file, false);
	}
	
	public OutputStream output(String filename) throws IOException {
		return output(file(filename));
	}

	public OutputStream output(File file, boolean append) throws IOException {
		return getOutputStream(file(file), append);
	}

	public OutputStream output(String filename, boolean append) throws IOException {
		return output(file(filename), append);
	}
	
	public Reader reader(File file) throws IOException {
		return new InputStreamReader(input(file));
	}
	
	public Reader reader(String filename) throws IOException {
		return reader(file(filename));
	}
	
	public Writer writer(File file) throws IOException {
		return new OutputStreamWriter(output(file));
	}
	
	public Writer writer(String filename) throws IOException {
		return writer(file(filename));
	}
	
	public Writer writer(File file, boolean append) throws IOException {
		return writer(file);
	}
	
	public Writer writer(String filename, boolean append) throws IOException {
		return writer(file(filename));
	}
	
	public InputStream input(FileDescriptor fdObj) {
		throw new UnsupportedOperationException();
	}
	
	public OutputStream output(FileDescriptor fdObj) {
		throw new UnsupportedOperationException();
	}
	
	public Reader reader(FileDescriptor fdObj) {
		throw new UnsupportedOperationException();
	}
	
	public Writer writer(FileDescriptor fdObj) {
		throw new UnsupportedOperationException();
	}

}
