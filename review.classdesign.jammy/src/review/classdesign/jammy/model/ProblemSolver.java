package review.classdesign.jammy.model;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.common.NamedObject;
import review.classdesign.jammy.model.builder.DatasetBuilder;
import review.classdesign.jammy.model.builder.JavaProjectBuilder;
import review.classdesign.jammy.model.builder.SolverBuilder;
import review.classdesign.jammy.model.webservice.ContestInfo;
import review.classdesign.jammy.model.webservice.Problem;

/**
 * TODO : Javadoc.
 * 
 * @author fv
 */
public final class ProblemSolver extends NamedObject {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Suffix used for solver class file. **/
	private static final String SOLVER_SUFFIX = "Solver";

	/** Target solver class file. **/
	private final IFile solver;

	/** Sample dataset associated to this solver. **/
	private final ProblemSampleDataset dataset;

	/**
	 * Default constructor.
	 * 
	 * @param name Name of the target project.
	 * @param solver Target solver class file. 
	 * @param dataset Sample dataset associated to this solver.
	 */
	private ProblemSolver(final String name, final IFile solver, final ProblemSampleDataset dataset) {
		super(name);
		this.solver = solver;
		this.dataset = dataset;
	}

	/**
	 * Getter for the solver class file.
	 * 
	 * @return Target solver class file.
	 * @see #solver
	 */
	public IFile getFile() {
		return solver;
	}

	/**
	 * Retrieves and returns {@link IProject} instance the solver class belongs to.
	 * 
	 * @return {@link IProject} instance the solver class file belongs to.
	 * @see IResource#getProject()
	 */
	public IProject getProject() {
		return solver.getProject();
	} 

	/**
	 * Getter for the solver sample dataset.
	 * 
	 * @return Sample dataset associated to this solver
	 * @see #dataset
	 */
	public ProblemSampleDataset getSampleDataset() {
		return dataset;
	}

	/** Cache of all problem solver instances indexed by **/
	private static final Map<Problem, SoftReference<ProblemSolver>> INSTANCES = new ConcurrentHashMap<>();

	/** Task name for the project retrieval. **/
	private static final String PROJECT_TASK = "Retrieves associated Java project";

	/** Task name for the solver retrieval. **/
	private static final String SOLVER_TASK = "Retrieves associated Java solver class file";

	/** Task name for the dataset retrieval. **/
	private static final String DATASET_TASK = "Retrieves associated sample dataset";

	/**
	 * Static factory method that creates a {@link ProblemSolver} instance
	 * from the given <tt>problem</tt> instance. Creates project, solver, and
	 * dataset.
	 * 
	 * @param problem Problem to build solver from.
	 * @param monitor Monitor instance used for each builder involved.
	 * @return Created solver instance.
	 * @throws CoreException If any error occurs while creating solver.
	 */
	private static ProblemSolver createSolver(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final ContestInfo contest = problem.getParent();
		monitor.subTask(PROJECT_TASK);
		final IProject project = JavaProjectBuilder.build(contest.getProjectName(), monitor);
		final StringBuilder builder = new StringBuilder();
		builder.append(problem.getNormalizedName());
		builder.append(SOLVER_SUFFIX);
		final String name = builder.toString();
		monitor.subTask(SOLVER_TASK);
		final IFile file = new SolverBuilder(project, monitor).build(name);
		monitor.subTask(DATASET_TASK);
		final ProblemSampleDataset dataset = new DatasetBuilder(problem, project, monitor).build();
		return new ProblemSolver(name, file, dataset);
	}

	/**
	 * {@link ProblemSolver} instances access method. If the required instance
	 * does not exist, or has been garbaged, then a new instance is created and put
	 * into the local cache.
	 * 
	 * @param problem Problem to get solver instance from.
	 * @param monitor Monitor instance used if solver creation is required.
	 * @return Retrieved instance.
	 * @throws CoreException If any error occurs while creating required instance.
	 */
	public static ProblemSolver get(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final SoftReference<ProblemSolver> reference = INSTANCES.get(problem);
		final ProblemSolver solver;
		if (reference == null || reference.get() == null) {
			solver = createSolver(problem, monitor);
			INSTANCES.put(problem, new SoftReference<ProblemSolver>(solver));
		}
		else {
			solver = reference.get();
		}
		return solver;
	}

}
