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

public class SubmissionCompareEditorInput extends CompareEditorInput {

	/** **/
	private static final String LEFT_LABEL = "Actual";

	/** **/
	private static final String RIGHT_LABEL = "Expected";

	/**
	 * 
	 * @author fv
	 */
	private static class Item extends BufferedContent implements ITypedElement {

		/** **/
		private final IFile file;
	
		/**
		 * 
		 * @param file
		 */
		private Item(final IFile file) {
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

	/** **/
	private final IFile actual;
	
	/** **/
	private final IFile expected;

	/**
	 * 
	 * @param configuration
	 */
	private SubmissionCompareEditorInput(final CompareConfiguration configuration, final IFile actual, final IFile expected) {
		super(configuration);
		setTitle("Dataset output comparaison");
		this.actual = actual;
		this.expected = expected;
	}

	/** {@inheritDoc} **/
	@Override
	protected Object prepareInput(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		return new DiffNode(new Item(actual), new Item(expected));
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 * @return
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
