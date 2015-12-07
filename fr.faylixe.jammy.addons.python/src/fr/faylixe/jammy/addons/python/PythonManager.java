package fr.faylixe.jammy.addons.python;

import fr.faylixe.jammy.core.addons.AbstractProcessManager;

/**
 * 
 * @author fv
 */
public final class PythonManager extends AbstractProcessManager {

	/** **/
	private static final String SOLVER_EXTENSION = ".py";

	/** {@inheritDoc} **/
	@Override
	public String getTemplate(final String name) {
		return "";
	}

	/** {@inheritDoc} **/
	@Override
	public String getExtension() {
		return SOLVER_EXTENSION;
	}

	/** {@inheritDoc} **/
	@Override
	protected String getCommand() {
		return "python";
	}

}
