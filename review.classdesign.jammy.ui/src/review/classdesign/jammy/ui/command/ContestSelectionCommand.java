package review.classdesign.jammy.ui.command;

import java.util.Optional;

import org.eclipse.jface.wizard.IWizard;

import review.classdesign.jammy.ui.internal.AbstractWizardCommand;
import review.classdesign.jammy.ui.wizard.ContestWizard;

/**
 * Default handler used for Jammy contest selection
 * command.
 * 
 * @author fv
 */
public final class ContestSelectionCommand extends AbstractWizardCommand {

	/** Command identifier. **/
	public static final String ID = "review.classdesign.jammy.command.contestselection";

	/**
	 * Default constructor.
	 */
	public ContestSelectionCommand() {
		super();
	}

	/** {@inheritDoc} **/
	@Override
	protected Optional<IWizard> createWizard() {
		return Optional.of(new ContestWizard());
	}

}
