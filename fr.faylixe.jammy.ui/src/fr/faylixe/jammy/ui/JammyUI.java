package fr.faylixe.jammy.ui;

import java.io.IOException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.ui.internal.EditorCache;
import fr.faylixe.jammy.ui.view.SubmissionView;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author fv
 */
public final class JammyUI extends AbstractUIPlugin {

	/** Plugin identifier. **/
	public static final String PLUGIN_ID = "fr.faylixe.jammy.ui"; //$NON-NLS-1$

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
	private static final String ICONS_PATH = "/icons/tests/";

	/** Extension used for plugin icon. **/
	private static final String ICON_EXTENSION = ".gif";

	/** Unique plugin instance. **/
	private static JammyUI plugin;

	/**
	 * Creates an {@link ImageDescriptor} instance
	 * for the given image <tt>name</tt>.
	 * 
	 * @return Created image descriptor.
	 * @throws IOException 
	 */
	private ImageDescriptor createImageDescriptor(final String name) {
		final String path =	new StringBuilder(ICONS_PATH)
			.append(name)
			.append(ICON_EXTENSION)
			.toString();
		final URL url = getClass().getResource(path);
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
		return getInstance().getImageRegistry().get(name);
	}

	/** {@inheritDoc} **/
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Display.getDefault().asyncExec(() -> {
			final IWorkbenchPage page = EclipseUtils.getActivePage();
			if (page != null) {
				page.addPartListener(EditorCache.getInstance());
			}
		});
	}

	/** {@inheritDoc} **/
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Getter for the plugin unique instance.
	 * Such instance could be <tt>null</tt>
	 * if the bundle has not been started.
	 * 
	 * @return Plugin instance.
	 */
	public static JammyUI getInstance() {
		return plugin;
	}

}
