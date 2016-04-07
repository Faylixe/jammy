package fr.faylixe.jammy.ui.command;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.OnlineSubmission;

/**
 * 
 * @author fv
 */
public final class RunInputCommand extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Object object = structuredSelection.getFirstElement();
			if (object instanceof ProblemInput) {
				final ProblemInput input = (ProblemInput) object;
				final ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
				try {
					dialog.run(true, false, createRunnable(input));
				}
				catch (final InvocationTargetException | InterruptedException e) {
					EclipseUtils.showError(e);
				}				
			}
		}
		return null;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	private static IRunnableWithProgress createRunnable(final ProblemInput input) {
		return monitor -> {
			final ProblemSolverFactory factory = ProblemSolverFactory.getInstance();
			try {
				final ProblemSolver solver = factory.getSolver(input.getProblem(), monitor);
				final ISubmission submission = new OnlineSubmission(solver, input);
				ISubmission.runAsJob(submission);
			}
			catch (final CoreException e) {
				EclipseUtils.showError(e);
			}
		};
	}

}
