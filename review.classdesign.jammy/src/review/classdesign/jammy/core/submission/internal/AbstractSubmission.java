package review.classdesign.jammy.core.submission.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.addons.ILanguageManager;
import review.classdesign.jammy.addons.ISolverRunner;
import review.classdesign.jammy.core.ProblemSolver;
import review.classdesign.jammy.core.submission.ISubmission;
import review.classdesign.jammy.core.submission.SubmissionException;
import review.classdesign.jammy.service.ISubmissionService;

/**
 * Abstract {@link ISubmission} implementation that language extension
 * should extends.
 * 
 * @author fv
 */
public abstract class AbstractSubmission implements ISubmission {

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
				try {
					submit(monitor);
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
		final ILanguageManager manager = Jammy.getDefault().getCurrentLanguageManager();
		final ISolverRunner execution = manager.getRunner(solver, monitor);
		execution.run(arguments, getOutput().getLocation().toString());
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
