package fr.faylixe.jammy.ui.command;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.command.AbstractProblemCommand;
import fr.faylixe.jammy.core.command.AbstractRunnableCommand;
import fr.faylixe.jammy.ui.wizard.submission.SubmissionWizard;

/**
 * Abstract command implementation that starts a {@link ProgressMonitorDialog},
 * using a built in runnable from the current selection (if such selection
 * is a {@link ProblemInput}) or by trying to retrieve input from wizard dialog.
 * 
 * @author fv
 */
public abstract class AbstractProblemInputCommand extends AbstractRunnableCommand<ProblemInput> {

	/**
	 * Default constructor.
	 */
	protected AbstractProblemInputCommand() {
		super(ProblemInput.class);
	}
	
	/** {@inheritDoc} **/
	@Override
	protected final ProblemInput getAlternative(final ExecutionEvent event) {
		final Problem problem = AbstractProblemCommand.getProblemFromEditor(event);
		if (problem != null) {
			final AtomicReference<ProblemInput> selected = new AtomicReference<>();
			final IWizard wizard = new SubmissionWizard(problem, selected::set);
			final WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
			dialog.open();
			return selected.get();
		}
		return null;
	}

}
