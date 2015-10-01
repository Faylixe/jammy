package review.classdesign.jammy.wizard.login;

import org.eclipse.jface.wizard.IWizard;

import review.classdesign.jammy.wizard.AbstractWizardHandler;

/**
 * Default handler used for Jammy login command.
 * 
 * @author fv
 */
public final class LoginHandler extends AbstractWizardHandler {

	/** {@inheritDoc} **/
	@Override
	protected IWizard createWizard() {
		return new LoginWizard();
	}


}
