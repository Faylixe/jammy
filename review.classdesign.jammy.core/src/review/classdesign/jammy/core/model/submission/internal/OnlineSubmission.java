package review.classdesign.jammy.core.model.submission.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.core.model.ProblemSolver;
import review.classdesign.jammy.core.model.submission.SubmissionException;

/**
 * Online submission that consists in downloading problem input,
 * running solver, and uploading output.
 * 
 * @author fv
 */
public final class OnlineSubmission extends AbstractSubmission {

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 */
	public OnlineSubmission(final ProblemSolver solver) {
		super(solver);
	}

	/** {@inheritDoc} **/
	@Override
	public void submit(final IProgressMonitor monitor) throws SubmissionException {
		// TODO : Use submission service to check if success.
	}

	/** {@inheritDoc} **/
	@Override
	public void start(final IProgressMonitor monitor) throws CoreException {
		// TODO : Retrieve input using submission service.
		// TODO : Run solver with the input.
		// TODO : Post result using submission service.
	}

	/** {@inheritDoc} **/
	@Override
	public IFile getOutput() {
		return null;
	}

	/** {@inheritDoc} **/
	@Override
	public String getName() {
		return null;
	}

}
