package review.classdesign.jammy.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.google.api.client.http.HttpRequestFactory;

/**
 * 
 * @author fv
 */
public interface IGoogleSessionService {

	/**
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	void login() throws IOException, GeneralSecurityException;

	/**
	 * 
	 */
	void logout();

	/**
	 * 
	 * @return
	 */
	HttpRequestFactory createRequestFactory();

	/**
	 * 
	 * @return
	 */
	public static IGoogleSessionService get() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Object service = workbench.getService(IGoogleSessionService.class);
		return (IGoogleSessionService) service;
	}

}
