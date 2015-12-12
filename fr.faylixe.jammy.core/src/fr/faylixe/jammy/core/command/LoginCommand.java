package fr.faylixe.jammy.core.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.faylixe.jammy.core.Jammy;

/**
 * Simple command that trigger the {@link Jammy#login()} method.
 * 
 * @author fv
 */
public final class LoginCommand extends AbstractHandler {

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		Jammy.getInstance().login();
		return null;
	}

}
