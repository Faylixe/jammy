package review.classdesign.jammy.listener;

import review.classdesign.jammy.model.ContestInfo;

/** 
 * Listener that is used for notifying
 * contest selection change.
 * 
 * @author fv
 */
public interface ContestSelectionListener {

	/**
	 * Notifies that the current contextual {@link ContestInfo}
	 * instance has changed.
	 * 
	 * @param contest Newly selected contest instance.
	 */
	void contestSelected(ContestInfo contest);

}
