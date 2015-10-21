package review.classdesign.jammy.model.listener;

import review.classdesign.jammy.model.submission.ISubmission;

/**
 * 
 * @author fv
 */
public interface ISubmissionListener {

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
