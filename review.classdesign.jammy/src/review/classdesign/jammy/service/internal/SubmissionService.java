package review.classdesign.jammy.service.internal;

import java.util.ArrayList;
import java.util.Collection;

import review.classdesign.jammy.model.listener.ISubmissionListener;
import review.classdesign.jammy.model.submission.ISubmission;
import review.classdesign.jammy.service.ISubmissionService;

/**
 * TODO : Service javadoc.
 * 
 * @author fv
 */
public final class SubmissionService implements ISubmissionService {

	/** **/
	private final Collection<ISubmissionListener> listeners;

	/**
	 * Default constructor.
	 * 
	 */
	public SubmissionService() {
		this.listeners = new ArrayList<ISubmissionListener>();
	}

	/** {@inheritDoc} **/
	@Override
	public void addSubmissionListener(final ISubmissionListener listener) {
		listeners.add(listener);
	}

	/** {@inheritDoc} **/
	@Override
	public void removeSubmissionListener(final ISubmissionListener listener) {
		listeners.remove(listener);
	}

	/** {@inheritDoc} **/
	@Override
	public void fireSubmissionStarted(final ISubmission submission) {
		for (final ISubmissionListener listener : listeners) {
			listener.submissionStarted(submission);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void fireExecutionFinished(final ISubmission submission) {
		for (final ISubmissionListener listener : listeners) {
			listener.executionFinished(submission);
		}
	}

}
