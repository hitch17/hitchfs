package hitchfs;

import static hitchfs.IOFileSystem.FileState.NONE;
import static hitchfs.IOFileSystem.FileState.READING;
import static hitchfs.IOFileSystem.FileState.WRITING;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

public class IOFileSystem extends MemoryFileSystem {

	@Override
	public OutputStream getOutputStream(FakeFile fake, boolean append) throws IOException {
		if (fake.exists() && !fake.canWrite()) {
			throw new IOException("No write permission.");
		}
		if (fake.isDirectory()) {
			throw new IOException("Cannot write to a directory.");
		}
		fake.createNewFile();
		Content content = getContent(fake);
		return content.getOutputStream(fake, append);
	}

	@Override
	public InputStream getInputStream(FakeFile fake) throws IOException {
		if (!fake.exists()) {
			throw new FileNotFoundException(fake.getPath() + " not found.");
		}
		if (!fake.canRead()) {
			throw new IOException("No read permission.");
		}
		if (fake.isDirectory()) {
			throw new IOException("Cannot read a directory.");
		}
		Content content = getContent(fake);
		return content.getInputStream(fake);
	}

	public long length(FakeFile fake) {
		if (!fake.exists()) {
			return 0;
		} else {
			return getContent(fake).data.length;
		}
	}
	
	public boolean delete(FakeFile fake) {
		boolean r = super.delete(fake);
		if (r) {
			getContent(fake).clear();
		}
		return r;
	}
	
	Content getContent(FakeFile fake) {
		Content c = fake.getProperty(Content.class);
		if (c == null) {
			c = new Content();
			fake.withProperty(c);
		}
		return c;
	}

	static class Content implements FileProp {

		AtomicReference<FileState> state = new AtomicReference<FileState>(NONE);
		byte[] data = new byte[0];
		
		public OutputStream getOutputStream(FakeFile fake, boolean append) throws IOException {
			if (!state.compareAndSet(NONE, WRITING)) {
				throw new IOException("File is already open.");
			}
			return new ContentOutputStream(fake, this, append);
		}

		public InputStream getInputStream(FakeFile fake) throws IOException {
			if (!state.compareAndSet(NONE, READING)) {
				throw new IOException("File is already open.");
			}
			return new ContentInputStream(this);
		}
		
		public void clear() {
			this.data = new byte[0];
			state.set(FileState.NONE);
		}
		
	}
	
	static enum FileState {
		NONE, READING, WRITING
	}
	
	static class ContentOutputStream extends OutputStream {

		private Content content;
		private FakeFile fake;
		private ByteArrayOutputStream output;

		public ContentOutputStream(FakeFile fake, Content content, boolean append) throws IOException {
			this.fake = fake;
			this.content = content;
			if (append) {
				this.output = new ByteArrayOutputStream(content.data.length);
				this.output.write(content.data);
			} else {
				this.output = new ByteArrayOutputStream();
			}
			this.content.data = new byte[0];
		}

		@Override
		public void write(int b) throws IOException {
			output.write(b);
		}
		
		@Override
		public void close() throws IOException {
			content.data = output.toByteArray();
			if (!content.state.compareAndSet(WRITING, NONE)) {
				throw new IOException("File was changed from writing state before close.");
			}
			fake.touch();
		}
		
	}
	
	static class ContentInputStream extends InputStream {

		private Content content;
		private ByteArrayInputStream input;

		public ContentInputStream(Content content) {
			this.content = content;
			this.input = new ByteArrayInputStream(content.data);
		}

		@Override
		public int read() throws IOException {
			return input.read();
		}
		
		@Override
		public void close() throws IOException {
			if (!content.state.compareAndSet(READING, NONE)) {
				throw new IOException("File was changed from reading state before close.");
			}
		}
		
	}
	
}
