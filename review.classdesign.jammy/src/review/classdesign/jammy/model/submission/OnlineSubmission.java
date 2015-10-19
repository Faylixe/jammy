package review.classdesign.jammy.model.submission;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.model.ProblemSolver;

/**
 * 
 * @author fv
 */
public final class OnlineSubmission extends AbstractSubmission {

	/**
	 * 
	 * @param solver
	 */
	public OnlineSubmission(ProblemSolver solver) {
		super(solver);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean isSuccess() {
		// TODO : Use submission service to check if success.
		return false;
	}

	/** {@inheritDoc} **/
	@Override
	public void submit(final IProgressMonitor monitor) throws CoreException {
		// TODO : Retrieve input using submission service.
		// TODO : Run solver with the input.
		// TODO : Post result using submission service.
	}

}
