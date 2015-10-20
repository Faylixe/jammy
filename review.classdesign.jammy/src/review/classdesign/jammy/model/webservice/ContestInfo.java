package review.classdesign.jammy.model.webservice;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import review.classdesign.jammy.common.RequestUtils;
import review.classdesign.jammy.common.Template;
import review.classdesign.jammy.model.Round;
import review.classdesign.jammy.model.webservice.Problem.Deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * POJO that aims to be bind to the <tt>/ContestInfo</tt>
 * request, using Gson API.
 * 
 * @author fv
 */
public final class ContestInfo implements Serializable, ObjectInputValidation {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Prefix used for contest project. **/
	private static final String CONTEST_PROJECT_PREFIX = "jammy.";

	/** Boolean flag that indicates if this contest have analysis available. **/
	@SerializedName("has_analysis")
	private int analysis;

	/** Integer that defines the problems version. **/
	@SerializedName("problem_version")
	private int version;

	/** List of problem this contest is exposing. **/
	@SerializedName("problems")
	private Problem [] problems;

	/** Name of the project associated to this contest. **/
	private String projectName;

	/**
	 * Sets the internal parent round. Aims to be only used
	 * by the static factory method {@link #get(Round)}.
	 * @param round
	 */
	private void createProjectName(final Round round) {
		final StringBuilder builder = new StringBuilder(CONTEST_PROJECT_PREFIX);
		builder.append(Template.normalize(round.getContestName()).toLowerCase());
		builder.append(".");
		builder.append(Template.normalize(round.getName()).toLowerCase());
		projectName = builder.toString();
	}
	
	/**
	 * Getter for the project name associated to this contest.
	 * 
	 * @return Name of the project associated to this contest.
	 */
	public String getProjectName() {
		return projectName;
	}

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
		return (problems != null ? Arrays.asList(problems) : Collections.emptyList());
	}

	/** {@inheritDoc} **/
	@Override
	public void validateObject() throws InvalidObjectException {
		for (final Problem problem : problems) {
			problem.setParent(this);
		}
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
		final Gson parser = new GsonBuilder().registerTypeAdapter(Problem.class, new Deserializer()).create();
		final ContestInfo info = parser.fromJson(json, ContestInfo.class);
		info.createProjectName(round);
		for (final Problem problem : info.getProblems()) {
			problem.setParent(info);
		}
		return info;
	}

}
