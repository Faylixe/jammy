package fr.faylixe.jammy.core.service.internal;

import java.util.ArrayList;
import java.util.Collection;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.model.listener.ISubmissionListener;
import fr.faylixe.jammy.core.model.submission.ISubmission;
import fr.faylixe.jammy.core.model.submission.SubmissionException;
import fr.faylixe.jammy.core.service.ISubmissionService;

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
		listeners.forEach(listener -> listener.submissionStarted(submission));
	}

	/** {@inheritDoc} **/
	@Override
	public void fireSubmissionFinished(final ISubmission submission) {
		listeners.forEach(listener -> listener.submissionFinished(submission));
	}

	/** {@inheritDoc} **/
	@Override
	public void fireExecutionStarted(final ISubmission submission) {
		listeners.forEach(listener -> listener.executionStarted(submission));
	}

	/** {@inheritDoc} **/
	@Override
	public void fireExecutionFinished(final ISubmission submission) {
		listeners.forEach(listener -> listener.executionFinished(submission));
	}

	/** {@inheritDoc} **/
	@Override
	public void fireErrorCaught(final ISubmission submission, final SubmissionException exception) {
		listeners.forEach(listener -> listener.errorCaught(submission, exception));
	}

	/** {@inheritDoc} **/
	@Override
	public void downloadInput(final ProblemInput input) {
		// TODO : Implements input download.
	}

}
