package review.classdesign.jammy.core.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import review.classdesign.jammy.core.Jammy;

/**
 * Static toolbox that exposes methods for template managment.
 * 
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
	 * Retrieves the template file content of the given <tt>path</tt>
	 * using the given <tt>loader</tt> for retrieving the file stream.
	 * 
	 * @param path Path to retrieve template file from.
	 * @param bundle Bundle the path is relative to.
	 * @return Content read from the required template file.
	 * @throws IOException If any error occurs while reading template content.
	 */
	public static String getTemplate(final String path, final Bundle bundle) throws IOException {
		final URL url = FileLocator.find(bundle, new Path(path), null);
		final InputStream stream = url.openStream();	
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		final Stream<String> lines = reader.lines();
		return lines.collect(Collectors.joining("\n"));
	}
	
	/**
	 * Getter for the HTML template content.
	 * 
	 * @return HTML page template.
	 * @throws IOException If any error occurs while reading template content.
	 */
	public static String getHTMLTemplate() throws IOException {
		return getTemplate(HTML_TEMPLATE_PATH, Jammy.getDefault().getBundle()); // NOPMD
	}

}
