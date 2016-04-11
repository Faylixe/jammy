package fr.faylixe.jammy.core.addons;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.ProblemSolver;

/**
 * 
 * @author fv
 *
 */
public final class LanguageManager {

	/** Identifier of the associated OSGi extension. **/
	public static final String EXTENSION_ID = "fr.faylixe.jammy.addons";

	/** Extension attribute name for the manager target language. **/
	public static final String LANGUAGE = "language";

	/** **/
	private static final String SOLVER_PATH = "path";

	/** **/
	private static final String SOLVER_EXTENSION = "extension";
	
	/** **/
	private static final String TEMPLATE_FACTORY = "templateFactory";

	/** Extension attribute name for the manager implementation class. **/
	private static final String PROJECT_CONFIGURATOR = "projectConfigurator";

	/** Extension attribute name for the manager implementation class. **/
	private static final String RUNNER_FACTORY = "runnerFactory";

	/** Task name for the project creation. **/
	private static final String CREATE_PROJECT_TASK = "Creates %s project for current round";

	/** **/
	private static final String PROJECT_PREFIX = "codejam-";

	/** **/
	private final String language;

	/** **/
	private final String solverPath;

	/** **/
	private final String solverExtension;

	/** **/
	private final ISolverTemplateFactory templateFactory;

	/** **/
	private final IProjectConfigurator projectConfigurator;

	/** **/
	private final ISolverRunnerFactory runnerFactory;

	/**
	 * 
	 * @param language
	 * @param solverPath
	 * @param solverExtension
	 * @param templateFactory
	 * @param projectConfigurator
	 * @param runnerFactory
	 */
	public LanguageManager(
			final String language,
			final String solverPath,
			final String solverExtension,
			final ISolverTemplateFactory templateFactory,
			final IProjectConfigurator projectConfigurator,
			final ISolverRunnerFactory runnerFactory) {
		this.language = language;
		this.solverPath = solverPath;
		this.solverExtension = solverExtension;
		this.templateFactory = templateFactory;
		this.projectConfigurator = projectConfigurator;
		this.runnerFactory = runnerFactory;
	}
	/**
	 * Builds and returns a project name from
	 * the given <tt>contestName</tt>.
	 * 
	 * @param contestName Contest name to use for building project name.
	 * @return Built project name.
	 */
	private String buildProjectName(final String contestName) {
		return new StringBuilder(PROJECT_PREFIX)
			.append(contestName)
			.append('-')
			.append(language.toLowerCase())
			.toString();
	}

	/**
	 * Retrieves the project associated to the given <tt>problem</tt> if exist.
	 * Creates it using the given <tt>monitor</tt> otherwise.
	 * 
	 * @param problem Problem to retrieve project from.
	 * @param monitor Monitor instance to use for project creation.
	 * @return Retrieved project instance.
	 * @throws CoreException If any error occurs while creating the project.
	 */
	public IProject getProject(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final String contestName = Jammy.getInstance().getContestName();
		final String projectName = buildProjectName(contestName);
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject project = workspace.getRoot().getProject(projectName);
		if (!project.exists()) {
			if (!projectConfigurator.shouldCreate()) {
				// TODO : Handler error.
				return null;
			}
			monitor.subTask(CREATE_PROJECT_TASK);
			project.create(monitor);
			project.open(monitor);
			projectConfigurator.configure(project, monitor);
		}
		return project;
	}

	/**
	 * Retrieves the solver file associated to the given <tt>problem</tt> if exist.
	 * Creates it using the given <tt>monitor</tt> otherwise.
	 * 
	 * @param problem Problem to retrieve solver file from.
	 * @param monitor Monitor instance to use for solver file creation.
	 * @return Retrieved solver file instance.
	 * @throws CoreException  If any error occurs while creating the file.
	 */
	public IFile getSolver(Problem problem, IProgressMonitor monitor) throws CoreException {
		final IProject project = getProject(problem, monitor);
		final String name = problem.getNormalizedName();
		final StringBuilder builder = new StringBuilder();
		builder
			.append(solverPath)
			.append('/')
			.append(name)
			.append(solverExtension);
		final IFile file = project.getFile(builder.toString());
		if (!file.exists()) {
			final String template = templateFactory.getTemplate(name);
			final InputStream stream = new ByteArrayInputStream(template.getBytes());
			file.create(stream, true, monitor);
		}
		return file;
	}

	/**
	 * Retrieves a valid {@link ISolverRunner} instance that could
	 * manage the given <tt>solver</tt> execution.
	 * 
	 * @param solver Solver instance to get valid runner from.
	 * @param monitor Monitor instance used for creating runner.
	 * @return Created runner instance for executing the given <tt>solver</tt>
	 */
	public ISolverRunner getRunner(ProblemSolver solver, IProgressMonitor monitor) {
		return runnerFactory.create(solver, monitor);
	}

	/**
	 * Static factory method that creates a {@link LanguageManager}
	 * instance from the given configuration <tt>element</tt>.
	 * 
	 * @param element Configuration element to create {@link LanguageManager} from.
	 * @return Created instance.
	 * @throws CoreException If any error occurs while parsing the element.
	 */
	public static LanguageManager create(final IConfigurationElement element) throws CoreException {
		final String language = element.getAttribute(LANGUAGE);
		final String solverPath = element.getAttribute(SOLVER_PATH);
		final String solverExtension = element.getAttribute(SOLVER_EXTENSION);
		final ISolverTemplateFactory templateFactory = (ISolverTemplateFactory) element.createExecutableExtension(TEMPLATE_FACTORY);
		final IProjectConfigurator projectConfigurator = (IProjectConfigurator) element.createExecutableExtension(PROJECT_CONFIGURATOR);
		final ISolverRunnerFactory runnerFactory = (ISolverRunnerFactory) element.createExecutableExtension(RUNNER_FACTORY);
		return new LanguageManager(language, solverPath, solverExtension, templateFactory, projectConfigurator, runnerFactory);
	}

}
