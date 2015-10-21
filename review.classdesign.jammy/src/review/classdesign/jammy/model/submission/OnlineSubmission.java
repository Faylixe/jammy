package review.classdesign.jammy.model.submission;

import org.eclipse.core.resources.IFile;
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
	public void validate() throws SubmissionException {
		// TODO : Use submission service to check if success.
	}

	/** {@inheritDoc} **/
	@Override
	public void submit(final IProgressMonitor monitor) throws CoreException {
		// TODO : Retrieve input using submission service.
		// TODO : Run solver with the input.
		// TODO : Post result using submission service.
	}

	/** {@inheritDoc} **/
	@Override
	public IFile getOutput() {
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
