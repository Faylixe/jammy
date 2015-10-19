package review.classdesign.jammy.handler;

import java.util.Optional;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.model.ProblemSolver;
import review.classdesign.jammy.model.submission.ISubmission;
import review.classdesign.jammy.model.submission.LocalSubmission;
import review.classdesign.jammy.model.webservice.ContestInfo;
import review.classdesign.jammy.model.webservice.Problem;

/**
 * 
 * @author fv
 */
public final class RunSampleDatasetCommand extends AbstractJobHandler {

	/** {@inheritDoc} **/
	@Override
	public IStatus run(final IProgressMonitor monitor) {
		final Optional<Problem> currentProblem = Jammy.getDefault().getCurrentProblem();
		final Optional<ContestInfo> currentContest = Jammy.getDefault().getCurrentContest();
		if (currentContest.isPresent() && currentProblem.isPresent()) {
			final Problem problem = currentProblem.get();
			try {
				final ProblemSolver solver = ProblemSolver.get(problem, monitor);
				final ISubmission submission = new LocalSubmission(solver);
			}
			catch (final CoreException e) {
				return e.getStatus();
			}
		}
		return Status.OK_STATUS;
	}

}
