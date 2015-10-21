package review.classdesign.jammy.ui.command.internal;

import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import review.classdesign.jammy.service.IGoogleSessionService;

/**
 * Abstract handler implementation that only run a {@link Wizard}
 * created by the {@link #createWizard()} abstract method.
 * 
 * TODO : Consider remerging abstract handler with contest selection handler.
 * 
 * @author fv
 */
public abstract class AbstractWizardCommand extends AbstractHandler {

	/** Name of the created job. **/
	private static final String JOB_NAME = "Contest Selection";
	
	/** {@inheritDoc} **/
	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException {
		if (IGoogleSessionService.requireLogin()) {
			final Job job = Job.create(JOB_NAME, this::onLoggedSession);
			job.schedule();
		}
		return null;
	}
		
	/**
	 * 
	 * @param monitor
	 * @return
	 */
	private final IStatus onLoggedSession(final IProgressMonitor monitor) {
		Display.getDefault().asyncExec(() -> {
			// Retrieves current shell.
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final Shell shell = workbench.getActiveWorkbenchWindow().getShell();
			// Creates wizard and run it.
			final Optional<IWizard> wizard = createWizard();
			if (wizard.isPresent()) {
				final WizardDialog dialog = new WizardDialog(shell, wizard.get());
				dialog.open();
			}
		});
		return Status.OK_STATUS;
	}

	/**
	 * Creates and returns the wizard instance this handler should run.
	 * 
	 * @return Created wizard instance.
	 */
	protected abstract Optional<IWizard> createWizard();

}
