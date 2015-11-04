package review.classdesign.jammy.ui.internal;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
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

	/** Error message thrown when build page with <tt>null</tt> selection consumer. **/
	private static final String CONSUMER_NULL = "Page selection consumer could not be null";

	/** Error message thrown when build page with <tt>null</tt> content provider. **/
	private static final String CONTENT_PROVIDER_NULL = "Content provider could not be null";

	/** Error message thrown when build page with <tt>null</tt> label provider. **/
	private static final String LABEL_PROVIDER_NULL = "Label provider could not be null";

	/**
	 * Custom wizard page implementation that displays a list
	 * based on the builder parameters.
	 * 
	 * @author fv
	 */
	public class ListPage extends WizardPage {

		/** **/
		private ListViewer viewer;

		/**
		 * Default constructor.
		 * 
		 * @param name Name of this page.
		 */
		private ListPage(final String name) {
			super(name);
		}
		
		/**
		 * 
		 */
		public void refresh() {
			viewer.setInput(Jammy.CHILDLESS);
		}
		
		/**
		 * Callback method used when use select an item on the list.
		 * 
		 * @param supplier Supplier that provides user selection.
		 */
		private void onSelectionChanged(final Supplier<ISelection> supplier) {
			final IStructuredSelection selection = (IStructuredSelection) supplier.get();
			final Object object = selection.getFirstElement();
			consumer.accept(object);
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
			if (next != null) {
				// TODO : 	Figure out how to finalize wizard workflow.
				//			Using WizardDialog buttonPressed().
				container.showPage(next);
			}
		}

		/** {@inheritDoc} **/
		@Override
		public void createControl(final Composite parent) {
			this.viewer = createListViewer(parent);
			viewer.addSelectionChangedListener(event -> onSelectionChanged(event::getSelection));
			viewer.addDoubleClickListener(event -> {
				onSelectionChanged(event::getSelection);
				onDoubleClick();
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
	 * Factory method that creates a {@link ListViewer} instance
	 * from this builder internal attributes, which is bind to the
	 * given <tt>parent</tt> container.
	 * 
	 * @param parent Parent composite container which should own the created component.
	 * @return Created viewer.
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
	 * @return Built wizard page.
	 */
	public ListPage build() {
		Objects.requireNonNull(consumer, CONSUMER_NULL);
		Objects.requireNonNull(contentProvider, CONTENT_PROVIDER_NULL);
		Objects.requireNonNull(labelProvider, LABEL_PROVIDER_NULL);
		final ListPage page = new ListPage(name);
		page.setDescription(description);
		return page;
	}
}
