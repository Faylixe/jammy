package review.classdesign.jammy.wizard.problem;

import org.eclipse.jface.wizard.Wizard;

import review.classdesign.jammy.model.Round;

/**
 * 
 * @author fv
 */
public final class ProblemWizard extends Wizard {

	/** Wizard title. **/
	private static final String TITLE = "Problem selection for %s round";

	/** **/
	private final ProblemWizardPage page;

	/**
	 * 
	 * @param round
	 */
	public ProblemWizard(final Round round) {
		super();
		setWindowTitle("");
		page = new ProblemWizardPage();
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
