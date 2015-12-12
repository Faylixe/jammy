package fr.faylixe.jammy.core.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.faylixe.jammy.core.Jammy;

/**
 * Simple command that trigger the {@link Jammy#logout()} method.
 * 
 * @author fv
 */
public final class LogoutCommand extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		Jammy.getInstance().logout();
		return null;
	}

}
