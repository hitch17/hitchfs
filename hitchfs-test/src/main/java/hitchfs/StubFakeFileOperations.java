package hitchfs;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
 * A stub of FakeFileOperations for use in testing.
 */
public class StubFakeFileOperations implements FakeFileOperations {

	public boolean canExecute(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean canRead(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean canWrite(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public int compareTo(FakeFile fake, File pathname) {
		throw new UnsupportedOperationException();
	}

	public boolean createNewFile(FakeFile fake) throws IOException {
		throw new UnsupportedOperationException();
	}

	public boolean delete(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public void deleteOnExit(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean equals(FakeFile fake, Object obj) {
		throw new UnsupportedOperationException();
	}

	public boolean exists(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public File geAbsoluteFile(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public String getAbsolutePath(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public File getCanonicalFile(FakeFile fake) throws IOException {
		throw new UnsupportedOperationException();
	}

	public String getCanonicalPath(FakeFile fake) throws IOException {
		throw new UnsupportedOperationException();
	}

	public long getFreeSpace(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public String getName(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public String getParent(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public File getParentFile(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public String getPath(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public long getTotalSpace(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public long getUsableSpace(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public int hashCode(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean isAbsolute(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean isDirectory(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean isFile(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean isHidden(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public long lastModified(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public long length(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public String[] list(FakeFile fake, FilenameFilter filter) {
		throw new UnsupportedOperationException();
	}

	public String[] list(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public File[] listFiles(FakeFile fake, FileFilter filter) {
		throw new UnsupportedOperationException();
	}

	public File[] listFiles(FakeFile fake, FilenameFilter filter) {
		throw new UnsupportedOperationException();
	}

	public File[] listFiles(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean mkdir(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean mkdirs(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean renameTo(FakeFile fake, File dest) {
		throw new UnsupportedOperationException();
	}

	public boolean setExecutable(FakeFile fake, boolean executable,
			boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	public boolean setExecutable(FakeFile fake, boolean executable) {
		throw new UnsupportedOperationException();
	}

	public boolean setLastModified(FakeFile fake, long time) {
		throw new UnsupportedOperationException();
	}

	public boolean setReadOnly(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public boolean setReadable(FakeFile fake, boolean readable,
			boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	public boolean setReadable(FakeFile fake, boolean readable) {
		throw new UnsupportedOperationException();
	}

	public boolean setWritable(FakeFile fake, boolean writable,
			boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	public boolean setWritable(FakeFile fake, boolean writable) {
		throw new UnsupportedOperationException();
	}

	public String toString(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public URI toURI(FakeFile fake) {
		throw new UnsupportedOperationException();
	}

	public URL toURL(FakeFile fake) throws MalformedURLException {
		throw new UnsupportedOperationException();
	}
	
	public InputStream getInputStream(FakeFile fake) throws IOException {
		throw new UnsupportedOperationException();
	}

	public OutputStream getOutputStream(FakeFile fake, boolean append) throws IOException {
		throw new UnsupportedOperationException();
	}

}
