package review.classdesign.jammy.listener;

import review.classdesign.jammy.model.Round;

/** 
 * Listener that is used for notifying
 * round selection change.

 * 
 * @author fv
 */
public interface RoundSelectionListener {

	/**
	 * Notifies that the current contextual {@link Round}
	 * instance has changed.
	 * 
	 * @param round Newly selected round instance.
	 */
	void roundSelected(final Round round);

}
