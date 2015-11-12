package review.classdesign.jammy.ui.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 * @author fv
 */
public final class EditorCache implements IPartListener {

	/** **/
	private static EditorCache INSTANCE;

	/** **/
	private final Map<String, IReusableEditor> cache;

	/**
	 * 
	 */
	public EditorCache() {
		this.cache = new HashMap<String, IReusableEditor>();
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public IReusableEditor getEditor(final DatasetEditorInput input) {
		return cache.get(input.getName());
	}

	/** {@inheritDoc} **/
	@Override
	public void partOpened(final IWorkbenchPart part) {
		if (part instanceof IReusableEditor) {
			final IReusableEditor editor = (IReusableEditor) part;
			final IEditorInput input = editor.getEditorInput();
			if (input instanceof DatasetEditorInput) {
				cache.put(input.getName(), editor);
			}
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void partClosed(final IWorkbenchPart part) {
		if (part instanceof IReusableEditor) {
			final IReusableEditor editor = (IReusableEditor) part;
			for (final String name : cache.keySet()) {
				final IReusableEditor value = cache.get(name);
				if (editor.equals(value)) {
					cache.remove(name);
					break;
				}
			}
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void partActivated(final IWorkbenchPart part) {
		// Do nothing.
	}
	
	/** {@inheritDoc} **/
	@Override
	public void partBroughtToTop(final IWorkbenchPart part) {
		// Do nothing.
	}

	
	/** {@inheritDoc} **/
	@Override
	public void partDeactivated(final IWorkbenchPart part) {
		// Do nothing.
	}

	/**
	 * 
	 * @return
	 */
	public static EditorCache getInstance() {
		synchronized (EditorCache.class) {
			if (INSTANCE == null) {
				INSTANCE = new EditorCache();
			}
		}
		return INSTANCE;
	}

}
