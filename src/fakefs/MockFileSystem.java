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

	public File file(File parent, String child) {
		return new FakeFile(mockFS, parent, child);
	}

	public File file(String parent, String child) {
		// TODO Auto-generated method stub
		return null;
	}

	public File file(String pathname) {
		// TODO Auto-generated method stub
		return null;
	}

	public File file(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public File file(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream input(File file) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream input(String file) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream input(FileDescriptor fdObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public OutputStream output(File file) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public OutputStream output(FileDescriptor fdObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public OutputStream output(String name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public OutputStream output(File file, boolean append)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public OutputStream output(String name, boolean append)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader reader(File file) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader reader(String name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader reader(FileDescriptor fdObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public Writer writer(File file) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Writer writer(FileDescriptor fdObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public Writer writer(String name) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Writer writer(File file, boolean append) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Writer writer(String name, boolean append) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
