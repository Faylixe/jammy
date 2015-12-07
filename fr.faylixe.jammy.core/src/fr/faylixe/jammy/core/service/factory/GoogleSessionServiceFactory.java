package fr.faylixe.jammy.core.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import fr.faylixe.jammy.core.service.IGoogleSessionService;
import fr.faylixe.jammy.core.service.internal.GoogleSessionService;

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
		super();
		this.service = new GoogleSessionService();
	}

	/** {@inheritDoc} **/
	@Override
	public Object create(
			@SuppressWarnings("rawtypes")
			final Class serviceInterface,
			final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		if (IGoogleSessionService.class.equals(serviceInterface)) {
			return service;
		}
		return null;
	}

}
