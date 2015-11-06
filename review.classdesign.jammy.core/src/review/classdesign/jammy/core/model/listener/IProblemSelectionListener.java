package review.classdesign.jammy.core.model.listener;

import review.classdesign.jammy.core.model.webservice.contest.Problem;

/**
 * Listener that is used for notifying
 * problem selection change.
 * 
 * @author fv
 */
public interface IProblemSelectionListener {

	/**
	 * Notifies that the current contextual {@link Problem}
	 * instance has changed.
	 *  
	 * @param problem Newly selected problem instance.
	 */
	void problemSelected(Problem problem);

}
