package review.classdesign.jammy.core.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author fv
 */
public final class NamedObjectTest {

	/** Target name of our mock instance. **/
	private static final String NAME = "bob";

	/**
	 * Test named object behavior.
	 */
	@Test
	public void test() {
		final NamedObject mock = new NamedObject(NAME);
		assertEquals(NAME, mock.getName());
	}

}
