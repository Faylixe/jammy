package fr.faylixe.jammy.core.command;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * Abstract command implementation that starts a {@link ProgressMonitorDialog},
 * using a built in runnable from the current selection (if such selection
 * is a {@link Problem}).
 * 
 * @author fv
 */
public abstract class AbstractProblemCommand extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		final Object object = EclipseUtils.getFirstSelection(selection);
		if (object != null && object instanceof Problem) {
			final Problem problem = (Problem) object;
			final ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event));
			try {
				dialog.run(true, false, createRunnable(problem));
			}
			catch (final InvocationTargetException | InterruptedException e) {
				EclipseUtils.showError(e);
			}				
		}
		return null;
	}

	/**
	 * Factory method that creates a {@link IRunnableWithProgress} instance that
	 * is in charge of processing for the given <tt>problem</tt>.
	 * 
	 * @param problem Problem to create runnable for.
	 * @return Created instance.
	 */
	protected abstract IRunnableWithProgress createRunnable(Problem problem);

}
