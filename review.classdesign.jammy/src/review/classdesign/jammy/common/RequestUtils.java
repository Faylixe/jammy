package review.classdesign.jammy.common;

import java.io.IOException;
import java.io.InputStream;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;

import review.classdesign.jammy.service.IGoogleSessionService;

/**
 * 
 * @author fv
 */
public final class RequestUtils {

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private RequestUtils() {
		// Do nothing.
	}

	/**
	 * 
	 * @param url
	 */
	public static String get(final String url) throws IOException {
		final IGoogleSessionService service = IGoogleSessionService.get();
		final HttpRequestFactory factory = service.createRequestFactory();
		final HttpRequest request = factory.buildGetRequest(new GenericUrl(url));
		final HttpResponse response = request.execute();
		return response.parseAsString();
	}


}
