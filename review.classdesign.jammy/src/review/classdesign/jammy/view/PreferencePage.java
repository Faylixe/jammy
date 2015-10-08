package review.classdesign.jammy.view;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
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

	/** **/
	private StringFieldEditor hostname;

	/**
	 * Default constructor.
	 * Binds this preferences page to the Jammy plugin preference store.
	 */
	public PreferencePage() {
		setPreferenceStore(Jammy.getDefault().getPreferenceStore());
	}

	/**
	 * 
	 * @param event
	 */
	private void hostnameChanged(final PropertyChangeEvent event) {
		
	}

	/**
	 * 
	 */
	private void createHostname() {
		hostname = new StringFieldEditor(
				JammyPreferences.HOSTNAME_PROPERTY,
				HOSTNAME_LABEL,
				getFieldEditorParent());
		hostname.setPropertyChangeListener(this::hostnameChanged);
		addField(hostname);
	}

	/** {@inheritDoc} **/
	@Override
	protected void createFieldEditors() {
		createHostname();
	}

	/** {@inheritDoc} **/
	@Override
	public void init(final IWorkbench workbench) {
		
	}

}
