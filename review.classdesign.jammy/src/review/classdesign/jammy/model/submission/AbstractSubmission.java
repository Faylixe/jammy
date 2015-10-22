package review.classdesign.jammy.model.submission;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import review.classdesign.jammy.ILanguageManager;
import review.classdesign.jammy.ISolverExecution;
import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.model.ProblemSolver;
import review.classdesign.jammy.service.ISubmissionService;

/**
 * 
 * @author fv
 */
public abstract class AbstractSubmission implements ISubmission {

	/** Job name used for {@link LaunchMonitorJob} instances. **/
	private static final String JOB_NAME = "Solver execution listener";

	/** **/
	protected static final String OUTPUT_PATH = "output";

	/**
	 * Job implementation that checks for launch life cycle.
	 * 
	 * @author fv
	 */
	private class LaunchMonitorJob extends Job {

		/** Target launch that is monitored by this job. **/
		private final ISolverExecution execution;

		/**
		 * Default constructor.
		 * 
		 * @param execution Target launch that is monitored by this job.
		 */
		public LaunchMonitorJob(final ISolverExecution execution) {
			super(JOB_NAME);
			this.execution = execution;
		}

		/** {@inheritDoc} **/
		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			if (execution.isTerminated()) {
				service.fireExecutionFinished(AbstractSubmission.this);
				try {
					submit();
					service.fireSubmissionFinished(AbstractSubmission.this);
				}
				catch (final SubmissionException e) {
					service.fireErrorCaught(AbstractSubmission.this, e);
				}
			}
			else {
				startJob(execution);
			}
			return Status.OK_STATUS;
		}
	
	}

	/** Target problem solver this submission will work on. **/
	private final ProblemSolver solver;

	/** **/
	private final ISubmissionService service;

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 */
	protected AbstractSubmission(final ProblemSolver solver) {
		this.solver = solver;
		this.service = ISubmissionService.get();
	}

	/**
	 * Getter for the target problem solver.
	 * 
	 * @return Target problem solver this submission will work on.
	 * @see #solver
	 */
	protected final ProblemSolver getSolver() {
		return solver;
	}

	/**
	 * 
	 * @param arguments
	 * @param monitor
	 * @throws CoreException 
	 */
	protected final void run(final String arguments, final IProgressMonitor monitor) throws CoreException {
		final ILanguageManager manager = Jammy.getDefault().getCurrentLanguageManager();
		final ISolverExecution execution = manager.getExecution(solver, monitor);
		execution.run(arguments, getOutput().getLocation().toString());
		service.fireExecutionStarted(this);
		startJob(execution);
	}

	/**
	 * 
	 * @return
	 */
	protected final ISubmissionService getService() {
		return service;
	}

	/**
	 * Starts a {@link LaunchMonitorJob} using the given <tt>launch</tt>.
	 * 
	 * @param launch Target launch that will be monitored by created job.
	 */
	private final void startJob(final ISolverExecution execution) {
		final Job job = new LaunchMonitorJob(execution);
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
	}

}
