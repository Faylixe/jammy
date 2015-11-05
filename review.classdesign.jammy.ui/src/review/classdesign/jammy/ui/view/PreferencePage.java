package review.classdesign.jammy.ui.view;

import java.util.Set;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.JammyPreferences;

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

	/** Field editor for the host name.**/
	private StringFieldEditor hostname;

	/** Field editor for the language. **/
	private ComboFieldEditor language;


	/**
	 * Default constructor.
	 * Binds this preferences page to the Jammy plugin preference store.
	 */
	public PreferencePage() {
		super();
		setPreferenceStore(Jammy.getDefault().getPreferenceStore());
	}

	/**
	 * Creates and adds the language field to this page.
	 */
	private void createLanguage() {
		final Set<String> languages = Jammy.getDefault().getLanguages();
		final String [][] values = new String[languages.size()][2];
		int i = 0;
		for (final String language : languages) {
			values[i][0] = language;
			values[i][1] = language;
			i++;
		}
		language = new ComboFieldEditor(
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
		hostname = new StringFieldEditor(
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
