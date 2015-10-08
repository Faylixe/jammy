package review.classdesign.jammy.wizard.submit;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.Wizard;

import review.classdesign.jammy.wizard.problem.ProblemWizardPage;

/**
 * 
 * @author fv
 *
 */
public final class SubmissionWizard extends Wizard {

	/** **/
	private static final String TITLE = "";

	/** **/
	private final IFile file;

	/** **/
	private final ProblemWizardPage page;

	/**
	 * 
	 * @param file
	 */
	public SubmissionWizard(final IFile file) {
		super();
		this.file = file;
		this.page = new ProblemWizardPage();
	}
	
	/** {@inheritDoc} **/
	@Override
	public String getWindowTitle() {
		return TITLE;
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
