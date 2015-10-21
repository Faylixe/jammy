package review.classdesign.jammy.model.submission;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

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
		private final ILaunch launch;

		/**
		 * Default constructor.
		 * 
		 * @param launch Target launch that is monitored by this job.
		 */
		public LaunchMonitorJob(final ILaunch launch) {
			super(JOB_NAME);
			this.launch = launch;
		}

		/** {@inheritDoc} **/
		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			if (launch.isTerminated()) {
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
				startJob(launch);
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
	private final void startJob(final ILaunch launch) {
		final Job job = new LaunchMonitorJob(launch);
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
	}

}
