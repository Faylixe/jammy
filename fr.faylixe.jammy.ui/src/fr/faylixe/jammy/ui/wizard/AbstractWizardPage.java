package fr.faylixe.jammy.ui.wizard;

import java.util.List;
import java.util.function.Supplier;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import fr.faylixe.googlecodejam.client.common.NamedObject;
import fr.faylixe.jammy.core.Jammy;

/**
 * 
 * @author fv
 */
public abstract class AbstractWizardPage extends WizardPage {

	/** Viewer instance that displays the list of the contest. **/
	private ListViewer viewer;

	/**
	 * Custom label provider used for displaying
	 * a list of named object.
	 * 
	 * @author fv
	 */
	protected static class NamedLabelProvider extends LabelProvider {

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
	
	/**
	 * Custom content provider used for displaying
	 * a list of named object.
	 * 
	 * @author fv
	 */
	protected final class NamedContentProvider implements IStructuredContentProvider {
		
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
				 final List<?> contests = (List<?>) inputElement;
				 return contests.toArray();
			}
			return null;
		}

	}

	/**
	 * 
	 * @param pageName
	 */
	protected AbstractWizardPage(final String pageName) {
		super(pageName);
	}
	
	/**
	 * 
	 * @param input
	 */
	protected final void setInput(final Object input) {
		viewer.setInput(input);
	}

	/**
	 * Callback method used when use select an item on the list.
	 * 
	 * @param supplier Supplier that provides user selection.
	 */
	private void onSelectionChanged(final Supplier<ISelection> supplier) {
		final IStructuredSelection selection = (IStructuredSelection) supplier.get();
		final Object object = selection.getFirstElement();
		onSelectionChanged(object);
		setPageComplete(true);
	}

	/**
	 * Callback method that is triggered when user
	 * has made a selection.
	 * 
	 * @param selection Object that has been selected by user.
	 */
	protected abstract void onSelectionChanged(Object selection);

	/**
	 * Callback method that is triggered when
	 * user double click on a list item.
	 */
	protected abstract void onDoubleClick();

	/** {@inheritDoc} **/
	@Override
	public void createControl(Composite parent) {
		this.viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setLabelProvider(new NamedLabelProvider());
		viewer.setContentProvider(new NamedContentProvider());
		viewer.setInput(Jammy.CHILDLESS);
		viewer.addSelectionChangedListener(event -> onSelectionChanged(event::getSelection));
		viewer.addDoubleClickListener(event -> {
			onSelectionChanged(event::getSelection);
			onDoubleClick();
		});
		setControl(viewer.getControl());
		setPageComplete(false);
	}

}
