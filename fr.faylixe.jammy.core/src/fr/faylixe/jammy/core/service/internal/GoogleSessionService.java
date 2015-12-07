package fr.faylixe.jammy.core.service.internal;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.google.api.client.http.HttpRequestFactory;

import fr.faylixe.jammy.core.service.IGoogleLogger;
import fr.faylixe.jammy.core.service.IGoogleSessionService;

/**
 * {@link IGoogleSessionService} implementation.
 * 
 * TODO : Handle preference feature ?.
 * @author fv
 */
public final class GoogleSessionService implements IGoogleSessionService {

	/**
	 * Default constructor.
	 * Initializes current session as an empty one.
	 */
	public GoogleSessionService() {
	}

	/**
	 * Session setter. This methods aims to be used as
	 * a {@link Session} {@link Consumer}.
	 * 
	 * @param session Session to be consumed.
	 */
	private void setSession() {
		GoogleSessionProvider.get().setLogged(true);
	}

	/** {@inheritDoc} **/
	@Override
	public void login() throws IOException, GeneralSecurityException {
//		if (!session.isPresent()) {
//			final IWorkbench workbench = PlatformUI.getWorkbench();
//			final Shell shell = workbench.getDisplay().getActiveShell();
//			final IGoogleLogger logger = GoogleLogger.createLogger(this::setSession);
//			final OAuthLoginDialog dialog = new OAuthLoginDialog(shell, logger);
//			logger.addListener(() -> {
//				workbench.getDisplay().asyncExec(dialog::close);
//			});
//			dialog.open();
//		}
	}

	/** {@inheritDoc} **/
	@Override
	public void logout() {
//		session = Session.EMPTY;
		GoogleSessionProvider.get().setLogged(false);
	}

	/** {@inheritDoc} **/
	@Override
	public Optional<HttpRequestFactory> createRequestFactory() {
//		if (!session.isPresent()) {
//			return Optional.empty();
//		}
		return null; //Optional.of(session.createRequestFactory());
	}

}
