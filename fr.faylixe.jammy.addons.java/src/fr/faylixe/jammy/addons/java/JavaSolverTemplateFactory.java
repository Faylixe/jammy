package fr.faylixe.jammy.addons.java;

import fr.faylixe.jammy.core.addons.ISolverTemplateFactory;

/**
 * 
 * @author fv
 */
public final class JavaSolverTemplateFactory implements ISolverTemplateFactory {

	/** {@inheritDoc} **/
	@Override
	public String getTemplate(final String name) {
		final Object [] solvers = new Object[4];
		for (int i = 0; i < 4; i++) {
			solvers[i] = name;
		}
		return String.format(JavaAddonPlugin.getDefault().getSolverTemplate(), solvers);	
	}

}
