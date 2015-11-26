package review.classdesign.jammy.core.listener;

import io.faylixe.googlecodejam.client.CodeJamSession;

/**
 * 
 * @author fv
 */
public interface ISessionListener {

	/**
	 * 
	 * @param session
	 */
	void sessionChanged(CodeJamSession session);

}
