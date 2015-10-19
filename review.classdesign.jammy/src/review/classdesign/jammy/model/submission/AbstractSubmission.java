package review.classdesign.jammy.model.submission;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import review.classdesign.jammy.model.listener.SubmissionListener;

/**
 * 
 * @author fv
 */
public abstract class AbstractSubmission implements ISubmission {

	/** **/
	private static final String JOB_NAME = "Solver execution listener";

	/**
	 * 
	 * @author fv
	 */
	private class LaunchMonitorJob extends Job {

		/** **/
		private final ILaunch launch;

		/**
		 * 
		 * @param launch
		 */
		public LaunchMonitorJob(final ILaunch launch) {
			super(JOB_NAME);
			this.launch = launch;
		}

		/** {@inheritDoc} **/
		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			if (launch.isTerminated()) {
				fireExecutionFinished();
			}
			else {
				start(launch);
			}
			return Status.OK_STATUS;
		}
	
	}

	/**
	 * 
	 * @param launch
	 */
	private void start(final ILaunch launch) {
		final Job job = new LaunchMonitorJob(launch);
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
	}

	/** **/
	private final Collection<SubmissionListener> listeners;

	/**
	 * 
	 */
	protected AbstractSubmission() {
		this.listeners = new ArrayList<SubmissionListener>();
	}
	
	/** {@inheritDoc} **/
	@Override
	public final void addListener(final SubmissionListener listener) {
		listeners.add(listener);
	}
	
	/** {@inheritDoc} **/
	@Override
	public final void removeListener(final SubmissionListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	private final ILaunchConfiguration createConfiguration(final String name) {
		// TODO : Create configuration.
		return null;
	}

	/**
	 * 
	 * @throws CoreException 
	 */
	protected final void run(final IProgressMonitor monitor) throws CoreException {
		final StringBuilder builder = new StringBuilder();
		// TODO : Create configuration name from solver.
		final String name = builder.toString();
		final ILaunchConfiguration configuration = getLaunchConfiguration(name);
		// TODO : Save workspace
		start(configuration.launch(ILaunchManager.RUN_MODE, monitor));
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws CoreException
	 */
	private final ILaunchConfiguration getLaunchConfiguration(final String name) throws CoreException {
		final DebugPlugin plugin = DebugPlugin.getDefault();
		final ILaunchManager manager = plugin.getLaunchManager();
		final ILaunchConfigurationType type = manager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		for (final ILaunchConfiguration configuration : manager.getLaunchConfigurations(type)) {
			if (configuration.getName().equals(name)) {
				return configuration;
			}
		}
		return createConfiguration(name);
	}
	
	/**
	 * 
	 */
	protected final void fireExecutionFinished() {
		for (final SubmissionListener listener : listeners) {
			listener.executionFinished(this);
		}
	}

}
