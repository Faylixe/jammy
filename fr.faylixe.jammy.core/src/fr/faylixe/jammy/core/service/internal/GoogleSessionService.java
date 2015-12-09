package fr.faylixe.jammy.core.service.internal;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openqa.selenium.firefox.FirefoxDriver;

import fr.faylixe.googlecodejam.client.executor.HttpRequestExecutor;
import fr.faylixe.googlecodejam.client.executor.SeleniumCookieSupplier;
import fr.faylixe.jammy.core.JammyPreferences;
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
	 * Session setter.
	 */
	private void setSession() {
		GoogleSessionProvider.get().setLogged(true);
	}

	/** {@inheritDoc} **/
	@Override
	public void login() throws IOException, GeneralSecurityException {
		// TODO : Open blocking dialog.
		final Job loggingJob = Job.create("", monitor -> {
			final StringBuilder builder = new StringBuilder();
			builder.append(JammyPreferences.getHostname());
			builder.append("/codejam");
			final SeleniumCookieSupplier cookieSupplier = new SeleniumCookieSupplier(builder.toString(), FirefoxDriver::new);
			final String cookie = cookieSupplier.get();
			HttpRequestExecutor.create(JammyPreferences.getHostname(), cookie);
			// TODO : Set created session.
			// TODO : Close blocking dialog.
			setSession();
			return Status.OK_STATUS;
		});
		loggingJob.schedule();
	}

	/** {@inheritDoc} **/
	@Override
	public void logout() {
		GoogleSessionProvider.get().setLogged(false);
	}

}
