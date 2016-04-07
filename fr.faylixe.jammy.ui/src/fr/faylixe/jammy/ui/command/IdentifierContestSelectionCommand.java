package fr.faylixe.jammy.ui.command;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.faylixe.googlecodejam.client.Round;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * <p>{@link IdentifierContestSelectionCommand} allows
 * to select a {@link Round} that
 * will act as a current context for Jammy related
 * components.</p>
 *
 * @author fv
 */
public final class IdentifierContestSelectionCommand extends AbstractHandler {

	/** Command identifier. **/
	public static final String ID = "fr.faylixe.jammy.command.identifiercontestselection";

	/** Dialog title. **/
	private static final String TITLE = "Select contextual round";

	/** Dialog message. **/
	private static final String MESSAGE = "Please indicate the contest id you want to compete :";

	/** {@inheritDoc} **/
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveShell(event);
		final InputDialog dialog = new InputDialog(shell, TITLE, MESSAGE, "", EclipseUtils::isNumberValid);
		final int result = dialog.open();
		if (result == Window.OK) {
			final String identifier = dialog.getValue();
			EclipseUtils.createUIJob(monitor -> {
				try {
					final Jammy jammy = Jammy.getInstance();
					final Round round = Round.fromIdentifier(identifier, jammy.getCookie());
					jammy.createSession(round);
				}
				catch (final IOException | GeneralSecurityException e) {
					EclipseUtils.showError(e);
				}
			});
		}
		return null;
	}

}
