package review.classdesign.jammy.service;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IServiceLocator;

import review.classdesign.jammy.model.listener.SubmissionListener;
import review.classdesign.jammy.model.submission.ISubmission;

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
	void addSubmissionListener(SubmissionListener listener);

	/**
	 * 
	 * @param listener
	 */
	void removeSubmissionListener(SubmissionListener listener);

	/**
	 * 
	 * @param submission
	 */
	void fireExecutionFinished(ISubmission submission);

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
