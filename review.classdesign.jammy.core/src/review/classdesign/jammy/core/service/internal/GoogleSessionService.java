package review.classdesign.jammy.core.service.internal;

import io.faylixe.googlecodejam.client.CodeJamSession;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import review.classdesign.jammy.core.service.IGoogleLogger;
import review.classdesign.jammy.core.service.IGoogleSessionService;

/**
 * {@link IGoogleSessionService} implementation.
 * 
 * TODO : Handle preference feature ?.
 * @author fv
 */
public final class GoogleSessionService implements IGoogleSessionService {

	/** Current user session. **/
	private CodeJamSession session;

	/**
	 * Default constructor.
	 * Initializes current session as an empty one.
	 */
	public GoogleSessionService() {
		// TODO : Set session.
	}
	
	/** {@inheritDoc} **/
	@Override
	public CodeJamSession getSession() {
		return session;
	}

	/**
	 * Session setter. This methods aims to be used as
	 * a {@link Session} {@link Consumer}.
	 * 
	 * @param session Session to be consumed.
	 */
	private void setSession(final CodeJamSession session) {
		this.session = session;
		if (session != null) {
			GoogleSessionProvider.get().setLogged(true);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void login() throws IOException, GeneralSecurityException {
		if (session == null) {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final Shell shell = workbench.getDisplay().getActiveShell();
			final IGoogleLogger logger = GoogleLogger.createLogger(this::setSession);
			final OAuthLoginDialog dialog = new OAuthLoginDialog(shell, logger);
			logger.addListener(() -> {
				workbench.getDisplay().asyncExec(dialog::close);
			});
			dialog.open();
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void logout() {
		session = Session.EMPTY;
		GoogleSessionProvider.get().setLogged(false);
	}

}
