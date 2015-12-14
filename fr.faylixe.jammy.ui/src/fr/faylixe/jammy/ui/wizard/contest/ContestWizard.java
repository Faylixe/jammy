package fr.faylixe.jammy.ui.wizard.contest;

import org.eclipse.jface.wizard.Wizard;

import fr.faylixe.googlecodejam.client.Contest;
import fr.faylixe.googlecodejam.client.Round;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.common.EclipseUtils;

/** 
 * {@link ContestWizard} allows to select
 * a {@link Contest} and a {@link Round} that
 * will act as a current context for Jammy related
 * components.
 *
 * @author fv
 */
public final class ContestWizard extends Wizard {

	/** Wizard title. **/
	private static final String TITLE = "Google Code Jam contest selection";

	/** Page dedicated to contest selection. **/
	private final ContestWizardPage contestPage;

	/** Page dedicated to round selection. **/
	private final RoundWizardPage roundPage;	

	/**
	 * Default constructor.
	 */
	public ContestWizard() {
		super();
		setWindowTitle(TITLE);
		this.roundPage = new RoundWizardPage();
		this.contestPage = new ContestWizardPage(roundPage);
	}

	/** {@inheritDoc} **/
	@Override
	public void addPages() {
		addPage(contestPage);
		addPage(roundPage);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean performFinish() {
		final Round round = roundPage.getSelectedRound();
		if (round != null) {
			EclipseUtils.createUIJob(monitor -> {
				// TODO : Disable contest explorer view.
				Jammy.getInstance().createSession(round);
				// TODO : Reenable contest
			});
			return true;
		}
		return false;
	}

}
