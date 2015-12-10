package fr.faylixe.jammy.core;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.listener.ILanguageManagerListener;

/**
 * <p>{@link ProblemSolverFactory} is a singleton
 * responsible for creating and storing {@link ProblemSolver}
 * instance.</p>
 * 
 * @author fv
 */
public final class ProblemSolverFactory implements ILanguageManagerListener {

	/** Unique factory instance. **/
	private static ProblemSolverFactory instance;

	/** Cache of all problem solver instances indexed by **/
	private final Map<Problem, SoftReference<ProblemSolver>> solvers;

	/**
	 * Default constructor.
	 */
	private ProblemSolverFactory() {
		this.solvers = new ConcurrentHashMap<>();
	}

	/** {@inheritDoc} **/
	@Override
	public void languageManagerChanged() {
		EclipseUtils.closeAllFile();
	}

	/**
	 * {@link ProblemSolver} instances access method. If the required instance
	 * does not exist, or has been garbaged, then a new instance is created and put
	 * into the local cache.
	 * 
	 * @param problem Problem to get solver instance from.
	 * @param monitor Monitor instance used if solver creation is required.
	 * @return Retrieved instance.
	 * @throws CoreException If any error occurs while creating required instance.
	 */
	public ProblemSolver getSolver(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final SoftReference<ProblemSolver> reference = solvers.get(problem);
		ProblemSolver solver;
		if (reference == null || reference.get() == null) {
			solver = ProblemSolver.createSolver(problem, monitor);
			solvers.put(problem, new SoftReference<ProblemSolver>(solver));
		}
		else {
			solver = reference.get();
		}
		return solver;
	}

	/**
	 * Returns the unique factory instance,
	 * and creates it if not exists.
	 * 
	 * @return Unique factory instance.
	 */
	public static ProblemSolverFactory getInstance() {
		synchronized (ProblemSolverFactory.class) {
			if (instance == null) {
				instance = new ProblemSolverFactory();
			}
		}
		return instance;
	}

}
