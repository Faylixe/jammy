package fr.faylixe.jammy.core;

import org.eclipse.core.resources.IFile;

/**
 * Simple POJO class that contains a dataset which consists
 * in an input and an output file.
 * 
 * @author fv
 */
public final class ProblemSampleDataset {

	/** Dataset input file. **/
	private final IFile input;

	/** Dataset expected output file. **/
	private final IFile output;

	/**
	 * Default constructor.
	 * 
	 * @param input Dataset input file.
	 * @param output Dataset expected output file.
	 */
	public ProblemSampleDataset(final IFile input, final IFile output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * Getter for the dataset input file.
	 * 
	 * @return Dataset input file.
	 * @see #input
	 */
	public IFile getInput() {
		return input;
	}
	
	/**
	 * Getter for the dataset output file.
	 * 
	 * @return Dataset expected output file.
	 * @see #output
	 */
	public IFile getOutput() {
		return output;
	}
	
}
