package review.classdesign.jammy.ui.wizard;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.common.NamedObject;
import review.classdesign.jammy.model.ContestInfo;
import review.classdesign.jammy.model.Problem;
import review.classdesign.jammy.model.Round;
import review.classdesign.jammy.ui.internal.FunctionalContentProvider;
import review.classdesign.jammy.ui.internal.FunctionalLabelProvider;
import review.classdesign.jammy.ui.internal.ListPageBuilder;

/**
 * Wizard implementation for selecting a problem
 * within a contextual round.
 * 
 * @author fv
 */
public final class ProblemWizard extends Wizard  {

	/** Wizard title. **/
	private static final String TITLE = "Problem selection for %s round";

	/** Page name. **/
	private static final String NAME = "Problem selection";

	/** Page description. **/
	private static final String DESCRIPTION = "Please select a problem :";

	/** Error message displayed when an error occurs during problem retrieval. **/
	private static final String PROBLEM_ERROR_MESSAGE = "An unexpected error occurs while retrieving content dashboard.";

	/** Selected problem. **/
	private Problem problem;

	/**
	 * Default constructor.
	 */
	public ProblemWizard() {
		super();
		setWindowTitle(TITLE);
	}
	
	/**
	 * Functional method that acts as a {@link Supplier} of available
	 * probem.
	 * 
	 * @return List of contest available.
	 */
	private List<Problem> getProblems() {
		final Optional<Round> round = Jammy.getDefault().getCurrentRound();
		if (round.isPresent()) {
			try {
				final ContestInfo info = ContestInfo.get(round.get());
				return info.getProblems();
			}
			catch (final IOException e) {
				EclipseUtils.showError(PROBLEM_ERROR_MESSAGE, e);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Sets the selected problem.
	 * 
	 * @param problem Problem instance that has been selected.
	 */
	private void setProblem(final Object problem) {
		this.problem = (Problem) problem;
	}

	/** {@inheritDoc} **/
	@Override
	public void addPages() {
		final WizardPage page = new ListPageBuilder(NAME)
			.description(DESCRIPTION)
			.contentProvider(new FunctionalContentProvider(this::getProblems))
			.labelProvider(new FunctionalLabelProvider(NamedObject::getName))
			.selectionConsumer(this::setProblem)
			.build();
		addPage(page);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean performFinish() {
		if (problem != null) {
			Jammy.getDefault().setCurrentProblem(problem);
			return true;
		}
		return false;
	}

}
