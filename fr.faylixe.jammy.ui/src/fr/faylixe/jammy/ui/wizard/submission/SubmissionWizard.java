package fr.faylixe.jammy.ui.wizard.submission;

import java.util.function.Consumer;

import org.eclipse.jface.wizard.Wizard;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.service.ISubmission;

/**
 * <p>{@link SubmissionWizard} allows to select
 * a target problem input to run solver in during
 * the submission process.</p>
 *
 * @author fv
 */
public final class SubmissionWizard extends Wizard {

	/** Input selection page. **/
	private final SubmissionWizardPage page;

	/** Consumer that will creates and runs an {@link ISubmission} from the selected input. **/
	private final Consumer<ProblemInput> inputConsumer;

	/**
	 * Default constructor.
	 * 
	 * @param problem Problem to select input from.
	 * @param inputConsumer Consumer that will creates and runs an {@link ISubmission} from the selected input.
	 */
	public SubmissionWizard(final Problem problem, final Consumer<ProblemInput> inputConsumer) {
		this.page = new SubmissionWizardPage(problem);
		this.inputConsumer = inputConsumer;
	}
	
	/** {@inheritDoc} **/
	@Override
	public void addPages() {
		addPage(page);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean performFinish() {
		final ProblemInput selected = page.getSelectedInput();
		if (selected != null) {
			inputConsumer.accept(selected);
			return true;
		}
		return false;
	}

}
