package fakefs;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class StubFakeFileOperations implements FakeFileOperations {

	public boolean canExecute(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean canRead(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean canWrite(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public int compareTo(FakeFile MockFile, File pathname) {
		throw new UnsupportedOperationException();
	}

	public boolean createNewFile(FakeFile MockFile) throws IOException {
		throw new UnsupportedOperationException();
	}

	public boolean delete(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public void deleteOnExit(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean equals(FakeFile MockFile, Object obj) {
		throw new UnsupportedOperationException();
	}

	public boolean exists(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public File geAbsoluteFile(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public String getAbsolutePath(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public File getCanonicalFile(FakeFile MockFile) throws IOException {
		throw new UnsupportedOperationException();
	}

	public String getCanonicalPath(FakeFile MockFile) throws IOException {
		throw new UnsupportedOperationException();
	}

	public long getFreeSpace(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public String getName(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public String getParent(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public File getParentFile(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public String getPath(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public long getTotalSpace(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public long getUsableSpace(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public int hashCode(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean isAbsolute(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean isDirectory(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean isFile(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean isHidden(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public long lastModified(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public long length(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public String[] list(FakeFile MockFile, FilenameFilter filter) {
		throw new UnsupportedOperationException();
	}

	public String[] list(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public File[] listFiles(FakeFile MockFile, FileFilter filter) {
		throw new UnsupportedOperationException();
	}

	public File[] listFiles(FakeFile MockFile, FilenameFilter filter) {
		throw new UnsupportedOperationException();
	}

	public File[] listFiles(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean mkdir(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean mkdirs(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean renameTo(FakeFile MockFile, File dest) {
		throw new UnsupportedOperationException();
	}

	public boolean setExecutable(FakeFile MockFile, boolean executable,
			boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	public boolean setExecutable(FakeFile MockFile, boolean executable) {
		throw new UnsupportedOperationException();
	}

	public boolean setLastModified(FakeFile MockFile, long time) {
		throw new UnsupportedOperationException();
	}

	public boolean setReadOnly(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public boolean setReadable(FakeFile MockFile, boolean readable,
			boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	public boolean setReadable(FakeFile MockFile, boolean readable) {
		throw new UnsupportedOperationException();
	}

	public boolean setWritable(FakeFile MockFile, boolean writable,
			boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	public boolean setWritable(FakeFile MockFile, boolean writable) {
		throw new UnsupportedOperationException();
	}

	public String toString(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public URI toURI(FakeFile MockFile) {
		throw new UnsupportedOperationException();
	}

	public URL toURL(FakeFile MockFile) throws MalformedURLException {
		throw new UnsupportedOperationException();
	}

}
