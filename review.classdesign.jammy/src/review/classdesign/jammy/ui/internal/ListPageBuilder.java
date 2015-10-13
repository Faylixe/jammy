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
 * 
 * @param <T>
 * @author fv
 */
public final class ListPageBuilder {

	/**
	 * 
	 * @author fv
	 */
	private class ListPage extends WizardPage {

		/**
		 * 
		 * @param name
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

	/** **/
	private final String name;

	/** **/
	private String description;

	/** **/
	private Consumer<Object> consumer;

	/** **/
	private IContentProvider contentProvider;

	/** **/
	private IBaseLabelProvider labelProvider;

	/**
	 * 
	 * @param name
	 */
	public ListPageBuilder(final String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @param description
	 * @return Reference of this builder in order to chain call.
	 */
	public ListPageBuilder description(final String description) {
		this.description = description;
		return this;
	}
	
	/**
	 * 
	 * @param consumer
	 * @return Reference of this builder in order to chain call.
	 */
	public ListPageBuilder selectionConsumer(final Consumer<Object> consumer) {
		this.consumer = consumer;
		return this;
	}
	
	/**
	 * 
	 * @param contentProvider
	 * @return Reference of this builder in order to chain call.
	 */
	public ListPageBuilder contentProvider(final IContentProvider contentProvider) {
		this.contentProvider = contentProvider;
		return this;
	}
	
	/**
	 * 
	 * @param labelProvider
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
	 * 
	 * @return
	 */
	public WizardPage build() {
		final ListPage page = new ListPage(name);
		page.setDescription(description);
		return page;
	}
}
