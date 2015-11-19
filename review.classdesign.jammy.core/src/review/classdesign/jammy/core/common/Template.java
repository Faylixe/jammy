package review.classdesign.jammy.core.common;

import java.io.IOException;

import review.classdesign.jammy.core.Jammy;

/**
 * Static toolbox that exposes methods for template managment.
 * 
 * TODO : Consider merge this template content to the concerned class.
 * @author fv
 */
public final class Template {
	
	/** Normalization pattern used for creating project and file name. **/
	private static final String PATTERN = "[^A-Za-z0-9]";

	/** Bundle relative path of the HTML template file. **/
	private static final String HTML_TEMPLATE_PATH = "/templates/problem.template.html";

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private Template() {
		// Do nothing.
	}

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
	 * Getter for the HTML template content.
	 * 
	 * @return HTML page template.
	 * @throws IOException If any error occurs while reading template content.
	 */
	public static String getHTMLTemplate() throws IOException {
		return EclipseUtils.getResource(HTML_TEMPLATE_PATH, Jammy.getDefault().getBundle()); // NOPMD
	}

}
