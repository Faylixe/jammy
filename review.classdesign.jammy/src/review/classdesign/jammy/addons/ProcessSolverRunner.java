package review.classdesign.jammy.addons;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import review.classdesign.jammy.core.ProblemSolver;

public class ProcessSolverRunner implements ISolverRunner {

	/** **/
	private final ProblemSolver solver;

	/** **/
	private final String command;

	/** **/
	private Process process;
	
	/**
	 * 
	 * @param command
	 * @param solver
	 */
	public ProcessSolverRunner(final String command, final ProblemSolver solver) {
		this.command = command;
		this.solver = solver;
	}

	/** {@inheritDoc} **/
	@Override
	public boolean isTerminated() {
		return (process != null && !process.isAlive());
	}

	/** {@inheritDoc} **/
	@Override
	public void run(String arguments, String output) throws CoreException {
		final String path = solver.getFile().getLocation().toOSString();
		final List<String> commands = Arrays.asList(command, path, arguments);
		final ProcessBuilder builder = new ProcessBuilder(commands);
		builder.redirectOutput(new File(output));
		try {
			process = builder.start();
		}
		catch (final IOException e) {
			// TODO : Throw error.
			e.printStackTrace();
		}
	}

}
