package review.classdesign.jammy.service;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IServiceLocator;

import review.classdesign.jammy.model.listener.ISubmissionListener;
import review.classdesign.jammy.model.submission.ISubmission;
import review.classdesign.jammy.model.submission.SubmissionException;

/**
 * TODO : Service javadoc.
 * 
 * @author fv
 */
public interface ISubmissionService {

	/**
	 * 
	 * @param listener
	 */
	void addSubmissionListener(ISubmissionListener listener);

	/**
	 * 
	 * @param listener
	 */
	void removeSubmissionListener(ISubmissionListener listener);

	/**
	 * 
	 * @param submission
	 */
	void fireSubmissionStarted(ISubmission submission);

	/**
	 * 
	 * @param submission
	 */
	void fireSubmissionFinished(ISubmission submission);

	/**
	 * 
	 * @param submission
	 */
	void fireExecutionFinished(ISubmission submission);

	/**
	 * 
	 * @param submission
	 */
	void fireExecutionStarted(ISubmission submission);

	/**
	 * 
	 * @param submission
	 * @param exception
	 */
	void fireErrorCaught(ISubmission submission, SubmissionException exception);

	/**
	 * 
	 * @return
	 */
	public static ISubmissionService get() {
		final IServiceLocator locator = PlatformUI.getWorkbench();
		final Object service = locator.getService(ISubmissionService.class);
		return (ISubmissionService) service;
	}

}
