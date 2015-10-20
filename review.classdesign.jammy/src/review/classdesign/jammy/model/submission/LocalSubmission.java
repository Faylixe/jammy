package review.classdesign.jammy.model.submission;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.model.ProblemSampleDataset;
import review.classdesign.jammy.model.ProblemSolver;

/**
 * Submission that aims to use local dataset file.
 * 
 * @author fv
 */
public final class LocalSubmission extends AbstractSubmission {

	/** **/
	private static final String OUTPUT_EXTENSION = ".sample.out";

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 */
	public LocalSubmission(final ProblemSolver solver) {
		super(solver);
	}

	/** {@inheritDoc} 
	 * @throws CoreException **/
	@Override
	public void submit(final IProgressMonitor monitor) throws CoreException {
		final ProblemSampleDataset dataset = getSolver().getSampleDataset();
		final IFile input = dataset.getInput();
		if (!input.exists()) {
			// TODO : Create status.
			throw new CoreException(null);
		}
		run(input.getFullPath().toOSString(), monitor);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean isSuccess() {
		final IFile expected = getSolver().getSampleDataset().getOutput();
		final IFile actual = getOutput();
		return EclipseUtils.isFileEquals(expected, actual);
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
				EclipseUtils.showError("", e); // TODO : Customize error message.
			}
		}
		final StringBuilder builder = new StringBuilder();
		builder.append(getSolver().getName().toLowerCase());
		builder.append(OUTPUT_EXTENSION);
		return folder.getFile(builder.toString());
	}

}
