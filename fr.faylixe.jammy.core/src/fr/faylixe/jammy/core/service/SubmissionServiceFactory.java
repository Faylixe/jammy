package fr.faylixe.jammy.core.service;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import fr.faylixe.jammy.core.internal.submission.SubmissionService;

/**
 * Factory {@link ISubmissionService} class.
 * 
 * @author fv
 */
public final class SubmissionServiceFactory extends AbstractServiceFactory {

	/** Optional reference of our singleton service. **/
	private final ISubmissionService service;

	/**
	 * Default constructor.
	 * Initializes service reference.
	 */
	public SubmissionServiceFactory() {
		super();
		this.service = new SubmissionService();
	}

	/** {@inheritDoc} **/
	@Override
	public Object create(
			@SuppressWarnings("rawtypes")
			final Class serviceInterface,
			final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		if (ISubmissionService.class.equals(serviceInterface)) {
			return service;
		}
		return null;
	}

}
