package fr.faylixe.jammy.ui.wizard;

import org.eclipse.jface.wizard.Wizard;

import fr.faylixe.googlecodejam.client.Contest;
import fr.faylixe.googlecodejam.client.Round;
import fr.faylixe.googlecodejam.client.executor.HttpRequestExecutor;
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

	/** Request executor that is supposed to be authenticated. **/
	private final HttpRequestExecutor executor;

	/**
	 * Default constructor.
	 * 
	 * @param executor Executor instance used for retrieving contest.
	 */
	public ContestWizard(final HttpRequestExecutor executor) {
		super();
		setWindowTitle(TITLE);
		this.executor = executor;
		this.roundPage = new RoundWizardPage();
		this.contestPage = new ContestWizardPage(executor, roundPage);
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
		if (executor != null && round != null) {
			EclipseUtils.createUIJob(monitor -> {
				// TODO : Disable contest explorer view.
				Jammy.getInstance().createSession(executor, round);
				// TODO : Reenable contest
			});
			return true;
		}
		return false;
	}

}
