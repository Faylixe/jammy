package review.classdesign.jammy.service.internal;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;

/**
 * 
 * @author fv
 */
public final class Session {
	
	/** Authentification credentials. **/
	private final Credential credential;

	/** HTTP transport client. **/
	private final HttpTransport transport;
	
	/**
	 * 
	 * @param transport
	 * @param credential
	 */
	protected Session(final HttpTransport transport, final Credential credential) {
		this.credential = credential;
		this.transport = transport;
	}

	/**
	 * 
	 * @return
	 */
	protected boolean isPresent() {
		return (!EMPTY.equals(this));
	}
	
	/**
	 * 
	 * @return
	 */
	protected HttpRequestFactory createRequestFactory() {
		return transport.createRequestFactory(credential);
	}

	/** **/
	protected static final Session EMPTY = new Session(null, null);

}
