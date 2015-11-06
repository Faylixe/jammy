package review.classdesign.jammy.addons.python;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import review.classdesign.jammy.addons.ISolverRunner;
import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.core.ProblemSolver;

/**
 * 
 * @author fv
 */
public final class PythonRunner implements ISolverRunner {

	/** **/
	private static final String PYTHON_COMMAND = "python";

	/** **/
	private final ProblemSolver solver;

	/** **/
	private Process process;

	/**
	 * 
	 * @param solver
	 */
	public PythonRunner(final ProblemSolver solver) {
		this.solver = solver;
	}

	/** {@inheritDoc} **/
	@Override
	public boolean isTerminated() {
		return process != null && !process.isAlive();
	}

	/** {@inheritDoc} **/
	@Override
	public void run(final String arguments, final String output) throws CoreException {
		final String path = solver.getFile().getLocation().toOSString();
		final List<String> command = Arrays.asList(PYTHON_COMMAND, path, arguments);
		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectOutput(new File(output));
		try {
			process = builder.start();
		}
		catch (final IOException e) {
			EclipseUtils.showError(e);
		}
	}

}
