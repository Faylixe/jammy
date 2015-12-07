package fr.faylixe.jammy.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import fr.faylixe.jammy.core.addons.ILanguageManager;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.common.SerializationUtils;
import fr.faylixe.jammy.core.model.Round;
import fr.faylixe.jammy.core.model.listener.IContestSelectionListener;
import fr.faylixe.jammy.core.model.listener.IProblemSelectionListener;
import fr.faylixe.jammy.core.model.webservice.InitialValues;
import fr.faylixe.jammy.core.model.webservice.contest.ContestInfo;
import fr.faylixe.jammy.core.model.webservice.contest.Problem;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author fv
 */
public class Jammy extends AbstractUIPlugin {

	private static final String CONTEST_STATE = "current.contest";

	/** Plug-in instance. **/
	private static Jammy plugin;

	/** Plug-in ID. **/
	public static final String PLUGIN_ID = "review.classdesign.jammy"; //$NON-NLS-1$

	/**
	 * Empty object array in order to avoid empty array allocation duplication.
	 * This objects is also used a root element for both contest and round selection view.
	 */
	public static final Object [] CHILDLESS = new Object[0];

	/** **/
	private ContestInfo currentContest;
	
	/** **/
	private Problem currentProblem;

	/** **/
	private final List<IContestSelectionListener> contestListeners;

	/** **/
	private final List<IProblemSelectionListener> problemListeners;

	/** **/
	private final Map<String, ILanguageManager> managers;

	/**
	 * The constructor
	 */
	public Jammy() {
		super();
		this.contestListeners = new ArrayList<>();
		this.problemListeners = new ArrayList<>();
		this.managers = new HashMap<>();
	}
	
	/**
	 * Adds the given {@link IContestSelectionListener} to the listener list.
	 * 
	 * @param listener Listener instance to register.
	 */
	public void addContestSelectionListener(final IContestSelectionListener listener) {
		contestListeners.add(listener);
	}

	/**
	 * Removes the given {@link IContestSelectionListener} of the listener list.
	 * 
	 * @param listener Listener instance to unregister
	 */
	public void removeContestSelectionListener(final IContestSelectionListener listener) {
		contestListeners.remove(listener);
	}

	/**
	 * Adds the given {@link IProblemSelectionListener} to the listener list.
	 * 
	 * @param listener Listener instance to register.
	 */
	public void addProblemSelectionListener(final IProblemSelectionListener listener) {
		problemListeners.add(listener);
	}
	
	/**
	 * Removes the given {@link IProblemSelectionListener} of the listener list.
	 * 
	 * @param listener Listener instance to unregister
	 */
	public void removeProblemSelectionListener(final IProblemSelectionListener listener) {
		problemListeners.remove(listener);
	}

	/**
	 * Notifies all {@link IContestSelectionListener} instance
	 * registered that the current round has changed.
	 */
	private void fireContestSelectionChanged() {
		for (final IContestSelectionListener listener : contestListeners) {
			listener.contestSelected(currentContest);
		}
	}
	
	/**
	 * Notifies all {@link IProblemSelectionListener} instance
	 * registered that the current problem has changed.
	 */
	private void fireProblemSelectionChanged() {
		for (final IProblemSelectionListener listener : problemListeners) {
			listener.problemSelected(currentProblem);
		}
	}

	/**
	 * 
	 * @param round
	 */
	public void setCurrentRound(final Round round) {
		try {
			currentContest = ContestInfo.get(round);
			InitialValues.get(round);
		}
		catch (final IOException e) {
			EclipseUtils.showError(e);
		}
		fireContestSelectionChanged();
	}

	/**
	 * 
	 * @param problem
	 */
	public void setCurrentProblem(final Problem problem) {
		currentProblem = Objects.requireNonNull(problem);
		fireProblemSelectionChanged();
	}

	/**
	 * 
	 * @return
	 */
	public Optional<ContestInfo> getCurrentContest() {
		return Optional.ofNullable(currentContest);
	}
	
	/**
	 * 
	 * @return
	 */
	public Optional<Problem> getCurrentProblem() {
		return Optional.ofNullable(currentProblem);
	}

	/**
	 * 
	 */
	private void loadState() {
		final IPath state = getStateLocation();
		final IPath contest = state.append(CONTEST_STATE);
		final File file = contest.toFile();
		if (file.exists()) {
			try {
				currentContest = SerializationUtils.deserialize(file, ContestInfo.class);
				fireContestSelectionChanged();
			}
			catch (final IOException | ClassNotFoundException e) {
				EclipseUtils.showError(e.getMessage(), e);
			}
		}
	}

	/**
	 * 
	 */
	private void saveState() {
		final IPath state = getStateLocation();
		if (currentContest != null) {
			// Save current contest.
			final IPath contest = state.append(CONTEST_STATE);
			try {
				SerializationUtils.serialize(currentContest, contest.toFile());
			}
			catch (final IOException e) {
				EclipseUtils.showError(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 
	 */
	private void loadManagers() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final IConfigurationElement [] elements = registry.getConfigurationElementsFor(ILanguageManager.EXTENSION_ID);
		for (final IConfigurationElement element : elements) {
			final String language = element.getAttribute(ILanguageManager.LANGUAGE_ATTRIBUTE);
			try {
				final Object manager = element.createExecutableExtension(ILanguageManager.CLASS_ATTRIBUTE);
				managers.put(language, (ILanguageManager) manager);
			}
			catch (final CoreException e) {
				EclipseUtils.showError(e.getMessage(), e);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public ILanguageManager getCurrentLanguageManager() {
		final String language = JammyPreferences.getCurrentLanguage();
		return managers.get(language);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getLanguages() {
		return managers.keySet();
	}

	/** {@inheritDoc} **/
	public void start(final BundleContext context) throws Exception { // NOPMD
		super.start(context);
		final IPreferenceStore store = getPreferenceStore();
		JammyPreferences.load(store);
		loadManagers();
		loadState();
		store.addPropertyChangeListener(JammyPreferences::propertyChange);
		plugin = this;
	}

	/** {@inheritDoc} **/
	public void stop(final BundleContext context) throws Exception { // NOPMD
		plugin = null; // NOPMD
		super.stop(context);
		saveState();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Jammy getDefault() {
		return plugin;
	}

}