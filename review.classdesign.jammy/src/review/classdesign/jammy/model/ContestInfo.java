package review.classdesign.jammy.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.common.JavaProjectBuilder;
import review.classdesign.jammy.common.RequestUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * POJO that aims to be bind to the <tt>/ContestInfo</tt>
 * request, using Gson API.
 * 
 * @author fv
 */
public final class ContestInfo {

	/** Prefix used for contest project. **/
	private static final String CONTEST_PROJECT_PREFIX = "jammy.";
	
	/** File extension used for created Java solver. **/
	private static final String SOLVER_EXTENSION = ".java";

	/** Normalization pattern used for creating project and file name. **/
	private static final String PATTERN = "[^A-Za-z0-9]";

	/** Boolean flag that indicates if this contest have analysis available. **/
	@SerializedName("has_analysis")
	private int analysis;

	/** Integer that defines the problems version. **/
	@SerializedName("problem_version")
	private int version;

	/** List of problem this contest is exposing. **/
	@SerializedName("problems")
	private Problem [] problems;

	/** Source round instance that has been used to create this contest info. **/
	private transient Round parent;

	/** Associated project instance. **/
	private transient IProject project;

	/**
	 * Sets the internal parent round. Aims to be only used
	 * by the static factory method {@link #get(Round)}.
	 * @param round
	 */
	private void setRound(final Round round) {
		this.parent = round;
	}

	/**
	 * Creates and returns a valid project name
	 * using the following structure :
	 * <tt>jammy.contest_name.round_name</tt>.
	 * 
	 * @return Created project name.
	 */
	private String getProjectName() {
		// TODO : Fix pattern replacement issues.
		final StringBuilder builder = new StringBuilder(CONTEST_PROJECT_PREFIX);
		final String contest = parent.getContestName().replace(PATTERN, "");
		builder.append(contest.toLowerCase());
		builder.append(".");
		final String round = parent.getName().replace(PATTERN, "");
		builder.append(round.toLowerCase());
		return builder.toString();
	}

	/**
	 * Retrieves and returns solver file instance
	 * associated to the current problem. If the associated project
	 * is not existing, it will be created.
	 * 
	 * @param problem Problem instance to retrieve solver class file from.
	 * @param monitor Monitor instance to use for creating project if required.
	 * @return Associated {@link IFile} instance.
	 * @throws CoreException If any error occurs while creating project if required.
	 */
	public IFile getProblemFile(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		if (project == null) {
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			project = workspace.getRoot().getProject(getProjectName());
			if (!project.exists()) {
				JavaProjectBuilder.build(project, monitor);
			}
		}
		final StringBuilder builder = new StringBuilder();
		builder.append(JavaProjectBuilder.SOURCE_PATH);
		builder.append("/");
		builder.append(problem.getSolverName());
		builder.append(SOLVER_EXTENSION);
		return project.getFile(builder.toString());
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
		final Gson parser = new GsonBuilder().registerTypeAdapter(Problem.class, new Problem.Deserializer()).create();
		final ContestInfo info = parser.fromJson(json, ContestInfo.class);
		info.setRound(round);
		return info;
	}

}
