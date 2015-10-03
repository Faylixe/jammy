package review.classdesign.jammy.handler.wizard;

import java.util.Optional;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.IWizard;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.model.Round;
import review.classdesign.jammy.wizard.submit.SubmissionWizard;

/**
 * Default handler used for Jammy submission command.
 * 
 * @author fv
 */
public final class SubmissionHandler extends AbstractWizardHandler {

	/** {@inheritDoc} **/
	@Override
	protected IWizard createWizard() {
		final Optional<Round> round = Jammy.getDefault().getCurrentRound();
		if (!round.isPresent()) {
			// TODO : Shows error dialog.
		}
		final Optional<IFile> file = EclipseUtils.getCurrentFile();
		// TODO : What if no file is currently edited ? 
		return new SubmissionWizard(file.get());
	}

}
