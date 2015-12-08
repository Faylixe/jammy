package fr.faylixe.jammy.core.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IServiceLocator;

import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.service.internal.GoogleSessionProvider;

/**
 * This interface defines behavior of our Google Session service.
 * Such service aims to manage HTTP client that are synchronized
 * with a connected google account for accessing Google Services.
 * 
 * @author fv
 */
public interface IGoogleSessionService {

	/** Title of the dialog displayed to notify user that connection is required. **/
	String TITLE = "Google session invalid";

	/** Message of the dialog displayed to notify user that connection is required. **/
	String MESSAGE = "You should have an active Google session to use this feature, would you like to log in now ?";

	/**
	 * Uses OAuth mechanism to create a persistent Google account
	 * session that will be used for interacting with google services.
	 * 
	 * @throws IOException If any error occurs while creating session.
	 * @throws GeneralSecurityException If any error occurs during the OAuth phase.
	 */
	void login() throws IOException, GeneralSecurityException;

	/**
	 * Destroy the current logged OAuth session.
	 */
	void logout();

	/**
	 * Static method that allows service instance quick access
	 * without dealing with the eclipse service APÏ.
	 * 
	 * @return Service instance.
	 */
	static IGoogleSessionService get() {
		final IServiceLocator locator = PlatformUI.getWorkbench();
		final Object service = locator.getService(IGoogleSessionService.class);
		return (IGoogleSessionService) service;
	}

	/**
	 * TODO : Optimizes method design.
	 * 
	 * @return <tt>true</tt> if user is logged, <tt>false</tt> otherwise.
	 */
	static boolean requireLogin() {
		final GoogleSessionProvider provider = GoogleSessionProvider.get();
		if (!provider.isLogged()) {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final boolean shouldLog = MessageDialog.openQuestion(workbench.getDisplay().getActiveShell(), TITLE, MESSAGE);
			if (shouldLog) {
				final IGoogleSessionService service = get();
				try {
					service.login();
				}
				catch (final IOException | GeneralSecurityException e) {
					EclipseUtils.showError(e);
				}
			}
		}
		return provider.isLogged();
	}

}
