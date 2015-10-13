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
public final class ContestSelectionHandler extends AbstractWizardHandler {

	/** {@inheritDoc} **/
	@Override
	protected Optional<IWizard> createWizard() {
		return Optional.of(new ContestWizard());
	}


}
