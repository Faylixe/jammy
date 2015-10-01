package review.classdesign.jammy.wizard.contest;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * Default handler used for Jammy contest selection
 * command.
 * 
 * @author fv
 */
public final class ContestHandler extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		// Retrieves current shell.
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		// Creates wizard and run it.
		final ContestWizard wizard = new ContestWizard();
		final WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.open();
		return null;
	}

}
