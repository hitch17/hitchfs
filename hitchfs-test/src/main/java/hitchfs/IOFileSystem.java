package hitchfs;

import static hitchfs.IOFileSystem.FileState.NONE;
import static hitchfs.IOFileSystem.FileState.READING;
import static hitchfs.IOFileSystem.FileState.WRITING;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

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
			return getContent(fake).getLength();
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

	static interface FileContent {
		InputStream getInputStream(Content content, FakeFile fake) throws IOException;
		OutputStream getOutputStream(Content content, FakeFile fake, boolean append) throws IOException;
		void close();
		void clear();
	}
	
	static class Content implements FileProp {

		AtomicReference<FileState> state = new AtomicReference<FileState>(NONE);
		FileContent content = new Md5Content();
		AtomicLong length = new AtomicLong(0);
		
		public Content() {}

		public Content(FileContent content) {
			this.content = content;
		}

		public OutputStream getOutputStream(FakeFile fake, boolean append) throws IOException {
			ContentOutputStream out = new ContentOutputStream(
					this, fake, content.getOutputStream(this, fake, append));
			if (!append) {
				length.set(0);
			}
			return out;
		}

		public long getLength() {
			if (state.get() == NONE) {
				return length.get();
			} else {
				return 0;
			}
		}
		
		public void setLength(long length) {
			this.length.set(length);
		}
		
		public long incLength() {
			return this.length.incrementAndGet();
		}

		public InputStream getInputStream(FakeFile fake) throws IOException {
			return new ContentInputStream(this, fake, content.getInputStream(this, fake));
		}
		
		public void clear() {
			content.clear();
			length.set(0);
			state.set(NONE);
		}

		public void open(FakeFile fake, FileState newState) throws IOException {
			if (!state.compareAndSet(NONE, newState)) {
				throw new IOException(String.format("File is already open for %s.", state.get()));
			}
		}
		
		public void close(FakeFile fake, FileState expectedState, boolean updateLastModified) throws IOException {
			content.close();
			if (!state.compareAndSet(expectedState, NONE)) {
				throw new IOException(String.format("File was changed from %s state before close.", state));
			}
			if (updateLastModified) {
				fake.touch();
			}
		}

		public FileContent getContent() {
			return this.content;
		}
		
	}
	
	static enum FileState {
		NONE, READING, WRITING
	}
	
	static class ContentOutputStream extends OutputStream {

		private Content content;
		private FakeFile fake;
		private OutputStream output;

		public ContentOutputStream(Content content, FakeFile fake, OutputStream output) throws IOException {
			this.content = content;
			this.output = output;
			this.fake = fake;
			content.open(fake, WRITING);
		}

		@Override
		public void write(int b) throws IOException {
			output.write(b);
			content.incLength();
		}
		
		@Override
		public void close() throws IOException {
			content.close(fake, WRITING, true);
		}
		
	}
	
	static class ContentInputStream extends InputStream {

		private Content content;
		private InputStream input;
		private FakeFile fake;

		public ContentInputStream(Content content, FakeFile fake, InputStream input) throws IOException {
			this.content = content;
			this.fake = fake;
			this.input = input;
			content.open(fake, READING);
		}

		@Override
		public int read() throws IOException {
			return input.read();
		}
		
		@Override
		public void close() throws IOException {
			content.close(fake, READING, false);
		}
		
	}
	
}
