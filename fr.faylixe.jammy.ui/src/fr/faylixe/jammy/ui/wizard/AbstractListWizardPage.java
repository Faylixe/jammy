package fr.faylixe.jammy.ui.wizard;

import java.util.function.Supplier;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import fr.faylixe.googlecodejam.client.common.NamedObject;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.ui.internal.NamedObjectContentProvider;
import fr.faylixe.jammy.ui.internal.NamedObjectLabelProvider;

/**
 * Abstract {@link WizardPage} implementation that aims
 * to display a list of {@link NamedObject}.
 * 
 * @author fv
 */
public abstract class AbstractListWizardPage extends WizardPage {

	/** Viewer instance that displays the list of the contest. **/
	private ListViewer viewer;

	/**
	 * Default constructor.
	 * 
	 * @param pageName Name of the page.
	 * @param pageDescription Description of the page.
	 */
	protected AbstractListWizardPage(final String pageName, final String pageDescription) {
		super(pageName);
		setDescription(pageDescription);
	}
	
	/**
	 * Decoration method that calls, inner viewer
	 * {@link Viewer#setInput(Object)} method.
	 * 
	 * @param input Input to set.
	 * @see Viewer#setInput(Object)
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
	public void createControl(final Composite parent) {
		this.viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setLabelProvider(NamedObjectLabelProvider.getInstance());
		viewer.setContentProvider(NamedObjectContentProvider.getInstance());		
		viewer.addSelectionChangedListener(event -> onSelectionChanged(event::getSelection));
		viewer.addDoubleClickListener(event -> {
			onSelectionChanged(event::getSelection);
			onDoubleClick();
		});
		setControl(viewer.getControl());
		viewer.setInput(Jammy.CHILDLESS);
		setPageComplete(false);
	}

}
