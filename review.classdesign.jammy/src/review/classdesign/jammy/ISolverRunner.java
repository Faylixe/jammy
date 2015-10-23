package review.classdesign.jammy;

import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author fv
 */
public interface ISolverRunner {

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
