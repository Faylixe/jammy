package review.classdesign.jammy.service.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import review.classdesign.jammy.service.IGoogleLogger;

/**
 * 
 * @author fv
 */
public final class GoogleLogger implements IGoogleLogger {

	/** Target user id used for the session created. **/
	//private static final String USER_ID = "user";

	/** Default path of the JSON secret file. **/
	private static final String SECRET_PATH = "/client_secret.json";

	/** Default factory used to parse JSON data. **/
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Scopes of the created session. **/
	private static final Collection<String> SCOPES = Collections.singleton("https://www.googleapis.com/auth/plus.me");
	
	/** **/
	private final AuthorizationCodeFlow flow;

	/** **/
	private final LocalServerReceiver receiver;

	/** **/
	private final Consumer<Session> sessionConsumer;
	
	/** */
	private final Collection<Runnable> listeners;

	/** **/
	private String redirectUri;

	/**
	 * 
	 * @param builder
	 * @param sessionConsumer
	 */
	private GoogleLogger(final Builder builder, final Consumer<Session> sessionConsumer) {
		this.flow = builder.build();
		this.receiver = new LocalServerReceiver();
		this.listeners = new ArrayList<>(1);
		this.sessionConsumer = sessionConsumer;
	}

	/** **/
	@Override
	public void addListener(final Runnable listener) {
		listeners.add(listener);
	}

	/** **/
	@Override
	public void cancel() {
		try {
			receiver.stop();
		}
		catch (final IOException e) {
			// TODO : Handle error.
		}
	}

	/**
	 * 
	 * @param code
	 */
	private void authorize(final String code) {
		try {
			final AuthorizationCodeTokenRequest request = flow.newTokenRequest(code);
			request.setRedirectUri(redirectUri);
			final TokenResponse response = request.execute();
			final Credential credential = flow.createAndStoreCredential(response, "user"); // TODO : Match user id.
			sessionConsumer.accept(new Session(flow.getTransport(), credential));
			listeners.forEach(Runnable::run);
			receiver.stop();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param receiver
	 * @param codeConsumer
	 * @return
	 */
	private IStatus waitForCode(final IProgressMonitor monitor) {
		try {
			final String code = receiver.waitForCode();
			authorize(code);
		}
		catch (final IOException e) {
			// TODO : handle error.
		}
		return Status.OK_STATUS;
	}
	
	/** **/
	private static final String JOB_NAME = "receive-code";
	
	/** {@inheritDoc} **/
	@Override
	public String getURL() throws IOException {
		this.redirectUri = receiver.getRedirectUri();
		final Job job = Job.create(JOB_NAME, this::waitForCode); 
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
		final AuthorizationCodeRequestUrl request = flow.newAuthorizationUrl();
		request.setRedirectUri(redirectUri);
		return request.build();
	}

	/**
	 * 
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public static IGoogleLogger createLogger(final Consumer<Session> sessionConsumer) throws GeneralSecurityException, IOException {
		final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
		final Builder builder = new GoogleAuthorizationCodeFlow.Builder(transport, JSON_FACTORY, getSecret(), SCOPES);
		return new GoogleLogger(builder, sessionConsumer);
	}

	/**
	 * Static tool method that retrieves google client secret
	 * instance from internal JSON file.
	 * 
	 * @return {@link GoogleClientSecrets} instance created.
	 * @throws IOException If any error occurs while reading JSON file.
	 */
	private static GoogleClientSecrets getSecret() throws IOException {
		final InputStream stream = GoogleLogger.class.getResourceAsStream(SECRET_PATH);
		final InputStreamReader reader = new InputStreamReader(stream);
		return GoogleClientSecrets.load(JSON_FACTORY, reader);
	}

}
