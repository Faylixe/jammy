package review.classdesign.jammy.model.submission;

import review.classdesign.jammy.model.ProblemSolver;

/**
 * 
 * @author fv
 */
public final class LocalSubmission extends AbstractSubmission {

	/** **/
	private final ProblemSolver solver;

	/**
	 * 
	 * @param solver
	 */
	public LocalSubmission(final ProblemSolver solver) {
		this.solver = solver;
	}

	

	/** {@inheritDoc} **/
	@Override
	public boolean isSuccess() {
		return false;
	}

}
