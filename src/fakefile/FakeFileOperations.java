package fakefile;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
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

	public abstract boolean canExecute(FakeFile MockFile);

	public abstract boolean canRead(FakeFile MockFile);

	public abstract boolean canWrite(FakeFile MockFile);

	public abstract boolean createNewFile(FakeFile MockFile) throws IOException;

	public abstract boolean delete(FakeFile MockFile);

	public abstract void deleteOnExit(FakeFile MockFile);

	public abstract boolean exists(FakeFile MockFile);

	public abstract long getFreeSpace(FakeFile MockFile);

	public abstract long getTotalSpace(FakeFile MockFile);

	public abstract long getUsableSpace(FakeFile MockFile);

	public abstract boolean isAbsolute(FakeFile MockFile);

	public abstract boolean isDirectory(FakeFile MockFile);

	public abstract boolean isFile(FakeFile MockFile);

	public abstract boolean isHidden(FakeFile MockFile);

	public abstract long lastModified(FakeFile MockFile);

	public abstract long length(FakeFile MockFile);

	public abstract String[] list(FakeFile MockFile, FilenameFilter filter);

	public abstract String[] list(FakeFile MockFile);

	public abstract File[] listFiles(FakeFile MockFile, FileFilter filter);

	public abstract File[] listFiles(FakeFile MockFile, FilenameFilter filter);

	public abstract File[] listFiles(FakeFile MockFile);

	public abstract boolean mkdir(FakeFile MockFile);

	public abstract boolean mkdirs(FakeFile MockFile);

	public abstract boolean renameTo(FakeFile MockFile, File dest);

	public abstract boolean setExecutable(FakeFile MockFile,
			boolean executable, boolean ownerOnly);

	public abstract boolean setExecutable(FakeFile MockFile, boolean executable);

	public abstract boolean setLastModified(FakeFile MockFile, long time);

	public abstract boolean setReadable(FakeFile MockFile, boolean readable,
			boolean ownerOnly);

	public abstract boolean setReadable(FakeFile MockFile, boolean readable);

	public abstract boolean setReadOnly(FakeFile MockFile);

	public abstract boolean setWritable(FakeFile MockFile, boolean writable,
			boolean ownerOnly);

	public abstract boolean setWritable(FakeFile MockFile, boolean writable);

	public abstract URI toURI(FakeFile MockFile);

	/**
	 * @deprecated because File.toURL() is @deprecated
	 */
	@Deprecated
	public abstract URL toURL(FakeFile MockFile) throws MalformedURLException;

	public abstract File geAbsoluteFile(FakeFile MockFile);

	public abstract String getAbsolutePath(FakeFile MockFile);

	public abstract File getCanonicalFile(FakeFile MockFile) throws IOException;

	public abstract String getCanonicalPath(FakeFile MockFile)
			throws IOException;

	public abstract File getParentFile(FakeFile MockFile);

	public abstract String toString(FakeFile MockFile);

	public abstract boolean equals(FakeFile MockFile, Object obj);

	public abstract int hashCode(FakeFile MockFile);

	public abstract int compareTo(FakeFile MockFile, File pathname);

	public abstract String getName(FakeFile MockFile);

	public abstract String getParent(FakeFile MockFile);

	public abstract String getPath(FakeFile MockFile);
	
}
