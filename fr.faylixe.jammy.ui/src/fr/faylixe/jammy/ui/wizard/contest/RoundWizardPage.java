package fr.faylixe.jammy.ui.wizard.contest;

import java.util.function.Consumer;

import fr.faylixe.googlecodejam.client.Contest;
import fr.faylixe.googlecodejam.client.Round;
import fr.faylixe.jammy.ui.wizard.AbstractListWizardPage;

/**
 * <p>WizardPage implementation for round selection.</p>
 * 
 * @author fv
 */
public final class RoundWizardPage extends AbstractListWizardPage implements Consumer<Contest> {

	/** Name of this page. **/
	private static final String PAGE_NAME = "Round selection. ";

	/** Page description. **/
	private static final String PAGE_DESCRIPTION = "Please select any round";

	/** Round selected by the user. **/
	private Round selectedRound;

	/**
	 * Default constructor.
	 */
	public RoundWizardPage() {
		super(PAGE_NAME, PAGE_DESCRIPTION);
	}

	/**
	 * Getter for selected round.
	 * 
	 * @return Round instance selected, or <tt>null</tt> if no round was selected.
	 */
	protected Round getSelectedRound() {
		return selectedRound;
	}

	/** {@inheritDoc} **/
	@Override
	public void accept(final Contest contest) {
		setInput(contest.getRounds());
	}

	/** {@inheritDoc} **/
	@Override
	protected void onSelectionChanged(final Object selection) {
		if (selection instanceof Round) {
			selectedRound = (Round) selection;
		}
	}
	
	/** {@inheritDoc} **/
	@Override
	protected void onDoubleClick() {
		closeWizard();
	}

}
