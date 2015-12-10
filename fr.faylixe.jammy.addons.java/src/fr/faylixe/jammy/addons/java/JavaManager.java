package fr.faylixe.jammy.addons.java;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.addons.java.internal.JavaProjectBuilder;
import fr.faylixe.jammy.addons.java.internal.JavaSolverBuilder;
import fr.faylixe.jammy.addons.java.internal.JavaSolverRunner;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.addons.ILanguageManager;
import fr.faylixe.jammy.core.addons.ISolverRunner;

/**
 * {@link ILanguageManager} implementation for Java language,
 * which is the default language used for Jammy plugin.
 *
 * @author fv
 */
public final class JavaManager implements ILanguageManager {

	/** Suffix used for solver class file. **/
	private static final String SOLVER_SUFFIX = "Solver";

	/** **/
	private static final String PROJECT_SUFFIX = "-java";

	/** {@inheritDoc} **/
	@Override
	public String buildProjectName(final String contestName) {
		return new StringBuilder(PROJECT_PREFIX)
			.append(contestName)
			.append(PROJECT_SUFFIX)
			.toString();
	}

	/** {@inheritDoc} **/
	@Override
	public IProject getProject(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final String contestName = Jammy.getInstance().getContestName();
		final String projectName = buildProjectName(contestName);
		return JavaProjectBuilder.build(projectName, monitor);
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
