package review.classdesign.jammy.model.submission;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.model.ProblemSampleDataset;
import review.classdesign.jammy.model.ProblemSolver;

/**
 * 
 * @author fv
 */
public final class LocalSubmission extends AbstractSubmission {

	/**
	 * 
	 * @param solver
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
		run(input.getFullPath().toOSString(), monitor);
	}

	/** {@inheritDoc} **/
	@Override
	public boolean isSuccess() {
		final IFile expected = getSolver().getSampleDataset().getOutput();
		final IFile actual = getOutput();
		return EclipseUtils.isFileEquals(expected, actual);
	}

}
