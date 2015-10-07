package review.classdesign.jammy;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.service.prefs.BackingStoreException;

/**
 * 
 * @author fv
 */
public final class JammyPreferences {

	/** **/
	public static final String HOSTNAME = "hostname";

	/** **/
	private static final String DEFAULT_HOST = "https://code.google.com";

	/**
	 * 
	 * @param event
	 */
	protected static void propertyChange(final PropertyChangeEvent event) {
		final IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Jammy.PLUGIN_ID);
		preferences.put(HOSTNAME, getHostname());
		try {
			preferences.flush();
		}
		catch (final BackingStoreException e) {
			// TODO : Handle error.
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param store
	 */
	protected static void load(final IPreferenceStore store) {
		final IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Jammy.PLUGIN_ID);
		store.setValue(HOSTNAME, node.get(HOSTNAME, DEFAULT_HOST));
	}

	/**
	 * 
	 * @return
	 */
	public static String getHostname() {
		final IPreferenceStore store = Jammy.getDefault().getPreferenceStore();
		return store.getString(HOSTNAME);
	}

}
