package fr.faylixe.jammy.ui.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

import fr.faylixe.jammy.core.command.AbstractProgressiveSolverCommand;
import fr.faylixe.jammy.core.model.ProblemSolver;
import fr.faylixe.jammy.ui.internal.DatasetEditorInput;

/**
 * 
 * @author fv
 */
public final class OpenDatasetEditorCommand extends AbstractProgressiveSolverCommand {

	/** **/
	private static final String TASK_NAME = "";

	/** {@inheritDoc} **/
	@Override
	protected void processSolver(final ProblemSolver solver, final IProgressMonitor monitor) throws CoreException {
		Display.getDefault().asyncExec(() -> {
			DatasetEditorInput.openFrom(solver);
		});
	}

	/** {@inheritDoc} **/
	@Override
	protected String getTaskName() {
		return TASK_NAME;
	}

}
