package review.classdesign.jammy.ui.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import review.classdesign.jammy.model.listener.SubmissionListener;
import review.classdesign.jammy.model.submission.ISubmission;

/**
 * Custom eclipse view for submission monitoring.
 * 
 * @author fv
 */
public final class SubmissionView extends ViewPart implements SubmissionListener {

	public static SubmissionView getView() {
		return null;
	}

	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		
	}
	
	/** {@inheritDoc} **/
	@Override
	public void setFocus() {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionFinished(final ISubmission submission) {
	}

	/** {@inheritDoc} **/
	@Override
	public void executionFinished(final ISubmission submission) {		
	}

}
