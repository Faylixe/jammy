package review.classdesign.jammy.view;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.JammyPreferences;

/**
 * Preference page implementation for the Jammy plugin.
 * 
 * @author fv
 */
public final class PreferencePage extends FieldEditorPreferencePage {

	/** Label for the host name field. **/
	private static final String HOSTNAME_LABEL = "Host name of the code jam server to work with";

	/**
	 * Default constructor.
	 * Binds this preferences page to the Jammy plugin preference store.
	 */
	public PreferencePage() {
		setPreferenceStore(Jammy.getDefault().getPreferenceStore());
	}

	/** {@inheritDoc} **/
	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(JammyPreferences.HOSTNAME_PROPERTY, HOSTNAME_LABEL, getFieldEditorParent()));
	}

}
