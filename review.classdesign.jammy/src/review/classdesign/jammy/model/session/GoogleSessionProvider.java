package review.classdesign.jammy.model.session;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * 
 * @author fv
 */
public final class GoogleSessionProvider extends AbstractSourceProvider {

	/** Identifier of the source. **/
	private static final String LOGGED = "review.classdesign.jammy.logged";

	/** **/
	private static final String URL = "https://www.google.com/accounts/ClientLogin";

	/** Boolean flag that indicates if user is logged or not. **/
	private boolean logged;

	/**
	 * Sets the {@link #logged} property of this provider.
	 * 
	 * @param logged The new value of the {@link #logged} property.
	 */
	private void setLogged(final boolean logged) {
		this.logged = logged;
		fireSourceChanged(ISources.WORKBENCH, LOGGED, logged);
	}

	/**
	 * Indicates if the user is logged into a google account or not.
	 * 
	 * @return <tt>true</tt> if the user is logged into a google account, <tt>false</tt> otherwise.
	 */
	public boolean isLogged() {
		return logged;
	}

	/**
	 * Connect user to the google account specified by
	 * the given <tt>login</tt> and <tt>password</tt>.
	 * 
	 * @param login Login of the google account to connect in.
	 * @param password Password of the google account to connect in.
	 * @param monitor Monitor instance to use for handling progression.
	 * @throws URISyntaxException 
	 */
	public void login(final String login, final String password, final IProgressMonitor monitor) throws URISyntaxException {
		// TODO : Execute request
		setLogged(true);
	}

	/**
	 * Disconnect user of it google account.
	 */
	public void logout() {
		// TODO : Implement logout.
		setLogged(false);
	}


	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public Map<?, ?> getCurrentState() {
		return Collections.singletonMap(LOGGED, logged);
	}

	/** {@inheritDoc} **/
	@Override
	public String [] getProvidedSourceNames() {
		return new String[] { LOGGED };
	}

	/**
	 * Static method that allows to retrieve easily the provider instance.
	 * 
	 * @return Instance registered as a source provider.
	 */
	public static GoogleSessionProvider get() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final ISourceProviderService service = (ISourceProviderService) workbench.getService(ISourceProviderService.class);
		final ISourceProvider provider = service.getSourceProvider(LOGGED);
		return (GoogleSessionProvider) provider;
	}

}
