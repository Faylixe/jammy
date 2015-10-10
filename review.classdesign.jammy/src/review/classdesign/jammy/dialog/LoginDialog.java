package review.classdesign.jammy.dialog;

import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import review.classdesign.jammy.service.IGoogleLogger;

/**
 * 
 * @author fv
 */
public final class LoginDialog extends Dialog {

	private final IGoogleLogger logger;

	/**
	 * 
	 * @param parentShell
	 * @param logger
	 */
	public LoginDialog(final Shell parentShell, final IGoogleLogger logger) {
		super(parentShell);
		this.logger = logger;
		
	}
	
	private static final int WIDTH = 500;
	
	private static final int HEIGHT = 400;

	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(WIDTH, HEIGHT);
	}

	/** **/
	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		final Browser browser = new Browser(container, SWT.BORDER);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		try {
			final boolean reached = browser.setUrl(logger.getURL());
			if (!reached) {
				throw new IOException(""); // TODO : Set error message.
			}
		}
		catch (final IOException e) {
			// TODO : Handle error.
			e.printStackTrace();
		}
		return container;
	}

	/** {@inheritDoc} **/
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		final Button button = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		button.addMouseListener(new MouseAdapter() {
			/** {@inheritDoc} **/
			@Override
			public void mouseDown(final MouseEvent e) {
				logger.cancel();
			}
		});
	}

}
