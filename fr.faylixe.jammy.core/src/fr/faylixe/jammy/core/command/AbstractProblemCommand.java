package fr.faylixe.jammy.core.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.ProblemSolverFactory;

/**
 * Abstract command implementation that starts a {@link ProgressMonitorDialog},
 * using a built in runnable from the current selection (if such selection
 * is a {@link Problem}) or by trying to retrieve current problem from current editor.
 * 
 * @author fv
 */
public abstract class AbstractProblemCommand extends AbstractRunnableCommand<Problem> {

	/**
	 * Default constructor.
	 */
	public AbstractProblemCommand() {
		super(Problem.class);
	}
	
	/** {@inheritDoc} **/
	@Override
	protected Problem getAlternative(final ExecutionEvent event) {
		return getProblemFromEditor(event);
	}
	
	/**
	 * Static method that retrieves a Problem instance from the
	 * current editor input if any.
	 * 
	 * @param event Event to retrieve context editor for.
	 * @return Problem instance found if any, <tt>null</tt> otherwise.
	 */
	public static Problem getProblemFromEditor(final ExecutionEvent event) {
		final IEditorInput input = HandlerUtil.getActiveEditorInput(event);
		if (input instanceof IFileEditorInput) {
			final IFileEditorInput fileInput = (IFileEditorInput) input;
			final IFile file = fileInput.getFile();
			return ProblemSolverFactory.getInstance().getProblem(file);
		}
		return null;
	}

}
