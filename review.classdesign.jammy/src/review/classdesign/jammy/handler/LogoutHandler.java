package review.classdesign.jammy.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import review.classdesign.jammy.model.session.GoogleSessionProvider;

/**
 * Handler for login out of the current google session.
 * 
 * @author fv
 */
public final class LogoutHandler extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final GoogleSessionProvider provider = GoogleSessionProvider.get();
		provider.logout();
		return null;
	}

}
