package review.classdesign.jammy.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import review.classdesign.jammy.common.NamedObject;

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
