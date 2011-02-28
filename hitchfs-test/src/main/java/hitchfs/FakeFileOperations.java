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
 * This interface is provided for FakeFile call backs, mirroring the methods that File provides.
 */
public interface FakeFileOperations {

	public abstract boolean canExecute(FakeFile fake);

	public abstract boolean canRead(FakeFile fake);

	public abstract boolean canWrite(FakeFile fake);

	public abstract boolean createNewFile(FakeFile fake) throws IOException;

	public abstract boolean delete(FakeFile fake);

	public abstract void deleteOnExit(FakeFile fake);

	public abstract boolean exists(FakeFile fake);

	public abstract long getFreeSpace(FakeFile fake);

	public abstract long getTotalSpace(FakeFile fake);

	public abstract long getUsableSpace(FakeFile fake);

	public abstract boolean isAbsolute(FakeFile fake);

	public abstract boolean isDirectory(FakeFile fake);

	public abstract boolean isFile(FakeFile fake);

	public abstract boolean isHidden(FakeFile fake);
	
	public abstract void setHidden(FakeFile fakeFile, boolean hidden);

	public abstract long lastModified(FakeFile fake);

	public abstract long length(FakeFile fake);

	public abstract String[] list(FakeFile fake, FilenameFilter filter);

	public abstract String[] list(FakeFile fake);

	public abstract File[] listFiles(FakeFile fake, FileFilter filter);

	public abstract File[] listFiles(FakeFile fake, FilenameFilter filter);

	public abstract File[] listFiles(FakeFile fake);

	public abstract boolean mkdir(FakeFile fake);

	public abstract boolean mkdirs(FakeFile fake);

	public abstract boolean renameTo(FakeFile fake, File dest);

	public abstract boolean setExecutable(FakeFile fake,
			boolean executable, boolean ownerOnly);

	public abstract boolean setExecutable(FakeFile fake, boolean executable);

	public abstract boolean setLastModified(FakeFile fake, long time);

	public abstract boolean setReadable(FakeFile fake, boolean readable,
			boolean ownerOnly);

	public abstract boolean setReadable(FakeFile fake, boolean readable);

	public abstract boolean setReadOnly(FakeFile fake);

	public abstract boolean setWritable(FakeFile fake, boolean writable,
			boolean ownerOnly);

	public abstract boolean setWritable(FakeFile fake, boolean writable);

	public abstract URI toURI(FakeFile fake);

	/**
	 * @deprecated because File.toURL() is @deprecated
	 */
	@Deprecated
	public abstract URL toURL(FakeFile fake) throws MalformedURLException;

	public abstract File getAbsoluteFile(FakeFile fake);

	public abstract String getAbsolutePath(FakeFile fake);

	public abstract File getCanonicalFile(FakeFile fake) throws IOException;

	public abstract String getCanonicalPath(FakeFile fake)
			throws IOException;

	public abstract File getParentFile(FakeFile fake);

	public abstract String toString(FakeFile fake);

	public abstract boolean equals(FakeFile fake, Object obj);

	public abstract int hashCode(FakeFile fake);

	public abstract int compareTo(FakeFile fake, File pathname);

	public abstract String getName(FakeFile fake);

	public abstract String getParent(FakeFile fake);

	public abstract String getPath(FakeFile fake);
	
	public abstract InputStream getInputStream(FakeFile fake) throws IOException;

	public abstract OutputStream getOutputStream(FakeFile fake, boolean append) throws IOException;

	public abstract void touch(FakeFile fake);

	public abstract char getSeparatorChar();

	public abstract String getSeparator();
	
	public abstract String getPathSeparator();

	public abstract char getPathSeparatorChar();
	
	public abstract boolean isCaseSensitive();
	
}
