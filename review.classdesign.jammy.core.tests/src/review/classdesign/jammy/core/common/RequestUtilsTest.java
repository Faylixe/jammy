package review.classdesign.jammy.core.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

/**
 * Test case for {@link RequestUtils} class.
 * 
 * @author fv
 */
public final class RequestUtilsTest {

	/** URL to the test document to retrieve. **/
	private static final String URL = "http://faylixe.github.io/jammy/test.txt";

	/** Expected document content. **/
	private static final String EXPECTED_CONTENT = "Hello test case";

	/**
	 * Test for the {@link RequestUtils#get(String)} static tool method.
	 */
	@Test
	public void testGet() {
		try {
			final String content = RequestUtils.get(URL);
			assertEquals(EXPECTED_CONTENT, content);
		}
		catch (final IOException e) {
			fail("Error occured while retrieving URL content from " + URL + " : " + e.getMessage());
		}
	}

}
