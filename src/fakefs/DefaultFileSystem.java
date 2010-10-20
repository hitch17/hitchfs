package fakefs;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

public class DefaultFileSystem implements FileSystem {

	public File file(File parent, String child) {
		return new File(parent, child);
	}
	
	public File file(String parent, String child) {
		return new File(parent, child);
	}

	public File file(String pathname) {
		return new File(pathname);
	}

	public File file(URI uri) {
		return new File(uri);
	}

	public File file(File file) {
		return file;
	}

	// FileInputStream //

	public FileInputStream input(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public FileInputStream input(String name) throws FileNotFoundException {
		return new FileInputStream(name);
	}

	public FileInputStream input(FileDescriptor fdObj) {
		return new FileInputStream(fdObj);
	}

	// FileOutputStream //

	public FileOutputStream output(File file) throws FileNotFoundException {
		return new FileOutputStream(file);
	}

	public FileOutputStream output(FileDescriptor fdObj) {
		return new FileOutputStream(fdObj);
	}

	public FileOutputStream output(String name) throws FileNotFoundException {
		return new FileOutputStream(name);
	}

	public FileOutputStream output(File file, boolean append) throws FileNotFoundException {
		return new FileOutputStream(file, append);
	}

	public FileOutputStream output(String name, boolean append) throws FileNotFoundException {
		return new FileOutputStream(name, append);
	}
	
	// FileReader //
	
	public FileReader reader(File file) throws FileNotFoundException {
		return new FileReader(file);
	}

	public FileReader reader(String name) throws FileNotFoundException {
		return new FileReader(name);
	}

	public FileReader reader(FileDescriptor fdObj) {
		return new FileReader(fdObj);
	}
	
	// FileWriter //

	public FileWriter writer(File file) throws IOException {
		return new FileWriter(file);
	}

	public FileWriter writer(FileDescriptor fdObj) {
		return new FileWriter(fdObj);
	}

	public FileWriter writer(String name) throws IOException {
		return new FileWriter(name);
	}

	public FileWriter writer(File file, boolean append) throws IOException {
		return new FileWriter(file, append);
	}

	public FileWriter writer(String name, boolean append) throws IOException {
		return new FileWriter(name, append);
	}

}
