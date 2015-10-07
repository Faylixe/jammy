package review.classdesign.jammy.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import review.classdesign.jammy.common.RequestUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * POJO that aims to be bind to the <tt>/ContestInfo</tt>
 * request, using Gson API.
 * 
 * @author fv
 */
public final class ContestInfo {

	/** Boolean flag that indicates if this contest have analysis available. **/
	@SerializedName("has_analysis")
	private int analysis;

	/** Integer that defines the problems version. **/
	@SerializedName("problem_version")
	private int version;

	/** List of problem this contest is exposing. **/
	@SerializedName("problems")
	private Problem [] problems;

	/**
	 * Indicates if this contests already have
	 * analysis committed or not.
	 * 
	 * @return <tt>true</tt> if contest analysis is available, <tt>false</tt> otherwise.
	 */
	public boolean hasAnalysis() {
		return (analysis == 1);
	}
	
	/**
	 * Getter for the problems version.
	 * 
	 * @return Integer that defines the problems version.
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * Returns {@link IProblem} instance associated
	 * to this contest.
	 * 
	 * @return List of problem this contest exposes.
	 */
	public List<Problem> getProblems() {
		return Arrays.asList(problems);
	}
	
	/** Path of the ContestInfo request. **/
	private static final String REQUEST = "/ContestInfo";

	/**
	 * Static factory method that builds a {@link ContestInfo} instance
	 * from the given <tt>round</tt> using a <tt>/ContestInfo</tt>
	 * request from the round dashboard.
	 * 
	 * @param round Round to retrieve {@link ContestInfo} from.
	 * @return Built {@link ContestInfo} instance.
	 * @throws IOException If any error occurs while performing the request.
	 */
	public static ContestInfo get(final Round round) throws IOException {
		final StringBuilder builder = new StringBuilder();
		builder.append(round.getURL());
		builder.append(REQUEST);
		final String json = RequestUtils.get(builder.toString());
		final Gson parser = new Gson();
		return parser.fromJson(json, ContestInfo.class);
	}

}
