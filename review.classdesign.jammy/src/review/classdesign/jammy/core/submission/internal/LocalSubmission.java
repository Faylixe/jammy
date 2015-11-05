package review.classdesign.jammy.core.submission.internal;

import java.io.IOException;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.core.ProblemSampleDataset;
import review.classdesign.jammy.core.ProblemSolver;
import review.classdesign.jammy.core.submission.SubmissionException;

/**
 * Submission that aims to use local dataset file.
 * 
 * @author fv
 */
public final class LocalSubmission extends AbstractSubmission {

	/** Extension used for local submission output file. **/
	private static final String OUTPUT_EXTENSION = ".sample.out";

	/** Suffix used for local submission name. **/
	private static final String SAMPLE_SUFFIX = " - sample";

	/** Name of this submission.**/
	private final String name;

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 */
	public LocalSubmission(final ProblemSolver solver) {
		super(solver);
		final StringBuilder builder = new StringBuilder();
		builder
			.append(solver.getName())
			.append(SAMPLE_SUFFIX);
		this.name = builder.toString().toLowerCase();
	}

	/** {@inheritDoc} 
	 * @throws CoreException **/
	@Override
	public void start(final IProgressMonitor monitor) throws CoreException {
		getService().fireSubmissionStarted(this);
		final ProblemSampleDataset dataset = getSolver().getSampleDataset();
		final IFile input = dataset.getInput();
		if (!input.exists()) {
			// TODO : Add error message.
			throw EclipseUtils.createException("");
		}
		run(input.getLocation().toOSString(), monitor);
	}

	/** {@inheritDoc} **/
	@Override
	public void submit(final IProgressMonitor monitor) throws SubmissionException {
		final IFile expected = getSolver().getSampleDataset().getOutput();
		try {
			final IFile actual = getOutput();
			expected.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			actual.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			if (!EclipseUtils.isFileEquals(expected, actual)) {
				// TODO : Add error message.
				throw new SubmissionException("", () -> {
					CompareUI.openCompareEditor(SubmissionCompareEditorInput.create(actual, expected), true);
				});
			}
		}
		catch (final IOException | CoreException e) {
			throw new SubmissionException(e.getMessage());
		}
	}

	/** {@inheritDoc} **/
	@Override
	public IFile getOutput() throws CoreException {
		final IProject project = getSolver().getProject();
		final IFolder folder = EclipseUtils.getFolder(project, OUTPUT_PATH);
		final StringBuilder builder = new StringBuilder();
		builder
			.append(getSolver().getName().toLowerCase())
			.append(OUTPUT_EXTENSION);
		return folder.getFile(builder.toString());
	}

	/** {@inheritDoc} **/
	@Override
	public String getName() {
		return name;
	}

}
