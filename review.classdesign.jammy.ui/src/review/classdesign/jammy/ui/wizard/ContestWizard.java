package review.classdesign.jammy.ui.wizard;

import io.faylixe.googlecodejam.client.CodeJamSession;
import io.faylixe.googlecodejam.client.Contest;
import io.faylixe.googlecodejam.client.Round;
import io.faylixe.googlecodejam.client.common.NamedObject;
import io.faylixe.googlecodejam.client.executor.IHTTPRequestExecutor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.jface.wizard.Wizard;

import review.classdesign.jammy.core.JammyPreferences;
import review.classdesign.jammy.core.common.EclipseUtils;
import review.classdesign.jammy.core.model.listener.ISessionListener;
import review.classdesign.jammy.ui.internal.FunctionalContentProvider;
import review.classdesign.jammy.ui.internal.FunctionalLabelProvider;
import review.classdesign.jammy.ui.internal.ListPageBuilder;
import review.classdesign.jammy.ui.internal.ListPageBuilder.ListPage;

/** 
 * {@link ContestWizard} allows to select
 * a {@link Contest} and a {@link Round} that
 * will act as a current context for Jammy related
 * components.
 *
 * @author fv
 */
public final class ContestWizard extends Wizard {

	/** Error message displayed when contest could not be retrieved. **/
	private static final String RETRIEVAL_ERROR = "An error occurs while retrieving contest (is hostname preference valid ?)";

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

	/** **/
	private final IHTTPRequestExecutor executor;

	/** **/
	private final ISessionListener listener;

	/** Selected contest. **/
	private Contest contest;

	/** Selected round. **/
	private Round round;
	
	/** **/
	private ListPage roundPage;

	/**
	 * Default constructor.
	 * 
	 * @param executor
	 * @param listener
	 */
	public ContestWizard(final IHTTPRequestExecutor executor, final ISessionListener listener) {
		super();
		setWindowTitle(TITLE);
		this.executor = executor;
		this.listener = listener;
	}

	/**
	 * Functional method that acts as a {@link Supplier} of available
	 * contest.
	 * 
	 * @return List of contest available.
	 */
	private List<Contest> getContests() {
		List<Contest> contest = null;
		try {
			// TODO : Consider using a job based retrieval.
			contest = Contest.get(JammyPreferences.getHostname());
		}
		catch (final IOException | GeneralSecurityException e) {
			EclipseUtils.showError(RETRIEVAL_ERROR, e);
		}
		return contest;
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
		roundPage.refresh();
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
	 * 
	 * @param object
	 * @return
	 */
	private static String getName(final Object object) {
		if (object instanceof NamedObject) {
			return ((NamedObject) object).getName();
		}
		return null;
	}

	/**
	 * Creates the contest selection page.
	 * 
	 * @return Created wizard page.
	 */
	private ListPage createContestPage() {
		return new ListPageBuilder(CONTEST_NAME)
				.description(CONTEST_DESCRIPTION)
				.contentProvider(new FunctionalContentProvider(this::getContests))
				.labelProvider(new FunctionalLabelProvider(ContestWizard::getName))
				.selectionConsumer(this::setContest)
				.build();
	}
	
	/**
	 * Creates the round selection page.
	 * 
	 * @return Created wizard page.
	 */
	private ListPage createRoundPage() {
		return new ListPageBuilder(ROUND_NAME)
				.description(ROUND_DESCRIPTION)
				.contentProvider(new FunctionalContentProvider(this::getRounds))
				.labelProvider(new FunctionalLabelProvider(ContestWizard::getName))
				.selectionConsumer(this::setRound)
				.build();
	}

	/** {@inheritDoc} **/
	@Override
	public void addPages() {
		addPage(createContestPage());
		roundPage = createRoundPage();
		addPage(roundPage);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean performFinish() {
		if (round != null) {
			EclipseUtils.createUIJob(monitor -> {
				try {
					final CodeJamSession session = CodeJamSession.createSession(executor, round);
					listener.sessionChanged(session);
				}
				catch (final IOException e) {
					EclipseUtils.showError(e);
				}
			});
			return true;
		}
		return false;
	}

}
