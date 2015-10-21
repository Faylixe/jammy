package review.classdesign.jammy.model.submission;

/**
 * 
 * @author fv
 */
public final class SubmissionException extends Exception implements Runnable {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** **/
	private final Runnable delegate;

	/**
	 * 
	 * @param message
	 */
	public SubmissionException(final String message) {
		this(message, () -> {});
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
