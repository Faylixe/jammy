package fr.faylixe.jammy.ui.command;

import java.util.Optional;

import org.eclipse.jface.wizard.IWizard;

import fr.faylixe.jammy.ui.internal.AbstractWizardCommand;
import fr.faylixe.jammy.ui.wizard.ContestWizard;

/**
 * Default handler used for Jammy contest selection
 * command.
 * 
 * @author fv
 */
public final class ContestSelectionCommand extends AbstractWizardCommand {

	/** Command identifier. **/
	public static final String ID = "review.classdesign.jammy.command.contestselection";

	/** {@inheritDoc} **/
	@Override
	protected Optional<IWizard> createWizard() {
		// TODO : Retrieve HTTPExecutorInstance here !.
		return Optional.of(new ContestWizard(null));
	}

}
