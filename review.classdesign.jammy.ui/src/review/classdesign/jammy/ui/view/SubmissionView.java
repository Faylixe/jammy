package review.classdesign.jammy.ui.view;

import java.util.Arrays;

import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;
import org.eclipse.ui.part.ViewPart;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.model.listener.ISubmissionListener;
import review.classdesign.jammy.model.submission.ISubmission;
import review.classdesign.jammy.model.submission.SubmissionException;
import review.classdesign.jammy.service.ISubmissionService;

/**
 * Custom eclipse view for submission monitoring.
 * 
 * @author fv
 */
public final class SubmissionView extends ViewPart implements ISubmissionListener, IDoubleClickListener {

	/** **/
	public static final String ID = "review.classdesign.jammy.view.submission";

	/** **/
	private static final int INPUT = 0;

	/** **/
	private static final int RUNNING = 1;

	/** **/
	private static final int OUTPUT = 2;
	
	/** **/
	private static final String [] LABEL = { "Retrieving input", "Running solver", "Submitting output" };
	
	/**
	 * 
	 * @author fv
	 *
	 */
	private class StateLabelProvider extends LabelProvider {

		/** {@inheritDoc} **/
		@Override
		public String getText(final Object element) {
			return element.toString();
		}

		/** {@inheritDoc} **/
		@Override
		public Image getImage(final Object element) {
			final ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
			if (element.equals(currentSubmission)) {
				return DebugUITools.getImage(IDebugUIConstants.IMG_ACT_SYNCED);
			}
			final String label = element.toString();
			final int currentState = Arrays.binarySearch(LABEL, label);
			if (currentState < state) {
				return images.getImage(SharedImages.IMG_OBJS_TASK_TSK);
			}
			else if (currentState == state) {
				if (error != null) {
					return images.getImage(ISharedImages.IMG_ETOOL_DELETE);
				}
				else {
					return DebugUITools.getImage(IDebugUIConstants.IMG_OBJS_LAUNCH_RUN);
				}
			}
			return images.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
		
	}
	
	/**
	 * 
	 * @author fv
	 */
	private class StateContentProvider implements ITreeContentProvider {

		/** {@inheritDoc} **/
		@Override
		public void dispose() {
		}

		/** {@inheritDoc} **/
		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		}

		/** {@inheritDoc} **/
		@Override
		public Object[] getElements(final Object inputElement) {
			if (currentSubmission != null) {
				return new Object [] {currentSubmission};
			}
			return Jammy.CHILDLESS;
		}

		/** {@inheritDoc} **/
		@Override
		public Object[] getChildren(final Object parentElement) {
			if (parentElement.equals(currentSubmission)) {
				return LABEL;
			}
			return Jammy.CHILDLESS;
		}

		/** {@inheritDoc} **/
		@Override
		public Object getParent(final Object element) {
			if (!element.equals(currentSubmission)) {
				return currentSubmission;
			}
			return null;
		}

		/** {@inheritDoc} **/
		@Override
		public boolean hasChildren(final Object element) {
			return element.equals(currentSubmission);
		}
		
	}
	
	/** **/
	private static final int MINIMUM = 0;

	/** **/
	private static final int MAXIMUM = 3;

	/** **/
	private ProgressBar indicator;

	/** **/
	private TreeViewer viewer;

	/** **/
	private int state;

	/** **/
	private String currentSubmission;

	/** **/
	private SubmissionException error;

	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		ISubmissionService.get().addSubmissionListener(this);
		final GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		createProgressBar(parent);
		createView(parent);
	}

	/**
	 * 
	 * @param parent
	 */
	private void createProgressBar(final Composite parent) {
		indicator = new ProgressBar(parent, SWT.NONE);
		indicator.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		indicator.setMinimum(MINIMUM);
		indicator.setMaximum(MAXIMUM);
		indicator.setSelection(0);
	}

	/**
	 * 
	 * @param parent
	 */
	private void createView(final Composite parent) {
		viewer = new TreeViewer(parent, SWT.NONE);
		viewer.setUseHashlookup(true);
		viewer.getControl().setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		viewer.setContentProvider(new StateContentProvider());
		viewer.setLabelProvider(new StateLabelProvider());
		viewer.setInput(Jammy.CHILDLESS);
		viewer.addDoubleClickListener(this);
	}

	/** {@inheritDoc} **/
	@Override
	public void doubleClick(final DoubleClickEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		final String label = selection.getFirstElement().toString();
		final int index = Arrays.binarySearch(LABEL, label);
		if (index == state && error != null) {
			error.run();
		}
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

	private void update() {
		Display.getDefault().asyncExec(() -> {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			final IWorkbenchPage page = window.getActivePage();
			page.activate(SubmissionView.this);
			viewer.refresh();
			indicator.setSelection(indicator.getSelection() + 1);
		});
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionStarted(final ISubmission submission) {
		state = INPUT;
		currentSubmission = submission.getName();
		update();
		Display.getDefault().asyncExec(() -> {
			viewer.expandToLevel(TreeViewer.ALL_LEVELS);
		});
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionFinished(final ISubmission submission) {
		state = 4;
		update();
	}

	/** {@inheritDoc} **/
	@Override
	public void executionStarted(final ISubmission submission) {
		state = RUNNING;
		update();
	}

	/** {@inheritDoc} **/
	@Override
	public void executionFinished(final ISubmission submission) {
		state = OUTPUT;
		update();
	}

	/** {@inheritDoc} **/
	@Override
	public void errorCaught(final ISubmission submission, final SubmissionException exception) {
		error = exception;
		update();
	}

}
