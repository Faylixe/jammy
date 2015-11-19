package review.classdesign.jammy.core.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

/**
 * Test case for the {@link Round} class.
 * 
 * @author fv
 */
public final class RoundTest {

	/** Expected URL of the round retrieved. **/
	private static final String URL = "";

	/** Expected name of the retrieved round. **/
	private static final String NAME = "";

	/**
	 * 
	 */
	@Test
	public void testRoundConsistency() {
		final Contest contest = ContestTest.getTestContests();
		final List<Round> rounds = contest.getRounds();
		final Round round = rounds.get(0);
		assertEquals(URL, round.getURL());
		assertEquals(NAME, round.getName());
		assertEquals(ContestTest.NAME, round.getContestName());
	}

}
