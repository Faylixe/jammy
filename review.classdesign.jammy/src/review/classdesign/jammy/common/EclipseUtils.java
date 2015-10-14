package review.classdesign.jammy.common;

import java.util.Optional;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

import review.classdesign.jammy.Jammy;

/**
 * Toolbox class that contains helpful method for dealing
 * with Eclipse workbench features.
 * 
 * @author fv
 */
public final class EclipseUtils {

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private EclipseUtils() {
		// Do nothing.
	}

	public static String getCurrentUser() {
		// TODO : Figure out how to retrieve current user nickname (triggering secure storage ?)
		return "user";
	}

	/**
	 * Handles the given <tt>exception</tt> by displaying
	 * it to user using eclipse status.
	 * 
	 * @param message Error message that should be displayed.
	 * @param execption Error caught that should be logged.
	 */
	public static void showError(final String message, final Exception execption) {
		execption.printStackTrace(); // TODO : Remove for production.
		final Status status = new Status(IStatus.ERROR, Jammy.PLUGIN_ID, message, execption);
		StatusManager.getManager().handle(status, StatusManager.SHOW);
	}

	/**
	 * Retrieves file instance that is currently edited.
	 * File is returned as an {@link Optional} reference.
	 * 
	 * @return Current edited file.
	 */
	public static Optional<IFile> getCurrentFile() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window != null) {
			final IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				final IEditorPart editor = page.getActiveEditor();
				if (editor != null) {
					final IEditorInput input = editor.getEditorInput();
					if (input != null) {
						final IFile file = (IFile) input.getAdapter(IFile.class);
						return Optional.ofNullable(file);
					}
				}
			}
		}
		return Optional.empty();
	}

}
