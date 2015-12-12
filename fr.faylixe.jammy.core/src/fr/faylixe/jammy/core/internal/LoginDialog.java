package fr.faylixe.jammy.core.internal;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Custom dialog implementation, which 
 * aims to be used by login process.
 *
 * @author fv
 */
public final class LoginDialog extends Dialog {

	/** Message displayed into the dialog. **/
	private static final String MESSAGE = "Firefox web browser will open, please authenticate to Google Code Jam with it.";

	/**
	 * Default constructor.
	 * 
	 * @param shell Shell used.
	 */
	public LoginDialog(final Shell shell) {
		super(shell);
	}
	
	/** {@inheritDoc} **/
	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		final Label label = new Label(container, SWT.NONE);
		label.setText(MESSAGE);
		return container;
	}

	/** {@inheritDoc} **/
	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
	}

}
