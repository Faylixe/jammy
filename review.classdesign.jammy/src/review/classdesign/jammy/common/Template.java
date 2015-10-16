package review.classdesign.jammy.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import review.classdesign.jammy.Jammy;

/**
 * 
 * @author fv
 */
public enum Template {

	/**
	 * 
	 */
	SOLVER("/templates/solution.template.java"),
	
	/**
	 * 
	 */
	DESCRIPTION("/templates/problem.template.html")
	
	;
	
	private final String content;

	/**
	 * 
	 * @param path
	 */
	private Template(final String path) {
		final Class<?> loader = Jammy.class;
		final InputStream stream = loader.getResourceAsStream(path);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		this.content = reader.lines().collect(Collectors.joining("\n"));
	}
	
	/** {@inheritDoc} **/
	@Override
	public String toString() {
		return content;
	}

}
