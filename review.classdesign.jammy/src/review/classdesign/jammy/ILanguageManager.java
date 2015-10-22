package review.classdesign.jammy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.model.ProblemSolver;
import review.classdesign.jammy.model.submission.ISolverExecution;
import review.classdesign.jammy.model.webservice.Problem;

/**
 * 
 * @author fv
 */
public interface ILanguageManager {

	/** **/
	public static final String EXTENSION_ID = "";

	/** **/
	public static final String LANGUAGE_ATTRIBUTE = "language";

	/** **/
	public static final String CLASS_ATTRIBUTE = "class";

	/**
	 * 
	 * @param problem
	 * @param monitor
	 * @return
	 * @throws CoreException
	 */
	IProject getProject(Problem problem, IProgressMonitor monitor) throws CoreException;

	/**
	 * 
	 * @param problem
	 * @param monitor
	 * @return
	 * @throws CoreException 
	 */
	IFile getSolver(Problem problem, IProgressMonitor monitor) throws CoreException;

	/**
	 * 
	 * @param solver
	 * @return
	 */
	ISolverExecution getExecution(ProblemSolver solver, IProgressMonitor monitor) throws CoreException;

}
