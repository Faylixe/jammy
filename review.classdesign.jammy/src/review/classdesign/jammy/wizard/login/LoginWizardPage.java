package review.classdesign.jammy.wizard.login;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author fv
 */
public final class LoginWizardPage extends WizardPage {

	/** **/
	private static final String NAME = "Google Code Jam login";

	/** **/
	private static final String DESCRIPTION = "Connect to the Google Code jam service.";
	
	/**
	 * 
	 */
	protected LoginWizardPage() {
		super(NAME);
		setDescription(DESCRIPTION);
	}

	/**
	 * 
	 * @param name
	 * @param container
	 */
	private void createField(final String name, final Composite container) {
		final Label label = new Label(container, SWT.NONE);
		label.setText(name);
		final Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		// TODO : Add listener.
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		// Initializes page layout and container.
		final GridLayout layout = new GridLayout(2, false);
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		createField("Email", container);
		createField("Password", container);
		setControl(container);
		setPageComplete(false);
	}

}
