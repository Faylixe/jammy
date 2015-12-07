package fr.faylixe.jammy.core.command;

import java.util.Optional;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.ContestInfo;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.model.ProblemSolver;

/**
 * 
 * @author fv
 */
public abstract class AbstractProgressiveSolverCommand extends AbstractProgressiveCommand {
	
	/** {@inheritDoc} **/
	@Override
	public void run(final IProgressMonitor monitor) {
		final Optional<Problem> currentProblem = Jammy.getDefault().getCurrentProblem();
		final Optional<ContestInfo> currentContest = Jammy.getDefault().getCurrentContest();
		if (currentContest.isPresent() && currentProblem.isPresent()) {
			final Problem problem = currentProblem.get();
			try {
				monitor.beginTask(getTaskName(), IProgressMonitor.UNKNOWN);
				final ProblemSolver solver = ProblemSolver.get(problem, monitor);
				monitor.subTask(getTaskName());
				processSolver(solver, monitor);
			}
			catch (final CoreException e) {
				EclipseUtils.showError(e);
			}
		}
	}
	
	/**
	 * 
	 * @param solver
	 */
	protected abstract void processSolver(ProblemSolver solver, IProgressMonitor monitor) throws CoreException;

	/**
	 * 
	 * @return
	 */
	protected abstract String getTaskName();

}
