
package fr.faylixe.jammy.addons.java.internal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.jammy.addons.java.JavaAddonPlugin;

/**
 * A {@link JavaSolverBuilder} allows to create solver class
 * file for a given problem using internal bundle template.
 * 
 * @author fv
 */
public final class JavaSolverBuilder {

	/** File extension used for created Java solver. **/
	public static final String SOLVER_EXTENSION = ".java";

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
	public JavaSolverBuilder(final IProject project, final IProgressMonitor monitor) {
		this.project = project;
		this.monitor = monitor;
	}

	/**
	 * Retrieves and returns solver file instance
	 * associated to the current problem.
	 * 
	 * @param name Name of the file to retrieve.
	 * @return Associated {@link IFile} instance.
	 * @throws CoreException If any error occurs while creating project if required.
	 */
	private IFile getFile(final String name) {
		final StringBuilder builder = new StringBuilder();
		builder
			.append(JavaProjectBuilder.SOURCE_PATH)
			.append('/')
			.append(name)
			.append(SOLVER_EXTENSION);
		return project.getFile(builder.toString());
	}

	/**
	 * Creates and returns a valid solver template for this problem.
	 * 
	 * @param name Name of the solver class created.
	 * @return Created template.
	 */
	private static String getSolverTemplate(final String name) {
		final Object [] solvers = new Object[4];
		for (int i = 0; i < 4; i++) {
			solvers[i] = name;
		}
		return String.format(JavaAddonPlugin.getDefault().getSolverTemplate(), solvers);	
	}

	/**
	 * Builds the solver file for the given <tt>problem</tt>.
	 * 
	 * @param problem Problem to build solver class file for.
	 * @return Solver class file reference.
	 * @throws CoreException If any error occurs while creating solver file.
	 */
	public IFile build(final String name) throws CoreException {
		final IFile file = getFile(name);
		if (!file.exists()) {
			final String template = getSolverTemplate(name);
			final InputStream stream = new ByteArrayInputStream(template.getBytes());
			file.create(stream, true, monitor);
		}
		return file;
	}	

}
