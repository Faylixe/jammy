package fr.faylixe.jammy.core.service;

import org.eclipse.jface.dialogs.MessageDialog;

import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * Exception extension for error related to solver submission.
 * Such exception are runnable when a callback action is supported.
 * 
 * @author fv
 */
public final class SubmissionException extends Exception implements Runnable {

	/**
	 * Enumeration of exception type.
	 * 
	 * @author fv
	 */
	public static enum Type {
		
		/** Error during the submission process. **/
		ERROR,

		/** Fail indicates an error in the submission data. **/
		FAIL

	}

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** **/
	private static final String ERROR_TITLE = "Submission error";

	/** Delegate runnable instance used. **/
	private final Runnable delegate;

	/** Error type. **/
	private final Type type;

	/**
	 * Message based constructor with no callback action.
	 * 
	 * @param message Error message.
	 * @param type Error type.
	 * @see Exception#Exception(String)
	 */
	public SubmissionException(final String message, final Type type) {
		super(message);
		this.delegate = () -> {
			MessageDialog.openError(
					EclipseUtils.getActiveShell(), 
					ERROR_TITLE,
					message);
		};
		this.type = type;
	}

	/**
	 * {@link Throwable} based constructor with no callback action.
	 * 
	 * @param throwable Exception wrapped by this one.
	 * @param type Error type.
	 * @see Exception#Exception(Throwable)
	 */
	public SubmissionException(final Throwable throwable, final Type type) {
		super(throwable);
		this.delegate = () -> {
			MessageDialog.openError(
					EclipseUtils.getActiveShell(),
					ERROR_TITLE, 
					throwable.getMessage());
		};
		this.type = type;
	}
	
	/**
	 * Message based constructor with callback action
	 * defined by the given <tt>delegate</tt>.
	 * 
	 * @param message Error message.
	 * @param action Delegate instance to used when {@link #run()} method is called.
	 * @param type Error type.
	 */
	public SubmissionException(final String message, final Runnable delegate, final Type type) {
		super(message);
		this.delegate = delegate;
		this.type = type;
	}

	/** {@inheritDoc} **/
	@Override
	public void run() {
		delegate.run();
	}

	/**
	 * Type getter.
	 * 
	 * @return Error type.
	 */
	public Type getType() {
		return type;
	}

}
