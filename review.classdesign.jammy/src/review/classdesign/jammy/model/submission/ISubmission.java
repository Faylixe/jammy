package review.classdesign.jammy.model.submission;

import review.classdesign.jammy.model.listener.SubmissionListener;

/**
 * 
 * @author fv
 */
public interface ISubmission {

	/**
	 * 
	 * @return
	 */
	boolean isSuccess();
	
	/**
	 * 
	 * @param listener
	 */
	void addListener(SubmissionListener listener);

	/**
	 * 
	 * @param listener
	 */
	void removeListener(SubmissionListener listener);

}
