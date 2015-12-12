package fr.faylixe.jammy.ui.wizard;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;



import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Display;


import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.ui.JammyUI;
import fr.faylixe.googlecodejam.client.Contest;

/**
 * <p>WizardPage implementation for contest selection.</p>
 * 
 * @author fv
 */
public final class ContestWizardPage extends AbstractListWizardPage {

	/** Contest retrieval job name. **/
	private static final String JOB_NAME = "Extract contest";

	/** Name of this page. **/
	private static final String PAGE_NAME = "Contest selection. ";

	/** Page description. **/
	private static final String PAGE_DESCRIPTION = "Please select a Jam contest";

	/** Consumer that will use selected contest. **/
	private final Consumer<Contest> contestConsumer;

	/**
	 * Default constructor.
	 * 
	 * @param contestConsumer Consumer that will use selected contest. 
	 */
	public ContestWizardPage(final Consumer<Contest> contestConsumer) {
		super(PAGE_NAME, PAGE_DESCRIPTION);
		this.contestConsumer = contestConsumer;
	}

	/**
	 * Start a job based contest retrieval.
	 */
	private void retrieveContest() {
		final Job job = Job.create(JOB_NAME, monitor -> {
			try {
				final List<Contest> contest = Jammy.getInstance().getContests();
				Display.getDefault().asyncExec(() -> {
					setInput(contest);
				});
			}
			catch (final IOException e) {
				return new Status(IStatus.ERROR, JammyUI.PLUGIN_ID, e.getMessage(), e);
			}
			return Status.OK_STATUS;
		});
		job.schedule();
	}

	/** {@inheritDoc} **/
	@Override
	protected void onDoubleClick() {
		final IWizard wizard = getWizard();
		final IWizardPage next = wizard.getNextPage(this);
		final IWizardContainer container = wizard.getContainer();
		container.showPage(next);			
	}

	/** {@inheritDoc} **/
	@Override
	protected void onSelectionChanged(final Object selection) {
		if (selection instanceof Contest) {
			contestConsumer.accept((Contest) selection);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		super.createControl(parent);
		retrieveContest();
	}

}
