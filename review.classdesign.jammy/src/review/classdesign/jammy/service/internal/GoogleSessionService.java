package review.classdesign.jammy.service.internal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.google.api.client.http.HttpRequestFactory;

import review.classdesign.jammy.service.IGoogleSessionService;

/**
 * {@link IGoogleSessionService} implementation.
 * 
 * TODO : Handle preference feature.
 * @author fv
 */
public final class GoogleSessionService implements IGoogleSessionService {

	/** **/
	private Session session;

	/**
	 * Default constructor.
	 */
	public GoogleSessionService() {
		this.session = Session.EMPTY;
	}

	/**
	 * 
	 * @param session
	 */
	private void setSession(final Session session) {
		this.session = session;
		if (session.isPresent()) {
			GoogleSessionProvider.get().setLogged(true);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void login() throws IOException, GeneralSecurityException {
		if (!session.isPresent()) {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final Shell shell = workbench.getDisplay().getActiveShell();
			final ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
			final IRunnableWithProgress logger = GoogleLogger.createLogger(this::setSession);
			try {
				System.out.println("Logger created, run it through ProgressMonitorDialog");
				dialog.run(true, true, logger);
			}
			catch (final InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void logout() {
		session = Session.EMPTY;
		GoogleSessionProvider.get().setLogged(false);
	}

	/** {@inheritDoc} **/
	@Override
	public Optional<HttpRequestFactory> createRequestFactory() {
		if (!session.isPresent()) {
			return Optional.empty();
		}
		return Optional.of(session.createRequestFactory());
	}

}
