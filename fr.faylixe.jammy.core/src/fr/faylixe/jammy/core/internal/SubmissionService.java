package fr.faylixe.jammy.core.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.CodeJamSession;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.googlecodejam.client.webservice.SubmitResponse;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.ProblemSolverFactory;
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
	public static final IOException SESSION_NOT_PRESENT = new IOException("You must be logged to download or submit data");

	/** **/
	private final Collection<ISubmissionListener> listeners;

	/** **/
	private CodeJamSession session;

	/**
	 * Default constructor.
	 */
	public SubmissionService() {
		this.listeners = new HashSet<ISubmissionListener>();
		Jammy.getInstance().addSessionListener(this);
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
	public String buildFilename(final ISubmission submission) throws IOException {
		if (session == null) {
			throw SESSION_NOT_PRESENT;
		}
		final ProblemInput input = submission.getProblemInput();
		final int attempt = ProblemSolverFactory.getInstance().getProblemAttempt(input);
		return session.buildFilename(input, attempt);
	}

	/** {@inheritDoc} **/
	@Override
	public IFile downloadInput(final ISubmission submission, final IProgressMonitor monitor) throws IOException {
		if (session == null) {
			throw SESSION_NOT_PRESENT;
		}
		final ProblemInput input = submission.getProblemInput();
		final int attempt = ProblemSolverFactory.getInstance().getProblemAttempt(input);
		final InputStream stream = session.download(input, attempt);
		final IFolder folder = submission.getSolver().getDatasetFolder();
		final IFile file = folder.getFile(buildFilename(submission));
		try {
			if (file.exists()) {
				file.delete(true, monitor);
			}
			file.create(stream, true, monitor);
			return file;
		}
		catch (final CoreException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}
	
	/** {@inheritDoc} **/
	@Override
	public SubmitResponse submit(final ISubmission submission) throws IOException {
		if (session == null) {
			throw SESSION_NOT_PRESENT;
		}
		try {
			final File output = EclipseUtils.toFile(submission.getOutputFile());
			final File source = EclipseUtils.toFile(submission.getSolver().getFile());
			return session.submit(submission.getProblemInput(), output, source);
		}
		catch (final CoreException e) {
			throw new IOException(e);
		}
	}

}
