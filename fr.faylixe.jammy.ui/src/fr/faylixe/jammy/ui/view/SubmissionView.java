package fr.faylixe.jammy.ui.view;

import java.util.Arrays;

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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.listener.ISubmissionListener;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.ISubmissionService;
import fr.faylixe.jammy.core.service.SubmissionException;
import fr.faylixe.jammy.ui.JammyUI;

/**
 * Custom eclipse view for submission monitoring.
 * 
 * @author fv
 */
public final class SubmissionView extends ViewPart implements IDoubleClickListener {

	/** **/
	public static final String ID = "review.classdesign.jammy.view.submission";

	/** **/
	private static final String [] LABEL = { "Retrieving input", "Running solver", "Submitting output" };

	/** **/
	private static final int INPUT = 0;

	/** **/
	private static final int RUNNING = 1;

	/** **/
	private static final int OUTPUT = 2;

	/** **/
	private static final int MINIMUM = 0;

	/** **/
	private static final int MAXIMUM = 3;

	/** **/
	private final SubmissionListener listener;

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

	/**
	 * 
	 * @author fv
	 */
	private class SubmissionListener implements ISubmissionListener {

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
			if (element.equals(currentSubmission)) {
				return JammyUI.getImage(JammyUI.IMG_SUBMISSION_SUITE);
			}
			final String label = element.toString();
			final int currentState = Arrays.binarySearch(LABEL, label);
			if (currentState < state) {
				return JammyUI.getImage(JammyUI.IMG_SUBMISSION_SUCCESS);
			}
			else if (currentState == state) {
				if (error == null) {
					return JammyUI.getImage(JammyUI.IMG_SUBMISSION_RUN);
				}
				else {
					return JammyUI.getImage(JammyUI.IMG_SUBMISSION_FAIL);
				}
			}
			return JammyUI.getImage(JammyUI.IMG_SUBMISSION_TEST);
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
			// Do nothing.
		}

		/** {@inheritDoc} **/
		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			// Do nothing.
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
				return LABEL; // NOPMD
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

	/**
	 * Default constructor.
	 */
	public SubmissionView() {
		super();
		this.listener = new SubmissionListener();
	}

	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		ISubmissionService.get().addSubmissionListener(listener);
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
		ISubmissionService.get().removeSubmissionListener(listener);
	}

	/** {@inheritDoc} **/
	@Override
	public void setFocus() {
		// Do nothing.
	}

	/**
	 * 
	 */
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

}
