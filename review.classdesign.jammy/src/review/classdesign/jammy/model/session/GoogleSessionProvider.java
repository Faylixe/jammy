package review.classdesign.jammy.model.session;

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

	/** **/
	private static final String LOGGED = "review.classdesign.jammy.logged";

	/** Boolean flag that indicates if user is logged or not. **/
	private boolean logged;

	/**
	 * 
	 * @param logged
	 */
	public void setLogged(final boolean logged) {
		this.logged = logged;
		fireSourceChanged(ISources.WORKBENCH, LOGGED, logged);
	}

	public boolean isLogged() {
		return logged;
	}

	/**
	 * 
	 * @param login
	 * @param password
	 * @param monitor
	 */
	public void login(final String login, final String password, final IProgressMonitor monitor) {
		System.out.println("LOGGED !");
		setLogged(true);
	}

	/**
	 * 
	 */
	public void logout() {
		setLogged(false);
	}


	/** {@inheritDoc} **/
	@Override
	public void dispose() {
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
	 * 
	 * @return
	 */
	public static GoogleSessionProvider get() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final ISourceProviderService service = (ISourceProviderService) workbench.getService(ISourceProviderService.class);
		final ISourceProvider provider = service.getSourceProvider(LOGGED);
		return (GoogleSessionProvider) provider;
	}
}
