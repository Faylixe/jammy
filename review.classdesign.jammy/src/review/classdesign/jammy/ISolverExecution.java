package review.classdesign.jammy;

import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author fv
 */
public interface ISolverExecution {

	/**
	 * 
	 * @return
	 */
	boolean isTerminated();

	/**
	 * 
	 * @param arguments
	 * @param output
	 * @throws CoreException
	 */
	void run(String arguments, String output) throws CoreException;

	
}
