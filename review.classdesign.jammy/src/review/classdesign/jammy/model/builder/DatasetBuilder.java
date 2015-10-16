package review.classdesign.jammy.model.builder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Function;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.model.webservice.Problem;

/**
 * 
 * @author fv
 */
public final class DatasetBuilder extends ProjectContributor {

	/** Path of the input folder in which dataset will be written. **/
	private static final String INPUT_PATH = "input";

	/** Path of the created test input file. **/
	private static final String DATASET_INPUT_SUFFIX = ".test.in";
	
	/** Path of the created test output file. **/
	private static final String DATASET_OUTPUT_SUFFIX = ".test.out";

	/** Problem instance dataset is built from. **/
	private final Problem problem;

	/** Target folder in which dataset will be written. **/
	private IFolder folder;

	/**
	 * Default constructor. 
	 * 
	 * @param project Target java project to be created.
	 * @param monitor Monitor instance used for project creation.
	 * @param problem Problem instance dataset is built from. 
	 */
	private DatasetBuilder(final IProject project, final IProgressMonitor monitor, final Problem problem) {
		super(project, monitor);
		this.problem = problem;
	}

	/**
	 * Creates a dataset file for the target problem using the given
	 * file name <tt>suffix</tt>, and the given function that extracts
	 * dataset content from the problem description.
	 * 
	 * @param suffix File name suffix to use for the created file.
	 * @param extractor Function that is in charge of retrieving dataset content from problem description.
	 * @throws CoreException If any error occurs while creating file.
	 */
	private void createFile(final String suffix, final Function<String, String> extractor) throws CoreException {
		final StringBuilder builder = new StringBuilder();
		builder.append(problem.getSolverName());
		builder.append(suffix);
		final IFile dataset = folder.getFile(problem.getSolverName());
		if (!dataset.exists()) {
			final String content = extractor.apply(problem.getBody());
			final InputStream stream = new ByteArrayInputStream(content.getBytes());
			dataset.create(stream, true, getMonitor());
		}
	}

	/**
	 * Creates input and output dataset associated to the target problem instance.
	 * 
	 * @throws CoreException If any error occurs while creating dataset files.
	 */
	private void build() throws CoreException {
		folder = createFolder(INPUT_PATH);
		createFile(DATASET_INPUT_SUFFIX, DatasetBuilder::getInput);
		createFile(DATASET_OUTPUT_SUFFIX, DatasetBuilder::getOutput);
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	private static String getInput(final String content) {
		return null;
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	private static String getOutput(final String content) {
		return null;
	}

	/**
	 * 
	 * @param problem
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
	public static void build(final Problem problem, final IProject project, final IProgressMonitor monitor) throws CoreException {
		new DatasetBuilder(project, monitor, problem).build();
	}

}
