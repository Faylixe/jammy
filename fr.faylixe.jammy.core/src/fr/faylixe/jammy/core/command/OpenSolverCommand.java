package fr.faylixe.jammy.core.command;

import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * Default handler used for opening a solver file.
 * 
 * @author fv
 */
public final class OpenSolverCommand extends AbstractProgressiveSolverCommand {

	/** Command identifier. **/
	public static final String ID = "review.classdesign.jammy.command.opensolver";

	/** Task name for the file opening. **/
	private static final String OPEN_FILE_TASK = "Opening solver class file";

	/** {@inheritDoc} **/
	@Override
	protected void processSolver(final ProblemSolver solver, final IProgressMonitor monitor) {
		EclipseUtils.openFile(solver.getFile());
	}

	/** {@inheritDoc} **/
	@Override
	protected String getTaskName() {
		return OPEN_FILE_TASK;
	}

}
