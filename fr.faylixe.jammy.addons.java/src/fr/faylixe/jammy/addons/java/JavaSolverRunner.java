package fr.faylixe.jammy.addons.java;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.addons.AbstractLaunchSolverRunner;
import fr.faylixe.jammy.core.addons.ISolverRunner;

/**
 * {@link ISolverRunner} implementation for Java language.
 * 
 * @author fv
 */
public final class JavaSolverRunner extends AbstractLaunchSolverRunner {

	/**
	 * Default constructor.
	 * 
	 * @param solver Solver which aims to be ran.
	 * @param monitor Monitor instance used for Java execution.
	 */
	public JavaSolverRunner(final ProblemSolver solver, final IProgressMonitor monitor) {
		super(solver, monitor);
	}


	/** {@inheritDoc} **/
	@Override
	protected String getLaunchConfigurationType() {
		return IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION;
	}

	/** {@inheritDoc} **/
	@Override
	protected Map<String, String> createAttributesMap(final String arguments, final String output) {
		final Map<String, String> attributes = createBaseMap(output);
		final IFile file = getSolver().getFile();
		final String filename = file.getName();
		final String name = filename.substring(0, filename.length() - 5); // Note : -5 for .java extension.
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, name);
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, getSolver().getProject().getName());
		attributes.put(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, arguments);
		return attributes;
	}

}
