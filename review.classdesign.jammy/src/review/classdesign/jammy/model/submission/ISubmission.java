package review.classdesign.jammy.model.submission;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * @author fv
 */
public interface ISubmission {

	/**
	 * 
	 * @return
	 */
	boolean isSuccess();

	/**
	 * 
	 * @param monitor
	 * @throws CoreException 
	 */
	void submit(IProgressMonitor monitor) throws CoreException;

	/**
	 * 
	 * @return
	 */
	IFile getOutput();

}
