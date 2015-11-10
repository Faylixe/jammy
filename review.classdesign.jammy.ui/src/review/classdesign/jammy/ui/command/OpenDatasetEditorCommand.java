package review.classdesign.jammy.ui.command;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

import review.classdesign.jammy.core.command.AbstractProgressiveSolverCommand;
import review.classdesign.jammy.core.model.ProblemSampleDataset;
import review.classdesign.jammy.core.model.ProblemSolver;
import review.classdesign.jammy.ui.internal.DatasetEditorInput;

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
		final ProblemSampleDataset dataset = solver.getSampleDataset();
		final IFile input = dataset.getInput();
		final IFile output = dataset.getOutput();
		Display.getDefault().asyncExec(() -> {
			DatasetEditorInput.openWith(input, output);
		});
	}

	/** {@inheritDoc} **/
	@Override
	protected String getTaskName() {
		return TASK_NAME;
	}

}
