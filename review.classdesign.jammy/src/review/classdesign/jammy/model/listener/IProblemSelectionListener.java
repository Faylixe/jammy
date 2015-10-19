package review.classdesign.jammy.model.listener;

import review.classdesign.jammy.model.webservice.Problem;

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
