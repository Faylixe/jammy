package review.classdesign.jammy;

import org.eclipse.core.runtime.CoreException;

/**
 * An {@link ISolverRunner} represents a solver execution for a given
 * input.
 * 
 * @author fv
 */
public interface ISolverRunner {

	/**
	 * Indicates if this solver running step is terminated or not.
	 * 
	 * @return <tt>true</tt> if this solver running is terminated, <tt>false</tt> otherwise.
	 */
	boolean isTerminated();

	/**
	 * Starts the solver running step, using the given
	 * <tt>arguments</tt> as execution input
	 * (provided through command line arguments).
	 * The provided <tt>output</tt> path should be used
	 * to write execution output.
	 * 
	 * @param arguments Command line arguments to provide to the runner.
	 * @param output Path of the output file on which solver output should be redirected.
	 * @throws CoreException If any error occurs during execution.
	 */
	void run(String arguments, String output) throws CoreException;
	
}
