package review.classdesign.jammy.addons.java.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import review.classdesign.jammy.model.ProblemSolver;
import review.classdesign.jammy.model.submission.ISolverExecution;

public final class JavaSolverRunner implements ISolverExecution {

	/** **/
	private static final String TRUE_ATTRIBUTE = "true";

	/** Target problem solver this submission will work on. **/
	private final ProblemSolver solver;

	private final IProgressMonitor monitor;

	/** **/
	private ILaunch launch;

	/** **/
	private String output;

	/**
	 * 
	 * @param solver
	 */
	public JavaSolverRunner(final ProblemSolver solver, final IProgressMonitor monitor) {
		this.solver = solver;
		this.monitor = monitor;
	}

	/**
	 * 
	 * @param arguments
	 * @param monitor
	 * @throws CoreException
	 */
	protected final void run(final String arguments, final String output) throws CoreException {
		final ILaunchConfigurationWorkingCopy workingCopy = getLaunchConfiguration(solver.getName(), arguments);
		final Map<String, String> attributes = createAttributesMap(arguments);
		for (final String key : attributes.keySet()) {
			workingCopy.setAttribute(key, attributes.get(key));
		}
		final ILaunchConfiguration configuration = workingCopy.doSave();
		this.launch = configuration.launch(ILaunchManager.RUN_MODE, monitor);
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
		attributes.put(IDebugUIConstants.ATTR_CAPTURE_IN_FILE, output);
		return attributes;
	}

	/**
	 * 
	 * @param name
	 * @param arguments
	 * @return
	 * @throws CoreException
	 */
	private static final ILaunchConfigurationWorkingCopy getLaunchConfiguration(final String name, final String arguments) throws CoreException {
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

	/** **/
	@Override
	public boolean isTerminated() {
		return launch.isTerminated();
	}

}
