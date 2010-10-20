package fakefs;
import static junit.framework.Assert.assertEquals;

import java.io.File;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import fakefs.FakeFileOperations;
import fakefs.FileSystem;
import fakefs.MockFileSystem;

public class StubFileSystemTest {

	@Test
	public void testFakeTheReal() {
		Mockery mock = new Mockery();
		final FakeFileOperations ops = mock.mock(FakeFileOperations.class);
		mock.checking(new Expectations() {{
			one(ops).length(null);
			will(returnValue(5));
		}});
		
		FileSystem fs = new MockFileSystem(ops);
		File real = new File("fakedir/fakefile.txt");
		File fake = fs.file(real);
		
		assertEquals(real.length(), fake.length());
		mock.assertIsSatisfied();
	}
	
}
