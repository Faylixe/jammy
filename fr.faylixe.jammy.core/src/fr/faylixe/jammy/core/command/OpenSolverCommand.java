package fr.faylixe.jammy.core.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableWithProgress;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * Default handler used for opening a solver file.
 * 
 * @author fv
 */
public final class OpenSolverCommand extends AbstractProblemCommand {

	/** Command identifier. **/
	public static final String ID = "fr.faylixe.jammy.command.opensolver";

	/** {@inheritDoc} **/
	@Override
	protected IRunnableWithProgress createRunnable(final Problem problem) {
		return monitor -> {
			final ProblemSolverFactory factory = ProblemSolverFactory.getInstance();
			try {
				final ProblemSolver solver = factory.getSolver(problem, monitor);
				EclipseUtils.openFile(solver.getFile());
			}
			catch (final CoreException e) {
				EclipseUtils.showError(e);
			}
		};
	}

}
