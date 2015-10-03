package review.classdesign.jammy.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author fv
 */
public final class Problem {

	/**
	 * 
	 * @param round
	 * @return
	 */
	public static List<Problem> get(final Round round) {		
		final Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("cmd", "GetProblems");
		parameters.put("contest", "contest_id_here");
		return null;
	}

}
