package review.classdesign.jammy.core.listener;

import review.classdesign.jammy.core.submission.ISubmission;
import review.classdesign.jammy.core.submission.SubmissionException;

/**
 * Listener that is used for notifying
 * submission state change.
 * 
 * @author fv
 */
public interface ISubmissionListener {

	/**
	 * Notify this listener that the given <tt>submission</tt> has thrown
	 * the given <tt>error</tt>.
	 * 
	 * @param submission Submission that has been throwing an error.
	 * @param error Error caught.
	 */
	void errorCaught(ISubmission submission, SubmissionException error);

	/**
	 * Notify this listener that the given <tt>submission</tt>
	 * has started.
	 * 
	 * @param submission Target submission that have started.
	 */
	void submissionStarted(ISubmission submission);

	/**
	 * Notify this listener that the given <tt>submission</tt>
	 * execution has started.
	 * 
	 * @param submission Target submission that have it execution started.
	 */
	void executionStarted(ISubmission submission);

	/**
	 * Notify this listener that the given <tt>submission</tt>
	 * has finished.
	 * 
	 * @param submission Target submission that have finished.
	 */
	void submissionFinished(ISubmission submission);

	/**
	 * Notify this listener that the given <tt>submission</tt>
	 * execution has finished.
	 * 
	 * @param submission Target submission that have it execution finished.
	 */
	void executionFinished(ISubmission submission);

}
