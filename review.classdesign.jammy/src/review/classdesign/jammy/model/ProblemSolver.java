package review.classdesign.jammy.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

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
public final class ProblemSolver {


	/** **/
	private final IFile solver;

	/** **/
	private final ProblemSampleDataset dataset;

	/**
	 * 
	 * @param solver
	 * @param dataset
	 */
	private ProblemSolver(final IFile solver, final ProblemSampleDataset dataset) {
		this.solver = solver;
		this.dataset = dataset;
	}

	/**
	 * 
	 * @return
	 */
	public IFile getFile() {
		return solver;
	}
	
	/**
	 * 
	 * @return
	 */
	public ProblemSampleDataset getSampleDataset() {
		return dataset;
	}

	/** **/
	private static Map<Problem, ProblemSolver> INSTANCES;

	/**
	 * TODO : Consider saving solver reference.
	 *
	 * @param problem
	 * @param monitor
	 * @return
	 * @throws CoreException
	 */
	public static ProblemSolver get(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		synchronized (ProblemSolver.class) {
			if (INSTANCES == null) {
				INSTANCES = new ConcurrentHashMap<>();
			}
		}
		if (!INSTANCES.containsKey(problem)) {
			final ContestInfo contest = problem.getParent();
			final IProject project = JavaProjectBuilder.build(contest.getProjectName(), monitor);
			final IFile file = new SolverBuilder(project, monitor).build(problem);
			final ProblemSampleDataset dataset = new DatasetBuilder(problem, project, monitor).build();
			final ProblemSolver solver = new ProblemSolver(file, dataset);
			INSTANCES.put(problem, solver);
		}
		return INSTANCES.get(problem);
	}

}
