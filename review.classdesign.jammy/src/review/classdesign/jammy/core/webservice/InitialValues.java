package review.classdesign.jammy.core.webservice;

import java.io.IOException;

import review.classdesign.jammy.common.RequestUtils;
import review.classdesign.jammy.core.Round;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author fv
 */
public class InitialValues { // NOPMD

	/** **/
	private static final String REQUEST = "/do?cmd=GetInitialValues&zx=%s&csrfmiddlewaretoken=undefined";

	/** **/
	@SerializedName("admin_html_snippet")
	private String admin;

	/** **/
	@SerializedName("clar_last_seen")
	private int seen;

	/** **/
	@SerializedName("cs")
	private int cs;

	/** **/
	@SerializedName("csrf_middleware_token")
	private String token;

	/** **/
	@SerializedName("email")
	private String email;

	/** **/
	@SerializedName("input_panel_html")
	private String input;

	/** **/
	@SerializedName("insight_html_snippet")
	private String snippet;

	/** Boolean flag that indicates if user is logged in or not. **/
	@SerializedName("logged_in")
	private boolean logged;

	/** **/
	@SerializedName("login_html")
	private String loginHTML;

	/** **/
	@SerializedName("logout_html")
	private String logoutHTML;

	/** Current contest name. **/
	@SerializedName("name")
	private String name;

	/** Boolean flag that indicates if user is qualified for the next round or not. **/
	@SerializedName("qualified")
	private boolean qualified;

	/** **/
	@SerializedName("second_left")
	private long left;

	/** **/
	@SerializedName("second_until_start")
	private long untilStart;

	/** **/
	@SerializedName("start_int")
	private long start;

	/** **/
	@SerializedName("version")
	private int version;

	/**
	 * 
	 * @param round
	 * @return
	 * @throws IOException 
	 */
	public static InitialValues get(final Round round) throws IOException {
		final StringBuilder builder = new StringBuilder();
		builder.append(round.getURL());
		final String time = String.valueOf(System.currentTimeMillis() * 1000);
		builder.append(String.format(REQUEST, time));
		final String json = RequestUtils.get(builder.toString());
		final Gson parser = new Gson();
		final InitialValues values = parser.fromJson(json, InitialValues.class);
		return values;
	}
}
