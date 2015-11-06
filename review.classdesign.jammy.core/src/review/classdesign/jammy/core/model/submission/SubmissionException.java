package review.classdesign.jammy.core.model.submission;

/**
 * Exception extension for error related to solver submission.
 * Such exception are runnable when a callback action is supported.
 * 
 * @author fv
 */
public final class SubmissionException extends Exception implements Runnable {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Default runnable instance used as delegate which does nothing. **/
	private static final Runnable DEFAULT_RUNNABLE = () -> {};

	/** Delegate runnable instance used. **/
	private final Runnable delegate;

	/**
	 * Message based constructor with no callback action.
	 * 
	 * @param message Error message.
	 * @see Exception#Exception(String)
	 */
	public SubmissionException(final String message) {
		super(message);
		this.delegate = DEFAULT_RUNNABLE;
	}

	/**
	 * {@link Throwable} based constructor with no callback action.
	 * 
	 * @param throwable Exception wrapped by this one.
	 * @see Exception#Exception(Throwable)
	 */
	public SubmissionException(final Throwable throwable) {
		super(throwable);
		this.delegate = DEFAULT_RUNNABLE;
	}
	
	/**
	 * Message based constructor with callback action
	 * defined by the given <tt>delegate</tt>.
	 * 
	 * @param message Error message.
	 * @param action Delegate instance to used when {@link #run()} method is called.
	 */
	public SubmissionException(final String message, final Runnable delegate) {
		super(message);
		this.delegate = delegate;
	}

	/** {@inheritDoc} **/
	@Override
	public void run() {
		delegate.run();
	}

}
