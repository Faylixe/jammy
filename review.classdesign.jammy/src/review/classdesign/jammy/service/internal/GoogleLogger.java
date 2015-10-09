package review.classdesign.jammy.service.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * 
 * @author fv
 */
public final class GoogleLogger implements IRunnableWithProgress {

	/** Target user id used for the session created. **/
	private static final String USER_ID = "user";

	/** Default path of the JSON secret file. **/
	private static final String SECRET_PATH = "/client_secret.json";

	/** Default factory used to parse JSON data. **/
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Scopes of the created session. **/
	private static final Collection<String> SCOPES = Collections.singleton("https://www.googleapis.com/auth/plus.me");

	/** **/
	private final Builder builder;

	/** **/
	private final Consumer<Session> consumer;

	/** **/
	private volatile boolean isRunning;

	/**
	 * 
	 * @param builder
	 * @param consumer
	 */
	private GoogleLogger(final Builder builder, final Consumer<Session> consumer) {
		this.builder = builder;
		this.consumer = consumer;
		this.isRunning = true;
	}

	/**
	 * 
	 * @param job
	 */
	private void runJob(final Job job) {
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
	}

	/**
	 * 
	 * @param application
	 * @param notifier
	 * @return
	 */
	private Thread createAuthorizationThread(final AuthorizationCodeInstalledApp application) {
		final Thread thread = new Thread(() -> {
			try {
				final Credential credential = application.authorize(USER_ID);
				final Session session = new Session(builder.getTransport(), credential);
				consumer.accept(session);
			}
			catch (final Exception e) {
				e.printStackTrace();
			}
			finally {
				terminate();
			}
		});
		final Job job = Job.create("", (monitor) -> {
			thread.start();
			return Status.OK_STATUS;
		});
		runJob(job);
		return thread;
	}

	/**
	 * 
	 */
	private void terminate() {
		isRunning = false;
	}

	/**
	 * 
	 */
	private void waitForCompletion() {
		while (isRunning) {
			try {
				Thread.sleep(1000);
			}
			catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	/** **/
	private static final String TASK = "Google session login";

	/** {@inheritDoc} **/
	@Override
	public void run(final IProgressMonitor monitor) {
		monitor.beginTask(TASK, IProgressMonitor.UNKNOWN);
		monitor.worked(1);
		final LocalServerReceiver receiver = new LocalServerReceiver();
		final AuthorizationCodeFlow flow = builder.build();
		final AuthorizationCodeInstalledApp application = new AuthorizationCodeInstalledApp(flow, receiver);
		final Thread thread = createAuthorizationThread(application);
		final Job job = new Job("") {
			/** **/
			@Override
			protected IStatus run(IProgressMonitor jobMonitor) {
				if (monitor.isCanceled()) {
					try {
						receiver.stop();
						thread.interrupt();
					}
					catch (final Exception e) {
						e.printStackTrace();
					}
					finally {
						terminate();
					}
				}
				else {
					schedule(1000);
				}
				return Status.OK_STATUS;
			}
		};
		runJob(job);
		waitForCompletion();
	}

	/**
	 * 
	 * @param consumer
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static IRunnableWithProgress createLogger(final Consumer<Session> consumer) throws IOException, GeneralSecurityException {
		final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
		final Builder builder = new GoogleAuthorizationCodeFlow.Builder(transport, JSON_FACTORY, getSecret(), SCOPES);
		return new GoogleLogger(builder, consumer);
	}

	/**
	 * Static tool method that retrieves google client secret
	 * instance from internal JSON file.
	 * 
	 * @return {@link GoogleClientSecrets} instance created.
	 * @throws IOException If any error occurs while reading JSON file.
	 */
	private static GoogleClientSecrets getSecret() throws IOException {
		final InputStream stream = GoogleSessionProvider.class.getResourceAsStream(SECRET_PATH);
		final InputStreamReader reader = new InputStreamReader(stream);
		return GoogleClientSecrets.load(JSON_FACTORY, reader);
	}

}
