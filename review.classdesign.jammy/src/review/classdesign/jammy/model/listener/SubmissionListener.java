package review.classdesign.jammy.model.listener;

import review.classdesign.jammy.model.submission.ISubmission;

/**
 * 
 * @author fv
 */
public interface SubmissionListener {

	/**
	 * 
	 * @param submission
	 */
	void submissionFinished(ISubmission submission);

	/**
	 * 
	 * @param submission
	 */
	void executionFinished(ISubmission submission);

}
