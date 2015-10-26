package review.classdesign.jammy.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import review.classdesign.jammy.Jammy;

/**
 * Static toolbox that exposes methods for template managment.
 * 
 * @author fv
 */
public final class Template {
	
	/**
	 * Private constructor for avoiding instantiation.
	 */
	private Template() {
		// Do nothing.
	}
	
	/** Normalization pattern used for creating project and file name. **/
	private static final String PATTERN = "[^A-Za-z0-9]";

	/** Bundle relative path of the HTML template file. **/
	private static final String HTML_TEMPLATE_PATH = "/templates/problem.template.html";

	/**
	 * Normalizes the given <tt>name</tt> by removing
	 * all non alphanumeric character.
	 * 
	 * @param name Name to normalize.
	 * @return Normalized name.
	 */
	public static String normalize(final String name) {
		return name.replaceAll(PATTERN, "");
	}

	/**
	 * Retrieves the template file content of the given <tt>path</tt>
	 * using the given <tt>loader</tt> for retrieving the file stream.
	 * 
	 * @param path Path to retrieve template file from.
	 * @param loader Class instance used to retrieve template file stream from.
	 * @return
	 */
	public static String getTemplate(final String path, final Class<?> loader) {
		// TODO : Consider using file locator instead.
		final InputStream stream = loader.getResourceAsStream(path);	
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().collect(Collectors.joining("\n"));
	}
	
	/**
	 * Getter for the HTML template content.
	 * 
	 * @return HTML page template.
	 */
	public static String getHTMLTemplate() {
		return getTemplate(HTML_TEMPLATE_PATH, Jammy.class);
	}

}
