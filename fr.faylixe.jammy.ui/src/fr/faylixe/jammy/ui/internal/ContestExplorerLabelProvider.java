package fr.faylixe.jammy.ui.internal;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;

import fr.faylixe.googlecodejam.client.common.NamedObject;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.ui.view.ContestExplorer;

/**
 * Custom label provider for the {@link ContestExplorer}.
 * 
 * @author fv
 */
public final class ContestExplorerLabelProvider extends LabelProvider {

	/** {@inheritDoc} **/
	@Override
	public String getText(final Object element) {
		if (element instanceof NamedObject) {
			final NamedObject named = (NamedObject) element;
			return named.getName();
		}
		return super.getText(element);
	}
		
	/** {@inheritDoc} **/
	@Override
	public Image getImage(final Object element) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final ISharedImages shared = workbench.getSharedImages();
		if (element instanceof Problem) {
			return shared.getImage(SharedImages.IMG_OBJ_PROJECT);
		}
		else if (element instanceof ProblemInput) {
			return shared.getImage(ISharedImages.IMG_TOOL_PASTE);
		}
		return super.getImage(element);
	}

}
