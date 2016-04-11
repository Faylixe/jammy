package fr.faylixe.jammy.ui.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableWithProgress;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.OnlineSubmission;

/**
 * This command will run the selected problem
 * input.
 * 
 * @author fv
 */
public final class RunInputCommand extends AbstractProblemInputCommand {

	/**
	 * Factory method that creates a {@link IRunnableWithProgress} instance that
	 * is in charge of creating the submission and to submit it to the service.
	 * 
	 * @param input Problem input to create runnable for.
	 * @return Created instance.
	 */
	protected IRunnableWithProgress createRunnable(final ProblemInput input) {
		return monitor -> {
			final ProblemSolverFactory factory = ProblemSolverFactory.getInstance();
			try {
				final ProblemSolver solver = factory.getSolver(input.getProblem(), monitor);
				final ISubmission submission = new OnlineSubmission(solver, input);
				ISubmission.runAsJob(submission);
			}
			catch (final CoreException e) {
				EclipseUtils.showError(e);
			}
		};
	}

}
