package fr.faylixe.jammy.core.internal.submission;

import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.service.ISubmissionService;
import fr.faylixe.jammy.core.service.SubmissionException;

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
	private static String buildName(final ProblemInput input) {
		final StringBuilder builder = new StringBuilder();
		builder
			.append(input.getProblem().getNormalizedName())
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
		// TODO : Build extension based on file input.
		this.extension = extensionBuilder.toString();
	}

	/** {@inheritDoc} **/
	@Override
	public void submit(final IProgressMonitor monitor) throws SubmissionException {
		try {
			getService().submit(this);
		}
		catch (final IOException e) {
			throw new SubmissionException(e);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void start(final IProgressMonitor monitor) throws SubmissionException {
		final ISubmissionService service = getService();
		service.fireSubmissionStarted(this);
		try {
			final Path path = service.downloadInput(this);
			run(path.toString(), monitor);
		}
		catch (final IOException | CoreException e) {
			// TODO : Consider building a debuging runnable.
			throw new SubmissionException(e);
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
