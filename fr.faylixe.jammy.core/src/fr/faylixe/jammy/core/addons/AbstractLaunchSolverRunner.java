package fr.faylixe.jammy.core.addons;

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

import fr.faylixe.jammy.core.ProblemSolver;

/**
 * Abstract {@link ISolverRunner} implementation that is based on
 * {@link ILaunch} execution.
 * 
 * @author fv
 */
public abstract class AbstractLaunchSolverRunner implements ISolverRunner {

	/** Target problem solver this runner will work on. **/
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
	protected AbstractLaunchSolverRunner(final ProblemSolver solver, final IProgressMonitor monitor) {
		this.solver = solver;
		this.monitor = monitor;
	}

	/** {@inheritDoc} **/
	@Override
	public final boolean isTerminated() {
		return launch.isTerminated();
	}

	/** {@inheritDoc} **/
	@Override
	public final void run(final String arguments, final String output) throws CoreException {
		final ILaunchConfigurationWorkingCopy workingCopy = getLaunchConfiguration(solver.getName());
		final Map<String, String> attributes = createAttributesMap(arguments, output);
		for (final String key : attributes.keySet()) {
			workingCopy.setAttribute(key, attributes.get(key));
		}
		final ILaunchConfiguration configuration = workingCopy.doSave();
		this.launch = configuration.launch(ILaunchManager.RUN_MODE, monitor);
	}

	/**
	 * Target solver getter.
	 * 
	 * @return Target problem solver this runner will work on.
	 */
	protected final ProblemSolver getSolver() {
		return solver;
	}

	/**
	 * Getter for the launch configuration type to use.
	 * 
	 * @return Launch configuration type to use for the created configuration.
	 */
	protected abstract String getLaunchConfigurationType();

	/**
	 * Delegate method that is in charge of creating the attribute map
	 * that will be uses for the created launch configuration.
	 * 
	 * @param arguments Program arguments to use (namely the path of the input file).
	 * @param output Target output file to save.
	 * @return Created attribute map.
	 */
	protected abstract Map<String, String> createAttributesMap(String arguments, String output);

	/**
	 * Static factory method that creates an attribute map
	 * with base attribute (namely target output file and
	 * configuration privacy).
	 * 
	 * @return Base attribute map.
	 */
	protected final Map<String, String> createBaseMap(final String output) {
		final HashMap<String, String> attributes = new HashMap<>();
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
	private final ILaunchConfigurationWorkingCopy getLaunchConfiguration(final String name) throws CoreException {
		final DebugPlugin plugin = DebugPlugin.getDefault();
		final ILaunchManager manager = plugin.getLaunchManager();
		final ILaunchConfigurationType type = manager.getLaunchConfigurationType(getLaunchConfigurationType());
		for (final ILaunchConfiguration configuration : manager.getLaunchConfigurations(type)) {
			if (configuration.getName().equals(name)) {
				configuration.delete();
			}
		}
		return type.newInstance(null, name);
	}

}
