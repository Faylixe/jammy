package review.classdesign.jammy;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.service.prefs.BackingStoreException;

/**
 * 
 * @author fv
 */
public final class JammyPreferences {

	/** Preference key for the target host name.  **/
	public static final String HOSTNAME_PROPERTY = "review.classdesign.jammy.hostname";

	/** Default host name value to use. **/
	private static final String DEFAULT_HOST = "https://code.google.com";

	/**
	 * Functional method that acts as a {@link IPropertyChangeListener}
	 * in order to handle preferences changing, by saving the updated
	 * preferences. 
	 * 
	 * TODO : Trigger a single save by preference page validation in UI.
	 * 
	 * @param event Target property change event.
	 */
	protected static void propertyChange(final PropertyChangeEvent event) {
		final IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Jammy.PLUGIN_ID);
		preferences.put(HOSTNAME_PROPERTY, getHostname());
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
		store.setValue(HOSTNAME_PROPERTY, node.get(HOSTNAME_PROPERTY, DEFAULT_HOST));
	}

	/**
	 * 
	 * @return
	 */
	public static String getHostname() {
		final IPreferenceStore store = Jammy.getDefault().getPreferenceStore();
		return store.getString(HOSTNAME_PROPERTY);
	}

}
