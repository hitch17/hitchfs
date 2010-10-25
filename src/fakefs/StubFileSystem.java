package fakefs;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

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
	
	public FakeFile file(File parent, String child) {
		return new FakeFile(this, parent, child);
	}
	
	public FakeFile file(String parent, String child) {
		return new FakeFile(this, parent, child);
	}
	
	public FakeFile file(String pathname) {
		return new FakeFile(this, pathname);
	}
	
	public FakeFile file(URI uri) {
		return new FakeFile(this, uri);
	}

	public FakeFile file(File regular) {
		return new FakeFile(this, regular);
	}	
	
	public InputStream input(File file) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}
	
	public InputStream input(String file) throws FileNotFoundException {
		return this.input(this.file(file));
	}

	public OutputStream output(File file) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}
	
	public OutputStream output(String filename) throws FileNotFoundException {
		return this.output(this.file(filename));
	}

	public OutputStream output(File file, boolean append) throws FileNotFoundException {
		return this.output(file);
	}

	public OutputStream output(String filename, boolean append) throws FileNotFoundException {
		return this.output(filename);
	}
	
	public Reader reader(File file) throws FileNotFoundException {
		return new InputStreamReader(this.input(file));
	}
	
	public Reader reader(String filename) throws FileNotFoundException {
		return this.reader(this.file(filename));
	}
	
	public Writer writer(File file) throws IOException {
		return new OutputStreamWriter(this.output(file));
	}
	
	public Writer writer(String filename) throws IOException {
		return this.writer(this.file(filename));
	}
	
	public Writer writer(File file, boolean append) throws IOException {
		return this.writer(file);
	}
	
	public Writer writer(String filename, boolean append) throws IOException {
		return this.writer(this.file(filename));
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
