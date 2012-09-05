package gs.hitchin.hitchfs;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 * An interface to file system operations, including getting input/output streams, readers, writers
 * and is the preferred method for getting files.
 */
public interface FileSystem {

	public File file(File parent, String child);
	public File file(String parent, String child);
	public File file(String pathname);
	public File file(URI uri);
	public File file(File file);
	
	public InputStream input(File file) throws IOException;
	public InputStream input(String file) throws IOException;
	public InputStream input(FileDescriptor fdObj);
	
	public OutputStream output(File file) throws IOException;
	public OutputStream output(FileDescriptor fdObj);
	public OutputStream output(String name) throws IOException;
	public OutputStream output(File file, boolean append) throws IOException;
	public OutputStream output(String name, boolean append) throws IOException;

	public Reader reader(File file) throws IOException;
	public Reader reader(String name) throws IOException;
	public Reader reader(FileDescriptor fdObj);
	
	public Writer writer(File file) throws IOException;
	public Writer writer(FileDescriptor fdObj);
	public Writer writer(String name) throws IOException;
	public Writer writer(File file, boolean append) throws IOException;
	public Writer writer(String name, boolean append) throws IOException;

}
