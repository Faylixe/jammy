package review.classdesign.jammy.common;

import java.util.function.Function;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * {@link ILabelProvider} implementation that uses
 * a delegate {@link Function} as label provider.
 * 
 * @author fv
 */
public final class FunctionalLabelProvider implements ILabelProvider {

	/** Delegate function used to retrieve label. **/
	private final Function<Object, String> delegate;

	/**
	 * Default constructor.
	 * 
	 * @param delegate Delegate function used to retrieve label.
	 */
	public FunctionalLabelProvider(final Function<Object, String> delegate) {
		this.delegate = delegate;
	}

	/** {@inheritDoc} **/
	@Override
	public String getText(final Object element) {
		return delegate.apply(element);
	}

	/** {@inheritDoc} **/
	@Override
	public void addListener(final ILabelProviderListener listener) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void removeListener(final ILabelProviderListener listener) {
		// Do nothing.
	}
	
	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	/** {@inheritDoc} **/
	@Override
	public Image getImage(final Object element) {
		return null;
	}

}
