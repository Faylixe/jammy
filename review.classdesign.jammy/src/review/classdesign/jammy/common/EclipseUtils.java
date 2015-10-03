package review.classdesign.jammy.common;

import java.util.Optional;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
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
