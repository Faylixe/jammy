package review.classdesign.jammy.model;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import review.classdesign.jammy.JammyPreferences;
import review.classdesign.jammy.common.HTMLConstant;
import review.classdesign.jammy.common.NamedObject;
import review.classdesign.jammy.common.TemplateLoader;
import review.classdesign.jammy.ui.view.ProblemView;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

/**
 * POJO that aims to be bind to the <tt>/ContestInfo</tt>
 * request, using Gson API. {@link Problem} instance belong
 * to a {@link ContestInfo} object, and consists in the problem
 * metadata such a name, description, and IO details.
 * 
 * @author fv
 */
public final class Problem extends NamedObject {

	/** Problem HTML template. **/
	private static String TEMPLATE;

	/** Path of the template file resources used for problem display. **/
	private static final String TEMPLATE_PATH = "/templates/problem.template.html";

	/**
	 * Custom deserializer that normalizes problem body content.
	 * 
	 * @author fv
	 */
	public static class Deserializer implements JsonDeserializer<Problem> {

		/** {@inheritDoc} **/
		@Override
		public Problem deserialize(
				final JsonElement element,
				final Type type,
				final JsonDeserializationContext context) throws JsonParseException {
			final Gson parser = new Gson();
			final Problem problem = parser.fromJson(element, Problem.class);
			final String normalized = normalize(problem.body);
			problem.body = format(normalized);
			return problem;
		}
	
	}
	
	/**
	 * Normalizes the given HTML <tt>body</tt> text, by replacing
	 * images URI by absolute URI using preference hostname.
	 * 
	 * TODO : Move this to Problem class.
	 * 
	 * @param body HTML body to normalize.
	 * @return Normalized HTML content.
	 */
	private static String normalize(final String body) {
		final Document document = Jsoup.parse(body);
		final Elements images = document.getElementsByTag(HTMLConstant.IMG);
		for (final Element image : images) {
			final String original = image.attr(HTMLConstant.SRC);
			if (!original.startsWith("https://")) {
				final String hostname = JammyPreferences.getHostname();
				final StringBuilder builder = new StringBuilder();
				builder.append(hostname.endsWith("/") ? hostname.substring(0, -1) : hostname);
				builder.append("/");
				builder.append(original.startsWith("/") ? original.substring(1) : original);
				image.attr(HTMLConstant.SRC, builder.toString());
			}
		}
		return document.html();
	}

	/**
	 * Static method that formats the given <tt>body</tt>
	 * by using internal HTML template.
	 * 
	 * @param body Body of the text to format.
	 * @return Formatted HTML text.
	 */
	public static String format(final String body) {
		synchronized (ProblemView.class) {
			if (TEMPLATE == null) {
				TEMPLATE = TemplateLoader.load(TEMPLATE_PATH);
			}
		}
		return String.format(TEMPLATE, body);
	}

	/** Full HTML text that describes this problem. **/
	@SerializedName("body")
	private String body;

	/** Problem unique identifier. **/
	@SerializedName("id")
	private String id;

	/** TODO : Figure out what is key for. **/
	@SerializedName("key")
	private String key;

	/** TODO : Figure out what is type for. **/
	@SerializedName("type")
	private String type;
	
	/** List of inputs that are available for solving in this problem. **/
	@SerializedName("io")
	private ProblemInput [] inputs;

	/**
	 * Getter for the problem body description.
	 * 
	 * @return Full HTML text that describes this problem.
	 * @see #body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Getter for the problem id.
	 * 
	 * @return Problem unique identifier.
	 * @see #id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter for the problem key.

	 * @return TODO : Figure out what is key for.
	 * @see #key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Getter for the problem type.
	 * 
	 * @return TODO : Figure out what is type for.
	 * @see #type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Getter for the problem inputs.
	 * 
	 * @return List of inputs that are available for solving in this problem. 
	 * @see #inputs
	 */
	public List<ProblemInput> getProblemInputs() {
		return (inputs != null ? Arrays.asList(inputs) : Collections.emptyList());
	}

}
