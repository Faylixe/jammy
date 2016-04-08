package fr.faylixe.jammy.ui.wizard.contest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;

import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.ui.wizard.AbstractListWizardPage;
import fr.faylixe.googlecodejam.client.Contest;

/**
 * <p>WizardPage implementation for contest selection.</p>
 * 
 * @author fv
 */
public final class ContestWizardPage extends AbstractListWizardPage {

	/** Contest retrieval job name. **/
	private static final String RETRIEVE_CONTEST = "Extract contest";

	/** Name of this page. **/
	private static final String PAGE_NAME = "Contest selection. ";

	/** Page description. **/
	private static final String PAGE_DESCRIPTION = "Please select a Jam contest";

	/** Title of the error dialog displayed when contest retrieval failed. **/
	private static final String CONTEST_ERROR_TITLE = "Contest retrieval failed";

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
		final ProgressMonitorDialog loader = new ProgressMonitorDialog(getShell());
		try {
			loader.run(true, true, monitor -> {
				try {
					monitor.beginTask(RETRIEVE_CONTEST, 1);
					final List<Contest> contest = Jammy.getInstance().getContests();
					setInput(contest);
				}
				catch (final IOException e) {
					MessageDialog.openError(getShell(), CONTEST_ERROR_TITLE, e.getMessage());
					final IWizard wizard = getWizard();
					final IWizardContainer container = wizard.getContainer();
					if (container instanceof WizardDialog) {
						final WizardDialog dialog = (WizardDialog) container;
						dialog.close();
					}
				}
			});
		}
		catch (final InterruptedException | InvocationTargetException e) {
			// TODO : Add custom error message ?
			EclipseUtils.showError(e);
		}
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
