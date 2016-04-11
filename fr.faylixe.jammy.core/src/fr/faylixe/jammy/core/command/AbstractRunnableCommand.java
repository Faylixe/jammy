package fr.faylixe.jammy.core.command;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * Abstract command that aims to process a selection
 * of <tt>T</tt> object using a delegate runnable instance.
 * 
 * @param <T> Target object this command handle.
 * @author fv
 */
public abstract class AbstractRunnableCommand<T> extends AbstractHandler {

	/** Target class that match the type <tt>T</tt>. **/
	private final Class<T> targetClass;

	/**
	 * Default constructor.
	 * 
	 * @param targetClass Target class that match the type <tt>T</tt>.
	 */
	protected AbstractRunnableCommand(final Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	/** {@inheritDoc} **/
	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		final T target = EclipseUtils.getSelection(selection, targetClass).orElse(getAlternative(event));
		if (target != null) {
			final ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event));
			try {
				dialog.run(true, false, createRunnable(target));
			}
			catch (final InvocationTargetException | InterruptedException e) {
				EclipseUtils.showError(e);
			}
		}
		return null;
	}

	/**
	 * Returns an alternative selection from another source if
	 * the current active selection does not match.
	 * 
	 * @param event Event to extract alternative selection from.
	 * @return Matched target.
	 */
	protected T getAlternative(final ExecutionEvent event) {
		return null;
	}

	/**
	 * Factory method that creates a {@link IRunnableWithProgress} instance that
	 * is in charge of processing for the given <tt>target</tt>.
	 * 
	 * @param target Target object to create runnable for.
	 * @return Created instance.
	 */
	protected abstract IRunnableWithProgress createRunnable(T target);

}
