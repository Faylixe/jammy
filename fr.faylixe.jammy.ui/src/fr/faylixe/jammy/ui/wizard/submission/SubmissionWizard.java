package fr.faylixe.jammy.ui.wizard.submission;

import org.eclipse.jface.wizard.Wizard;

import fr.faylixe.googlecodejam.client.webservice.Problem;

/**
 * 
 * @author fv
 */
public final class SubmissionWizard extends Wizard {

	/** **/
	private final SubmissionWizardPage page;

	/**
	 * 
	 * @param problem
	 */
	public SubmissionWizard(final Problem problem) {
		this.page = new SubmissionWizardPage(problem);
	}
	
	/** {@inheritDoc} **/
	@Override
	public void addPages() {
		addPage(page);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean performFinish() {
		return false;
	}

}
