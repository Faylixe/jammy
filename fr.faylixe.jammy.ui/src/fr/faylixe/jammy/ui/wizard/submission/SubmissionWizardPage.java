package fr.faylixe.jammy.ui.wizard.submission;

import org.eclipse.swt.widgets.Composite;

import fr.faylixe.googlecodejam.client.common.NamedObject;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.ui.wizard.AbstractListWizardPage;

/**
 * 
 * @author fv
 */
public final class SubmissionWizardPage extends AbstractListWizardPage {

	/** Name of this page. **/
	private static final String PAGE_NAME = "Dataset selection. ";

	/** Page description. **/
	private static final String PAGE_DESCRIPTION = "Please select a target dataset";

	/** **/
	private final Problem problem;

	/**
	 * Default constructor.
	 * 
	 */
	protected SubmissionWizardPage(final Problem problem) {
		super(PAGE_NAME, PAGE_DESCRIPTION);
		this.problem = problem;
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		super.createControl(parent);
		// TODO : Adapt ProblemInput into custom NamedObject.
		setInput(problem.getProblemInputs());
	}
	
	/** {@inheritDoc} **/
	@Override
	protected void onSelectionChanged(final Object selection) {
		if (selection instanceof NamedObject) {
			
		}
	}

	/** {@inheritDoc} **/
	@Override
	protected void onDoubleClick() {
		
	}

}
