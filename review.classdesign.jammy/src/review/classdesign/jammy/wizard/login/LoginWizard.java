package review.classdesign.jammy.wizard.login;

import org.eclipse.jface.wizard.Wizard;

/**
 * {@link LoginWizard} allows to login into google service.
 * 
 * @author fv
 */
public final class LoginWizard extends Wizard {

	/** Wizard title. **/
	private static final String TITLE = "Google Code Jam login";

	/** Login page. **/
	private final LoginWizardPage page;

	/**
	 * Default constructor.
	 */
	public LoginWizard() {
		super();
		setWindowTitle(TITLE);
		this.page = new LoginWizardPage();
	}
	
	/** {@inheritDoc} **/
	@Override
	public void addPages() {
		addPage(page);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean performFinish() {
		// Connection attempt here !
		return false;
	}

}
