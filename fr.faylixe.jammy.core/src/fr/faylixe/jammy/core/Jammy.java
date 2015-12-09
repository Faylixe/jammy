package fr.faylixe.jammy.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import fr.faylixe.googlecodejam.client.CodeJamSession;
import fr.faylixe.googlecodejam.client.Round;
import fr.faylixe.googlecodejam.client.executor.HttpRequestExecutor;
import fr.faylixe.googlecodejam.client.webservice.ContestInfo;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.addons.ILanguageManager;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.common.SerializationUtils;
import fr.faylixe.jammy.core.listener.IContestSelectionListener;
import fr.faylixe.jammy.core.listener.IProblemSelectionListener;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author fv
 */
public class Jammy extends AbstractUIPlugin {

	/** Path of the contest state which is save when plugin is stopped. **/
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

	/** Listeners that are triggered when the currently selected round change. **/
	private final List<IContestSelectionListener> contestListeners;

	/** Listeners that are triggered when the currently selected problem change. **/
	private final List<IProblemSelectionListener> problemListeners;

	/** **/
	private final Map<String, ILanguageManager> managers;

	/** Current session used for interracting with code jam platform. **/
	private CodeJamSession session;

	/**
	 * {@link Problem} instance that is currently selected
	 * by user and which acts as the contextual problem.
	 */
	private Problem selectedProblem;

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
		if (session != null) {
			listener.contestSelected(session.getContestInfo());
		}
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
		if (session != null) {
			listener.problemSelected(null); // TODO : Retrieve selected problem.
		}
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
	 * 
	 * @param contestInfo Information about the currently selected contest.
	 */
	private void fireContestSelectionChanged(final ContestInfo contestInfo) {
		for (final IContestSelectionListener listener : contestListeners) {
			listener.contestSelected(contestInfo);
		}
	}
	
	/**
	 * Notifies all {@link IProblemSelectionListener} instance
	 * registered that the current problem has changed.
	 */
	private void fireProblemSelectionChanged() {
		for (final IProblemSelectionListener listener : problemListeners) {
			listener.problemSelected(selectedProblem);
		}
	}

	/**
	 * 
	 * @param round
	 */
	public void setCurrentRound(final Round round) {
		if (round != null) {
			// TODO : Retrieve executor from service.
			final HttpRequestExecutor executor = null;
			try {
				session = CodeJamSession.createSession(executor, round);
				fireContestSelectionChanged(session.getContestInfo());
			}
			catch (final IOException e) {
				EclipseUtils.showError(e);
			}
		}
	}

	/**
	 * Sets the currently selected problem.
	 * 
	 * @param problem The newly selected problem.
	 */
	public void setSelectedProblem(final Problem problem) {
		selectedProblem = problem;
		fireProblemSelectionChanged();
	}
	
	/**
	 * Getter for the currently selected {@link Problem}.
	 * 
	 * @return The selected {@link Problem} instance, or <tt>null</tt> if no problem has been selected.
	 * @see #selectedProblem
	 */
	public Problem getCurrentProblem() {
		return selectedProblem;
	}

	/**
	 * Loads this plugin inner state.
	 * Such states consists in a serialized form of the current session.
	 */
	private void loadState() {
		final IPath state = getStateLocation();
		final IPath contest = state.append(CONTEST_STATE);
		final File file = contest.toFile();
		if (file.exists()) {
			try {
				session = SerializationUtils.deserialize(file, CodeJamSession.class);
				fireContestSelectionChanged(session.getContestInfo());
			}
			catch (final IOException | ClassNotFoundException e) {
				EclipseUtils.showError(e.getMessage(), e);
			}
		}
	}

	/**
	 * Saves this plugin current state. Which
	 * consists in serializing the current session.
	 */
	private void saveState() {
		final IPath state = getStateLocation();
		if (session != null) {
			// Save current contest.
			final IPath sessionPath = state.append(CONTEST_STATE);
			try {
				SerializationUtils.serialize(session, sessionPath.toFile());
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
		saveState();
		super.stop(context);
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
