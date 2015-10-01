package review.classdesign.jammy.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

/**
 * 
 * @author fv
 */
public final class GoogleServiceFactory extends AbstractServiceFactory {

	/** {@inheritDoc} **/
	@Override
	public Object create(
			@SuppressWarnings("rawtypes")
			final Class serviceInterface,
			final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		final Object parentService = parentLocator.getService(serviceInterface);
		if (parentService == null) {
			
		}
		return null;
	}

}
