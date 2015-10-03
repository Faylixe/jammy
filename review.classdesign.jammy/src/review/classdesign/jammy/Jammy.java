package review.classdesign.jammy;

import java.util.List;
import java.util.Optional;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import review.classdesign.jammy.model.Contest;
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
	private Optional<List<Contest>> contests;

	/** Current contest this plugin is working on. **/
	private Optional<Contest> currentContest;

	/** **/
	private Optional<Round> currentRound;

	/**
	 * The constructor
	 */
	public Jammy() {
		this.contests = Optional.empty();
		this.currentRound = Optional.empty();
		this.currentContest = Optional.empty();
	}
	
	/**
	 * 
	 * @param contest
	 * @param round
	 */
	public void setCurrent(final Optional<Contest> contest, final Optional<Round> round) {
		currentRound = round;
		currentContest = contest;
	}

	/**
	 * 
	 * @return
	 */
	public Optional<Round> getCurrentRound() {
		return currentRound;
	}

	/**
	 * 
	 * @return
	 */
	public List<Contest> getContests() {
		if (!contests.isPresent()) {
			try {
				contests = Optional.ofNullable(Contest.get());
			}
			catch (final Exception e) {
				e.printStackTrace();
				// TODO : Handle error.
			}
		}
		return contests.get();
	}

	/** {@inheritDoc} **/
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/** {@inheritDoc} **/
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Functional method.
	 * 
	 * @param dummy
	 * @return
	 * @category Function
	 */
	public static Object[] getContests(final Object dummy) {
		return plugin.getContests().toArray();
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
