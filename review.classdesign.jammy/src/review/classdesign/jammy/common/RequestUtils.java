package review.classdesign.jammy.common;

import java.io.IOException;
import java.util.Optional;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;

import review.classdesign.jammy.service.IGoogleSessionService;

/**
 * Toolbox class that contains helpful method for dealing with
 * HTTP request. All methods are using an HTTP transport instance
 * provided by the {@link IGoogleSessionService, which implies
 * that a google session should be logged in before any usage
 * of such methods.
 * 
 * @author fv
 */
public final class RequestUtils {

	/** **/
	private static final String SESSION_NOT_LOGGED = "Google sesson has not be logged in.";

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private RequestUtils() {
		// Do nothing.
	}

	/**
	 * Performs a GET request to the given <tt>url</tt>.
	 *
	 * @param url URL to reach with a GET request.
	 * @return Response content of the performed request.
	 */
	public static String get(final String url) throws IOException {
		final IGoogleSessionService service = IGoogleSessionService.get();
		final Optional<HttpRequestFactory> factory = service.createRequestFactory();
		if (!factory.isPresent()) {
			throw new IOException(SESSION_NOT_LOGGED);
		}
		final HttpRequest request = factory.get().buildGetRequest(new GenericUrl(url));
		final HttpResponse response = request.execute();
		System.out.println("--------------------");
		System.out.println("Request : " + request.getHeaders().toString());
		System.out.println("Response headers : " + response.getHeaders().toString());
		System.out.println("Response code : " + response.getStatusCode());
		System.out.println("Response message : " + response.getStatusMessage());
		return response.parseAsString();
	}

}
