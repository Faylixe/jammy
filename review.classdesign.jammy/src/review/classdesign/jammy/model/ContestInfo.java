package review.classdesign.jammy.model;

import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * TODO : Javadoc.
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

}
