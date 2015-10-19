package review.classdesign.jammy.ui.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import review.classdesign.jammy.model.listener.ISubmissionListener;
import review.classdesign.jammy.model.submission.ISubmission;
import review.classdesign.jammy.service.ISubmissionService;

/**
 * Custom eclipse view for submission monitoring.
 * 
 * @author fv
 */
public final class SubmissionView extends ViewPart implements ISubmissionListener {

	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		ISubmissionService.get().addSubmissionListener(this);
	}
	
	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		super.dispose();
		ISubmissionService.get().removeSubmissionListener(this);
	}

	/** {@inheritDoc} **/
	@Override
	public void setFocus() {
		// Do nothing.
	}

	/**
	 * 
	 */
	private void activate() {
		Display.getDefault().asyncExec(() -> { 
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			final IWorkbenchPage page = window.getActivePage();
			page.activate(this);
		});
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionStarted(final ISubmission submission) {
		activate();
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionFinished(final ISubmission submission) {
		activate();
	}

	/** {@inheritDoc} **/
	@Override
	public void executionFinished(final ISubmission submission) {
		activate();
	}

}
