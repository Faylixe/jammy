package fr.faylixe.jammy.core.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.LocalSubmission;

/**
 * <p>Command that run the target solver using the sample dataset.
 * A {@link LocalSubmission} instance is created and scheduled
 * through a {@link Job}</p>
 * 
 * @author fv
 */
public final class RunSampleDatasetCommand extends AbstractProgressiveSolverCommand {

	/** Task name for the file opening. **/
	private static final String RUN_SAMPLE_TASK = "Running sample dataset";

	/** {@inheritDoc} **/
	@Override
	protected void processSolver(final ProblemSolver solver, final IProgressMonitor monitor) throws CoreException {
		ISubmission.runAsJob(new LocalSubmission(solver));
	}

	/** {@inheritDoc} **/
	@Override
	protected String getTaskName() {
		return RUN_SAMPLE_TASK;
	}

}
