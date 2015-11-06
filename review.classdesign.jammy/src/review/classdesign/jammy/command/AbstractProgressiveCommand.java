package review.classdesign.jammy.command;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

import review.classdesign.jammy.common.EclipseUtils;

/**
 * Abstract handler implementation that is in charge of
 * creating and running a {@link Job}.
 * 
 * @author fv
 */
public abstract class AbstractProgressiveCommand extends AbstractHandler implements IRunnableWithProgress {

	/** {@inheritDoc} **/
	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
		try {
			dialog.run(true, false, this);
		}
		catch (final InvocationTargetException | InterruptedException e) {
			EclipseUtils.showError(e);
		}
		return null;
	}

}
