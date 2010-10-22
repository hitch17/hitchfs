package fakefs;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;


public class MockFileSystem implements FileSystem {

	private final FakeFileOperations mockFS;
	
	public MockFileSystem(FakeFileOperations mockFS) {
		this.mockFS = mockFS;
	}

	public FakeFile file(File parent, String child) {
		return new FakeFile(mockFS, parent, child);
	}

	public FakeFile file(String parent, String child) {
		return new FakeFile(mockFS, parent, child);
	}

	public FakeFile file(String pathname) {
		return new FakeFile(mockFS, pathname);
	}

	public FakeFile file(URI uri) {
		return new FakeFile(mockFS, uri);
	}

	public FakeFile file(File file) {
		return new FakeFile(mockFS, file);
	}

	public InputStream input(File file) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	public InputStream input(String file) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	public InputStream input(FileDescriptor fdObj) {
		throw new UnsupportedOperationException();
	}

	public OutputStream output(File file) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	public OutputStream output(FileDescriptor fdObj) {
		throw new UnsupportedOperationException();
	}

	public OutputStream output(String name) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	public OutputStream output(File file, boolean append)
			throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	public OutputStream output(String name, boolean append)
			throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	public Reader reader(File file) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	public Reader reader(String name) throws FileNotFoundException {
		throw new UnsupportedOperationException();
	}

	public Reader reader(FileDescriptor fdObj) {
		throw new UnsupportedOperationException();
	}

	public Writer writer(File file) throws IOException {
		throw new UnsupportedOperationException();
	}

	public Writer writer(FileDescriptor fdObj) {
		throw new UnsupportedOperationException();
	}

	public Writer writer(String name) throws IOException {
		throw new UnsupportedOperationException();
	}

	public Writer writer(File file, boolean append) throws IOException {
		throw new UnsupportedOperationException();
	}

	public Writer writer(String name, boolean append) throws IOException {
		throw new UnsupportedOperationException();
	}

}
