package review.classdesign.jammy.handler.wizard;

import org.eclipse.jface.wizard.IWizard;

import review.classdesign.jammy.wizard.login.LoginWizard;

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
