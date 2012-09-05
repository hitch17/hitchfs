package gs.hitchin.hitchfs;

import gs.hitchin.hitchfs.IOFileSystem.Content;
import gs.hitchin.hitchfs.IOFileSystem.FileContent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Md5Content implements FileContent {

	private MessageDigestOutputStream output;
	private String digest;

	@Override
	public void clear() {
		this.digest = null;
	}

	@Override
	public void close() {
		if (this.output != null) {
			this.digest = output.getDigestAsHex();
		}
	}

	@Override
	public InputStream getInputStream(Content content, FakeFile fake) throws IOException {
		return new ByteArrayInputStream(new byte[0]);
	}

	@Override
	public OutputStream getOutputStream(Content content, FakeFile fake, boolean append) throws IOException {
		output = MessageDigestOutputStream.md5();
		return output;
	}
	
	public String getDigest() {
		return digest;
	}
	
}
