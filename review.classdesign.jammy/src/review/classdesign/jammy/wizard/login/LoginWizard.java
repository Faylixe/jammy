package review.classdesign.jammy.wizard.login;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import review.classdesign.jammy.common.ExceptionHandler;
import review.classdesign.jammy.model.session.GoogleSessionProvider;

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
		setNeedsProgressMonitor(true);
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
		final GoogleSessionProvider provider = GoogleSessionProvider.get();
		try {
			getContainer().run(true, true, (monitor) -> {
				final AtomicReference<String> login = new AtomicReference<String>();
				final AtomicReference<String> password = new AtomicReference<String>();
				Display.getDefault().syncExec(() -> {
					login.set(page.getLogin());
					password.set(page.getPassword());
				});
				provider.login(login.get(), password.get(), monitor);
			});
		}
		catch (final InvocationTargetException | InterruptedException e) {
			ExceptionHandler.handle(e);
			return false;
		}
		return provider.isLogged();
	}

}
