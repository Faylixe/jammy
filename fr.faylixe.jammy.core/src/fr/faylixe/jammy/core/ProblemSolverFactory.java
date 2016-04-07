package fr.faylixe.jammy.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.listener.ILanguageManagerListener;
import fr.faylixe.jammy.core.listener.ISubmissionListener;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.SubmissionException;

/**
 * <p>{@link ProblemSolverFactory} is a singleton
 * responsible for creating and storing {@link ProblemSolver}
 * instance.</p>
 * 
 * @author fv
 */
public final class ProblemSolverFactory implements ILanguageManagerListener, ISubmissionListener {

	/** Path of the persistent attempt map. **/
	private static final String ATTEMPT_PATH = "attempt.map";

	/** Path of the persistent attempt map. **/
	private static final String PASSED_PATH = "passed.set";

	/** Error message displayed when loading attempts mapping failed. **/
	private static final String LOAD_FAIL = "An error occurs while loading problem attempt mapping";

	/** Error message displayed when saving attempts mapping failed. **/
	private static final String SAVE_FAIL = "An error occurs while saving problem attempt mapping";

	/** Unique factory instance. **/
	private static ProblemSolverFactory instance;

	/** Cache of all problem solver instances indexed by **/
	private final Map<Problem, SoftReference<ProblemSolver>> solvers;

	/** Problem submission attempt mapping. Key are made using problem id and input name. **/
	private final Map<String, Integer> attempts;

	/** Problem submission that was successful. **/
	private final Set<String> passed;

	/**
	 * Default constructor.
	 */
	private ProblemSolverFactory() {
		this.passed = new HashSet<>();
		this.solvers = new ConcurrentHashMap<>();
		this.attempts = new ConcurrentHashMap<>();
	}

	/** {@inheritDoc} **/
	@Override
	public void languageManagerChanged() {
		EclipseUtils.closeAllFile();
	}

	/**
	 * {@link ProblemSolver} instances access method. If the required instance
	 * does not exist, or has been garbaged, then a new instance is created and put
	 * into the local cache.
	 * 
	 * @param problem Problem to get solver instance from.
	 * @param monitor Monitor instance used if solver creation is required.
	 * @return Retrieved instance.
	 * @throws CoreException If any error occurs while creating required instance.
	 */
	public ProblemSolver getSolver(final Problem problem, final IProgressMonitor monitor) throws CoreException {
		final SoftReference<ProblemSolver> reference = solvers.get(problem);
		ProblemSolver solver;
		if (reference == null || reference.get() == null) {
			solver = ProblemSolver.createSolver(problem, monitor);
			solvers.put(problem, new SoftReference<ProblemSolver>(solver));
		}
		else {
			solver = reference.get();
		}
		return solver;
	}
	
	/**
	 * Builds and returns input unique key.
	 * 
	 * @param input Input to build key from.
	 * @return Built key.
	 */
	private static String getKey(final ProblemInput input) {
		final Problem problem = input.getProblem();
		final StringBuilder builder = new StringBuilder();
		builder
			.append(problem.getId())
			.append('-')
			.append(input.getName());
		return builder.toString();
	}

	/**
	 * Retrieves the current attempt number for the given <tt>input</tt>.
	 * 
	 * @param input Input to retrieve attempt from.
	 * @return Attempt number if any, 0 otherwise.
	 */
	public int getProblemAttempt(final ProblemInput input) {
		return attempts.getOrDefault(getKey(input), 0);
	}
	
	/**
	 * Sets the current attempt number for the given problem.
	 * 
	 * @param input Input to set attempt for.
	 * @param attempt Attempt number to set.
	 */
	public void setProblemAttempt(final ProblemInput input, final int attempt) {
		attempts.put(getKey(input), attempt);
		saveState();
	}

	/**
	 * Loads the attempt mapping from the plugin state location.
	 */
	private void loadState() {
		final IPath path = Jammy.getInstance().getStateLocation();
		final IPath attemptPath = path.append(ATTEMPT_PATH);
		final File file = attemptPath.toFile();
		if (file.exists()) {
			try {
				final InputStream stream = new FileInputStream(file);
				final Object object = SerializationUtils.deserialize(stream);
				if (object instanceof Map) {
					final Map<?, ?> attemptMap = (Map<?, ?>) object;
					for (final Object key : attemptMap.keySet()) {
						final int value = (int) attemptMap.get(key);
						attempts.put(key.toString(), value);
					}
				}
			}
			catch (final IOException e) {
				EclipseUtils.showError(LOAD_FAIL, e);
			}
		}
	}
	

	/**
	 * Saves the attempt mapping from the plugin state location.
	 */
	private void saveState() {
		final IPath path = Jammy.getInstance().getStateLocation();
		final IPath passedPath = path.append(PASSED_PATH);
		final IPath attemptPath = path.append(ATTEMPT_PATH);
		final File file = attemptPath.toFile();
		try {
			final OutputStream stream = new FileOutputStream(file);
			SerializationUtils.serialize((Serializable) attempts, stream);
		}
		catch (final IOException e) {
			EclipseUtils.showError(SAVE_FAIL, e);
		}
	}

	/**
	 * Returns the unique factory instance,
	 * and creates it if not exists.
	 * 
	 * @return Unique factory instance.
	 */
	public static ProblemSolverFactory getInstance() {
		synchronized (ProblemSolverFactory.class) {
			if (instance == null) {
				instance = new ProblemSolverFactory();
				instance.loadState();
			}
		}
		return instance;
	}

	/** {@inheritDoc} **/
	@Override
	public void errorCaught(final ISubmission submission, final SubmissionException error) {
		// Do nothing.
	}
	
	/** {@inheritDoc} **/
	@Override
	public void submissionStarted(final ISubmission submission) {
		// TODO : Start timer.
	}

	/** {@inheritDoc} **/
	@Override
	public void executionStarted(final ISubmission submission) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionFinished(final ISubmission submission) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void executionFinished(final ISubmission submission) {
		// Do nothing.
	}

}
