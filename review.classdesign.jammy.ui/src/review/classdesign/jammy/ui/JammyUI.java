package review.classdesign.jammy.ui;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
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

	/** **/
	public static final String IMG_SUBMISSION_SUITE = "suite";

	/** **/
	public static final String IMG_SUBMISSION_TEST = "test";

	/** **/
	public static final String IMG_SUBMISSION_RUN = "run";

	/** **/
	public static final String IMG_SUBMISSION_FAIL = "fail";

	/** **/
	public static final String IMG_SUBMISSION_SUCCESS = "success";

	/** **/
	private static final IPath ICONS_PATH = new Path("/resources/icons/tests/");

	/** **/
	private static final String ICON_EXTENSION = ".gif";

	/** Unique plugin instance. **/
	private static JammyUI plugin;
	
	/**
	 * Default constructor.
	 */
	public JammyUI() {
		// Do nothing.
	}

	/**
	 * Creates an {@link ImageDescriptor} instance
	 * for the given image <tt>name</tt>.
	 * 
	 * @return Created image descriptor.
	 */
	private ImageDescriptor createImageDescriptor(final String name) {
		final IPath path = ICONS_PATH.append(name + ICON_EXTENSION);
		final URL url = FileLocator.find(getBundle(), path, null);
		return ImageDescriptor.createFromURL(url);
	}

	/** {@inheritDoc} **/
	@Override
	protected void initializeImageRegistry(final ImageRegistry registry) {
		super.initializeImageRegistry(registry);
		registry.put(IMG_SUBMISSION_SUITE, createImageDescriptor(IMG_SUBMISSION_SUITE));
		registry.put(IMG_SUBMISSION_TEST, createImageDescriptor(IMG_SUBMISSION_TEST));
		registry.put(IMG_SUBMISSION_RUN, createImageDescriptor(IMG_SUBMISSION_RUN));
		registry.put(IMG_SUBMISSION_FAIL, createImageDescriptor(IMG_SUBMISSION_FAIL));
		registry.put(IMG_SUBMISSION_SUCCESS, createImageDescriptor(IMG_SUBMISSION_SUCCESS));
	}

	/**
	 * 
	 * @param symbolicName
	 * @return
	 */
	public static Image getImage(final String name) {
		return getDefault().getImageRegistry().get(name);
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
