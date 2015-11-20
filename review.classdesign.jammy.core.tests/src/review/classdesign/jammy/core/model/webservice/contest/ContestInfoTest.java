package review.classdesign.jammy.core.model.webservice.contest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.fail;

import review.classdesign.jammy.core.model.Round;
import review.classdesign.jammy.core.model.RoundTest;

/**
 * Test case for {@link ContestInfo} class.
 * 
 * @author fv
 */
public final class ContestInfoTest {

	/** Expected project generated name. **/
	private static final String PROJECT_NAME = "";

	/** Expected version of this contest. **/
	private static final int VERSION = 1;

	/** Expected number of problem instance. **/
	private static final int PROBLEM = 4;

	/**
	 * Retrieves the {@link ContestInfo} instance
	 * that will be used for testing. Using
	 * test {@link Round} as reference.
	 * 
	 * @return ContestInfo instance for testing.
	 */
	public static ContestInfo getTestContestInfo() {
		final Round round = RoundTest.getTestRound();
		ContestInfo info = null;
		try {
			info = ContestInfo.get(round);
		}
		catch (final IOException e) {
			fail("Error occured while retrieving contest info : " + e.getMessage());
		}
		return info;
	}

	/**
	 * Ensures retrieved {@link ContestInfo} is
	 * consistent. Uses {@link ContestInfo} from
	 * target test {@link Round} instance.
	 */
	@Test
	public void testContestInfoConsistency() {
		final ContestInfo info = getTestContestInfo();
		assertEquals(PROJECT_NAME, info.getProjectName());
		assertEquals(VERSION, info.getVersion());
		assertTrue(info.hasAnalysis());
		final List<Problem> problems = info.getProblems();
		assertEquals(PROBLEM, problems.size());
	}

}
