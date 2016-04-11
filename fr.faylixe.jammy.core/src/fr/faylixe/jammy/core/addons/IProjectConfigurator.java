package fr.faylixe.jammy.core.addons;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * @author fv
 *
 */
public interface IProjectConfigurator {

	/**
	 * 
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
	void configure(IProject project, IProgressMonitor monitor) throws CoreException;

}
