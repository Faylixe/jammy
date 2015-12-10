package fr.faylixe.jammy.core.listener;

import fr.faylixe.googlecodejam.client.CodeJamSession;

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
