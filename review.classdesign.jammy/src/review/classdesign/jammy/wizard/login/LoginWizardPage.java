package review.classdesign.jammy.wizard.login;

import java.util.regex.Pattern;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Page for the login wizard.
 * 
 * @author fv
 */
public final class LoginWizardPage extends WizardPage implements ModifyListener {

	/** Page name. **/
	private static final String NAME = "Google Code Jam login";

	/** Page description. **/
	private static final String DESCRIPTION = "Connect to the Google Code jam service.";
	
	/** Pattern that matches google account email used for validation. **/
	private static final Pattern pattern = Pattern.compile("^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$");

	/** Text field for the login input. **/
	private Text login;

	/** Text field for the password input. **/
	private Text password;

	/**
	 * Default constructor.
	 */
	protected LoginWizardPage() {
		super(NAME);
		setDescription(DESCRIPTION);
	}

	/**
	 * Getter for the login value.
	 * 
	 * @return Text value of the login field.
	 */
	public String getLogin() {
		return login.getText();
	}
	
	/**
	 * Getter for the password value.
	 * 
	 * @return Text value of the password field.
	 */
	public String getPassword() {
		return password.getText();
	}

	/** {@inheritDoc} **/
	@Override
	public void modifyText(final ModifyEvent e) {
		final String login = getLogin();
		final String password = getPassword();
		if (!login.isEmpty() && !password.isEmpty()) {
			setPageComplete(pattern.matcher(login).matches());
		}
	}

	/**
	 * Creates labeled text field using given <tt>name</tt> as label.
	 * 
	 * @param name Name of the field to create.
	 * @param container Container of the created field.
	 */
	private Text createField(final String name, final Composite container, final int property) {
		final Label label = new Label(container, SWT.NONE);
		label.setText(name);
		final Text text = new Text(container, SWT.BORDER | SWT.SINGLE | property);
		text.addModifyListener(this);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return text;
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		// Initializes page layout and container.
		final GridLayout layout = new GridLayout(2, false);
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		login = createField("Email", container, SWT.NONE);
		password = createField("Password", container, SWT.PASSWORD);
		setControl(container);
		setPageComplete(false);
	}

}
