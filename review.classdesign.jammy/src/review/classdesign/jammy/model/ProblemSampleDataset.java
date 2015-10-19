package review.classdesign.jammy.model;

import org.eclipse.core.resources.IFile;

/**
 * 
 * @author fv
 */
public final class ProblemSampleDataset {

	/** **/
	private final IFile input;

	/** **/
	private final IFile output;

	/**
	 * 
	 * @param input
	 * @param output
	 */
	public ProblemSampleDataset(final IFile input, final IFile output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * 
	 * @return
	 */
	public IFile getInput() {
		return input;
	}
	
	/**
	 * 
	 * @return
	 */
	public IFile getOutput() {
		return output;
	}
	
}
