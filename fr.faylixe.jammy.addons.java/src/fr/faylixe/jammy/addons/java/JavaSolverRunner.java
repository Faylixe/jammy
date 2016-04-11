package fr.faylixe.jammy.addons.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
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

import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.addons.ISolverRunner;

/**
 * {@link ISolverRunner} implementation for Java language.
 * 
 * @author fv
 */
public final class JavaSolverRunner implements ISolverRunner {

	/** Target problem solver this submission will work on. **/
	private final ProblemSolver solver;

	/** Monitor instance used for Java execution. **/
	private final IProgressMonitor monitor;

	/** Delegate {@link ILaunch} instance which correspond to our solver execution. **/
	private ILaunch launch;

	/**
	 * Default constructor.
	 * 
	 * @param solver Solver which aims to be ran.
	 * @param monitor Monitor instance used for Java execution.
	 */
	public JavaSolverRunner(final ProblemSolver solver, final IProgressMonitor monitor) {
		this.solver = solver;
		this.monitor = monitor;
	}

	/** {@inheritDoc} **/
	@Override
	public boolean isTerminated() {
		return launch.isTerminated();
	}

	/** {@inheritDoc} **/
	@Override
	public void run(final String arguments, final String output) throws CoreException {
		final ILaunchConfigurationWorkingCopy workingCopy = getLaunchConfiguration(solver.getName());
		final Map<String, String> attributes = createAttributesMap(arguments, output);
		for (final String key : attributes.keySet()) {
			workingCopy.setAttribute(key, attributes.get(key));
		}
		final ILaunchConfiguration configuration = workingCopy.doSave();
		this.launch = configuration.launch(ILaunchManager.RUN_MODE, monitor);
	}

	/**
	 * Creates a {@link Map} of attributes used for the generated launch configuration.
	 * 
	 * @param arguments Command line argument to be used.
	 * @param output Target output file path.
	 * @return Created attributes map.
	 */
	private Map<String, String> createAttributesMap(final String arguments, final String output) {
		final HashMap<String, String> attributes = new HashMap<>();
		final IFile file = solver.getFile();
		final String filename = file.getName();
		final String name = filename.substring(0, filename.length() - 5); // Note : -5 for .java extension.
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, name);
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, solver.getProject().getName());
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, arguments);
		attributes.put(IDebugUIConstants.ATTR_PRIVATE, Boolean.toString(true));
		attributes.put(IDebugUIConstants.ATTR_CAPTURE_IN_FILE, output);
		return attributes;
	}

	/**
	 * Creates a working copy of {@link ILaunchConfiguration} for the given <tt>name</tt>.
	 * If such configuration already exist, they are removed first.
	 * 
	 * @param name Name of the launch configuration to create.
	 * @return Retrieved launch configuration.
	 * @throws CoreException If any error occurs while creating launch configuration.
	 */
	private static ILaunchConfigurationWorkingCopy getLaunchConfiguration(final String name) throws CoreException {
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
