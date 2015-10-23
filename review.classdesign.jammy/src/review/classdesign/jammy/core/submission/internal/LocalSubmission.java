package review.classdesign.jammy.core.submission.internal;

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

	/** **/
	private static final String OUTPUT_EXTENSION = ".sample.out";

	/** **/
	private static final String SAMPLE_SUFFIX = " - sample";

	/** **/
	private final String name;

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 */
	public LocalSubmission(final ProblemSolver solver) {
		super(solver);
		final StringBuilder builder = new StringBuilder();
		builder.append(solver.getName());
		builder.append(SAMPLE_SUFFIX);
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
			// TODO : Create status.
			throw new CoreException(null);
		}
		run(input.getLocation().toOSString(), monitor);
	}

	/** {@inheritDoc} **/
	@Override
	public void submit(final IProgressMonitor monitor) throws SubmissionException {
		final IFile expected = getSolver().getSampleDataset().getOutput();
		final IFile actual = getOutput();
		try {
			expected.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			actual.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		}
		catch (final CoreException e) {
			throw new SubmissionException(e.getMessage());
		}
		if (!EclipseUtils.isFileEquals(expected, actual)) {
			throw new SubmissionException("", () -> {
				CompareUI.openCompareEditor(SubmissionCompareEditorInput.create(actual, expected), true);
			});
		}
	}

	/** {@inheritDoc} **/
	@Override
	public IFile getOutput() {
		final IProject project = getSolver().getProject();
		final IFolder folder = project.getFolder(OUTPUT_PATH);
		if (!folder.exists()) {
			try {
				folder.create(true, true, null);
			}
			catch (final CoreException e) {
				e.printStackTrace();
				EclipseUtils.showError(e.getMessage(), e); // TODO : Customize error message.
			}
		}
		final StringBuilder builder = new StringBuilder();
		builder.append(getSolver().getName().toLowerCase());
		builder.append(OUTPUT_EXTENSION);
		return folder.getFile(builder.toString());
	}

	/** {@inheritDoc} **/
	@Override
	public String getName() {
		return name;
	}

}
