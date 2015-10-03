package review.classdesign.jammy.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;

import review.classdesign.jammy.Jammy;

/**
 * 
 * @author fv
 */
public final class ExceptionHandler {

	/**
	 * 
	 * @param e
	 */
	public static void handle(final Exception e) {
		e.printStackTrace();
		final Status status = new Status(IStatus.ERROR, Jammy.PLUGIN_ID, e.getMessage(), e);
		StatusManager.getManager().handle(status, StatusManager.SHOW);
	}

}
