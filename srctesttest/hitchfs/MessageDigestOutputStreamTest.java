package hitchfs;

import static junit.framework.Assert.assertEquals;

import hitchfs.MessageDigestOutputStream;

import java.io.ByteArrayOutputStream;

import org.junit.Test;


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
public class MessageDigestOutputStreamTest {

	@Test
	public void testMd2() {
		// Ensure it does not throw a NoSuchAlgorithmException
		MessageDigestOutputStream.md2();
	}

	@Test
	public void testMd5() throws Exception {
		MessageDigestOutputStream out = MessageDigestOutputStream.md5();
		out.write("fake file with text.".getBytes());
		out.close();
		assertEquals("9d2110c9a94894f10cfee35afaf8ceb2", out.getDigestAsHex());
	}

	@Test
	public void testSha1() throws Exception {
		MessageDigestOutputStream out = MessageDigestOutputStream.sha1();
		out.write("fake file with text.".getBytes());
		out.close();
		assertEquals("9bd737f825e09c898e81cb81d8db4743a7b0c2e0", out.getDigestAsHex());
	}

	@Test
	public void testSha256() throws Exception {
		MessageDigestOutputStream out = MessageDigestOutputStream.sha256();
		out.write("fake file with text.".getBytes());
		out.close();
		assertEquals("14b6b2d38f2e7440dc4d7ba33cd96bc53a5b670796b04309f0095b9bfe95ca26", out.getDigestAsHex());
	}

	@Test
	public void testSha384() throws Exception {
		MessageDigestOutputStream out = MessageDigestOutputStream.sha384();
		out.write("fake file with text.".getBytes());
		out.close();
		assertEquals("e41c00c09baad897308594cd3776323906a14ad9bc618e8584d5fa11f21ee591" +
				"e0b84b1a1218477dd5c16c0bb46e6ad2", out.getDigestAsHex());
	}

	@Test
	public void testSha512() throws Exception {
		MessageDigestOutputStream out = MessageDigestOutputStream.sha512();
		out.write("fake file with text.".getBytes());
		out.close();
		assertEquals("f187d7e2b9f0dbd6054357faeccbb4fb82416767f0a45cb682f70f73bda1a974" +
				"cac2cfbf07355336ce86a2c49ba4da26f451552474cfb637dd2bcccae09ef2ea", out.getDigestAsHex());
	}

	@Test
	public void testWithOutput() throws Exception {
		String string = "fake file with text.";
		MessageDigestOutputStream out = MessageDigestOutputStream.sha512();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(string.length());
		out.setOutput(baos);
		out.write(string.getBytes());
		out.close();
		assertEquals("f187d7e2b9f0dbd6054357faeccbb4fb82416767f0a45cb682f70f73bda1a974" +
				"cac2cfbf07355336ce86a2c49ba4da26f451552474cfb637dd2bcccae09ef2ea", out.getDigestAsHex());
		assertEquals(string, new String(baos.toByteArray()));
	}
	
}
