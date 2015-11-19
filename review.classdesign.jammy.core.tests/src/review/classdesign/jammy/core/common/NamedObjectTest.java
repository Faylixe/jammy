package review.classdesign.jammy.core.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import review.classdesign.jammy.core.Jammy;

import com.google.gson.Gson;

/**
 * Test case for {@link NamedObject} class.
 * 
 * @author fv
 */
public final class NamedObjectTest {

	/** Target name of our mock instance. **/
	private static final String NAME = "bob";

	/** Path of the target json named object file relative to this bundle. **/
	private static final String PATH = "resources/named.object.json";

	/**
	 * Test named object behavior from default instantiation.
	 */
	@Test
	public void testDefault() {
		final NamedObject object = new NamedObject(NAME);
		assertEquals(NAME, object.getName());
	}
	
	/**
	 * Test named object behavior through JSON built object.
	 */
	@Test
	public void testSerialized() {
		final Gson parser = new Gson();
		try {
			final String json = EclipseUtils.getResource(PATH, Jammy.getDefault().getBundle());
			final NamedObject object = parser.fromJson(json, NamedObject.class);
			assertEquals(NAME, object.getName());
		}
		catch (final IOException e) {
			fail("Error while loading named object : " + e.getMessage());
		}
	}

}
