package fr.faylixe.jammy.core.service;

import java.io.IOException;

/**
 * Interface for components that provides OAuth authentification
 * mecanisms using Google API.
 * 
 * @author fv
 */
public interface IGoogleLogger {

	/**
	 * Returns a valid authorization URL that can be used for
	 * authorize this application to work with google API.
	 * A local server is started in background using jetty
	 * in order to receive validation token once user has
	 * validated his identity.
	 * 
	 * @return OAuth URL.
	 * @throws IOException If any error occurs while creating URL or starting receiver server.
	 */
	String getURL() throws IOException;

	/**
	 * Adds the given <tt>runnable</tt> as a listener that will
	 * be triggered when the authorization process is finished.
	 * 
	 * @param runnable Runnable instance that will be registered as a listener.
	 */
	void addListener(Runnable runnable);

	/**
	 * Performs a cancel in the authorization process,
	 * which consists in stopping the waiting server.
	 */
	void cancel();

}
