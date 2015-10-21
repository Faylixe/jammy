package review.classdesign.jammy.ui.view;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
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

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.model.listener.ISubmissionListener;
import review.classdesign.jammy.model.submission.ISubmission;
import review.classdesign.jammy.service.ISubmissionService;

/**
 * Custom eclipse view for submission monitoring.
 * 
 * @author fv
 */
public final class SubmissionView extends ViewPart implements ISubmissionListener {

	/** **/
	public static final String ID = "review.classdesign.jammy.view.submission";


	/** **/
	private static final String INPUT = "Retrieving input";

	/** **/
	private static final String RUNNING = "Running solver";

	/** **/
	private static final String OUTPUT = "Submitting output";
	
	/** **/
	private static final String [] LABEL = { INPUT, RUNNING, OUTPUT };
	
	/**
	 * 
	 * @author fv
	 *
	 */
	private class StateLabelProvider extends LabelProvider implements IFontProvider {

		/** {@inheritDoc} **/
		@Override
		public String getText(final Object element) {
			return element.toString();
		}

		/** {@inheritDoc} **/
		@Override
		public Image getImage(final Object element) {
			return null;
		}

		/** {@inheritDoc} **/
		@Override
		public Font getFont(final Object element) {
			if (element.equals(getState())) {
				return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
			}
			return viewer.getControl().getFont();
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
	private String state;

	/** **/
	private String currentSubmission;

	/**
	 * 
	 * @return
	 */
	private String getState() {
		return state;
	}

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
		viewer = new TreeViewer(parent);
		viewer.getControl().setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		viewer.setContentProvider(new StateContentProvider());
		viewer.setLabelProvider(new StateLabelProvider());
		viewer.setInput(Jammy.CHILDLESS);
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
			page.activate(SubmissionView.this);
		});
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionStarted(final ISubmission submission) {
		activate();
		state = INPUT;
		currentSubmission = submission.getName();
		Display.getDefault().asyncExec(() -> {
			viewer.refresh();
			viewer.expandToLevel(TreeViewer.ALL_LEVELS);
			indicator.setSelection(indicator.getSelection() + 1);
		});
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionFinished(final ISubmission submission) {
		activate();
		state = null;
	}

	/** {@inheritDoc} **/
	@Override
	public void executionFinished(final ISubmission submission) {
		activate();
		state = OUTPUT;
		Display.getDefault().asyncExec(() -> {
			viewer.refresh();
			indicator.setSelection(indicator.getSelection() + 1);
		});
	}

	/** {@inheritDoc} **/
	@Override
	public void executionStarted(ISubmission submission) {
		activate();
		state = RUNNING;
		Display.getDefault().asyncExec(() -> {
			viewer.refresh();
		});
	}

}
