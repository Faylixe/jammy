package review.classdesign.jammy.common;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author fv
 */
public class NamedEntity {

	@SerializedName("name")
	private String name;

	/**
	 * 
	 */
	protected NamedEntity() {
	}
	
	/**
	 * 
	 * @param name
	 */
	protected NamedEntity(final String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public final String getName() {
		return name;
	}

}
