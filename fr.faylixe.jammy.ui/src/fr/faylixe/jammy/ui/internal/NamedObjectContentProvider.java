package fr.faylixe.jammy.ui.internal;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import fr.faylixe.googlecodejam.client.common.NamedObject;

/**
 * <p>{@link IStructuredContentProvider} that
 * handles list of {@link NamedObject} only.</p>
 * 
 * @author fv
 */
public final class NamedObjectContentProvider implements IStructuredContentProvider {

	/** Singleton instance. **/
	private static NamedObjectContentProvider instance;

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private NamedObjectContentProvider() {
		// Do nohting.
	}
	
	/**
	 * Static method for accessing singleton instance.
	 * 
	 * @return Unique singleton instance of this class.
	 */
	public static NamedObjectContentProvider getInstance() {
		synchronized (NamedObjectContentProvider.class) {
			if (instance == null) {
				instance = new NamedObjectContentProvider();
			}			
		}
		return instance;
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
		if (inputElement instanceof List) {
			 final List<?> objects = (List<?>) inputElement;
			 return objects.toArray();
		}
		return null;
	}

}
