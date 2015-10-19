package review.classdesign.jammy.model.submission;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.jdt.core.ICompilationUnit;
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

	/** **/
	private final ProblemSolver solver;

	protected AbstractSubmission(final ProblemSolver solver) {
		this.solver = solver;
	}

	/**
	 * 
	 * @return
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
	
	/** {@inheritDoc} **/
	@Override
	public final IFile getOutput() {
		return null;
	}

	/**
	 * 
	 * @param arguments
	 * @param monitor
	 * @throws CoreException
	 */
	protected final void run(final String arguments, final IProgressMonitor monitor) throws CoreException {
		final StringBuilder builder = new StringBuilder();
		// TODO : Create configuration name from solver.
		final String name = builder.toString();
		final ILaunchConfiguration configuration = getLaunchConfiguration(name, arguments);
		// TODO : Save workspace
		start(configuration.launch(ILaunchManager.RUN_MODE, monitor));
	}

	/**
	 * 
	 * @param name
	 * @param arguments
	 * @return
	 * @throws CoreException
	 */
	private final ILaunchConfiguration getLaunchConfiguration(final String name, final String arguments) throws CoreException {
		final DebugPlugin plugin = DebugPlugin.getDefault();
		final ILaunchManager manager = plugin.getLaunchManager();
		final ILaunchConfigurationType type = manager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		for (final ILaunchConfiguration configuration : manager.getLaunchConfigurations(type)) {
			if (configuration.getName().equals(name)) {
				configuration.delete();
			}
		}
		return createConfiguration(type, name, arguments);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws CoreException 
	 */
	private final ILaunchConfiguration createConfiguration(final ILaunchConfigurationType type, final String name, final String arguments) throws CoreException {
		final ILaunchConfigurationWorkingCopy copy = type.newInstance(null, name);
		final IFile solverFile = solver.getFile();
		final ICompilationUnit unit = (ICompilationUnit) solver.getFile().getAdapter(ICompilationUnit.class);
		copy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, unit.getElementName());
		copy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, solverFile.getProject().getName());
		copy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, arguments);
		return copy.doSave();
	}

}
