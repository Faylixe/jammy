package fr.faylixe.jammy.addons.java;

import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.addons.ISolverRunner;
import fr.faylixe.jammy.core.addons.ISolverRunnerFactory;

/**
 * {@link ISolverRunnerFactory} implementation for Java addon.
 * 
 * @author fv
 */
public final class JavaSolverRunnerFactory implements ISolverRunnerFactory {

	/** {@inheritDoc} **/
	@Override
	public ISolverRunner create(final ProblemSolver solver, final IProgressMonitor monitor) {
		return new JavaSolverRunner(solver, monitor);
	}

}
