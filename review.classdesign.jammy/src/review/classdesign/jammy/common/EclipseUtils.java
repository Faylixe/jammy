package review.classdesign.jammy.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.services.IServiceLocator;
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

	/** Error message displayed when an error occurs while executing a command.**/
	private static final String COMMAND_ERROR = "An error occurs while executing the command %s";

	/** {@link NullProgressMonitor} instance shared in order to avoid instance duplication. **/
	public static final IProgressMonitor NULL_MONITOR = new NullProgressMonitor();

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
	 * @param execption Error caught that should be logged.
	 */
	public static void showError(final Exception exception) {
		showError(exception.getMessage(), exception);
	}

	/**
	 * Handles the given <tt>exception</tt> by displaying
	 * it to user using eclipse status.
	 * 
	 * @param message Error message that should be displayed.
	 * @param execption Error caught that should be logged.
	 */
	public static void showError(final String message, final Exception execption) {
		execption.printStackTrace(); // NOPMD
		final Status status = new Status(IStatus.ERROR, Jammy.PLUGIN_ID, message, execption);
		StatusManager.getManager().handle(status, StatusManager.SHOW);
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static CoreException createException(final String message) {
		final Status status = new Status(IStatus.ERROR, Jammy.PLUGIN_ID, message);
		return new CoreException(status);
	}

	/**
	 * Retrieves the content as a {@link String} of the given <tt>file</tt>
	 * 
	 * @param file File to retrieve content from.
	 * @return Content of the given file.
	 * @throws CoreException If any error occurs while reading the file content.
	 * @throws IOException If any error occurs while colsing file input stream.
	 */
	private static String getContent(final IFile file) throws CoreException, IOException {
		try (final InputStream stream = file.getContents()) {
			final InputStreamReader reader = new InputStreamReader(stream);
			final BufferedReader bufferedReader = new BufferedReader(reader);
			final String content = bufferedReader.lines().collect(Collectors.joining("\n"));
			return content;
		}
	}

	/**
	 * Compares and indicates if both given files are equals or not.
	 * 
	 * @param expected File that is containing expected content.
	 * @param actual File that is compared to the expected one.
	 * @return <tt>true</tt> if both file have the same content, <tt>false</tt> otherwise.
	 * @throws CoreException If any error occurs while reading file content.
	 * @throws IOException 
	 */
	public static boolean isFileEquals(final IFile expected, final IFile actual) throws CoreException, IOException {
		return getContent(expected).equals(getContent(actual));
	}

	/**
	 * Retrieves folder denoted by the given <tt>path</tt> from
	 * the given <tt>project</tt>. If the folder does not exist,
	 * it will be created using a default {@link NullProgressMonitor}
	 * before to be returned.
	 * 
	 * @param project Project to retrieve folder from.
	 * @param path Project relative path of the folder to retrieve.
	 * @return Retrieved folder.
	 * @throws CoreException If any error occurs while creating folder when required.
	 */
	public static IFolder getFolder(final IProject project, final String path) throws CoreException {
		return getFolder(project, path, NULL_MONITOR);
	}
	
	/**
	 * Retrieves folder denoted by the given <tt>path</tt> from
	 * the given <tt>project</tt>. If the folder does not exist,
	 * it will be created before to be returned.
	 * 
	 * @param project Project to retrieve folder from.
	 * @param path Project relative path of the folder to retrieve.
	 * @param monitor Monitor instance to use when creating folder.
	 * @return Retrieved folder.
	 * @throws CoreException If any error occurs while creating folder when required.
	 */
	public static IFolder getFolder(final IProject project, final String path, final IProgressMonitor monitor) throws CoreException {
		final IFolder folder = project.getFolder(path);
		if (!folder.exists()) {
			folder.create(false, true, monitor);
		}
		return folder;
	}

	/**
	 * Executes the command denoted by the given <tt>commandId</tt>.
	 * 
	 * @param commandId Identifier of the command to execute.
	 */
	public static void executeCommand(final String commandId) {
		final IServiceLocator locator = PlatformUI.getWorkbench();
		final ICommandService service = (ICommandService) locator.getService(ICommandService.class);
		final Command command = service.getCommand(commandId);
		try {
			command.executeWithChecks(new ExecutionEvent());
		}
		catch (final ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			showError(String.format(COMMAND_ERROR, commandId), e);
		}
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
			final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			final IWorkbenchPage page = window.getActivePage();
			try {
				IDE.openEditor(page, file);
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
		IFile file = null;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window != null) {
			final IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				final IEditorPart editor = page.getActiveEditor();
				if (editor != null) {
					final IEditorInput input = editor.getEditorInput();
					if (input != null) {
						file = (IFile) input.getAdapter(IFile.class);
					}
				}
			}
		}
		return Optional.ofNullable(file);
	}

}
