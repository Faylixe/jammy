package fr.faylixe.jammy.core.internal;

import java.util.function.Consumer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import fr.faylixe.googlecodejam.client.executor.Request;

/**
 * Custom dialog implementation, which 
 * aims to be used by login process.
 *
 * @author fv
 */
public final class LoginDialog extends Dialog implements LocationListener {

	/** **/
	private static final int WIDTH = 500;

	/** **/
	private static final int HEIGHT = 400;

	/** **/
	private final Consumer<String> cookieConsumer;

	/** **/
	private final String source;

	/** **/
	private final String target;

	/**
	 * Default constructor.
	 * 
	 * @param shell Shell used.
	 */
	public LoginDialog(final Shell shell, final String source, final String target, final Consumer<String> cookieConsumer) {
		super(shell);
		this.source = source;
		this.target = target;
		this.cookieConsumer = cookieConsumer;
	}

	/** {@inheritDoc} **/
	@Override
	public void changing(final LocationEvent event) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void changed(final LocationEvent event) {
		final String location = event.location;
		if (location.equals(target)) {
			final String cookie = Browser.getCookie(Request.COOKIE_NAME, target);
			cookieConsumer.accept(cookie);
			close();
		}
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
		browser.addLocationListener(this);
		browser.setUrl(source);
		return container;
	}

	/** {@inheritDoc} **/
	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
	}

}
