package fr.faylixe.jammy.core.service.internal;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;

/**
 * POJO implementation that represents a Google session.
 * Such session consists in :
 * <ul>
 * 	<li>Session credential that contains our OAuth token.</li>
 * 	<li>HttpTransport instance that is used to create request factory using internal credential.</li>
 * </ul>
 * @author fv
 */
public final class Session {

	/** Instance that is used for representing non valid session.**/
	public static final Session EMPTY = new Session(null, null);

	/** Authentification credentials. **/
	private final Credential credential;

	/** HTTP transport client. **/
	private final HttpTransport transport;
	
	/**
	 * Default constructor.
	 * 
	 * @param transport HTTP transport client.
	 * @param credential Authentification credentials.
	 */
	protected Session(final HttpTransport transport, final Credential credential) {
		this.credential = credential;
		this.transport = transport;
	}

	/**
	 * Indicates if this session is valid or not.
	 * 
	 * @return <tt>true</tt> if this session is not equals to {@link #EMPTY} session, <tt>false</tt> otherwise.
	 */
	public boolean isPresent() {
		return !EMPTY.equals(this);
	}
	
	/**
	 * Creates and returns a {@link HttpTransport} instance that is configured with OAuth token.
	 * 
	 * @return Created request factory.
	 */
	public HttpRequestFactory createRequestFactory() {
		return transport.createRequestFactory(credential);
	}

}
