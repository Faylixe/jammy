package review.classdesign.jammy.ui.command;

import io.faylixe.googlecodejam.client.executor.IHTTPRequestExecutor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import review.classdesign.jammy.core.Jammy;
import review.classdesign.jammy.core.service.IGoogleSessionService;
import review.classdesign.jammy.ui.wizard.ContestWizard;

/**
 * Default handler used for Jammy contest selection
 * command.
 * 
 * @author fv
 */
public final class ContestSelectionCommand extends AbstractHandler {

	/** Command identifier. **/
	public static final String ID = "review.classdesign.jammy.command.contestselection";

	/** Name of the created job. **/
	//private static final String JOB_NAME = "Contest Selection";
	
	/** {@inheritDoc} **/
	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException {
		// TODO : Check login, and set up callback.
		if (IGoogleSessionService.requireLogin()) {
//			final Job job = Job.create(JOB_NAME, this::onLoggedSession);
//			job.schedule();
		}
		return null;
	}
		
	/**
	 * 
	 * @param monitor
	 * @return
	 */
	private final void onLoggedSession(final IHTTPRequestExecutor executor) {
		Display.getDefault().asyncExec(() -> {
			// Retrieves current shell.
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final Shell shell = workbench.getActiveWorkbenchWindow().getShell();
			// Creates wizard and run it.
			final IWizard wizard = new ContestWizard(executor, Jammy.getDefault());
			final WizardDialog dialog = new WizardDialog(shell, wizard);
			dialog.open();
		});
	}

}
