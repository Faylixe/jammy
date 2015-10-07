package review.classdesign.jammy.ui.wizard.contest;

import java.util.Optional;

import org.eclipse.jface.wizard.Wizard;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.model.Contest;
import review.classdesign.jammy.model.Round;

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
	
	/** Page for {@link Contest} selection. **/
	private final ContestWizardPage contestPage;

	/** Page for {@link Round} selection. **/
	private final RoundWizardPage roundPage;

	/**
	 * Default constructor.
	 * Initializes wizard page.
	 */
	public ContestWizard() {
		super();
		setWindowTitle(TITLE);
		roundPage = new RoundWizardPage();
		contestPage = new ContestWizardPage(roundPage);
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
		final Optional<Contest> contest = roundPage.getContest();
		final Optional<Round> round = roundPage.getRound();
		if (contest.isPresent() && round.isPresent()) {
			Jammy.getDefault().setCurrent(contest, round);
			return true;
		}
		return false;
	}

}
