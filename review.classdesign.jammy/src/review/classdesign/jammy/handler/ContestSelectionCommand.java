package review.classdesign.jammy.handler;

import java.util.Optional;

import org.eclipse.jface.wizard.IWizard;

import review.classdesign.jammy.ui.wizard.ContestWizard;

/**
 * Default handler used for Jammy contest selection
 * command.
 * 
 * @author fv
 */
public final class ContestSelectionCommand extends AbstractWizardHandler {

	/** Command identifier. **/
	public static final String ID = "review.classdesign.jammy.command.contestselection";

	/** {@inheritDoc} **/
	@Override
	protected Optional<IWizard> createWizard() {
		return Optional.of(new ContestWizard());
	}

}
