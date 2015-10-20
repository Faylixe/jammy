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
	private static final String TRUE_ATTRIBUTE = "true";

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
				ISubmissionService.get().fireExecutionFinished(AbstractSubmission.this);
			}
			else {
				start(launch);
			}
			return Status.OK_STATUS;
		}
	
	}

	/** Target problem solver this submission will work on. **/
	private final ProblemSolver solver;

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 */
	protected AbstractSubmission(final ProblemSolver solver) {
		this.solver = solver;
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
	 * Starts a {@link LaunchMonitorJob} using the given <tt>launch</tt>.
	 * 
	 * @param launch Target launch that will be monitored by created job.
	 */
	private final void start(final ILaunch launch) {
		final Job job = new LaunchMonitorJob(launch);
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
	}


	/**
	 * 
	 * @param arguments
	 * @param monitor
	 * @throws CoreException
	 */
	protected final void run(final String arguments, final IProgressMonitor monitor) throws CoreException {
		final ILaunchConfigurationWorkingCopy workingCopy = getLaunchConfiguration(solver.getName(), arguments);
		final Map<String, String> attributes = createAttributesMap(arguments);
		for (final String key : attributes.keySet()) {
			workingCopy.setAttribute(key, attributes.get(key));
		}
		final ILaunchConfiguration configuration = workingCopy.doSave();
		final ILaunch launch = configuration.launch(ILaunchManager.RUN_MODE, monitor);
		start(launch);
	}

	/**
	 * 
	 * @param arguments
	 * @return
	 */
	private final Map<String, String> createAttributesMap(final String arguments) {
		final HashMap<String, String> attributes = new HashMap<>();
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, solver.getName());
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, solver.getProject().getName());
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, arguments);
		attributes.put(IDebugUIConstants.ATTR_PRIVATE, TRUE_ATTRIBUTE);
		attributes.put(IDebugUIConstants.ATTR_CAPTURE_IN_FILE, getOutput().getLocation().toOSString());
		return attributes;
	}

	/**
	 * 
	 * @param name
	 * @param arguments
	 * @return
	 * @throws CoreException
	 */
	private final ILaunchConfigurationWorkingCopy getLaunchConfiguration(final String name, final String arguments) throws CoreException {
		final DebugPlugin plugin = DebugPlugin.getDefault();
		final ILaunchManager manager = plugin.getLaunchManager();
		final ILaunchConfigurationType type = manager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		for (final ILaunchConfiguration configuration : manager.getLaunchConfigurations(type)) {
			if (configuration.getName().equals(name)) {
				configuration.delete();
			}
		}
		return type.newInstance(null, name);
	}

}
