package review.classdesign.jammy.handler;

import java.util.Optional;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.IWizard;

import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.ui.wizard.ContestWizard;

/**
 * Default handler used for Jammy contest selection
 * command.
 * 
 * @author fv
 */
public final class ContestSelectionHandler extends AbstractWizardHandler {

	/** Error message displayed when an error occurs during contest selection. **/
	private static final String HANDLER_ERROR = "An error occurs while selecting contest / round.";

	/** {@inheritDoc} **/
	@Override
	protected Optional<IWizard> createWizard() {
		return Optional.of(new ContestWizard());
	}

	/**
	 * Executes this handler asynchronously using handler service.
	 */
	public static void execute() {
		final ContestSelectionHandler handler = new ContestSelectionHandler();
		try {
			handler.execute(new ExecutionEvent());
		}
		catch (final ExecutionException e) {
			EclipseUtils.showError(HANDLER_ERROR, e);
		}
	}

}
