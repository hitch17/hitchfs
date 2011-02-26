package hitchfs;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
public class BasicFileSystem extends StubFileSystem {

	public BasicFileSystem() {}

	public BasicFileSystem(FakeFileOperations operations) {
		super(operations);
	}

	@Override
	public String getPath(FakeFile fake) {
		return fake._getPath();
	}
	
	@Override
	public String toString(FakeFile fake) {
		return fake._toString();
	}
	
	@Override
	public String getAbsolutePath(FakeFile fake) {
		return absolute(fake.getPath());
	}

	@Override
	public File getAbsoluteFile(FakeFile fake) {
		return file(fake.getAbsolutePath());
	}
	
	@Override
	public String getParent(FakeFile fake) {
		return fake._getParent();
	}
	
	@Override
	public File getParentFile(FakeFile fake) {
		String parent = fake.getParent();
		if (parent != null) {
			return file(parent);
		} else {
			return null;
		}
	}
	
	@Override
	public String getCanonicalPath(FakeFile fake) throws IOException {
		return canonical(fake.getPath());
	}
	
	@Override
	public File getCanonicalFile(FakeFile fake) throws IOException {
		return file(fake.getCanonicalPath());
	}
	
	@Override
	public String getName(FakeFile fake) {
		return fake._getName();
	}
	
	@Override
	public boolean isAbsolute(FakeFile fake) {
		return !isRelative(fake.getPath());
	}
	
	@Override
	public int compareTo(FakeFile fake, File pathname) {
		return fake.getAbsolutePath().compareTo(pathname.getAbsolutePath());
	}
	
	@Override
	public boolean equals(FakeFile fake, Object obj) {
		return fake._equals(obj);
	}
	
	@Override
	public int hashCode(FakeFile fake) {
		return 1234321 ^ fake.getPath().hashCode();
	}
	
	@Override
	public URI toURI(FakeFile fake) {
		try {
			return new URI("file", null, fake.getAbsolutePath(), null);
		} catch (URISyntaxException x) {
			throw new RuntimeException();
		}	
	}
	
	@Override
	public URL toURL(FakeFile fake) throws MalformedURLException {
		return new URL("file", "", fake.getAbsolutePath());
	}
	
}
