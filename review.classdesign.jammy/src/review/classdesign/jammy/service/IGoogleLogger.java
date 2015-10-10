package review.classdesign.jammy.service;

import java.io.IOException;

/**
 * 
 * @author fv
 */
public interface IGoogleLogger {

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	String getURL() throws IOException;

	/**
	 * 
	 * @param runnable
	 */
	void addListener(Runnable runnable);

	/**
	 * 
	 */
	void cancel();

}
