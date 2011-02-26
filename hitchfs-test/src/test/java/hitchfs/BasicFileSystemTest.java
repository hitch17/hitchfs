package hitchfs;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

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
public class BasicFileSystemTest {

	@Test
	public void testBasicFileSystem() throws Exception {
		// must use an non-existent user directory or else some tests will fail.
		System.setProperty("user.dir", "/xyzabc"); 

		BasicFileSystem fs = new BasicFileSystem();
		String filename = "c";
		checkTheSame(new File(filename), fs.file(filename));
		filename = "c/../c";
		checkTheSame(new File(filename), fs.file(filename));
		filename = "/a";
		checkTheSame(new File(filename), fs.file(filename));
		filename = "/a/b/c/d";
		checkTheSame(new File(filename), fs.file(filename));
	}

	@SuppressWarnings("deprecation")
	private void checkTheSame(File real, File fake) throws IOException {
		if (real == null && fake == null) {
			return;
		}
		assertEquals(real.toString(), fake.toString());
		assertEquals(real.getPath(), fake.getPath());
		assertEquals(real.getCanonicalPath(), fake.getCanonicalPath());
		assertEquals(real.getCanonicalFile(), fake.getCanonicalFile());
		assertEquals(real.getAbsolutePath(), fake.getAbsolutePath());
		assertEquals(real.getAbsoluteFile(), fake.getAbsoluteFile());
		assertEquals(real.getName(), fake.getName());
		assertEquals(real.isAbsolute(), fake.isAbsolute());
		assertEquals(0, real.compareTo(fake));
		assertEquals(0, fake.compareTo(real));
		assertTrue(real.equals(fake));
		assertTrue(fake.equals(real));
		assertEquals(real.hashCode(), fake.hashCode());
		assertEquals(real.toURI(), fake.toURI());
		assertEquals(real.toURL(), fake.toURL());
		
		checkTheSame(real.getParentFile(), fake.getParentFile());
		checkTheSame(real.getAbsoluteFile().getParentFile(), fake.getAbsoluteFile().getParentFile());
		checkTheSame(real.getCanonicalFile().getParentFile(), fake.getCanonicalFile().getParentFile());
	}

}
