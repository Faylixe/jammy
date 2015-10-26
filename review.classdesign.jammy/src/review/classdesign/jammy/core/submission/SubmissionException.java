package review.classdesign.jammy.core.submission;

/**
 * 
 * @author fv
 */
public final class SubmissionException extends Exception implements Runnable {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** **/
	private static final Runnable DEFAULT_RUNNABLE = () -> {};

	/** **/
	private final Runnable delegate;

	/**
	 * 
	 * @param message
	 */
	public SubmissionException(final String message) {
		super(message);
		this.delegate = DEFAULT_RUNNABLE;
	}

	/**
	 * 
	 * @param throwable
	 */
	public SubmissionException(final Throwable throwable) {
		super(throwable);
		this.delegate = DEFAULT_RUNNABLE;
	}
	
	/**
	 * 
	 * @param message
	 * @param action
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
