package review.classdesign.jammy.common;

import com.google.gson.annotations.SerializedName;

/**
 * Class that represents a object which
 * could be identified by a name.
 * 
 * @author fv
 */
public class NamedObject {

	/** Name of this object. **/
	@SerializedName("name")
	private String name;

	/**
	 * Default constructor.
	 */
	protected NamedObject() {
	}
	
	/**
	 * Constructor with name parameter
	 * that should be used when such object
	 * are created without using JSON serialization.
	 * 
	 * @param name Name of this object.
	 */
	protected NamedObject(final String name) {
		this.name = name;
	}

	/**
	 * Getter for the name of this object.
	 * 
	 * @return Name of this object.
	 * @see #name
	 */
	public final String getName() {
		return name;
	}

}
