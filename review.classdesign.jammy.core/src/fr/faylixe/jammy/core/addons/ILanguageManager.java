package fr.faylixe.jammy.core.addons;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.jammy.core.model.ProblemSolver;
import fr.faylixe.jammy.core.model.webservice.contest.Problem;

/**
 * An {@link ILanguageManager} is in charge of managing
 * problem solver execution by providing for a given problem,
 * following features :
 * 
 * <ul>
 * 	<li>Retrieving a valid project instance</li>
 * 	<li>Retrieving a valid solver file</li>
 * 	<li>Run a given solver file for a given output.</li>
 * </ul>
 * 
 * Such manager can be registered to Jammy using the extension
 * point <tt>review.classdesign.jammy.addons</tt>.
 * 
 * @author fv
 */
public interface ILanguageManager {

	/** Identifier of the associated OSGi extension. **/
	String EXTENSION_ID = "review.classdesign.jammy.addons";

	/** Extension attribute name for the manager target language. **/
	String LANGUAGE_ATTRIBUTE = "language";

	/** Extension attribute name for the manager implementation class. **/
	String CLASS_ATTRIBUTE = "class";

	/** Task name for the project creation. **/
	String CREATE_PROJECT_TASK = "Creates %s project for current round";

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
	 * Retrieves a valid {@link ISolverRunner} instance that could
	 * manage the given <tt>solver</tt> execution.
	 * 
	 * @param solver Solver instance to get valid runner from.
	 * @param monitor Monitor instance used for creating runner.
	 * @return Created runner instance for executing the given <tt>solver</tt>
	 */
	ISolverRunner getRunner(ProblemSolver solver, IProgressMonitor monitor);

}
