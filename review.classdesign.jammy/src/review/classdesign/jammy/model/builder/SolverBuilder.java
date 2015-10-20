package review.classdesign.jammy.model.builder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.common.Template;

/**
 * A {@link SolverBuilder} allows to create solver class
 * file for a given problem using internal bundle template.
 * 
 * @author fv
 */
public final class SolverBuilder extends ProjectContributor {

	/** File extension used for created Java solver. **/
	private static final String SOLVER_EXTENSION = ".java";

	/**
	 * Default constructor.
	 * 
	 * @param project Target java project to be created.
	 * @param monitor Monitor instance used for project creation.
	 */
	public SolverBuilder(final IProject project, final IProgressMonitor monitor) {
		super(project, monitor);
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
		builder.append(JavaProjectBuilder.SOURCE_PATH);
		builder.append("/");
		builder.append(name);
		builder.append(SOLVER_EXTENSION);
		return getProject().getFile(builder.toString());
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
		return String.format(Template.SOLVER.get(), solvers);	
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
		final String template = getSolverTemplate(name);
		final InputStream stream = new ByteArrayInputStream(template.getBytes());
		file.create(stream, true, getMonitor());
		return file;
	}	

}
