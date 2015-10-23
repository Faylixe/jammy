package review.classdesign.jammy.core.builder;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Base class that aims to be subclassed for
 * implementing builder that are in charge of
 * contributing to eclipse {@link IProject}.
 * 
 * @author fv
 */
public class ProjectContributor {

	/** Target project contribution is made for. **/
	private final IProject project;

	/** Monitor instance used for project creation. **/
	private final IProgressMonitor monitor;

	/**
	 * Default constructor.
	 * 
	 * @param project Target java project to be created.
	 * @param monitor Monitor instance used for project creation.
	 */
	protected ProjectContributor(final IProject project, final IProgressMonitor monitor) {
		this.project = project;
		this.monitor = monitor;
	}

	/**
	 * Getter for contributor monitor.
	 * @return Monitor instance used for project creation. 
	 * @see #monitor
	 */
	protected final IProgressMonitor getMonitor() {
		return monitor;
	}

	/**
	 * Getter for contributor project.
	 * 
	 * @return Target project contribution is made for.
	 * @see #project
	 */
	protected final IProject getProject() {
		return project;
	}
	
	/**
	 * Creates a folder using the given <tt>name</tt> inside the internal project.
	 * 
	 * @param name Name of the folder to be created.
	 * @return Created folder instance.
	 * @throws CoreException If any error occurs while creating folder.
	 */
	protected final IFolder createFolder(final String name) throws CoreException {
		final IFolder folder = project.getFolder(name);
		if (!folder.exists()) {
			folder.create(false, true, monitor);
		}
		return folder;
	}

}
