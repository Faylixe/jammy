package fr.faylixe.jammy.core.internal;

import java.util.Collections;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * Source provider that contains property about Jammy.
 * 
 * @author fv
 */
public final class JammySourceProvider extends AbstractSourceProvider {

	/** Identifier of the source. **/
	private static final String LOGGED = "fr.faylixe.jammy.logged";

	/** Boolean flag that indicates is user is logged or not. **/
	private boolean logged;
	
	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		// Do nothing.
	}

	/**
	 * Sets the logged state of the plugin.
	 * 
	 * @param logged <tt>true</tt> if plugin has a logged session, <tt>false</tt> otherwise.
	 */
	public void setLogged(final boolean logged) {
		this.logged = logged;
		fireSourceChanged(ISources.WORKBENCH, LOGGED, logged);
	}
	
	/** {@inheritDoc} **/
	@Override
	public Map<?, ?> getCurrentState() {
		return Collections.singletonMap(LOGGED, logged);
	}

	/** {@inheritDoc} **/
	@Override
	public String[] getProvidedSourceNames() {
		return new String [] { LOGGED };
	}
	
	/**
	 * Static getter which retrieves the
	 * {@link JammySourceProvider} instance.
	 * 
	 * @return Provider instance if any.
	 */
	public static JammySourceProvider get() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final ISourceProviderService service = (ISourceProviderService) workbench.getService(ISourceProviderService.class);
		final ISourceProvider provider = service.getSourceProvider(LOGGED);
		return (JammySourceProvider) provider;
	}

}
