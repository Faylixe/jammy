package review.classdesign.jammy.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import review.classdesign.jammy.Jammy;

/**
 * This class allows 
 * @author fv
 */
public final class TemplateLoader {

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private TemplateLoader() {
		// Do nothing.
	}

	/**
	 * Loads the template file from the given plugin based <tt>path</tt>.
	 * 
	 * @param path Path to load template file from.
	 * @return Read template.
	 */
	public static String load(final String path) {
		final Class<?> loader = Jammy.class;
		final InputStream stream = loader.getResourceAsStream(path);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().collect(Collectors.joining("\n"));
	}

}
