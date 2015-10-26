package review.classdesign.jammy;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.service.prefs.BackingStoreException;

import review.classdesign.jammy.common.EclipseUtils;

/**
 * This class contains set of tools methods that are
 * used to manipulate plugin preferences.
 * 
 * @author fv
 */
public final class JammyPreferences {

	/** Preference key for the target host name.  **/
	public static final String HOSTNAME_PROPERTY = "review.classdesign.jammy.hostname";

	/** Preference key for the target language. **/
	public static final String LANGUAGE_PROPERTY = "review.classdesign.jammy.language";

	/** Default host name value to use. **/
	private static final String DEFAULT_HOST = "https://code.google.com";

	/** Default language value to use. **/
	private static final String DEFAULT_LANGUAGE = "Java";

	/** Error message displayed when an error occurs while saving preferences. **/
	private static final String PREFERENCE_SAVE_ERROR = "An unexpected error occurs while saving Jammy preferences.";

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
			EclipseUtils.showError(PREFERENCE_SAVE_ERROR, e);
		}
	}

	/**
	 * Loads the plugin preferences into the given <tt>store<tt>.
	 * Aims to be called only by the {@link Jammy} plugin.
	 * 
	 * @param store Preference store to load preferences into.
	 */
	protected static void load(final IPreferenceStore store) {
		final IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Jammy.PLUGIN_ID);
		store.setValue(HOSTNAME_PROPERTY, node.get(HOSTNAME_PROPERTY, DEFAULT_HOST));
		store.setValue(LANGUAGE_PROPERTY, node.get(LANGUAGE_PROPERTY, DEFAULT_LANGUAGE));
	}

	/**
	 * Getter for the language property of the preferences.
	 * 
	 * @return The language property of the preferences.
	 */
	public static String getCurrentLanguage() {
		final IPreferenceStore store = Jammy.getDefault().getPreferenceStore();
		return store.getString(LANGUAGE_PROPERTY);
	}

	/**
	 * Getter for the hostname property of the preferences.
	 * 
	 * @return The hostname property of the preferences.
	 */
	public static String getHostname() {
		final IPreferenceStore store = Jammy.getDefault().getPreferenceStore();
		return store.getString(HOSTNAME_PROPERTY);
	}

}
