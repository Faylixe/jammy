package review.classdesign.jammy.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author fv
 */
public final class JammyUI extends AbstractUIPlugin {

	/** Plugin identifier. **/
	public static final String PLUGIN_ID = "review.classdesign.jammy.ui"; //$NON-NLS-1$

	/** Unique plugin instance. **/
	private static JammyUI plugin;
	
	/**
	 * Default constructor.
	 */
	public JammyUI() {
		// Do nothing.
	}
	
	/** {@inheritDoc} **/
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/** {@inheritDoc} **/
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Getter for the plugin instance.
	 *
	 * @return Unique plugin instance, or <tt>null</tt> if the plugin has not been activated.
	 */
	public static JammyUI getDefault() {
		return plugin;
	}

}
