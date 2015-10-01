package review.classdesign.jammy.tests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import org.junit.Test;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleTest {

	@Test
	public void test() {
		try {
			final JsonFactory factory = JacksonFactory.getDefaultInstance();
			final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
			final GoogleClientSecrets secrets = GoogleClientSecrets.load(
					factory,
					new InputStreamReader(getClass().getResourceAsStream("/client_secret.json")));
			final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, factory, secrets, Collections.singleton("https://www.googleapis.com/auth/plus.me")).build();
			final Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
			final HttpRequestFactory httpFactory = transport.createRequestFactory(credential);
			final String url = "https://code.google.com/contest/dashboard?c=6224486%26cmd=GetProblems%26contest=6224486";
			final HttpRequest request = httpFactory.buildGetRequest(new GenericUrl(url));
			request.getHeaders().put("Referer", "https://code.google.com/contest/dashboard?c=6224486");
			final HttpResponse response = request.execute();
			final InputStream stream = response.getContent();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			reader.lines().forEach(System.out::println);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
