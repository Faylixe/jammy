package fr.faylixe.jammy.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import fr.faylixe.jammy.ui.wizard.contest.ContestWizard;

/**
 * Default handler used for Jammy contest selection
 * command.
 * 
 * @author fv
 */
public final class ArchiveContestSelectionCommand extends AbstractHandler {

	/** Command identifier. **/
	public static final String ID = "fr.faylixe.jammy.command.contestselection";

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		final IWizard wizard = new ContestWizard();
		final WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.open();
		return null;
	}

}
