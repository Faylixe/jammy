package review.classdesign.jammy.common;

import java.util.Optional;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.statushandlers.StatusManager;

import review.classdesign.jammy.Jammy;

/**
 * Toolbox class that contains helpful method for dealing
 * with Eclipse workbench features.
 * 
 * @author fv
 */
public final class EclipseUtils {

	/** Error message displayed when a file could not be opened. **/
	private static final String CANNOT_OPEN_FILE = "An error occurs while opening file %s";

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private EclipseUtils() {
		// Do nothing.
	}

	/**
	 * Retrieves the current user name.
	 * 
	 * @return Current user nickname.
	 */
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
	 * Opens the given <tt>file</tt> into a default editor
	 * based on file nature.
	 * 
	 * @param file File to open in an editor.
	 */
	public static void openFile(final IFile file) {
		Display.getDefault().asyncExec(() -> {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final IEditorDescriptor descriptor = workbench.getEditorRegistry().getDefaultEditor(file.getName());
			final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			final IWorkbenchPage page = window.getActivePage();
			try {
				page.openEditor(new FileEditorInput(file), descriptor.getId());
			}
			catch (final PartInitException e) {
				showError(String.format(CANNOT_OPEN_FILE, file.getName()), e);
			}
		});
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
