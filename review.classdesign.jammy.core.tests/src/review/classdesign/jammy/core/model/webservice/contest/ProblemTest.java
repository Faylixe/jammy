package review.classdesign.jammy.core.model.webservice.contest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import review.classdesign.jammy.core.Jammy;
import review.classdesign.jammy.core.common.EclipseUtils;

/**
 * Test case for the {@link Problem} class.
 * 
 * @author fv
 */
public final class ProblemTest {

	/** Path of the file that contains expected body content for the test problem. **/
	private static final String BODY_PATH = "resources/body.html";

	/** Expected name for the test problem. **/
	private static final String NAME = "";

	/** Expected id for the test problem. **/
	private static final String ID = "";

	/** Expected type for the test problem. **/
	private static final String TYPE = "";

	/** Expected key for the test problem. **/
	private static final String KEY = "";

	/** Expected normalized name for the test problem. **/
	private static final String NORMALIZED_NAME = "";

	/** Expected number of child input for the test problem. **/
	private static final int INPUT = 2;

	/**
	 * Retrieves the {@link Problem} instance
	 * that will be used for testing. Using
	 * test {@link ContestInfo} as reference.
	 * 
	 * @return Problem instance for testing.
	 * @return
	 */
	public static Problem getTestProblem() {
		final ContestInfo info = ContestInfoTest.getTestContestInfo();
		final List<Problem> problems = info.getProblems();
		final Problem problem = problems.get(0);
		return problem;
	}

	/**
	 * Ensures retrieved {@link Problem} is
	 * consistent. Uses the first {@link Problem}
	 * from {@link ContestInfo} test instance.
	 */
	@Test
	public void testProblemConsistency() {
		final Problem problem = getTestProblem();
		assertEquals(NAME, problem.getName());
		assertEquals(ID, problem.getId());
		assertEquals(TYPE, problem.getType());
		assertEquals(KEY, problem.getKey());
		assertEquals(NORMALIZED_NAME, problem.getNormalizedName());
		try {
			final String body = EclipseUtils.getResource(BODY_PATH, Jammy.getDefault().getBundle());
			assertEquals(body, problem.getBody());
		}
		catch (final IOException e) {
			fail("Error occured while retrieving test body : " + e.getMessage());
		}
		final List<ProblemInput> inputs = problem.getProblemInputs();
		assertEquals(INPUT, inputs.size());
	}

}
