package review.classdesign.jammy.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * 
 * @author fv
 */
public final class RefreshProblemCommand extends AbstractHandler {

	/** Command identifier. **/
	public static final String ID = "review.classdesign.jammy.command.refreshproblem";
	
	/**
	 * Default constructor.
	 */
	public RefreshProblemCommand() {
		super();
	}

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		// TODO : Reload contest info ?
		return null;
	}

	
}
