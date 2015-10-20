package review.classdesign.jammy.command;

import java.util.Optional;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.model.ProblemSolver;
import review.classdesign.jammy.model.webservice.ContestInfo;
import review.classdesign.jammy.model.webservice.Problem;

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
				// TODO : Handle error.
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
