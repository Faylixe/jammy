package review.classdesign.jammy;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author fv
 */
public class Activator extends AbstractUIPlugin {

	/** Plug-in ID. **/
	public static final String PLUGIN_ID = "review.classdesign.jammy"; //$NON-NLS-1$

	/** Plug-in instance. **/
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/** {@inheritDoc} **/
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/** {@inheritDoc} **/
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
