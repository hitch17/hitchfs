package fakefs;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class FakeFile extends File {

	private static final long serialVersionUID = 6833759609396625529L;
	private FakeFileOperations fs;
	
	protected FakeFile(FakeFileOperations fs, File parent, String child) {
		super(parent, child);
		this.fs = fs;
	}

	protected FakeFile(FakeFileOperations fs, String parent, String child) {
		super(parent, child);
		this.fs = fs;
	}

	protected FakeFile(FakeFileOperations fs, String pathname) {
		super(pathname);
		this.fs = fs;
	}

	protected FakeFile(FakeFileOperations fs, URI uri) {
		super(uri);
		this.fs = fs;
	}

	@Override
	public boolean canExecute() {
		return this.fs.canExecute(this);
	}

	@Override
	public boolean canRead() {
		return this.fs.canRead(this);
	}

	@Override
	public boolean canWrite() {
		return this.fs.canWrite(this);
	}

	@Override
	public boolean createNewFile() throws IOException {
		return this.fs.createNewFile(this);
	}

	@Override
	public boolean delete() {
		return this.fs.delete(this);
	}

	@Override
	public void deleteOnExit() {
		this.fs.deleteOnExit(this);
	}

	@Override
	public boolean exists() {
		return this.fs.exists(this);
	}

	@Override
	public File getAbsoluteFile() {
		return this.fs.geAbsoluteFile(this);
	}

	@Override
	public String getAbsolutePath() {
		return this.fs.getAbsolutePath(this);
	}

	@Override
	public File getCanonicalFile() throws IOException {
		return this.fs.getCanonicalFile(this);
	}

	@Override
	public String getCanonicalPath() throws IOException {
		return this.fs.getCanonicalPath(this);
	}

	@Override
	public long getFreeSpace() {
		return this.fs.getFreeSpace(this);
	}

	@Override
	public File getParentFile() {
		return this.fs.getParentFile(this);
	}

	@Override
	public long getTotalSpace() {
		return this.fs.getTotalSpace(this);
	}

	@Override
	public long getUsableSpace() {
		return this.fs.getUsableSpace(this);
	}

	@Override
	public boolean isAbsolute() {
		return this.fs.isAbsolute(this);
	}

	@Override
	public boolean isDirectory() {
		return this.fs.isDirectory(this);
	}

	@Override
	public boolean isFile() {
		return this.fs.isFile(this);
	}

	@Override
	public boolean isHidden() {
		return this.fs.isHidden(this);
	}

	@Override
	public long lastModified() {
		return this.fs.lastModified(this);
	}

	@Override
	public long length() {
		return this.fs.length(this);
	}

	@Override
	public String[] list() {
		return this.fs.list(this);
	}

	@Override
	public String[] list(FilenameFilter filter) {
		return this.fs.list(this, filter);
	}

	@Override
	public File[] listFiles() {
		return this.fs.listFiles(this);
	}

	@Override
	public File[] listFiles(FileFilter filter) {
		return this.fs.listFiles(this, filter);
	}

	@Override
	public File[] listFiles(FilenameFilter filter) {
		return this.fs.listFiles(this, filter);
	}

	@Override
	public boolean mkdir() {
		return this.fs.mkdir(this);
	}

	@Override
	public boolean mkdirs() {
		return this.fs.mkdirs(this);
	}

	@Override
	public boolean renameTo(File dest) {
		return this.fs.renameTo(this, dest);
	}

	@Override
	public boolean setExecutable(boolean executable, boolean ownerOnly) {
		return this.fs.setExecutable(this, executable, ownerOnly);
	}

	@Override
	public boolean setExecutable(boolean executable) {
		return this.fs.setExecutable(this, executable);
	}

	@Override
	public boolean setLastModified(long time) {
		return this.fs.setLastModified(this, time);
	}

	@Override
	public boolean setReadable(boolean readable, boolean ownerOnly) {
		return this.fs.setReadable(this, readable, ownerOnly);
	}

	@Override
	public boolean setReadable(boolean readable) {
		return this.fs.setReadable(this, readable);
	}

	@Override
	public boolean setReadOnly() {
		return this.fs.setReadOnly(this);
	}

	@Override
	public boolean setWritable(boolean writable, boolean ownerOnly) {
		return this.fs.setWritable(this, writable, ownerOnly);
	}

	@Override
	public boolean setWritable(boolean writable) {
		return this.fs.setWritable(this, writable);
	}

	@Override
	public String toString() {
		return this.fs.toString(this);
	}

	@Override
	public URI toURI() {
		return this.fs.toURI(this);
	}

	@Override
	public URL toURL() throws MalformedURLException {
		return this.fs.toURL(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.fs.equals(this, obj);
	}

	@Override
	public int hashCode() {
		return this.fs.hashCode(this);
	}

	@Override
	public int compareTo(File pathname) {
		return this.fs.compareTo(this, pathname);
	}

	@Override
	public String getName() {
		return this.fs.getName(this);
	}

	@Override
	public String getParent() {
		return this.fs.getParent(this);
	}

	@Override
	public String getPath() {
		return this.fs.getPath(this);
	}

	public boolean _canExecute() {
		return super.canExecute();
	}

	public boolean _canRead() {
		return super.canRead();
	}

	public boolean _canWrite() {
		return super.canWrite();
	}

	public int _compareTo(File pathname) {
		return super.compareTo(pathname);
	}

	public boolean _createNewFile() throws IOException {
		return super.createNewFile();
	}

	public boolean _delete() {
		return super.delete();
	}

	public void _deleteOnExit() {
		super.deleteOnExit();
	}

	public boolean _equals(Object obj) {
		return super.equals(obj);
	}

	public boolean _exists() {
		return super.exists();
	}

	public File _getAbsoluteFile() {
		return super.getAbsoluteFile();
	}

	public String _getAbsolutePath() {
		return super.getAbsolutePath();
	}

	public File _getCanonicalFile() throws IOException {
		return super.getCanonicalFile();
	}

	public String _getCanonicalPath() throws IOException {
		return super.getCanonicalPath();
	}

	public long _getFreeSpace() {
		return super.getFreeSpace();
	}

	public String _getName() {
		return super.getName();
	}

	public String _getParent() {
		return super.getParent();
	}

	public File _getParentFile() {
		return super.getParentFile();
	}

	public String _getPath() {
		return super.getPath();
	}

	public long _getTotalSpace() {
		return super.getTotalSpace();
	}

	public long _getUsableSpace() {
		return super.getUsableSpace();
	}

	public int _hashCode() {
		return super.hashCode();
	}

	public boolean _isAbsolute() {
		return super.isAbsolute();
	}

	public boolean _isDirectory() {
		return super.isDirectory();
	}

	public boolean _isFile() {
		return super.isFile();
	}

	public boolean _isHidden() {
		return super.isHidden();
	}

	public long _lastModified() {
		return super.lastModified();
	}

	public long _length() {
		return super.length();
	}

	public String[] _list() {
		return super.list();
	}

	public String[] _list(FilenameFilter filter) {
		return super.list(filter);
	}

	public File[] _listFiles() {
		return super.listFiles();
	}

	public File[] _listFiles(FileFilter filter) {
		return super.listFiles(filter);
	}

	public File[] _listFiles(FilenameFilter filter) {
		return super.listFiles(filter);
	}

	public boolean _mkdir() {
		return super.mkdir();
	}

	public boolean _mkdirs() {
		return super.mkdirs();
	}

	public boolean _renameTo(File dest) {
		return super.renameTo(dest);
	}

	public boolean _setExecutable(boolean executable, boolean ownerOnly) {
		return super.setExecutable(executable, ownerOnly);
	}

	public boolean _setExecutable(boolean executable) {
		return super.setExecutable(executable);
	}

	public boolean _setLastModified(long time) {
		return super.setLastModified(time);
	}

	public boolean _setReadable(boolean readable, boolean ownerOnly) {
		return super.setReadable(readable, ownerOnly);
	}

	public boolean _setReadable(boolean readable) {
		return super.setReadable(readable);
	}

	public boolean _setReadOnly() {
		return super.setReadOnly();
	}

	public boolean _setWritable(boolean writable, boolean ownerOnly) {
		return super.setWritable(writable, ownerOnly);
	}

	public boolean _setWritable(boolean writable) {
		return super.setWritable(writable);
	}

	public String _toString() {
		return super.toString();
	}

	public URI _toURI() {
		return super.toURI();
	}

	@SuppressWarnings("deprecation")
	public URL _toURL() throws MalformedURLException {
		return super.toURL();
	}
	
}
