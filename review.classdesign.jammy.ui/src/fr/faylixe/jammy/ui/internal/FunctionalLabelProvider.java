package fr.faylixe.jammy.ui.internal;

import java.util.function.Function;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * {@link ILabelProvider} implementation that uses
 * a delegate {@link Function} as label provider.
 * 
 * @author fv
 */
public final class FunctionalLabelProvider extends LabelProvider {

	/** Delegate function used to retrieve label. **/
	private final Function<Object, String> textProvider;

	/** Delegate function used to retrieve image. **/
	private final Function<Object, Image> imageProvider;

	/**
	 * Constructor with no image provider.
	 * 
	 * @param textProvider Delegate function used to retrieve label.
	 */
	public FunctionalLabelProvider(final Function<Object, String> textProvider) {
		this(textProvider, object -> null);
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param textProvider Delegate function used to retrieve label.
	 * @param imageProvider Delegate function used to retrieve image.
	 */
	public FunctionalLabelProvider(final Function<Object, String> textProvider, final Function<Object, Image> imageProvider) {
		super();
		this.textProvider = textProvider;
		this.imageProvider = imageProvider;
	}

	/** {@inheritDoc} **/
	@Override
	public String getText(final Object element) {
		return textProvider.apply(element);
	}

	/** {@inheritDoc} **/
	@Override
	public Image getImage(final Object element) {
		return imageProvider.apply(element);
	}

}
