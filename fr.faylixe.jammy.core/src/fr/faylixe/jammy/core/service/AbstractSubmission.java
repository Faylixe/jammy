package fr.faylixe.jammy.core.service;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fr.faylixe.googlecodejam.client.common.NamedObject;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.addons.ILanguageManager;
import fr.faylixe.jammy.core.addons.ISolverRunner;
import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * Abstract {@link ISubmission} implementation that language extension
 * should extends.
 * 
 * @author fv
 */
public abstract class AbstractSubmission extends NamedObject implements ISubmission {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Job name used for {@link LaunchMonitorJob} instances. **/
	private static final String JOB_NAME = "Solver execution listener";

	/** Path of the output file for a given project. **/
	protected static final String OUTPUT_PATH = "output";

	/** Target problem solver this submission will work on. **/
	private final ProblemSolver solver;

	/** {@link ISubmissionService} instance used by this submission. **/
	private final ISubmissionService service;

	/**
	 * Job implementation that checks for launch life cycle.
	 * 
	 * @author fv
	 */
	private class LaunchMonitorJob extends Job {

		/** Target launch that is monitored by this job. **/
		private final ISolverRunner execution;

		/**
		 * Default constructor.
		 * 
		 * @param execution Target launch that is monitored by this job.
		 */
		public LaunchMonitorJob(final ISolverRunner execution) {
			super(JOB_NAME);
			this.execution = execution;
		}

		/** {@inheritDoc} **/
		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			if (execution.isTerminated()) {
				service.fireExecutionFinished(AbstractSubmission.this);
				if (submit(monitor)) {
					service.fireSubmissionFinished(AbstractSubmission.this);
				}
			}
			else {
				startJob(execution);
			}
			return Status.OK_STATUS;
		}
	
	}

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 * @param name Name of this submission.
	 */
	protected AbstractSubmission(final ProblemSolver solver, final String name) {
		super(name);
		this.solver = solver;
		this.service = ISubmissionService.get();
	}

	/** {@inheritDoc} **/
	@Override
	public final ProblemSolver getSolver() {
		return solver;
	}

	/**
	 * Retrieves and returns the output file
	 * relative to the given <tt>extension</tt>
	 * 
	 * @param extension Extension of the output file to retrieve.
	 * @return Output file of this submission.
	 * @throws CoreException If any error occurs while retrieving file.
	 */
	protected final IFile getOutputFile(final String extension) throws CoreException {
		final IProject project = getSolver().getProject();
		final IFolder folder = EclipseUtils.getFolder(project, OUTPUT_PATH);
		final StringBuilder builder = new StringBuilder();
		builder
			.append(getSolver().getName().toLowerCase())
			.append(extension);
		return folder.getFile(builder.toString());
	}

	/**
	 * Creates and runs a contextual {@link ISolverRunner} with
	 * the given <tt>arguments</tt>. Since solver should be ran
	 * asynchronously, a monitoring job is started to check
	 * execution life cycle.
	 * 
	 * @param arguments Arguments to submit to the runner.
	 * @param monitor Monitor instance used for the solver execution.
	 * @throws CoreException If any error occurs while running solver instance.
	 */
	protected final void run(final String arguments, final IProgressMonitor monitor) throws CoreException {
		final ILanguageManager manager = Jammy.getInstance().getCurrentLanguageManager();
		final ISolverRunner execution = manager.getRunner(solver, monitor);
		execution.run(arguments, getOutputFile().getLocation().toString());
		service.fireExecutionStarted(this);
		startJob(execution);
	}

	/**
	 * Getter for the {@link ISubmissionService} instance used by this submission.
	 * 
	 * @return Service instance to use.
	 */
	protected final ISubmissionService getService() {
		return service;
	}

	/**
	 * Starts a {@link LaunchMonitorJob} using the given <tt>launch</tt>.
	 * 
	 * @param launch Target launch that will be monitored by created job.
	 */
	private final void startJob(final ISolverRunner execution) {
		final Job job = new LaunchMonitorJob(execution);
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
	}

}
