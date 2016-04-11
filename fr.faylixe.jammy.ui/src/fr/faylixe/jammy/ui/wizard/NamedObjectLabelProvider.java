package fr.faylixe.jammy.ui.wizard;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;

import fr.faylixe.googlecodejam.client.common.NamedObject;

/**
 * <p>{@link LabelProvider} implementation that
 * handles {@link NamedObject} only.</p>
 * 
 * @author fv
 */
public final class NamedObjectLabelProvider extends LabelProvider{

	/** Singleton instance. **/
	private static NamedObjectLabelProvider instance;

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private NamedObjectLabelProvider() {
		// Do nothing.
	}
	
	/**
	 * Static method for accessing singleton instance.
	 * 
	 * @return Unique singleton instance of this class.
	 */
	public static NamedObjectLabelProvider getInstance() {
		synchronized (NamedObjectLabelProvider.class) {
			if (instance == null) {
				instance = new NamedObjectLabelProvider();
			}
		}
		return instance;
	}

	/** {@inheritDoc} **/
	@Override
	public String getText(final Object element) {
		if (element instanceof NamedObject) {
			final NamedObject object = (NamedObject) element;
			return object.getName();
		}
		return element.toString();
	}

	/** {@inheritDoc} **/
	@Override
	public Image getImage(final Object element) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		// TODO : Consider using problem status (info -> discovery, error -> failed, task -> succsess) ?
		return workbench.getSharedImages().getImage(SharedImages.IMG_OBJ_PROJECT);
	}

}
