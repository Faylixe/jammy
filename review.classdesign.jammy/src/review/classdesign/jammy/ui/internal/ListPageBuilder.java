package review.classdesign.jammy.ui.internal;

import java.util.function.Consumer;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import review.classdesign.jammy.Jammy;

/**
 * A {@link ListPageBuilder} allows to build a {@link WizardPage}
 * that aims to display a list of object.
 * 
 * @author fv
 */
public final class ListPageBuilder {

	/**
	 * Custom wizard page implementation that displays a list
	 * based on the builder parameters.
	 * 
	 * @author fv
	 */
	private class ListPage extends WizardPage {

		/**
		 * Default constructor.
		 * 
		 * @param name Name of this page.
		 */
		private ListPage(final String name) {
			super(name);
		}

		/** {@inheritDoc} **/
		@Override
		public void createControl(final Composite parent) {
			final ListViewer viewer = createListViewer(parent);
			viewer.addSelectionChangedListener(event -> {
				final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				final Object object = selection.getFirstElement();
				consumer.accept(object);
				setPageComplete(true);
			});
			setControl(viewer.getControl());
			setPageComplete(false);
		}
		
	}

	/** Name of the created page. **/
	private final String name;

	/** Description of the created page. **/
	private String description;

	/** Selection consumer that will handle user selection. **/
	private Consumer<Object> consumer;

	/** Content provider instance used by the created list. **/
	private IContentProvider contentProvider;

	/** Label provider instance used by the created list. **/
	private IBaseLabelProvider labelProvider;

	/**
	 * Default constructor.
	 * 
	 * @param name Name of the created page.
	 */
	public ListPageBuilder(final String name) {
		this.name = name;
	}
	
	/**
	 * Sets the created page description.
	 * 
	 * @param description Description of the target page.
	 * @return Reference of this builder in order to chain call.
	 */
	public ListPageBuilder description(final String description) {
		this.description = description;
		return this;
	}
	
	/**
	 * Sets the consumer that will be used for handling user selection through the list.
	 * 
	 * @param consumer Selection consumer that will handle user selection.
	 * @return Reference of this builder in order to chain call.
	 */
	public ListPageBuilder selectionConsumer(final Consumer<Object> consumer) {
		this.consumer = consumer;
		return this;
	}
	
	/**
	 * Sets the content provider used by the displayed list.
	 * 
	 * @param contentProvider Content provider instance used by the created list.
	 * @return Reference of this builder in order to chain call.
	 */
	public ListPageBuilder contentProvider(final IContentProvider contentProvider) {
		this.contentProvider = contentProvider;
		return this;
	}
	
	/**
	 * Sets the label provider used by the displayed list.
	 * 
	 * @param labelProvider Label provider instance used by the created list.
	 * @return Reference of this builder in order to chain call.
	 */
	public ListPageBuilder labelProvider(final IBaseLabelProvider labelProvider) {
		this.labelProvider = labelProvider;
		return this;
	}
	
	/**
	 * 
	 * @param parent
	 * @return
	 */
	private ListViewer createListViewer(final Composite parent) {
		final ListViewer viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.setInput(Jammy.CHILDLESS);
		return viewer;
	}

	/**
	 * Builds and returns the wizard page instance that will display the list.
	 * 
	 * TODO : Check for null parameters.
	 * @return
	 */
	public WizardPage build() {
		final ListPage page = new ListPage(name);
		page.setDescription(description);
		return page;
	}
}
