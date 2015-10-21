package review.classdesign.jammy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.model.webservice.Problem;

/**
 * 
 * @author fv
 */
public interface ILanguageManager {

	/** **/
	public static final String EXTENSION_ID = "";

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

}
