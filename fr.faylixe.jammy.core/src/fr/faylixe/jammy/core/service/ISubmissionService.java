package fr.faylixe.jammy.core.service;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IServiceLocator;

import fr.faylixe.googlecodejam.client.webservice.SubmitResponse;
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
	 * @param monitor
	 * 
	 * @return
	 * @throws IOException 
	 */
	IFile downloadInput(ISubmission submission, IProgressMonitor monitor) throws IOException;

	/**
	 * 
	 * @throws IOException
	 */
	SubmitResponse submit(ISubmission submission) throws IOException;

	/**
	 * 
	 * @param input
	 * @return
	 */
	String buildFilename(ISubmission input) throws IOException;

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
