package fr.faylixe.jammy.core.addons;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.faylixe.googlecodejam.client.common.HTMLConstant;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.ProblemSampleDataset;

/**
 * A {@link DatasetBuilder} provides tools for extracting and
 * creating sample dataset for a given {@link Problem} instance.
 * 
 * @author fv
 */
public final class DatasetBuilder {

	/** Classname of the DIV that contains our testing dataset. **/
	private static final String IO_CLASSNAME = "problem-io-wrapper";

	/** Path of the created test input file. **/
	private static final String DATASET_INPUT_SUFFIX = ".test.in";
	
	/** Path of the created test output file. **/
	private static final String DATASET_OUTPUT_SUFFIX = ".test.out";

	/** Number of row used for dataset extraction. **/
	private static final int DATASET_ROW = 2;

	/** Monitor instance used for project creation. **/
	private final IProgressMonitor monitor;

	/** Problem instance dataset is built from. **/
	private final Problem problem;

	/** Target folder in which dataset will be written. **/
	private final IFolder folder;

	/**
	 * Default constructor. 
	 * 
	 * @param problem Problem instance dataset is built from.
	 * @param folder Target folder in which dataset will be written.
	 * @param monitor Monitor instance used for project creation.
	 */
	public DatasetBuilder(final Problem problem, final IFolder folder, final IProgressMonitor monitor) {
		this.monitor = monitor;
		this.problem = problem;
		this.folder = folder;
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
		file.create(stream, true, monitor);
	}

	/**
	 * Retrieves file associated to the given problem suffix.
	 * 
	 * @param suffix File name suffix to use for the retrieved file.
	 * @return Created file reference.
	 */
	private IFile getFile(final String suffix) {
		final String name = problem.getNormalizedName().toLowerCase();
		final StringBuilder builder = new StringBuilder();
		builder
			.append(name)
			.append(suffix);
		return folder.getFile(builder.toString());
	}

	/**
	 * Extracts and returns the dataset from the problem body.
	 * 
	 * @return HTML row element that contains our problem dataset.
	 */
	private Element extractDataset() {
		final Document document = (Document) Jsoup.parse(problem.getBody());
		final Elements problemIO = document.getElementsByClass(IO_CLASSNAME);
		if (problemIO.isEmpty()) {
			return null;
		}
		final Elements row = problemIO.first().getElementsByTag(HTMLConstant.TR);
		if (row.size() < DATASET_ROW) {
			return null;
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
		final IFile input = getFile(DATASET_INPUT_SUFFIX);
		final IFile output = getFile(DATASET_OUTPUT_SUFFIX);
		if (!input.exists() || !output.exists()) {
			final Element row = extractDataset();
			if (row == null) {
				return null;
			}
			final Elements io = row.getElementsByTag(HTMLConstant.TD);
			if (io.size() < DATASET_ROW) {
				return null;
			}
			createFile(input, io.first().text());
			createFile(output, io.get(1).text());
		}
		return new ProblemSampleDataset(input, output);
	}

}
