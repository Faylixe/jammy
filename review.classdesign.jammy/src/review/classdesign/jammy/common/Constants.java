package review.classdesign.jammy.common;

/**
 * 
 * @author fv
 */
public final class Constants {

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private Constants() {
		// Do nothing.
	}

	/**
	 * 
	 * @author fv
	 *
	 */
	public static class HTML {

		/** HTML tag used for parsing contest title. **/
		public static final String H3 = "h3";
		
		/** **/
		public static final String ANCHOR = "a";

		/** **/
		public static final String HREF = "href";

		/** **/
		public static final String TR = "tr";
	}

	/** **/
	public static final String HOST = "https://code.google.com";

	/** URL of the contest index page. **/
	public static final String CONTEST_INDEX = HOST + "/codejam/contests.html";

	/** Class name of the element that contains contest data. **/
	public static final String CONTEST_CLASS_NAME = "year_row";

	/** **/
	public static final String DESCRIPTION_CLASS_NAME = "desc";


}
