package fr.faylixe.jammy.core.service;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.ProblemSolver;

/**
 * <p>This interface represents the action of submitting
 * a {@link ProblemSolver} as a valid problem solution.
 * It consists in running the solver with an input dataset
 * and to analyze result using reference output or online
 * dataset submission service.</p>
 * 
 * @author fv
 */
public interface ISubmission {

	/**
	 * Indicates if this submission has run successfully or not.
	 * In case of local submission, it will return <tt>true</tt> if the
	 * submission output is equals of the local dataset output.
	 * In online output it will correspond to the returned output analysis.
	 * 
	 * @param monitor Monitor instance used for submission.
	 * @return <tt>true</tt> if this submission is a success, <tt>false</tt> otherwise.
	 */
	void submit(IProgressMonitor monitor) throws SubmissionException;

	/**
	 * Starts this submission.
	 * 
	 * @param monitor Monitor instance used for submission execution.
	 * @throws CoreException If any error occurs while submitting.
	 */
	void start(IProgressMonitor monitor) throws SubmissionException;

	/**
	 * Getter for the target problem solver.
	 * 
	 * @return Target problem solver this submission will work on.
	 */
	ProblemSolver getSolver();

	/**
	 * 
	 * @return
	 */
	ProblemInput getProblemInput();

	/**
	 * Returns the output file associated to this submission.
	 * Such file contains the console content from our solver execution.
	 * 
	 * @return File that contains our submission output content.
	 * @throws CoreException If any error occurs while retrieving output file.
	 */
	IFile getOutputFile() throws CoreException;

	/**
	 * Getter for this submission name.
	 * 
	 * @return Submission name.
	 */
	String getName();

	/**
	 * 
	 * @param submission
	 */
	static void runAsJob(final ISubmission submission) {
		// TODO : 	Implements ISchedulingRule in order to avoid submission conflict.
		// 			Consider down the job layer with rule to the submit() method (even with submission service).
		final Job job = Job.create("", submissionMonitor -> {
			try {
				submission.start(submissionMonitor);
			}
			catch (final SubmissionException e) {
				// TODO : Propagate error.
			}
			return Status.OK_STATUS;
		});
		//job.setRule(this);
		job.schedule();
	}

}
