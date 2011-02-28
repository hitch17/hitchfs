package hitchfs;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Lists.newLinkedList;
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
	
	String separator = File.separator;
	char separatorChar = File.separatorChar;
	String pathSeparator = File.pathSeparator;
	char pathSeparatorChar = File.pathSeparatorChar;
	boolean caseSensitive = (separatorChar == '/');
	
	FakeFileOperations operations = this;

	public StubFileSystem() {}
	
	public StubFileSystem(FakeFileOperations operations) {
		this.operations = operations;
	}
	
	public FakeFile file(File parent, String child) {
		return update(new FakeFile(getFileOperations(), parent, child));
	}
	
	public FakeFile file(String parent, String child) {
		return update(new FakeFile(getFileOperations(), parent, child));
	}
	
	public FakeFile file(String pathname) {
		return update(new FakeFile(getFileOperations(), pathname));
	}
	
	public FakeFile file(URI uri) {
		return update(new FakeFile(getFileOperations(), uri));
	}

	public FakeFile file(File regular) {
		if (regular instanceof FakeFile) {
			return update((FakeFile) regular);
		} else {
			return update(new FakeFile(getFileOperations(), regular));
		}
	}
	
	public FakeFileOperations getFileOperations() {
		return operations;
	}
	
	/**
	 * Updates the FakeFile with an absolute key based on the current
	 * directory of this file system.
	 */
	public FakeFile update(FakeFile file) {
		return register(file.setKey(canonical(file.getPathField())));
	}

	public FakeFile register(FakeFile file) {
		return file;
	}

	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}
	
	public String getCurrentDirectory() {
		return System.getProperty("user.dir");
	}

	public Iterable<String> getCurrentDirectorySplit() {
		return skip(asList(getCurrentDirectory().split("/")), 1);
	}
	
	/**
	 * http://www.docjar.com/html/api/java/io/File.java.html
	 */
	public boolean isAbsolute(String path) {
		if (getSeparatorChar() == '\\') {
			// for windows
			if (path.length() > 1 && path.charAt(0) == getSeparatorChar()
					&& path.charAt(1) == getSeparatorChar()) {
				return true;
			}
			if (path.length() > 2) {
				if (Character.isLetter(path.charAt(0)) && path.charAt(1) == ':'
					&& (path.charAt(2) == '/' || path.charAt(2) == '\\')) {
					return true;
				}
			}
			return false;
		}

		// for Linux
		return (path.length() > 0 && path.charAt(0) == getSeparatorChar());
	}
	
	public String getSeparator() {
		return separator;
	}
	
	public char getSeparatorChar() {
		return separatorChar;
	}
	
	public String getPathSeparator() {
		return pathSeparator;
	}

	public char getPathSeparatorChar() {
		return pathSeparatorChar;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	
	public Iterable<String> absoluteSplit(String path) {
		LinkedList<String> ps = newLinkedList();
		if (!isAbsolute(path)) {
			for (String p : getCurrentDirectorySplit()) {
				ps.addLast(p);
			}
		}
		for (String p : path.split(getSeparator())) {
			if (!"".equals(p)) {
				ps.addLast(p);
			}
		}
		return ps;
	}
	
	public String absolute(String path) {
		return makePath(absoluteSplit(path), getSeparator(), true);
	}
	
	public Iterable<String> canonicalSplit(String path) {
		LinkedList<String> ps = newLinkedList();
		if (!path.startsWith(getSeparator())) {
			for (String p : getCurrentDirectorySplit()) {
				ps.addLast(p);
			}
		}
		for (String p : path.split(getSeparator())) {
			if ("..".equals(p)) {
				if (ps.size() > 0) {
					ps.removeLast();
				}
			} else if (!".".equals(p) && !"".equals(p)) {
				ps.addLast(p);
			}
		}
		return ps;
	}
	
	public String canonical(String path) {
		return makePath(canonicalSplit(path), getSeparator(), true);
	}

	public static String makePath(Iterable<String> ps, String pathDelim, boolean absolute) {
		StringBuilder buffer = new StringBuilder();
		if (absolute) {
			buffer.append(pathDelim);
		}
		on(pathDelim).appendTo(buffer, ps);
		return buffer.toString();
	}

	public File createTempFile(String suffix, String prefix)  throws IOException {
		throw new UnsupportedOperationException();
	}

	public File createTempFile(String suffix, String prefix, File directory) throws IOException {
		throw new UnsupportedOperationException();
	}

	public File[] listRoots() {
		throw new UnsupportedOperationException();
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
		return writer(file, false);
	}
	
	public Writer writer(String filename) throws IOException {
		return writer(file(filename));
	}
	
	public Writer writer(File file, boolean append) throws IOException {
		return new OutputStreamWriter(output(file, append));
	}
	
	public Writer writer(String filename, boolean append) throws IOException {
		return writer(file(filename), append);
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
