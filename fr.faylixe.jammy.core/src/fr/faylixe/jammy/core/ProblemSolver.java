package fr.faylixe.jammy.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.common.NamedObject;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.addons.DatasetBuilder;
import fr.faylixe.jammy.core.addons.ILanguageManager;

/**
 * <p>A {@link ProblemSolver} is an object that
 * for a given {@link Problem} instance provides :</p>
 * <ul>
 * 	<li>An associated {@link IFile} which correspond to the solver source code</li>
 * 	<li>An associated {@link ProblemSampleDataset} which contains testing input and output.</li>
 * </ul>
 * 
 * @author fv
 */
public final class ProblemSolver extends NamedObject {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Task name for the project retrieval. **/
	private static final String PROJECT_TASK = "Retrieves associated Java project";

	/** Task name for the solver retrieval. **/
	private static final String SOLVER_TASK = "Retrieves associated Java solver class file";

	/** Task name for the dataset retrieval. **/
	private static final String DATASET_TASK = "Retrieves associated sample dataset";
	
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

	/**
	 * Static factory method that creates a {@link ProblemSolver} instance
	 * from the given <tt>problem</tt> instance. Creates project, solver, and
	 * dataset. This method only aims to be used by the {@link ProblemSolverFactory}
	 * class.
	 * 
	 * @param problem Problem to build solver from.
	 * @param monitor Monitor instance used for each builder involved.
	 * @return Created solver instance.
	 * @throws CoreException If any error occurs while creating solver.
	 */
	protected static ProblemSolver createSolver(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		monitor.subTask(PROJECT_TASK);
		final ILanguageManager manager = Jammy.getInstance().getCurrentLanguageManager();
		final IProject project = manager.getProject(problem, monitor);
		monitor.subTask(SOLVER_TASK);
		final IFile file = manager.getSolver(problem, monitor);
		monitor.subTask(DATASET_TASK);
		final ProblemSampleDataset dataset = new DatasetBuilder(problem, project, monitor).build();
		return new ProblemSolver(problem.getNormalizedName(), file, dataset);
	}

}
