package review.classdesign.jammy.handler;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import review.classdesign.jammy.service.IGoogleSessionService;
import review.classdesign.jammy.common.EclipseUtils;

/**
 * Handler for login out of the current google session.
 * 
 * @author fv
 */
public final class LoginHandler extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IGoogleSessionService service = IGoogleSessionService.get();
		try {
			service.login();
		}
		catch (final IOException | GeneralSecurityException e) {
			EclipseUtils.showError(e);
		}
		return null;
	}

}
