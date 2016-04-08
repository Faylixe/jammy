package fr.faylixe.jammy.ui.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.core.command.AbstractProblemCommand;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.ui.internal.DatasetEditorInput;

/**
 * This command opens the sample dataset editor for the 
 * selected problem.
 * 
 * @author fv
 */
public final class OpenDatasetEditorCommand extends AbstractProblemCommand {
	
	/** {@inheritDoc} **/
	@Override
	protected IRunnableWithProgress createRunnable(final Problem problem) {
		return monitor -> {
			final ProblemSolverFactory factory = ProblemSolverFactory.getInstance();
			try {
				final ProblemSolver solver = factory.getSolver(problem, monitor);
				Display.getDefault().asyncExec(() -> {
					DatasetEditorInput.openFrom(solver);
				});
			}
			catch (final CoreException e) {
				EclipseUtils.showError(e);
			}
		};
	}

}
