package review.classdesign.jammy.core.model;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test case for {@link Contest} class.
 * 
 * @author fv
 */
public final class ContestTest {

	/** Expected name of the retrieved contest. **/
	protected static final String NAME = "";

	/** Expected number of round for the retrieved contest. **/
	private static final int ROUND = 4;

	/**
	 * 
	 * @return
	 */
	public static Contest getTestContests() {
		Contest testContest = null;
		try {
			final List<Contest> contests = Contest.get();
			testContest = contests.get(contests.size() - 1);
		}
		catch (final IOException e) {
			fail("Error occured while retrieving contests : " + e.getMessage());
		}
		return testContest;
	}

	/**
	 * Ensures retrieved contests are consistent.
	 * In order to do so, it consider that the last extracted
	 * contest should never change (the older always stays the older).
	 */
	@Test
	public void testContestConsistency() {	
		final Contest contest = getTestContests();
		assertEquals(NAME, contest.getName());
		final List<Round> rounds = contest.getRounds();
		assertEquals(ROUND, rounds.size());
	}

}
