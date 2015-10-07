package review.classdesign.jammy.ui.handler.wizard;

import org.eclipse.jface.wizard.IWizard;

import review.classdesign.jammy.ui.wizard.contest.ContestWizard;

/**
 * Default handler used for Jammy contest selection
 * command.
 * 
 * @author fv
 */
public final class ContestHandler extends AbstractWizardHandler {

	/** {@inheritDoc} **/
	@Override
	protected IWizard createWizard() {
		return new ContestWizard();
	}


}
