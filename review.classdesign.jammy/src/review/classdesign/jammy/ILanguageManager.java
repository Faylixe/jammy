package review.classdesign.jammy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.model.ProblemSolver;
import review.classdesign.jammy.model.webservice.Problem;

/**
 * TODO : GOOD JAVADOC HERE !!!
 * 
 * @author fv
 */
public interface ILanguageManager {

	/** Identifier of the associated OSGi extension. **/
	public static final String EXTENSION_ID = "review.classdesign.jammy.addons";

	/** Extension attribute name for the manager target language. **/
	public static final String LANGUAGE_ATTRIBUTE = "language";

	/** Extension attribute name for the manager implementation class. **/
	public static final String CLASS_ATTRIBUTE = "class";

	/**
	 * Retrieves the project associated to the given <tt>problem</tt> if exist.
	 * Creates it using the given <tt>monitor</tt> otherwise.
	 * 
	 * @param problem Problem to retrieve project from.
	 * @param monitor Monitor instance to use for project creation.
	 * @return Retrieved project instance.
	 * @throws CoreException If any error occurs while creating the project.
	 */
	IProject getProject(Problem problem, IProgressMonitor monitor) throws CoreException;

	/**
	 * Retrieves the solver file associated to the given <tt>problem</tt> if exist.
	 * Creates it using the given <tt>monitor</tt> otherwise.
	 * 
	 * @param problem Problem to retrieve solver file from.
	 * @param monitor Monitor instance to use for solver file creation.
	 * @return Retrieved solver file instance.
	 * @throws CoreException  If any error occurs while creating the file.
	 */
	IFile getSolver(Problem problem, IProgressMonitor monitor) throws CoreException;

	/**
	 * 
	 * @param solver
	 * @return
	 */
	ISolverExecution getExecution(ProblemSolver solver, IProgressMonitor monitor) throws CoreException;

}
