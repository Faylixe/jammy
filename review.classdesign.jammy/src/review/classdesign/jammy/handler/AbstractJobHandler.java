package review.classdesign.jammy.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.core.runtime.jobs.Job;

/**
 * Abstract handler implementation that is in charge of
 * creating and running a {@link Job}.
 * 
 * @author fv
 */
public abstract class AbstractJobHandler extends AbstractHandler implements IJobFunction {

	/** {@inheritDoc} **/
	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		final Job job = Job.create("", this);
		job.schedule();
		return null;
	}

}
