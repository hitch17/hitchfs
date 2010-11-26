package hitchfs;

import static java.lang.String.format;
import static java.security.MessageDigest.getInstance;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
/**
 * Provides fake output stream that discards output, but records a message 
 * digest of the written bytes for verification later.
 */
public class MessageDigestOutputStream extends OutputStream {

	private final MessageDigest digest;
	private byte[] bytes;
	private boolean closed;
	private OutputStream output;

	public MessageDigestOutputStream(MessageDigest digest) {
		this.digest = digest;
	}
	
	@Override
	public void write(int b) throws IOException {
		digest.update((byte) (0xFF & b));
		if (output != null) {
			output.write(b);
		}
	}

	public byte[] getDigest() {
		if (this.bytes == null) {
			this.bytes = this.digest.digest();
		}
		return this.bytes;
	}
	
	public String getDigestAsHex() {
		StringBuilder hexString = new StringBuilder();
		for (byte b : this.getDigest()) {
			hexString.append(format("%02x", b));
		}
		return hexString.toString();
	}

	public void close() throws IOException {
		this.closed = true;
		if (output != null) {
			this.output.close();
		}
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void setOutput(OutputStream output) {
		this.output = output;
	}
	
	public boolean isEqual(byte[] expected){
		return MessageDigest.isEqual(getDigest(), expected);
	}

	public static MessageDigestOutputStream output(String algo) {
		try {
			return new MessageDigestOutputStream(getInstance(algo));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static MessageDigestOutputStream md2() {
		return output("md2");
	}

	public static MessageDigestOutputStream md5() {
		return output("md5");
	}
	
	public static MessageDigestOutputStream sha1() {
		return output("sha-1");
	}
	
	public static MessageDigestOutputStream sha256() {
		return output("sha-256");
	}
	
	public static MessageDigestOutputStream sha384() {
		return output("sha-384");
	}
	
	public static MessageDigestOutputStream sha512() {
		return output("sha-512");
	}

}
