package fr.faylixe.jammy.core.service;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.googlecodejam.client.webservice.SubmitResponse;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.service.SubmissionException.Type;

/**
 * <p>Online submission that consists in downloading problem input,
 * running solver, and uploading output.</p>
 * 
 * @author fv
 */
public final class OnlineSubmission extends AbstractSubmission {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;
	
	/** Separator between problem normalized name and input name. **/
	private static final String NAME_SEPARATOR = " - ";

	/** **/
	private static final String EXTENSION = ".out";

	/** Target problem input this submission is working on. **/
	private final ProblemInput input;

	/** Target output file extension to use. **/
	private final String extension;

	/**
	 * Static factory that build submission name based
	 * on the given <tt>input</tt> problem.
	 * 
	 * @param input Problem input to build name from.
	 * @return Built name.
	 */
	public static String buildName(final ProblemInput input) {
		final StringBuilder builder = new StringBuilder();
		builder
			.append(input.getProblem().getName())
			.append(NAME_SEPARATOR)
			.append(input.getName());
			// TODO : Consider appending attempt number.
		return builder.toString().toLowerCase();
	}

	/**
	 * Default constructor.
	 * 
	 * @param solver Target problem solver this submission will work on.
	 * @param input Problem input this submission is working on.
	 */
	public OnlineSubmission(final ProblemSolver solver, final ProblemInput input) {
		super(solver, buildName(input));
		this.input = input;
		final StringBuilder extensionBuilder = new StringBuilder();
		// TODO : Build extension based on file input
		extensionBuilder.append(EXTENSION);
		this.extension = extensionBuilder.toString();
	}

	/** {@inheritDoc} **/
	@Override
	public boolean submit(final IProgressMonitor monitor) {
		final ISubmissionService service = getService();
		try {
			final SubmitResponse response = service.submit(this);
			if (!response.isSuccess()) {
				service.fireErrorCaught(this, new SubmissionException(response.getMessage(), Type.FAIL));
				return false;
			}
			return true;
		}
		catch (final IOException e) {
			service.fireErrorCaught(this, new SubmissionException(e, Type.ERROR));
			return false;
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void start(final IProgressMonitor monitor) {
		final ISubmissionService service = getService();
		service.fireSubmissionStarted(this);
		try {
			final IFile file = service.downloadInput(this, monitor);
			run(file.getLocation().toOSString(), monitor);
		}
		catch (final IOException | CoreException e) {
			service.fireErrorCaught(this, new SubmissionException(e, Type.ERROR));
		}
	}

	/** {@inheritDoc} **/
	@Override
	public ProblemInput getProblemInput() {
		return input;
	}
	
	/** {@inheritDoc} **/
	@Override
	public IFile getOutputFile() throws CoreException {
		return getOutputFile(extension);
	}

}
