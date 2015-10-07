package review.classdesign.jammy.tests;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import review.classdesign.jammy.model.Contest;
import review.classdesign.jammy.model.ContestInfo;
import review.classdesign.jammy.model.Round;
import review.classdesign.jammy.service.IGoogleSessionService;

/**
 * 
 * @author fv
 */
public class GoogleTest {

	@Test
	public void test() {
		try {
			IGoogleSessionService.get().login();
			final List<Contest> contests = Contest.get();
			final Contest contest = contests.get(0);
			System.out.println("Contest selected : " + contest.getName());
			final List<Round> rounds = contest.getRounds();
			final Round round = rounds.get(0);
			System.out.println("Round selected : " + round.getName());
			final ContestInfo info = ContestInfo.get(round);
			System.out.println(info.getProblems().get(0).getName());
			System.out.println(info.getProblems().get(0).getBody());
		}
		catch (final Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
