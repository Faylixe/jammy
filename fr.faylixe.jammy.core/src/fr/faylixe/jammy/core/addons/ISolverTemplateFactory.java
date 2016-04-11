package fr.faylixe.jammy.core.addons;

/**
 * Factory interface for retrieving solver template content.
 * 
 * @author fv
 */
public interface ISolverTemplateFactory {

	/**
	 * Creates and returns the solver template
	 * for the problem with the given <tt>name</tt>
	 * 
	 * @param name Normalized name of the problem to solve.
	 * @return Created template.
	 */
	String getTemplate(String name);

}
