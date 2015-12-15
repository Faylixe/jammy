package fr.faylixe.jammy.ui.wizard.submission;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.ui.wizard.AbstractListWizardPage;

/**
 * <p>WizardPage implementation for problem input selection.</p>
 * 
 * @author fv
 */
public final class SubmissionWizardPage extends AbstractListWizardPage {

	/** Name of this page. **/
	private static final String PAGE_NAME = "Dataset selection. ";

	/** Page description. **/
	private static final String PAGE_DESCRIPTION = "Please select a target dataset";

	/** Source problem to choose input from. **/
	private final Problem problem;

	/** Input selected by the user. **/
	private ProblemInput selectedInput;

	/**
	 * Default constructor.
	 * 
	 * @param problem Source problem to choose input from.
	 */
	protected SubmissionWizardPage(final Problem problem) {
		super(PAGE_NAME, PAGE_DESCRIPTION);
		this.problem = problem;
	}

	/**
	 * Getter for the selected problem input.
	 * 
	 * @return Selected input from user.
	 */
	public ProblemInput getSelectedInput() {
		return selectedInput;
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		super.createControl(parent);
		final List<NamedProblemInput> inputs = NamedProblemInput.adapt(problem.getProblemInputs());
		setInput(inputs);
	}
	
	/** {@inheritDoc} **/
	@Override
	protected void onSelectionChanged(final Object selection) {
		if (selection instanceof NamedProblemInput) {
			final NamedProblemInput adapted = (NamedProblemInput) selection;
			this.selectedInput = adapted.getProblemInput();
		}
	}

	/** {@inheritDoc} **/
	@Override
	protected void onDoubleClick() {
		closeWizard();
	}

}
