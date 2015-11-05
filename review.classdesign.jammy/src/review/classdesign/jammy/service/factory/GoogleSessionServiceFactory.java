package review.classdesign.jammy.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import review.classdesign.jammy.service.IGoogleSessionService;
import review.classdesign.jammy.service.internal.GoogleSessionService;

/**
 * Factory for {@link IGoogleSessionService} class.
 * 
 * @author fv
 */
public final class GoogleSessionServiceFactory extends AbstractServiceFactory {

	/** Singleton service. **/
	private final IGoogleSessionService service;

	/**
	 * Default constructor.
	 * Initializes service reference.
	 */
	public GoogleSessionServiceFactory() {
		this.service = new GoogleSessionService();
	}

	/** {@inheritDoc} **/
	@Override
	public Object create(
			@SuppressWarnings("rawtypes")
			final Class serviceInterface,
			final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		Object result = null;
		if (IGoogleSessionService.class.equals(serviceInterface)) {
			result = service;
		}
		return result;
	}

}
