package fr.faylixe.jammy.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.listener.ILanguageManagerListener;
import fr.faylixe.jammy.core.listener.IProblemStateListener;
import fr.faylixe.jammy.core.listener.ISubmissionListener;
import fr.faylixe.jammy.core.service.ISubmission;
import fr.faylixe.jammy.core.service.ISubmissionService;
import fr.faylixe.jammy.core.service.OnlineSubmission;
import fr.faylixe.jammy.core.service.SubmissionException;
import fr.faylixe.jammy.core.service.SubmissionException.Type;

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

	/** Exception thrown when the loaded set is not valid. **/
	private static final Exception SET_NOT_VALID = new Exception("An error occurs when loading solver state : Not valid passed set");

	/** Exception thrown when the loaded map is not valid. **/
	private static final Exception MAP_NOT_VALID = new Exception("An error occurs when loading solver state : Not valid attempt map");

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

	/** Listener for problem state; **/
	private final List<IProblemStateListener> listeners;

	/**
	 * Default constructor.
	 */
	private ProblemSolverFactory() {
		this.passed = new HashSet<>();
		this.solvers = new ConcurrentHashMap<>();
		this.attempts = new ConcurrentHashMap<>();
		this.listeners = new ArrayList<>();
	}

	/**
	 * Adds the given <tt>listener</tt> to this factory.
	 * 
	 * @param listener Listener to add.
	 */
	public void addListener(final IProblemStateListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the given <tt>listener</tt> from this factory.
	 * 
	 * @param listener Listener to remove.
	 */
	public void removeListener(final IProblemStateListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all listeners that problem state has changed.
	 */
	private void fireProblemStateChanged() {
		listeners.forEach(IProblemStateListener::problemStateChanged);
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
		fireProblemStateChanged();
	}

	/**
	 * Adds the given <tt>input</tt> to the passed set.
	 * 
	 * @param input Input to add.
	 */
	public void setPassed(final ProblemInput input) {
		passed.add(getKey(input));
		saveState();
		fireProblemStateChanged();
	}

	/**
	 * Indicates if the given <tt>input</tt> has been passed or not.
	 *  
	 * @param input Input to check if passed of not.
	 * @return <tt>true</tt> if the given <tt>input</tt> is passed, <tt>false</tt> otherwise.
	 */
	public boolean isPassed(final ProblemInput input) {
		return passed.contains(input);
	}

	/**
	 * Loads the attempt mapping from the plugin state location.
	 */
	private void loadState() {
		loadSerializedState(PASSED_PATH, this::loadPassed);
		loadSerializedState(ATTEMPT_PATH, this::loadAttempt);
		fireProblemStateChanged();
	}
	
	/**
	 * Loads the object from the given file <tt>name</tt> using
	 * the given <tt>consumer</tt>.
	 * 
	 * @param name Name of the state file to load.
	 * @param consumer Consumer that is in charge of loading the read object.
	 */
	private void loadSerializedState(final String name, final Consumer<Object> consumer) {
		final IPath stateLocation = Jammy.getInstance().getStateLocation();
		final IPath serializablePath = stateLocation.append(name);
		final File file = serializablePath.toFile();
		if (file.exists()) {
			try (final InputStream stream = new FileInputStream(file)) {
				final Object object = SerializationUtils.deserialize(stream);
				consumer.accept(object);
			}
			catch (final IOException e) {
				EclipseUtils.showError(LOAD_FAIL, e);
			}
		}
	}
	
	/**
	 * Loads passed set entry from the given <tt>object</tt>.
	 * 
	 * @param object Object to load passed set from.
	 */
	private void loadPassed(final Object object) {
		if (!(object instanceof Set)) {
			EclipseUtils.showError(SET_NOT_VALID);
		}
		final Set<?> set = (Set<?>) object;
		for (final Object key : set) {
			passed.add(key.toString());
		}
	}
	
	/**
	 * Loads attempts map entry from the given <tt>object</tt>.
	 * 
	 * @param object Object to load attempts entry from.
	 */
	private void loadAttempt(final Object object) {
		if (!(object instanceof Map)) {
			EclipseUtils.showError(MAP_NOT_VALID);
		}
		final Map<?, ?> map = (Map<?, ?>) object;
		for (final Object key : map.keySet()) {
			final int value = (int) map.get(key);
			attempts.put(key.toString(), value);
		}
	}
	

	/**
	 * Saves this factory state.
	 */
	private void saveState() {
		saveSerializableState(PASSED_PATH, (Serializable) passed);
		saveSerializableState(ATTEMPT_PATH, (Serializable) attempts);
	}
	
	/**
	 * Saves the given <tt>serializable</tt> to the plugin state.
	 * 
	 * @param name Name of the file to save.
	 * @param serializable Serializable object to save.
	 */
	private void saveSerializableState(final String name, final Serializable serializable) {
		final IPath stateLocation = Jammy.getInstance().getStateLocation();
		final IPath serializablePath = stateLocation.append(name);
		final File file = serializablePath.toFile();
		try (final OutputStream stream = new FileOutputStream(file)) {
			SerializationUtils.serialize(serializable, stream);
		}
		catch (final IOException e) {
			EclipseUtils.showError(SAVE_FAIL, e);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void errorCaught(final ISubmission submission, final SubmissionException error) {
		if (submission instanceof OnlineSubmission && Type.FAIL.equals(error.getType())) {
			final ProblemInput input = submission.getProblemInput();
			final int attempt = getProblemAttempt(input);
			setProblemAttempt(input, attempt + 1);
		}
	}
	
	/** {@inheritDoc} **/
	@Override
	public void submissionStarted(final ISubmission submission) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void executionStarted(final ISubmission submission) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void executionFinished(final ISubmission submission) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void submissionFinished(final ISubmission submission) {
		if (submission instanceof OnlineSubmission) {
			// Submission is a success here : we mark the problem input as passed.
			setPassed(submission.getProblemInput());
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
				final ISubmissionService service = ISubmissionService.get();
				service.addSubmissionListener(instance);	
			}
		}
		return instance;
	}

}
