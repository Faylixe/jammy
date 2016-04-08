package fr.faylixe.jammy.ui.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.command.AbstractProgressiveSolverCommand;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.OnlineSubmission;
import fr.faylixe.jammy.ui.wizard.submission.SubmissionWizard;

/**
 * <p>Command that starts a problem input selection wizard
 * before to start an online submission.</p>
 * 
 * @author fv
 */
public final class RunOnlineDatasetCommand extends AbstractProgressiveSolverCommand {

	/** Task name for the file opening. **/
	private static final String RUN_ONLINE_TASK = "Running online dataset.";

	/** {@inheritDoc} **/
	@Override
	protected void processSolver(final ProblemSolver solver, final IProgressMonitor monitor) throws CoreException {
		// HandlerUtil.getActiveMenuSelection(get);
		Display.getDefault().asyncExec(() -> {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final Shell shell = workbench.getActiveWorkbenchWindow().getShell();
			final Problem problem = Jammy.getInstance().getSelectedProblem();
			final SubmissionWizard wizard = new SubmissionWizard(
					problem,
					input -> ISubmission.runAsJob(new OnlineSubmission(solver, input)));
			final WizardDialog dialog = new WizardDialog(shell, wizard);
			dialog.open();
		});
	}

	/** {@inheritDoc} **/
	@Override
	protected String getTaskName() {
		return RUN_ONLINE_TASK;
	}

}
