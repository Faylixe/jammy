package review.classdesign.jammy.core.addons;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import review.classdesign.jammy.core.common.EclipseUtils;
import review.classdesign.jammy.core.model.ProblemSolver;

/**
 * {@link ISolverRunner} implementation used for language
 * that are ran through a {@link Process}.
 * 
 * @author fv
 */
public class ProcessSolverRunner implements ISolverRunner {

	/** Error message displayed when an error occurs while executing subprocess. **/
	private static final String EXECUTION_ERROR = "An error occurs while running solver code";

	/** Problem solver instance this runner should execute. **/
	private final ProblemSolver solver;

	/** Command binary name that will be ran by this runner. **/
	private final String command;

	/** Process reference that is ran by this runner. **/
	private Process process;
	
	/**
	 * Default constructor.
	 * 
	 * @param command Command binary name that will be ran by this runner.
	 * @param solver Problem solver instance this runner should execute.
	 */
	public ProcessSolverRunner(final String command, final ProblemSolver solver) {
		this.command = command;
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
		final List<String> commands = Arrays.asList(command, path, arguments);
		final ProcessBuilder builder = new ProcessBuilder(commands);
		builder.redirectOutput(new File(output));
		try {
			process = builder.start();
		}
		catch (final IOException e) {
			EclipseUtils.showError(EXECUTION_ERROR, e);
		}
	}

}
