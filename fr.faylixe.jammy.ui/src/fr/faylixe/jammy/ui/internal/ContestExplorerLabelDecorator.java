package fr.faylixe.jammy.ui.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.ui.JammyUI;

/**
 * Custom decorator for adding attempt suffix to problem input.
 * 
 * @author fv
 */
public final class ContestExplorerLabelDecorator implements ILightweightLabelDecorator {

	/** Directory path for overlay icons. **/
	private static final String OVERLAY_ICON_PATH = "/icons/overlay/";

	/** Path for error overlay icons. **/
	private static final String ERROR_PATH = OVERLAY_ICON_PATH + "error.gif";

	/** Path for error overlay icons. **/
	private static final String SUCCESS_PATH = OVERLAY_ICON_PATH + "success.gif";

	/** Prefix for attempt decoration. **/
	private static final String ATTEMPT_PREFIX = "[attempt: ";

	/** Factory instance for retrieving problem state. **/
	private final ProblemSolverFactory factory;

	/** Overlay icon for error submission. **/
	private ImageDescriptor error;

	/** Overlay icon for successfull submission. **/
	private ImageDescriptor success;

	/**
	 * Default constructor.
	 */
	public ContestExplorerLabelDecorator() {
		this.factory = ProblemSolverFactory.getInstance();
	}

	/** {@inheritDoc} **/
	@Override
	public void addListener(final ILabelProviderListener listener) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	/** {@inheritDoc} **/
	@Override
	public void removeListener(final ILabelProviderListener listener) {
		// Do nothing.
	}

	/**
	 * Loads the error overlay icon if not already. And returns it.
	 * 
	 * @return Error overlay icons.
	 */
	private synchronized ImageDescriptor getErrorOverlay() {
		if (error == null) {
			error = ImageDescriptor.createFromURL(JammyUI.class.getResource(ERROR_PATH));
		}
		return error;
	}
	
	/**
	 * Loads the success overlay icon if not already. And returns it.
	 * 
	 * @return Success overlay icons.
	 */
	private synchronized ImageDescriptor getSuccessOverlay() {
		if (success == null) {
			success = ImageDescriptor.createFromURL(JammyUI.class.getResource(SUCCESS_PATH));
		}
		return success;
	}

	/** {@inheritDoc} **/
	@Override
	public void decorate(final Object element, final IDecoration decoration) {
		if (element instanceof ProblemInput) {
			final ProblemInput input = (ProblemInput) element;
			if (!factory.isPassed(input)) {
				final int attempt = factory.getProblemAttempt(input);
				final String suffix = new StringBuilder()
					.append(' ')
					.append(ATTEMPT_PREFIX)
					.append(attempt)
					.append(']')
					.toString();
				decoration.addSuffix(suffix);
				if (attempt > 0) {
					decoration.addOverlay(getErrorOverlay(), IDecoration.BOTTOM_LEFT);
				}
			}
			else {
				decoration.addOverlay(getSuccessOverlay(), IDecoration.BOTTOM_LEFT);
			}

		}
	}

}
