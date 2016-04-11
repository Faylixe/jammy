package fr.faylixe.jammy.core.addons;

import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.jammy.core.ProblemSolver;

/**
 * 
 * @author fv
 */
public interface ISolverRunnerFactory {

	/**
	 * 
	 * @param solver
	 * @param monitor
	 * @return
	 */
	ISolverRunner create(ProblemSolver solver, IProgressMonitor monitor);

}
