package review.classdesign.jammy.addons;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.core.ProblemSolver;
import review.classdesign.jammy.core.webservice.Problem;

/**
 * 
 * @author fv
 */
public abstract class AbstractProcessManager implements ILanguageManager {

	/** {@inheritDoc} **/
	@Override
	public IProject getProject(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final String name = problem.getParent().getProjectName();
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject project = workspace.getRoot().getProject(name + ".python");
		if (!project.exists()) {
			monitor.subTask(String.format(CREATE_PROJECT_TASK, ""));
			project.create(monitor);
			project.open(monitor);
		}
		return project;
	}

	/** {@inheritDoc} **/
	@Override
	public IFile getSolver(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final IProject project = getProject(problem, monitor);
		final StringBuilder builder = new StringBuilder();
		builder.append(problem.getNormalizedName());
		builder.append(getExtension());
		final IFile file = project.getFile(builder.toString());
		if (!file.exists()) {
			final String template = getTemplate(problem.getNormalizedName());
			final InputStream stream = new ByteArrayInputStream(template.getBytes());
			file.create(stream, true, monitor);
		}
		return file;
	}


	/** {@inheritDoc} **/
	@Override
	public ISolverRunner getRunner(final ProblemSolver solver, final IProgressMonitor monitor) {
		return new ProcessSolverRunner(getCommand(), solver);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected abstract String getTemplate(String name);

	/**
	 * 
	 * @return
	 */
	protected abstract String getExtension();

	/**
	 * 
	 * @return
	 */
	protected abstract String getCommand();

}
