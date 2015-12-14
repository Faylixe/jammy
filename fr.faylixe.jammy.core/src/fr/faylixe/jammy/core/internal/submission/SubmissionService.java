package fr.faylixe.jammy.core.internal.submission;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;

import fr.faylixe.googlecodejam.client.CodeJamSession;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.listener.ISessionListener;
import fr.faylixe.jammy.core.listener.ISubmissionListener;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.ISubmissionService;
import fr.faylixe.jammy.core.service.SubmissionException;

/**
 * TODO : Service javadoc.
 * 
 * @author fv
 */
public final class SubmissionService implements ISubmissionService, ISessionListener {

	/** **/
	private static final IOException SESSION_NOT_PRESENT = new IOException("");

	/** **/
	private static final String INPUT_PREFIX = "";

	/** **/
	private static final String INPUT_SUFFIX = "";

	/** **/
	private final Collection<ISubmissionListener> listeners;

	/** **/
	private CodeJamSession session;

	/**
	 * Default constructor.
	 * 
	 */
	public SubmissionService() {
		this.listeners = new ArrayList<ISubmissionListener>();
	}

	/** {@inheritDoc} **/
	@Override
	public void sessionChanged(final CodeJamSession session) {
		this.session = session;
	}

	/** {@inheritDoc} **/
	@Override
	public void addSubmissionListener(final ISubmissionListener listener) {
		listeners.add(listener);
	}

	/** {@inheritDoc} **/
	@Override
	public void removeSubmissionListener(final ISubmissionListener listener) {
		listeners.remove(listener);
	}

	/** {@inheritDoc} **/
	@Override
	public void fireSubmissionStarted(final ISubmission submission) {
		listeners.forEach(listener -> listener.submissionStarted(submission));
	}

	/** {@inheritDoc} **/
	@Override
	public void fireSubmissionFinished(final ISubmission submission) {
		listeners.forEach(listener -> listener.submissionFinished(submission));
	}

	/** {@inheritDoc} **/
	@Override
	public void fireExecutionStarted(final ISubmission submission) {
		listeners.forEach(listener -> listener.executionStarted(submission));
	}

	/** {@inheritDoc} **/
	@Override
	public void fireExecutionFinished(final ISubmission submission) {
		listeners.forEach(listener -> listener.executionFinished(submission));
	}

	/** {@inheritDoc} **/
	@Override
	public void fireErrorCaught(final ISubmission submission, final SubmissionException exception) {
		listeners.forEach(listener -> listener.errorCaught(submission, exception));
	}

	/** {@inheritDoc} **/
	@Override
	public Path downloadInput(final ISubmission submission) throws IOException {
		if (session == null) {
			throw SESSION_NOT_PRESENT;
		}
		final InputStream stream = session.download(submission.getProblemInput());
		final Path path = Files.createTempFile(INPUT_PREFIX, INPUT_SUFFIX);
		Files.copy(stream, path);
		return path.toAbsolutePath();
	}
	
	/** {@inheritDoc} **/
	@Override
	public void submit(final ISubmission submission) throws IOException {
		if (session == null) {
			throw SESSION_NOT_PRESENT;
		}
		try {
			final File output = EclipseUtils.toFile(submission.getOutputFile());
			final File source = EclipseUtils.toFile(submission.getSolver().getFile());
			session.submit(submission.getProblemInput(), output, source);
			// TODO : Handle result.
		}
		catch (final CoreException e) {
			throw new IOException(e);
		}
	}

}
