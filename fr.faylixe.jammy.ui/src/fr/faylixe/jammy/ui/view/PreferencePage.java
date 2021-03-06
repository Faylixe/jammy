package fr.faylixe.jammy.ui.view;

import java.util.Set;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.JammyPreferences;

/**
 * Preference page implementation for the Jammy plugin.
 * 
 * @author fv
 */
public final class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/** Label for the host name field. **/
	private static final String HOSTNAME_LABEL = "Hostname";

	/** Label for the language field. s**/
	private static final String LANGUAGE_LABEL = "Language";

	/**
	 * Default constructor.
	 * Binds this preferences page to the Jammy plugin preference store.
	 */
	public PreferencePage() {
		super();
		setPreferenceStore(Jammy.getInstance().getPreferenceStore());
	}

	/**
	 * Creates and adds the language field to this page.
	 */
	private void createLanguage() {
		final Set<String> languages = Jammy.getInstance().getLanguages();
		final String [][] values = new String[languages.size()][2];
		int index = 0;
		for (final String language : languages) {
			values[index][0] = language;
			values[index][1] = language;
			index++;
		}
		final ComboFieldEditor language = new ComboFieldEditor(
				JammyPreferences.LANGUAGE_PROPERTY,
				LANGUAGE_LABEL,
				values,
				getFieldEditorParent());
		addField(language);
	}

	/**
	 * Creates and adds the hostname field to this page.
	 */
	private void createHostname() {
		final StringFieldEditor hostname = new StringFieldEditor(
				JammyPreferences.HOSTNAME_PROPERTY,
				HOSTNAME_LABEL,
				getFieldEditorParent());
		addField(hostname);
	}

	/** {@inheritDoc} **/
	@Override
	protected void createFieldEditors() {
		createHostname();
		createLanguage();
	}

	/** {@inheritDoc} **/
	@Override
	public void init(final IWorkbench workbench) {
		// Do nothing.
	}

}
