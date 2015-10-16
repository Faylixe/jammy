package review.classdesign.jammy.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.TemplateLoader;
import review.classdesign.jammy.model.ContestInfo;
import review.classdesign.jammy.model.Problem;

/**
 * 
 * @author fv
 */
public final class ContestProjectBuilder extends Job {

	/** Path of the template file resources used for solver generation. **/
	private static final String TEMPLATE_PATH = "/templates/solution.template.java";

	/** Solver Java template. **/
	private static String TEMPLATE;

	/** **/
	private final ContestInfo contestInfo;

	/** **/
	private IProject project;

	/**
	 * 
	 * @param contestInfo
	 */
	private ContestProjectBuilder(final ContestInfo contestInfo) {
		super("");
		this.contestInfo = contestInfo;
	}


	/** {@inheritDoc} **/
	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		return Status.OK_STATUS;
	}

	/**
	 * 
	 * @return
	 */
	private IProject createJavaProject() {
		// TODO : Create java project.
		return null;
	}
	
	/**
	 * 
	 * @param problem
	 */
	private void createProblemSolver(final Problem problem) {
		final String template = getTemplate();
		// TODO : Format template.
		// TODO : Create file name.
		final IFile solver = project.getFile("");
		if (!solver.exists()) {
			try {
				solver.create(null, true, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 */
	private void createProblemSolvers() {
		for (final Problem problem : contestInfo.getProblems()) {
			createProblemSolver(problem);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private static String getTemplate() {
		synchronized (ContestProjectBuilder.class) {
			if (TEMPLATE == null) {
				TEMPLATE = TemplateLoader.load(TEMPLATE_PATH);
			}
		}
		return TEMPLATE;
	}

}
