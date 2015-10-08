package review.classdesign.jammy.handler;

import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import review.classdesign.jammy.service.IGoogleSessionService;

/**
 * Abstract handler implementation that only run a {@link Wizard}
 * created by the {@link #createWizard()} abstract method.
 * 
 * @author fv
 */
public abstract class AbstractWizardHandler extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		if (IGoogleSessionService.requireLogin()) {
			// Retrieves current shell.
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final Shell shell = workbench.getActiveWorkbenchWindow().getShell();
			// Creates wizard and run it.
			final Optional<IWizard> wizard = createWizard();
			if (wizard.isPresent()) {
				final WizardDialog dialog = new WizardDialog(shell, wizard.get());
				dialog.open();
			}
		}
		return null;
	}

	/**
	 * Creates and returns the wizard instance this handler should run.
	 * 
	 * @return Created wizard instance.
	 */
	protected abstract Optional<IWizard> createWizard();

}
