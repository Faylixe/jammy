package review.classdesign.jammy.core.submission.internal;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.BufferedContent;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;

/**
 * {@link CompareEditorInput} implementation for comparing
 * problem output files.
 * 
 * TODO : Check for setMessage() and setAncestorLabel() content.
 * 
 * @author fv
 */
public final class SubmissionCompareEditorInput extends CompareEditorInput {

	/** Editor title. **/
	private static final String TITLE = "Dataset output comparaison";

	/** Label used for the left side of the compare editor. **/
	private static final String LEFT_LABEL = "Actual";

	/** Label used for the right side of the compare editor. **/
	private static final String RIGHT_LABEL = "Expected";

	/** Actual file. **/
	private final IFile actual;
	
	/** Expected file. **/
	private final IFile expected;

	/**
	 * Item wrapper class for compared file.
	 * 
	 * @author fv
	 */
	private static final class Element extends BufferedContent implements ITypedElement {

		/** Target file that is wrapped by this item. **/
		private final IFile file;
	
		/**
		 * Default constructor.
		 * 
		 * @param file Target file that is wrapped by this item.
		 */
		private Element(final IFile file) {
			super();
			this.file = file;
		}

		/** {@inheritDoc} **/
		@Override
		public Image getImage() {
			return null;
		}

		/** {@inheritDoc} **/
		@Override
		public String getName() {
			return file.getName();
		}

		@Override
		public String getType() {
			return ITypedElement.TEXT_TYPE;
		}

		/** {@inheritDoc} **/
		@Override
		public InputStream createStream() throws CoreException {
			return file.getContents();
		}
		
	}

	/**
	 * Default constructor.
	 * 
	 * @param configuration Editor configuration.
	 * @param actual Actual file.
	 * @param expected Expected file.
	 */
	private SubmissionCompareEditorInput(final CompareConfiguration configuration, final IFile actual, final IFile expected) {
		super(configuration);
		setTitle(TITLE);
		this.actual = actual;
		this.expected = expected;
	}

	/** {@inheritDoc} **/
	@Override
	protected Object prepareInput(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		return new DiffNode(new Element(actual), new Element(expected));
	}

	/**
	 * Static factory method that creates a {@link SubmissionCompareEditorInput} from the given
	 * <tt>actual</tt> and <tt>expected</tt> file.
	 * 
	 * @param actual Actual file to be compared.
	 * @param expected Expected file to be compared.
	 * @return Created input instance.
	 */
	public static SubmissionCompareEditorInput create(final IFile actual, final IFile expected) {
		final CompareConfiguration configuration = new CompareConfiguration();
		configuration.setLeftEditable(false);
		configuration.setRightEditable(false);
		configuration.setLeftLabel(LEFT_LABEL);
		configuration.setRightLabel(RIGHT_LABEL);
		return new SubmissionCompareEditorInput(configuration, actual, expected);
	}

}
