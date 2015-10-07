package review.classdesign.jammy.ui.common;

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
	 * 
	 * @param supplier
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
