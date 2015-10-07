package review.classdesign.jammy.common;

/**
 * TODO : Make host name contextual.
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

	/** URL of the contest index page. **/
	public static final String CONTEST_INDEX = "/codejam/contests.html";


}
