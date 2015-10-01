package review.classdesign.jammy.service.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import review.classdesign.jammy.service.IGoogleSessionService;

/**
 * 
 * @author fv
 */
public final class GoogleSessionService implements IGoogleSessionService {

	/** **/
	private static final String USER_ID = "user";

	/** **/
	private static final String SECRET_PATH = "/client_secret.json";

	/** Default factory used to parse JSON data. **/
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** **/
	private static final Collection<String> SCOPES = Collections.singleton("https://www.googleapis.com/auth/plus.me");

	/**
	 * Optional reference to the authentification credentials.
	 * Initialized during the login phase using OAuth 2.0.
	 */
	private Optional<Credential> credential;

	/**
	 * Optional reference to the HTTP transport client.
	 * Initialized during the first login phase.
	 */
	private Optional<NetHttpTransport> transport;

	/**
	 * 
	 */
	public GoogleSessionService() {
		this.transport = Optional.empty();
		this.credential = Optional.empty();
	}

	/** {@inheritDoc} **/
	@Override
	public void login() throws IOException, GeneralSecurityException {
		if (!credential.isPresent()) {
			if (!transport.isPresent()) {
				transport = Optional.of(GoogleNetHttpTransport.newTrustedTransport());
			}
			final GoogleClientSecrets secrets = getSecret();
			final Builder builder = new GoogleAuthorizationCodeFlow.Builder(transport.get(), JSON_FACTORY, secrets, SCOPES);
			final LocalServerReceiver receiver = new LocalServerReceiver();
			final AuthorizationCodeFlow flow = builder.build();
			final AuthorizationCodeInstalledApp application = new AuthorizationCodeInstalledApp(flow, receiver);
			credential = Optional.of(application.authorize(USER_ID));
			GoogleSessionProvider.get().setLogged(true);
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private static GoogleClientSecrets getSecret() throws IOException {
		final InputStream stream = GoogleSessionProvider.class.getResourceAsStream(SECRET_PATH);
		final InputStreamReader reader = new InputStreamReader(stream);
		return GoogleClientSecrets.load(JSON_FACTORY, reader);
	}

	/** {@inheritDoc} **/
	@Override
	public void logout() {
		credential = Optional.empty();
		GoogleSessionProvider.get().setLogged(false);
	}

	/** {@inheritDoc} **/
	@Override
	public HttpRequestFactory createRequestFactory() {
		if (!transport.isPresent() || !credential.isPresent()) {
			// TODO : Handle error.
		}
		return transport.get().createRequestFactory(credential.get());
	}

}
