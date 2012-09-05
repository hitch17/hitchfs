package gs.hitchin.hitchfs;

import gs.hitchin.hitchfs.IOFileSystem.Content;
import gs.hitchin.hitchfs.IOFileSystem.FileContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteArrayContent implements FileContent {
	
	ByteArrayOutputStream output = null;
	byte[] data = new byte[0];

	public ByteArrayContent() {}

	public ByteArrayContent(byte[] data) {
		this.data = data;
	}
	
	@Override
	public void clear() {
		this.data = new byte[0];
		this.output = null;
	}
	
	@Override
	public InputStream getInputStream(Content content, FakeFile fake) {
		return new ByteArrayInputStream(data);
	}
	
	@Override
	public OutputStream getOutputStream(Content content, FakeFile fake, boolean append) throws IOException {
		if (append) {
			this.output = new ByteArrayOutputStream(this.data.length);
			this.output.write(this.data);
		} else {
			this.output = new ByteArrayOutputStream();
		}
		this.data = new byte[0];
		return output;
	}
	
	@Override
	public void close() {
		if (this.output != null) {
			data = this.output.toByteArray();
		}
	}

	public static Content byteArrayContent() {
		return byteArrayContent(new byte[0]);
	}

	public static Content byteArrayContent(CharSequence data) {
		byte[] bs = new byte[data.length()];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte) data.charAt(i);
		}
		return new Content(new ByteArrayContent(bs));
	}

	public static Content byteArrayContent(byte[] data) {
		return new Content(new ByteArrayContent(data));
	}

}
