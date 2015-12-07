package fr.faylixe.jammy.addons.java;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.ContestInfo;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.addons.java.internal.JavaProjectBuilder;
import fr.faylixe.jammy.addons.java.internal.JavaSolverBuilder;
import fr.faylixe.jammy.addons.java.internal.JavaSolverRunner;
import fr.faylixe.jammy.core.addons.ILanguageManager;
import fr.faylixe.jammy.core.addons.ISolverRunner;
import fr.faylixe.jammy.core.model.ProblemSolver;

/**
 * {@link ILanguageManager} implementation for Java language,
 * which is the default language used for Jammy plugin.
 *
 * @author fv
 */
public final class JavaManager implements ILanguageManager {

	/** Suffix used for solver class file. **/
	private static final String SOLVER_SUFFIX = "Solver";

	/** {@inheritDoc} **/
	@Override
	public IProject getProject(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final ContestInfo info = problem.getParent();
		final String name = info.toString();// TODO : Reimplement old getProjectName();
		return JavaProjectBuilder.build(name, monitor);
	}

	/** {@inheritDoc} **/
	@Override
	public IFile getSolver(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final StringBuilder builder = new StringBuilder();
		builder
			.append(problem.getNormalizedName())
			.append(SOLVER_SUFFIX);
		final String name = builder.toString();
		final IProject project = getProject(problem, monitor);
		return new JavaSolverBuilder(project, monitor).build(name);
	}

	/** {@inheritDoc} **/
	@Override
	public ISolverRunner getRunner(final ProblemSolver solver, final IProgressMonitor monitor) {
		return new JavaSolverRunner(solver, monitor);
	}

}
