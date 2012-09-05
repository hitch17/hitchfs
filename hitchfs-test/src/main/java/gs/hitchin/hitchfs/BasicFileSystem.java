package gs.hitchin.hitchfs;

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

	@Override
	public String getPath(FakeFile fake) {
		return fake.getPathField();
	}
	
	@Override
	public String toString(FakeFile fake) {
		return fake.getPath();
	}
	
	@Override
	public String getAbsolutePath(FakeFile fake) {
		return absolute(fake.getPath());
	}

	@Override
	public File getAbsoluteFile(FakeFile fake) {
		return file(fake.getAbsolutePath());
	}

	/**
	 * http://www.docjar.com/html/api/java/io/File.java.html
	 */
	@Override
	public String getParent(FakeFile fake) {
		String path = fake.getPath();
		int length = path.length(), firstInPath = 0;
		if (getSeparatorChar() == '\\' && length > 2 && path.charAt(1) == ':') {
			firstInPath = 2;
		}
		int index = path.lastIndexOf(getSeparatorChar());
		if (index == -1 && firstInPath > 0) {
			index = 2;
		}
		if (index == -1 || path.charAt(length - 1) == getSeparatorChar()) {
			return null;
		}
		if (path.indexOf(getSeparatorChar()) == index
				&& path.charAt(firstInPath) == getSeparatorChar()) {
			return path.substring(0, index + 1);
		}
		return path.substring(0, index);
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
	
	/**
	 * http://www.docjar.com/html/api/java/io/File.java.html
	 */
	@Override
	public String getName(FakeFile fake) {
		String path = fake.getPath();
		int separatorIndex = path.lastIndexOf(separator);
		return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1,
				path.length());	
	}
	
	@Override
	public boolean isAbsolute(FakeFile fake) {
		return isAbsolute(fake.getPath());
	}
	
	@Override
	public int compareTo(FakeFile fake, File pathname) {
		return fake.getAbsolutePath().compareTo(pathname.getAbsolutePath());
	}
	
	@Override
	public boolean equals(FakeFile fake, Object obj) {
		if (!(obj instanceof File)) {
			return false;
		}
		String path = fake.getPath();
		if (!caseSensitive) {
			return path.equalsIgnoreCase(((File) obj).getPath());
		}
		return path.equals(((File) obj).getPath());	
	}
	
	@Override
	public int hashCode(FakeFile fake) {
		String path = fake.getPath();
		if (caseSensitive) {
			return path.hashCode() ^ 1234321;
		}
		return path.toLowerCase().hashCode() ^ 1234321;
	}
	
	/**
	 * http://www.docjar.com/html/api/java/io/File.java.html
	 */
	@Override
	public URI toURI(FakeFile fake) {
		String name = fake.getAbsolutePath();
		try {
			if (!name.startsWith("/")) {
				// start with sep.
				return new URI("file", null, new StringBuilder(
						name.length() + 1).append('/').append(name).toString(),
						null, null);
			} else if (name.startsWith("//")) {
				return new URI("file", "", name, null); // UNC path
			}
			return new URI("file", null, name, null, null);
		} catch (URISyntaxException e) {
			// this should never happen
			return null;
		}	
	}
	
	/**
	 * http://www.docjar.com/html/api/java/io/File.java.html
	 */
	@Override
	public URL toURL(FakeFile fake) throws MalformedURLException {
		String name = fake.getAbsolutePath();
		if (!name.startsWith("/")) {
			// start with sep.
			return new URL(
					"file", "", -1, new StringBuilder(name.length() + 1) 
					.append('/').append(name).toString(), null);
		} else if (name.startsWith("//")) {
			return new URL("file:" + name); // UNC path
		}
		return new URL("file", "", -1, name, null);	
	}
	
	
}
