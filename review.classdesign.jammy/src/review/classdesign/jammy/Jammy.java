package review.classdesign.jammy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import review.classdesign.jammy.listener.ProblemSelectionListener;
import review.classdesign.jammy.listener.RoundSelectionListener;
import review.classdesign.jammy.model.Problem;
import review.classdesign.jammy.model.Round;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author fv
 */
public class Jammy extends AbstractUIPlugin {

	/** Plug-in ID. **/
	public static final String PLUGIN_ID = "review.classdesign.jammy"; //$NON-NLS-1$

	/** Plug-in instance. **/
	private static Jammy plugin;

	/**
	 * Empty object array in order to avoid empty array allocation duplication.
	 * This objects is also used a root element for both contest and round selection view.
	 */
	public static final Object [] CHILDLESS = new Object[0];

	/** **/
	private Round currentRound;
	
	/** **/
	private Problem currentProblem;

	/** **/
	private final List<RoundSelectionListener> roundListeners;

	/** **/
	private final List<ProblemSelectionListener> problemListeners;

	/**
	 * The constructor
	 */
	public Jammy() {
		this.roundListeners = new ArrayList<>();
		this.problemListeners = new ArrayList<>();
	}
	
	/**
	 * Adds the given {@link RoundSelectionListener} to the listener list.
	 * 
	 * @param listener Listener instance to register.
	 */
	public void addRoundSelectionListener(final RoundSelectionListener listener) {
		roundListeners.add(listener);
	}

	/**
	 * Removes the given {@link RoundSelectionListener} of the listener list.
	 * 
	 * @param listener Listener instance to unregister
	 */
	public void removeRoundSelectionListener(final RoundSelectionListener listener) {
		roundListeners.remove(listener);
	}

	/**
	 * Adds the given {@link ProblemSelectionListener} to the listener list.
	 * 
	 * @param listener Listener instance to register.
	 */
	public void addProblemSelectionListener(final ProblemSelectionListener listener) {
		problemListeners.add(listener);
	}
	
	/**
	 * Removes the given {@link ProblemSelectionListener} of the listener list.
	 * 
	 * @param listener Listener instance to unregister
	 */
	public void removeProblemSelectionListener(final ProblemSelectionListener listener) {
		problemListeners.remove(listener);
	}

	/**
	 * Notifies all {@link RoundSelectionListener} instance
	 * registered that the current round has changed.
	 */
	private void fireRoundSelectionChanged() {
		for (final RoundSelectionListener listener : roundListeners) {
			listener.roundSelected(currentRound);
		}
	}
	
	/**
	 * Notifies all {@link ProblemSelectionListener} instance
	 * registered that the current problem has changed.
	 */
	private void fireProblemSelectionChanged() {
		for (final ProblemSelectionListener listener : problemListeners) {
			listener.problemSelected(currentProblem);
		}
	}

	/**
	 * 
	 * @param contest
	 * @param round
	 */
	public void setCurrentRound(final Round round) {
		currentRound = Objects.requireNonNull(round);
		fireRoundSelectionChanged();
	}

	/**
	 * 
	 * @return
	 */
	public Optional<Round> getCurrentRound() {
		return Optional.ofNullable(currentRound);
	}
	
	/**
	 * 
	 * @return
	 */
	public Optional<Problem> getCurrentProblem() {
		return Optional.ofNullable(currentProblem);
	}

	/** {@inheritDoc} **/
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		final IPreferenceStore store = getPreferenceStore();
		JammyPreferences.load(store);
		store.addPropertyChangeListener(JammyPreferences::propertyChange);
		plugin = this;
	}

	/** {@inheritDoc} **/
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
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
