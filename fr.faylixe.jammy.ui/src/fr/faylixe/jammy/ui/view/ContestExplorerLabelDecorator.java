package fr.faylixe.jammy.ui.view;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.ProblemSolverFactory;

/**
 * Custom decorator for adding attempt suffix to problem input.
 * 
 * @author fv
 */
public final class ContestExplorerLabelDecorator implements ILightweightLabelDecorator {

	/** Prefix for attempt decoration. **/
	private static final String ATTEMPT_PREFIX = "[attempt: ";

	/** Factory instance for retrieving problem state. **/
	private final ProblemSolverFactory factory;

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

	/** {@inheritDoc} **/
	@Override
	public void decorate(final Object element, final IDecoration decoration) {
		if (element instanceof ProblemInput) {
			final ProblemInput input = (ProblemInput) element;
			final int attempt = factory.getProblemAttempt(input);
			if (attempt > 0) {
				final String suffix = new StringBuilder()
					.append(ATTEMPT_PREFIX)
					.append(attempt)
					.append(']')
					.toString();
				decoration.addSuffix(suffix);
			}
		}
	}

}
