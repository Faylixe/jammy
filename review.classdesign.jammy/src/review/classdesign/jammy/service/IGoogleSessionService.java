package review.classdesign.jammy.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import review.classdesign.jammy.service.internal.GoogleSessionProvider;

import com.google.api.client.http.HttpRequestFactory;

/**
 * This interface defines behavior of our Google Session service.
 * Such service aims to manage HTTP client that are synchronized
 * with a connected google account for accessing Google Services.
 * 
 * @author fv
 */
public interface IGoogleSessionService {

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
	 * Creates and returns a {@link HttpRequestFactory} using internal session.
	 * 
	 * @return Created request factory.
	 * @throws IllegalStateException If login step hasn't be performed before.
	 */
	Optional<HttpRequestFactory> createRequestFactory();

	/**
	 * Static method that allows service instance quick access
	 * without dealing with the eclipse service APÏ.
	 * 
	 * @return Service instance.
	 */
	public static IGoogleSessionService get() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Object service = workbench.getService(IGoogleSessionService.class);
		return (IGoogleSessionService) service;
	}

	/**
	 * TODO : Optimizes method design.
	 * 
	 * @return <tt>true</tt> if user is logged, <tt>false</tt> otherwise.
	 */
	public static boolean requireLogin() {
		final GoogleSessionProvider provider = GoogleSessionProvider.get();
		if (!provider.isLogged()) {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final boolean shouldLog = MessageDialog.openQuestion(workbench.getDisplay().getActiveShell(), "", "");
			if (shouldLog) {
				final IGoogleSessionService service = get();
				try {
					service.login();
				}
				catch (final Exception e) {
					return false;
				}
			}
		}
		return true;
	}

}
