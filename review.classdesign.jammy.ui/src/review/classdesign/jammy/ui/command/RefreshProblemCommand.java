package review.classdesign.jammy.ui.command;

import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.core.webservice.contest.Problem;

/**
 * 
 * @author fv
 */
public final class RefreshProblemCommand extends AbstractHandler {

	/** Command identifier. **/
	public static final String ID = "review.classdesign.jammy.command.refreshproblem";
	
	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Optional<Problem> problem = Jammy.getDefault().getCurrentProblem();
		// TODO : Recreates ContestInfo.
		return null;
	}

	
}
