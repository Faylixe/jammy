package review.classdesign.jammy.addons.java;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import review.classdesign.jammy.common.Template;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author fv
 */
public final class JavaAddonPlugin extends AbstractUIPlugin {

	/** Plugin identifier. **/
	public static final String PLUGIN_ID = "review.classdesign.jammy.addons.java"; //$NON-NLS-1$

	/** Template to use for Java solver class file. **/
	public static final String SOLVER_TEMPLATE = Template.getTemplate("/templates/solution.template.java", JavaAddonPlugin.class);

	/** Unique plugin instance. **/
	private static JavaAddonPlugin plugin;
	
	/** Default constructor. **/
	public JavaAddonPlugin() {
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
	public static JavaAddonPlugin getDefault() {
		return plugin;
	}

}
