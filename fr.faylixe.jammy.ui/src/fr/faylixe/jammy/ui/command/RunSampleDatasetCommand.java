package fr.faylixe.jammy.ui.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.core.command.AbstractProblemCommand;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.LocalSubmission;
import fr.faylixe.jammy.ui.view.SubmissionView;

/**
 * <p>Command that run the target solver using the sample dataset.
 * A {@link LocalSubmission} instance is created and scheduled
 * through a {@link Job}</p>
 * 
 * @author fv
 */
public final class RunSampleDatasetCommand extends AbstractProblemCommand {

	/** {@inheritDoc} **/
	@Override
	protected IRunnableWithProgress createRunnable(final Problem problem) {
		return monitor -> {
			final ProblemSolverFactory factory = ProblemSolverFactory.getInstance();
			try {
				SubmissionView.activate();
				final ProblemSolver solver = factory.getSolver(problem, monitor);
				ISubmission.runAsJob(new LocalSubmission(solver));
			}
			catch (final CoreException e) {
				EclipseUtils.showError(e);
			}
		};
	}

}
