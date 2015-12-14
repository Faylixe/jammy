package fr.faylixe.jammy.core.service;

import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IServiceLocator;

import fr.faylixe.jammy.core.listener.ISubmissionListener;

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
	 * @param input
	 * @return
	 * @throws IOException 
	 */
	Path downloadInput(ISubmission submission) throws IOException;

	/**
	 * 
	 * @throws IOException
	 */
	void submit(ISubmission submission) throws IOException;

	/**
	 * 
	 * @return
	 */
	static ISubmissionService get() {
		final IServiceLocator locator = PlatformUI.getWorkbench();
		final Object service = locator.getService(ISubmissionService.class);
		return (ISubmissionService) service;
	}

}
