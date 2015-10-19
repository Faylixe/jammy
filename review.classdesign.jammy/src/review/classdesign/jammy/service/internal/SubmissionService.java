package review.classdesign.jammy.service.internal;

import java.util.ArrayList;
import java.util.Collection;

import review.classdesign.jammy.model.listener.SubmissionListener;
import review.classdesign.jammy.model.submission.ISubmission;
import review.classdesign.jammy.service.ISubmissionService;

/**
 * TODO : Service javadoc.
 * 
 * @author fv
 */
public final class SubmissionService implements ISubmissionService {

	/** **/
	private final Collection<SubmissionListener> listeners;

	/**
	 * Default constructor.
	 * 
	 */
	public SubmissionService() {
		this.listeners = new ArrayList<SubmissionListener>();
	}

	/** {@inheritDoc} **/
	@Override
	public void addSubmissionListener(final SubmissionListener listener) {
		listeners.add(listener);
	}

	/** {@inheritDoc} **/
	@Override
	public void removeSubmissionListener(final SubmissionListener listener) {
		listeners.remove(listener);
	}

	/** {@inheritDoc} **/
	@Override
	public void fireExecutionFinished(final ISubmission submission) {
		for (final SubmissionListener listener : listeners) {
			listener.executionFinished(submission);
		}
	}

}
