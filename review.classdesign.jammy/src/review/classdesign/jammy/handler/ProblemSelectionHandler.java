package review.classdesign.jammy.handler;

import java.util.Optional;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.model.Round;
import review.classdesign.jammy.ui.wizard.ProblemWizard;

/**
 * Default handler used for Jammy problem selection
 * command.
 * 
 * @author fv
 */
public final class ProblemSelectionHandler extends AbstractWizardHandler {

	/** **/
	private static final String TITLE = "No contest found";

	/** **/
	private static final String MESSAGE = "A Codejam contest should be selected first, would you like to select one now ?";

	/**
	 * 
	 * @return
	 */
	private Optional<IWizard> create() {
		final Optional<Round> round = Jammy.getDefault().getCurrentRound();	
		return Optional.ofNullable(round.isPresent() ? new ProblemWizard() : null);
	}

	/**
	 * 
	 * @return
	 */
	private Optional<IWizard> selectContest() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final boolean shouldSelect = MessageDialog.openQuestion(workbench.getDisplay().getActiveShell(), TITLE, MESSAGE);
		if (shouldSelect) {
			final ExecutionEvent event = new ExecutionEvent();
			final ContestSelectionHandler handler = new ContestSelectionHandler();
			try {
				handler.execute(event);
			}
			catch (final ExecutionException e) {
				// TODO : Handle error.
			}
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

}
