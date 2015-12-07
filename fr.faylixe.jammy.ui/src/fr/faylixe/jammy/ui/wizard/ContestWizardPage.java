package fr.faylixe.jammy.ui.wizard;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.common.NamedObject;
import fr.faylixe.jammy.core.model.Contest;
import fr.faylixe.jammy.ui.JammyUI;

/**
 * 
 * @author fv
 */
public final class ContestWizardPage extends WizardPage {

	/** **/
	private static final String JOB_NAME = "";

	/** **/
	private static final String PAGE_NAME = "";

	/** **/
	private ListViewer viewer;

	/**
	 * 
	 * @author fv
	 */
	private static class ContestLabelProvider extends LabelProvider {

		/** {@inheritDoc} **/
		@Override
		public String getText(final Object element) {
			if (element instanceof NamedObject) {
				final NamedObject object = (NamedObject) element;
				return object.getName();
			}
			return element.toString();
		}

	}
	
	private final class ContestContentProvider implements IStructuredContentProvider {
		
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
			if (inputElement instanceof List) {
				 final List<Object> contests = (List<Object>) inputElement;
				 return contests.toArray();
			}
			return null;
		}

	}

	/**
	 * 
	 */
	public ContestWizardPage() {
		super(PAGE_NAME);
	}

	/**
	 * Start a job based contest retrieval.
	 */
	private void retrieveContest() {
		final Job job = Job.create(JOB_NAME, monitor -> {
			try {
				final List<Contest> contest = Contest.get();
				viewer.setInput(contest);
			}
			catch (final IOException e) {
				return new Status(IStatus.ERROR, JammyUI.PLUGIN_ID, e.getMessage(), e);
			}
			return Status.OK_STATUS;
		});
		job.schedule();
	}
	
	/**
	 * Callback method used when use select an item on the list.
	 * 
	 * @param supplier Supplier that provides user selection.
	 */
	private void onSelectionChanged(final Supplier<ISelection> supplier) {
		final IStructuredSelection selection = (IStructuredSelection) supplier.get();
		final Object object = selection.getFirstElement();
		// TODO : Finalizes object set operation.
		setPageComplete(true);
	}
	
	/**
	 * Callback method that is triggered when
	 * user double click on a list item.
	 */
	private void onDoubleClick() {
		final IWizard wizard = getWizard();
		final IWizardPage next = wizard.getNextPage(this);
		final IWizardContainer container = wizard.getContainer();
		if (next == null && container instanceof WizardDialog) {
			wizard.performFinish();
			final WizardDialog dialog = (WizardDialog) container;
			dialog.close();
		}
		else {
			container.showPage(next);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		this.viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setLabelProvider(new ContestLabelProvider());
		viewer.setContentProvider(new ContestContentProvider());
		viewer.setInput(Jammy.CHILDLESS);
		viewer.addSelectionChangedListener(event -> onSelectionChanged(event::getSelection));
		viewer.addDoubleClickListener(event -> {
			onSelectionChanged(event::getSelection);
			onDoubleClick();
		});
		setControl(viewer.getControl());
		setPageComplete(false);
		retrieveContest();
	}

}
