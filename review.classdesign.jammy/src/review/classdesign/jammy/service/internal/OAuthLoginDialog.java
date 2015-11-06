package review.classdesign.jammy.service.internal;

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

import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.service.IGoogleLogger;

/**
 * Custom dialog implementation that aims
 * to provide web browsing view for OAuth
 * login.
 * 
 * @author fv
 */
public final class OAuthLoginDialog extends Dialog {

	/** Error message displayed when an error occurs during OAuth phase. **/
	private static final String OAUTH_ERROR_MESSAGE = "An error occurs while retrieving OAuth widget.";

	/** Initial dialog width. **/
	private static final int WIDTH = 500;
	
	/** Initial dialog height. **/
	private static final int HEIGHT = 400;

	/** Logger instance that provides OAuth URL. **/
	private final IGoogleLogger logger;

	/**
	 * Default constructor.
	 * 
	 * @param parentShell Shell that owns this dialog.
	 * @param logger Logger instance that provides OAuth URL.
	 */
	public OAuthLoginDialog(final Shell parentShell, final IGoogleLogger logger) {
		super(parentShell);
		this.logger = logger;
		
	}

	/** {@inheritDoc} **/
	@Override
	protected boolean isResizable() {
		return true;
	}

	/** {@inheritDoc} **/
	@Override
	protected Point getInitialSize() {
		return new Point(WIDTH, HEIGHT);
	}

	/** {@inheritDoc} **/
	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		final Browser browser = new Browser(container, SWT.BORDER);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		try {
			final boolean reached = browser.setUrl(logger.getURL());
			if (!reached) {
				throw new IOException("Cannot reach OAuth page.");
			}
		}
		catch (final IOException e) {
			EclipseUtils.showError(OAUTH_ERROR_MESSAGE, e);
			logger.cancel();
			close();
		}
		return container;
	}

	/** {@inheritDoc} **/
	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		final Button button = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		button.addMouseListener(new MouseAdapter() {
			/** {@inheritDoc} **/
			@Override
			public void mouseDown(final MouseEvent event) {
				logger.cancel();
			}
		});
	}

}
