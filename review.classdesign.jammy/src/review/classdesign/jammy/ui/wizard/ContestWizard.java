package review.classdesign.jammy.ui.wizard;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.FunctionalContentProvider;
import review.classdesign.jammy.common.FunctionalLabelProvider;
import review.classdesign.jammy.common.NamedObject;
import review.classdesign.jammy.model.Contest;
import review.classdesign.jammy.model.Round;
import review.classdesign.jammy.ui.internal.ListPageBuilder;

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

	/** Contest page name. **/
	private static final String CONTEST_NAME = "Round selection";

	/** Contest page description. **/
	private static final String CONTEST_DESCRIPTION = "Please select a Jam contest";

	/** Round page name. **/
	private static final String ROUND_NAME = "Round selection";

	/** Round page description. **/
	private static final String ROUND_DESCRIPTION = "Please select any round";

	/** Selected contest. **/
	private Contest contest;

	/** Selected round. **/
	private Round round;

	/**
	 * Default constructor.
	 */
	public ContestWizard() {
		super();
		setWindowTitle(TITLE);
	}

	/**
	 * Functional method that acts as a {@link Supplier} of available
	 * contest.
	 * 
	 * @return List of contest available.
	 */
	private List<Contest> getContests() {
		try {
			return Contest.get();
		}
		catch (final Exception e) {
			// TODO : Log error.
		}
		return null;
	}
	
	/**
	 * Functional method that acts as a {@link Supplier} of available
	 * round.
	 * 
	 * @return List of contest available.
	 */
	private List<Round> getRounds() {
		return (contest == null ? Collections.emptyList() : contest.getRounds());
	}

	/**
	 * Sets the selected contest.
	 * 
	 * @param contest Contest instance that has been selected.
	 */
	private void setContest(final Object contest) {
		this.contest = (Contest) contest;
	}
	
	/**
	 * Sets the selected round.
	 * 
	 * @param round Round instance that has been selected.
	 */
	private void setRound(final Object round) {
		this.round = (Round) round;
	}

	/**
	 * Creates the contest selection page.
	 * 
	 * @return Created wizard page.
	 */
	private WizardPage createContestPage() {
		return new ListPageBuilder(CONTEST_NAME)
				.description(CONTEST_DESCRIPTION)
				.contentProvider(new FunctionalContentProvider(this::getContests))
				.labelProvider(new FunctionalLabelProvider(NamedObject::getName))
				.selectionConsumer(this::setContest)
				.build();
	}
	
	/**
	 * Creates the round selection page.
	 * 
	 * @return Created wizard page.
	 */
	private WizardPage createRoundPage() {
		return new ListPageBuilder(ROUND_NAME)
				.description(ROUND_DESCRIPTION)
				.contentProvider(new FunctionalContentProvider(this::getRounds))
				.labelProvider(new FunctionalLabelProvider(NamedObject::getName))
				.selectionConsumer(this::setRound)
				.build();
	}

	/** {@inheritDoc} **/
	@Override
	public void addPages() {
		addPage(createContestPage());
		addPage(createRoundPage());
	}

	/** {@inheritDoc} **/
	@Override
	public boolean performFinish() {
		if (round != null) {
			Jammy.getDefault().setCurrentRound(round);
			return true;
		}
		return false;
	}

}
