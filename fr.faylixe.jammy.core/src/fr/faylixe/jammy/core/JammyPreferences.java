package fr.faylixe.jammy.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.service.prefs.BackingStoreException;

import fr.faylixe.googlecodejam.client.executor.Request;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.listener.ILanguageManagerListener;

/**
 * <p>This class contains set of tools methods that are
 * used to manipulate plugin preferences.</p>
 * 
 * @author fv
 */
public final class JammyPreferences {

	/** **/
	public static final String TARGET_PAGE = "/codejam";

	/** Preference key for the target host name.  **/
	public static final String HOSTNAME_PROPERTY = "fr.faylixe.jammy.hostname";

	/** Preference key for the target language. **/
	public static final String LANGUAGE_PROPERTY = "fr.faylixe.jammy.language";

	/** Default language value to use. **/
	private static final String DEFAULT_LANGUAGE = "Java";

	/** Error message displayed when an error occurs while saving preferences. **/
	private static final String PREFERENCE_SAVE_ERROR = "An unexpected error occurs while saving Jammy preferences.";

	/** Collection of listener that would be notified when current language manager is changing. **/
	private static final List<ILanguageManagerListener> LISTENERS = new ArrayList<ILanguageManagerListener>();

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private JammyPreferences() {
		// Do nothing.
	}

	/**
	 * Adds the given <tt>listener</tt> instance to
	 * the listener list.
	 * 
	 * @param listener Listener instance to add.
	 */
	public static void addLanguageManagerListener(final ILanguageManagerListener listener) {
		LISTENERS.add(listener);
	}

	/**
	 * Removes the given <tt>listener</tt> instance of
	 * the listener list.
	 * 
	 * @param listener Listener instance to remove.
	 */
	public static void removeLanguageManagerListener(final ILanguageManagerListener listener) {
		LISTENERS.remove(listener);
	}

	/**
	 * Functional method that acts as a {@link IPropertyChangeListener}
	 * in order to handle preferences changing, by saving the updated
	 * preferences. 
	 * 
	 * @param event Target property change event.
	 */
	public static void propertyChange(final PropertyChangeEvent event) {
		final IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(Jammy.PLUGIN_ID);
		if (LANGUAGE_PROPERTY.equals(event.getProperty())) {
			preferences.put(LANGUAGE_PROPERTY, event.getNewValue().toString());
			for (final ILanguageManagerListener listener : LISTENERS) {
				listener.languageManagerChanged();
			}
		}
		else if (HOSTNAME_PROPERTY.equals(event.getProperty())) {
			preferences.put(HOSTNAME_PROPERTY, event.getNewValue().toString());			
		}
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
	public static void load(final IPreferenceStore store) {
		final IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Jammy.PLUGIN_ID);
		store.setValue(HOSTNAME_PROPERTY, node.get(HOSTNAME_PROPERTY, Request.getHostname()));
		store.setValue(LANGUAGE_PROPERTY, node.get(LANGUAGE_PROPERTY, DEFAULT_LANGUAGE));
		store.addPropertyChangeListener(JammyPreferences::propertyChange);
	}

	/**
	 * Getter for the language property of the preferences.
	 * 
	 * @return The language property of the preferences.
	 */
	public static String getCurrentLanguage() {
		final IPreferenceStore store = Jammy.getInstance().getPreferenceStore();
		return store.getString(LANGUAGE_PROPERTY);
	}

	/**
	 * Getter for the hostname property of the preferences.
	 * 
	 * @return The hostname property of the preferences.
	 */
	public static String getHostname() {
		final IPreferenceStore store = Jammy.getInstance().getPreferenceStore();
		return store.getString(HOSTNAME_PROPERTY);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getLoginTargetURL() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getHostname());
		builder.append(TARGET_PAGE);
		return builder.toString();
	}

}
