package fr.faylixe.jammy.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.Bundle;

import fr.faylixe.jammy.core.Jammy;

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

	/** Default name used by job created by {@link #createUIJob(IJobFunction)} method. **/
	private static final String DEFAULT_JOB_NAME = "Jammy job";

	/** **/
	private static final String NOT_NUMBER = "Input should be a number only";

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
	 * Retrieves the resource file content of the given <tt>path</tt>
	 * using the given <tt>bundle</tt> for retrieving the file stream.
	 * 
	 * @param path Path to retrieve template file from.
	 * @param bundle Bundle the path is relative to.
	 * @return Content read from the required resource file.
	 * @throws IOException If any error occurs while reading template content.
	 */
	public static String getResource(final String path, final Bundle bundle) throws IOException {
		final URL url = FileLocator.find(bundle, new Path(path), null);
		final InputStream stream = url.openStream();	
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		final Stream<String> lines = reader.lines();
		return lines.collect(Collectors.joining("\n"));
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
	 * 
	 * @param file
	 * @return
	 */
	public static File toFile(final IFile file) {
		return file.getRawLocation().makeAbsolute().toFile();
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
		executeCommand(commandId, new ExecutionEvent());
	}

	/**
	 * Executes the command denoted by the given <tt>commandId</tt>.
	 * 
	 * @param commandId Identifier of the command to execute.
	 * @param event Event to send to the command.
	 */
	public static void executeCommand(final String commandId, final ExecutionEvent event) {
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
	 * Creates and runs a UI thread based job that
	 * runs the given <tt>delegate</tt> {@link Consumer}.
	 * 
	 * @param delegate Delegate consumer instance executed into our created job.
	 * @return Created job instance.
	 */
	public static Job createUIJob(final Consumer<IProgressMonitor> delegate) {
		final Job job = Job.create(DEFAULT_JOB_NAME, monitor -> {
			Display.getDefault().asyncExec(() -> {
				delegate.accept(monitor);
			});
			return Status.OK_STATUS;
		});
		job.schedule();
		return job;
	}

	/**
	 * Opens the given <tt>file</tt> into a default editor
	 * based on file nature.
	 * 
	 * @param file File to open in an editor.
	 */
	public static void openFile(final IFile file) {
		Display.getDefault().asyncExec(() -> {
			final IWorkbenchPage page = getActivePage();
			try {
				IDE.openEditor(page, file);
			}
			catch (final PartInitException e) {
				showError(String.format(CANNOT_OPEN_FILE, file.getName()), e);
			}
		});
	}
	
	/**
	 * Closes all opened editor.
	 */
	public static void closeAllFile() {
		Display.getDefault().asyncExec(() -> {
			final IWorkbenchPage page = getActivePage();
			page.closeAllEditors(true);
		});
	}

	/**
	 * Retrieves and returns the current active workbench page.
	 * 
	 * @return Current active workbench page.
	 */
	public static IWorkbenchPage getActivePage() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

	/**
	 * Retrieves and returns the current active shell.
	 * 
	 * @return Current active shell.
	 */
	public static Shell getActiveShell() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		return display.getActiveShell();
	}

	/**
	 * Functional method that acts as {@link IInputValidator} instance.
	 * 
	 * @param input Input text to validate.
	 * @return <tt>null</tt> if the given <tt>text</tt> is a valid number, otherwise an error message.
	 */
	public static String isNumberValid(final String input) {
		for (final char character : input.toCharArray()) {
			if (!Character.isDigit(character)) {
				return NOT_NUMBER;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param selection
	 * @param target
	 * @return
	 */
	public static <T> Optional<T> getSelection(final ISelection selection, final Class<T> target) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Object object = structuredSelection.getFirstElement();
			if (target.isInstance(object)) {
				return Optional.of(target.cast(object));
			}
		}
		return Optional.empty();
	}

}
