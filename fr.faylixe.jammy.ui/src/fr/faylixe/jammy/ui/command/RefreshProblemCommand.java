package fr.faylixe.jammy.ui.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import fr.faylixe.jammy.core.Jammy;

/**
 * 
 * @author fv
 */
public final class RefreshProblemCommand extends AbstractHandler {

	/** Command identifier. **/
	public static final String ID = "fr.faylixe.jammy.command.refreshproblem";

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		Jammy.getInstance().refreshSession();
		return null;
	}

	
}
