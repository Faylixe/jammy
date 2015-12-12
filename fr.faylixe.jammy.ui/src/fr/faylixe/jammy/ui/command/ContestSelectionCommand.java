package fr.faylixe.jammy.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import fr.faylixe.jammy.ui.wizard.ContestWizard;

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
	private static final String JOB_NAME = "Contest Selection";

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Job job = Job.create(JOB_NAME, monitor -> {
			Display.getDefault().asyncExec(() -> {
				final IWorkbench workbench = PlatformUI.getWorkbench();
				final Shell shell = workbench.getActiveWorkbenchWindow().getShell();
				final IWizard wizard = new ContestWizard();
				final WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.open();
			});
			return Status.OK_STATUS;
		});
		job.schedule();
		return null;
	}

}
