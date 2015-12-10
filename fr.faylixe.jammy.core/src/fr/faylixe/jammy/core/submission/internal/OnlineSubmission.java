package fr.faylixe.jammy.core.submission.internal;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.service.ISubmissionService;
import fr.faylixe.jammy.core.submission.SubmissionException;

/**
 * <p>Online submission that consists in downloading problem input,
 * running solver, and uploading output.</p>
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
		try {
			getService().submit(null);
		}
		catch (final IOException e) {
			throw new SubmissionException(e);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void start(final IProgressMonitor monitor) throws CoreException {
		final ISubmissionService service = getService();
		service.fireSubmissionStarted(this);
		try {
			service.downloadInput(null);
		}
		catch (final IOException e) {
			// TODO : Handle error.
		}
		run(null, monitor);
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
