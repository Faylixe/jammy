package fr.faylixe.jammy.ui.internal;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * {@link IStructuredContentProvider} implementation that uses
 * a delegate {@link Function} as element provider.
 * 
 * @author fv
 */
public final class FunctionalContentProvider implements IStructuredContentProvider {

	/** Delegate function used to retrieve content. **/
	private final Function<Object, Object[]> delegate;

	/**
	 * Default constructor that uses a given <tt>suplier</tt> as
	 * a function.
	 * 
	 * @param supplier Supplier instance that is transformed into a valid provider function.
	 */
	public FunctionalContentProvider(final Supplier<List<?>> supplier) {
		this((object) -> supplier.get().toArray());
	}

	/**
	 * Default constructor.
	 * 
	 * @param delegate Delegate function used to retrieve content. 
	 */
	public FunctionalContentProvider(final Function<Object, Object[]> delegate) {
		this.delegate = delegate;
	}

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
		return delegate.apply(inputElement);
	}

}
