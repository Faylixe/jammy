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

import review.classdesign.jammy.ui.view.SubmissionView;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author fv
 */
public final class JammyUI extends AbstractUIPlugin {

	/** Plugin identifier. **/
	public static final String PLUGIN_ID = "review.classdesign.jammy.ui"; //$NON-NLS-1$

	/** Constant value used for identifying {@link SubmissionView} suite icon. **/
	public static final String IMG_SUBMISSION_SUITE = "suite";

	/** Constant value used for identifying {@link SubmissionView} test icon. **/
	public static final String IMG_SUBMISSION_TEST = "test";

	/** Constant value used for identifying {@link SubmissionView} run icon. **/
	public static final String IMG_SUBMISSION_RUN = "run";

	/** Constant value used for identifying {@link SubmissionView} fail icon. **/
	public static final String IMG_SUBMISSION_FAIL = "fail";

	/** Constant value used for identifying {@link SubmissionView} success icon. **/
	public static final String IMG_SUBMISSION_SUCCESS = "success";

	/** Path of the bundle relative folder which contains {@link SubmissionView} icons. **/
	private static final IPath ICONS_PATH = new Path("/resources/icons/tests/");

	/** Extension used for plugin icon. **/
	private static final String ICON_EXTENSION = ".gif";

	/** Unique plugin instance. **/
	private static JammyUI plugin;

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
	 * Retrieves and returns image denoted by the given <tt>name</tt>.
	 * 
	 * @param name Name of the required image.
	 * @return Retrieves image if exist, <tt>null</tt> otherwise.
	 */
	public static Image getImage(final String name) {
		return getDefault().getImageRegistry().get(name);
	}

	/** {@inheritDoc} **/
	@Override
	public void start(final BundleContext context) throws Exception { // NOPMD
		super.start(context);
		plugin = this;
	}

	/** {@inheritDoc} **/
	@Override
	public void stop(final BundleContext context) throws Exception { // NOPMD
		plugin = null; // NOPMD
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
