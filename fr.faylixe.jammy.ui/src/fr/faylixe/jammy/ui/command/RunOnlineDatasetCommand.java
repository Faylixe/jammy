package fr.faylixe.jammy.ui.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.OnlineSubmission;
import fr.faylixe.jammy.ui.view.SubmissionView;

/**
 * This command will run the selected problem
 * input.
 * 
 * @author fv
 */
public final class RunOnlineDatasetCommand extends AbstractProblemInputCommand {

	/** Title for the confirmation dialog. **/
	private static final String NOT_LOGGED_TITLE = "Not authenticated";

	/** Message for the confirmation dialog. **/
	private static final String NOT_LOGGED_MESSAGE = "You must first login";

	/** Title for the confirmation dialog. **/
	private static final String CONFIRMATION_TITLE = "Confirm submission";

	/** Message for the confirmation dialog. **/
	private static final String CONFIRMATION_MESSAGE = "You are about to trigger download for input %s for problem %s. Are you sure ?";

	/** {@inheritDoc} **/
	@Override
	protected boolean shouldExecute(final ProblemInput input) {
		final Shell shell = Display.getDefault().getActiveShell();
		if (!Jammy.getInstance().isLogged()) {
			MessageDialog.openInformation(shell, NOT_LOGGED_TITLE, NOT_LOGGED_MESSAGE);
			return false;
		}
		final String message = String.format(CONFIRMATION_MESSAGE, input.getName(), input.getProblem().getName());
		return MessageDialog.openConfirm(shell, CONFIRMATION_TITLE, message);
	}

	/**
	 * Factory method that creates a {@link IRunnableWithProgress} instance that
	 * is in charge of creating the submission and to submit it to the service.
	 * 
	 * @param input Problem input to create runnable for.
	 * @return Created instance.
	 */
	protected IRunnableWithProgress createRunnable(final ProblemInput input) {
		return monitor -> {
			final ProblemSolverFactory factory = ProblemSolverFactory.getInstance();
			try {
				SubmissionView.activate();
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
