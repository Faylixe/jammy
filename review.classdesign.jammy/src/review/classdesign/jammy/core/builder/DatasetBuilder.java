package review.classdesign.jammy.core.builder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.HTMLConstant;
import review.classdesign.jammy.core.ProblemSampleDataset;
import review.classdesign.jammy.core.webservice.Problem;

/**
 * A {@link DatasetBuilder} provides tools for extracting and
 * creating sample dataset for a given {@link Problem} instance.
 * 
 * @author fv
 */
public final class DatasetBuilder extends ProjectContributor {

	/** Path of the input folder in which dataset will be written. **/
	private static final String INPUT_PATH = "input";

	/** Classname of the DIV that contains our testing dataset. **/
	private static final String IO_CLASSNAME = "problem-io-wrapper";

	/** Path of the created test input file. **/
	private static final String DATASET_INPUT_SUFFIX = ".test.in";
	
	/** Path of the created test output file. **/
	private static final String DATASET_OUTPUT_SUFFIX = ".test.out";
	
	/** Error status thrown when problem dataset could not be found. **/
	private static final IStatus IO_NOT_FOUND = new Status(IStatus.ERROR, Jammy.PLUGIN_ID, "Problem dataset not found");

	/** Problem instance dataset is built from. **/
	private final Problem problem;

	/** Target folder in which dataset will be written. **/
	private IFolder folder;

	/**
	 * Default constructor. 
	 * 
	 * @param problem Problem instance dataset is built from.
	 * @param project Target java project to be created.
	 * @param monitor Monitor instance used for project creation.
	 */
	public DatasetBuilder(final Problem problem, final IProject project, final IProgressMonitor monitor) {
		super(project, monitor);
		this.problem = problem;
	}

	/**
	 * Creates a dataset file for the target problem by writing given
	 * <tt>content</tt> into the given <tt>file</tt>.
	 * 
	 * @param file File to write content info.
	 * @param content Content to write.
	 * @throws CoreException If any error occurs while writing file.
	 */
	private void createFile(final IFile file, final String content) throws CoreException {
		final InputStream stream = new ByteArrayInputStream(content.getBytes());
		file.create(stream, true, getMonitor());
	}

	/**
	 * Retrieves file associated to the given problem suffix.
	 * 
	 * @param suffix File name suffix to use for the retrieved file.
	 * @return Created file reference.
	 */
	private IFile getFile(final String suffix) {
		final StringBuilder builder = new StringBuilder();
		final String name = problem.getNormalizedName().toLowerCase();
		builder.append(name);
		builder.append(suffix);
		return folder.getFile(builder.toString());
	}

	/**
	 * Extracts and returns the dataset from the problem body.
	 * 
	 * @return HTML row element that contains our problem dataset.
	 * @throws CoreException If any error occurs while extracing dataset.
	 */
	private Element extractDataset() throws CoreException {
		final Document document = (Document) Jsoup.parse(problem.getBody());
		final Elements problemIO = document.getElementsByClass(IO_CLASSNAME);
		if (problemIO.isEmpty()) {
			throw new CoreException(IO_NOT_FOUND);
		}
		final Elements row = problemIO.first().getElementsByTag(HTMLConstant.TR);
		if (row.size() < 2) {
			throw new CoreException(IO_NOT_FOUND);
		}
		return row.get(1);
		
	}

	/**
	 * Creates input and output dataset associated to the target problem instance.
	 * 
	 * @return {@link ProblemSampleDataset} instance built.
	 * @throws CoreException If any error occurs while creating dataset files.
	 */
	public ProblemSampleDataset build() throws CoreException {
		folder = createFolder(INPUT_PATH);
		final IFile input = getFile(DATASET_INPUT_SUFFIX);
		final IFile output = getFile(DATASET_OUTPUT_SUFFIX);
		if (!input.exists() || !output.exists()) {
			final Element row = extractDataset();
			final Elements io = row.getElementsByTag(HTMLConstant.TD);
			if (io.size() < 2) {
				throw new CoreException(IO_NOT_FOUND);
			}
			createFile(input, io.first().text());
			createFile(output, io.get(1).text());
		}
		return new ProblemSampleDataset(input, output);
	}

}
