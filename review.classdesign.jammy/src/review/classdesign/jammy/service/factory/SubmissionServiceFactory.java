package review.classdesign.jammy.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import review.classdesign.jammy.service.ISubmissionService;
import review.classdesign.jammy.service.internal.SubmissionService;

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
		this.service = new SubmissionService();
	}

	/** {@inheritDoc} **/
	@Override
	public Object create(
			@SuppressWarnings("rawtypes")
			final Class serviceInterface,
			final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		Object result = null;
		if (ISubmissionService.class.equals(serviceInterface)) {
			result = service;
		}
		return result;
	}

}
