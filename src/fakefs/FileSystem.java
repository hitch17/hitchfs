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

public interface FileSystem {

	public File file(File parent, String child);
	public File file(String parent, String child);
	public File file(String pathname);
	public File file(URI uri);
	public File file(File file);
	
	public InputStream input(File file) throws FileNotFoundException;
	public InputStream input(String file) throws FileNotFoundException;
	public InputStream input(FileDescriptor fdObj);
	
	public OutputStream output(File file) throws FileNotFoundException;
	public OutputStream output(FileDescriptor fdObj);
	public OutputStream output(String name) throws FileNotFoundException;
	public OutputStream output(File file, boolean append) throws FileNotFoundException;
	public OutputStream output(String name, boolean append) throws FileNotFoundException;

	public Reader reader(File file) throws FileNotFoundException;
	public Reader reader(String name) throws FileNotFoundException;
	public Reader reader(FileDescriptor fdObj);
	
	public Writer writer(File file) throws IOException;
	public Writer writer(FileDescriptor fdObj);
	public Writer writer(String name) throws IOException;
	public Writer writer(File file, boolean append) throws IOException;
	public Writer writer(String name, boolean append) throws IOException;

}
