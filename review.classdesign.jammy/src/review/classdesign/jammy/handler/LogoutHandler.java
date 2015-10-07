package review.classdesign.jammy.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import review.classdesign.jammy.service.IGoogleSessionService;

/**
 * Handler for login out of the current google session.
 * 
 * @author fv
 */
public final class LogoutHandler extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IGoogleSessionService service = IGoogleSessionService.get();
		service.logout();
		return null;
	}

}
