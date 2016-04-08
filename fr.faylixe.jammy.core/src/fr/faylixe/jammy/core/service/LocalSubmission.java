package fr.faylixe.jammy.core.service;

import java.io.IOException;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.ProblemSampleDataset;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.internal.submission.AbstractSubmission;
import fr.faylixe.jammy.core.internal.submission.SubmissionCompareEditorInput;

/**
 * <p>Submission that aims to use local dataset file.</p>
 * 
 * @author fv
 */
public final class LocalSubmission extends AbstractSubmission {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Extension used for local submission output file. **/
	private static final String OUTPUT_EXTENSION = ".sample.out";

	/** Suffix used for local submission name. **/
	private static final String SAMPLE_SUFFIX = " - sample";

	/** Error message thrown when solver output does not match expected one. **/
	private static final String OUTPUT_MISMATCH = "Solver output does not match the expected one.";

	/** Exception thrown when local input file is not found. **/
	private static final SubmissionException FILE_NOT_EXIST = new SubmissionException("Sample input file not found.");

	/**
	 * Static factory that build submission name based
	 * on the given problem <tt>solver</tt>.
	 * 
	 * @param solver {@link ProblemSolver} to build name from.
	 * @return Built name.
	 */
	private static String buildName(final ProblemSolver solver) {
		final StringBuilder builder = new StringBuilder();
		builder
			.append(solver.getName())
			.append(SAMPLE_SUFFIX);
		return builder.toString().toLowerCase();
	}

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 */
	public LocalSubmission(final ProblemSolver solver) {
		super(solver , buildName(solver));
	}

	/** {@inheritDoc} **/
	@Override
	public void start(final IProgressMonitor monitor) {
		final ISubmissionService service = getService();
		service.fireSubmissionStarted(this);
		final ProblemSampleDataset dataset = getSolver().getSampleDataset();
		if (dataset == null) {
			service.fireErrorCaught(this, FILE_NOT_EXIST);
			return;
		}
		final IFile input = dataset.getInput();
		if (input.exists()) {
			try {
				run(input.getLocation().toOSString(), monitor);
			}
			catch (final CoreException e) {
				service.fireErrorCaught(this, new SubmissionException(e));
			}
		}
		else {
			service.fireErrorCaught(this, FILE_NOT_EXIST);			
		}
	}

	/** {@inheritDoc} **/
	@Override
	public boolean submit(final IProgressMonitor monitor) {
		final ISubmissionService service = getService();
		final ProblemSampleDataset dataset = getSolver().getSampleDataset();
		if (dataset == null) {
			service.fireErrorCaught(this, FILE_NOT_EXIST);
			return false;
		}
		final IFile expected = dataset.getOutput();
		try {
			final IFile actual = getOutputFile();
			expected.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			actual.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			if (!EclipseUtils.isFileEquals(expected, actual)) {
				service.fireErrorCaught(this, new SubmissionException(OUTPUT_MISMATCH, () -> {
					CompareUI.openCompareDialog(SubmissionCompareEditorInput.create(actual, expected));
				}));
				return false;
			}
			return true;
		}
		catch (final IOException | CoreException e) {
			service.fireErrorCaught(this, new SubmissionException(e));
			return false;
		}
	}

	/** {@inheritDoc} **/
	@Override
	public ProblemInput getProblemInput() {
		// Returns null as it does not use any problem input instance.
		return null;
	}

	/** {@inheritDoc} **/
	@Override
	public IFile getOutputFile() throws CoreException {
		return getOutputFile(OUTPUT_EXTENSION);
	}

}
