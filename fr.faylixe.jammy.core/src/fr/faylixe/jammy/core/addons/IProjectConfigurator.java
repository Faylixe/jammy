package fr.faylixe.jammy.core.addons;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Project configurator object are in charge
 * of configuring created project regarding
 * of a target language.
 * 
 * @author fv
 */
public interface IProjectConfigurator {

	/**
	 * 
	 * @return
	 */
	default boolean shouldCreate() {
		return true;
	}

	/**
	 * Configures the given created <tt>project</tt>.
	 * 
	 * @param project Project to configure.
	 * @param monitor Monitor instance used for project configuration.
	 * @throws CoreException If any error occurs while configuring project.
	 */
	void configure(IProject project, IProgressMonitor monitor) throws CoreException;

}
