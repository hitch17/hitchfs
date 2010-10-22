package fakefs;
import static junit.framework.Assert.assertEquals;

import java.io.File;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class StubFileSystemTest {

	@Test
	public void testFakeTheReal() {
		Mockery mock = new Mockery();
		final FakeFileOperations ops = mock.mock(FakeFileOperations.class);
		
		MockFileSystem fs = new MockFileSystem(ops);
		File real = new File("fakedir/fakefile.txt");
		final FakeFile fake = fs.file(real);

		mock.checking(new Expectations() {{
			one(ops).length(fake); will(returnValue(5));
		}});
		
		assertEquals(real.length(), fake.getAbsolutePath());
		mock.assertIsSatisfied();
		
	}
	
}
