package review.classdesign.jammy.service.factory;

import java.util.Optional;

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

	/** Optional reference of our singleton service. **/
	private Optional<IGoogleSessionService> service;

	/**
	 * Default constructor.
	 * Initializes service reference.
	 */
	public GoogleSessionServiceFactory() {
		this.service = Optional.empty();
	}

	/** {@inheritDoc} **/
	@Override
	public Object create(
			@SuppressWarnings("rawtypes")
			final Class serviceInterface,
			final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		if (IGoogleSessionService.class.equals(serviceInterface)) {
			if (!service.isPresent()) {
				service = Optional.of(new GoogleSessionService());
			}
			return service.get();
		}
		return null;
	}

}
