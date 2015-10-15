package review.classdesign.jammy.handler;

import java.util.Optional;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.model.Round;
import review.classdesign.jammy.ui.wizard.ProblemWizard;

/**
 * Default handler used for Jammy problem selection
 * command.
 * 
 * @author fv
 */
public final class ProblemSelectionHandler extends AbstractWizardHandler {

	/** Title displayed into the dialog.  **/
	private static final String TITLE = "No contest found";

	/** Message displayed into the dialog.**/
	private static final String MESSAGE = "A Codejam contest should be selected first, would you like to select one now ?";

	/** Error message displayed when an error occurs during problem 	 selection. **/
	private static final String HANDLER_ERROR = "An error occurs while starting problem selection wizard.";

	/**
	 * Creates and returns a {@link ProblemWizard} instance
	 * if a round is already selected.
	 * 
	 * @return Created {@link ProblemWizard} if round available, empty {@link Optional} otherwise.
	 */
	private Optional<IWizard> create() {
		final Optional<Round> round = Jammy.getDefault().getCurrentRound();	
		return Optional.ofNullable(round.isPresent() ? new ProblemWizard() : null);
	}

	/**
	 * This method use a dialog to prompt user for
	 * contest / round selection if required, before to return a
	 * problem selection wizard is possible.
	 * 
	 * @return Created {@link ProblemWizard} if round available, empty {@link Optional} otherwise.
	 */
	private Optional<IWizard> selectContest() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final boolean shouldSelect = MessageDialog.openQuestion(workbench.getDisplay().getActiveShell(), TITLE, MESSAGE);
		if (shouldSelect) {
			ContestSelectionHandler.execute();
		}
		return create();
	}

	/** {@inheritDoc} **/
	@Override
	protected Optional<IWizard> createWizard() {
		final Optional<IWizard> wizard = create();
		if (!wizard.isPresent()) {			
			return selectContest();
		}
		return wizard;
	}

	/**
	 * Executes this handler asynchronously using handler service.
	 */
	public static void execute() {
		final ProblemSelectionHandler handler = new ProblemSelectionHandler();
		try {
			handler.execute(new ExecutionEvent());
		}
		catch (final ExecutionException e) {
			EclipseUtils.showError(HANDLER_ERROR, e);
		}
	}

}
