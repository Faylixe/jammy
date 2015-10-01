package review.classdesign.jammy.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.ui.AbstractSourceProvider;

import review.classdesign.jammy.model.contest.Contest;
import review.classdesign.jammy.model.contest.Round;

/**
 * 
 * @author fv
 */
public final class JammyContext {

	/** **/
	private Optional<List<Contest>> contests;

	/** Current contest this plugin is working on. **/
	private Optional<Contest> currentContest;

	/** **/
	private Optional<Round> currentRound;

	/**
	 * Default constructor.
	 * 
	 */
	private JammyContext() {
		this.contests = Optional.empty();
		this.currentRound = Optional.empty();
		this.currentContest = Optional.empty();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return false;
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
	
	/** Unique instance of this class. **/
	private static JammyContext INSTANCE;

	/**
	 * Retrieves if exist the unique context instance,
	 * and creates it before if not.
	 * 
	 * @return Unique {@link JammyContext} instance.
	 */
	public static JammyContext getInstance() {
		synchronized (JammyContext.class) {
			if (INSTANCE == null) {
				INSTANCE = new JammyContext();
			}
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @param dummy
	 * @return
	 */
	public static Object[] getContests(final Object dummy) {
		return getInstance().getContests().toArray();
	}
}
