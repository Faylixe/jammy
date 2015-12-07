package fr.faylixe.jammy.core.model.webservice;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import fr.faylixe.jammy.core.common.RequestUtils;
import fr.faylixe.jammy.core.model.Round;

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
	 * @return
	 */
	public String getAdmin() {
		return admin;
	}

	/**
	 * 
	 * @return
	 */
	public int getSeen() {
		return seen;
	}

	/**
	 * 
	 * @return
	 */
	public int getCS() {
		return cs;
	}

	/**
	 * 
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @return
	 */
	public String getInput() {
		return input;
	}

	/**
	 * 
	 * @return
	 */
	public String getSnippet() {
		return snippet;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLogger() {
		return logged;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLoginHTML() {
		return loginHTML;
	}

	/**
	 * 
	 * @return
	 */
	public String getLogoutHTML() {
		return logoutHTML;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isQualified() {
		return qualified;
	}

	/**
	 * 
	 * @return
	 */
	public long getLeft() {
		return left;
	}

	/**
	 * 
	 * @return
	 */
	public long getUntilStart() {
		return untilStart;
	}

	/**
	 * 
	 * @return
	 */
	public long getStart() {
		return start;
	}

	/**
	 * 
	 * @return
	 */
	public int getVersion() {
		return version;
	}

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
