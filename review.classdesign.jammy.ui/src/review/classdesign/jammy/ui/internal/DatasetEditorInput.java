package review.classdesign.jammy.ui.internal;

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
public final class DatasetEditorInput extends CompareEditorInput {

	/** Editor title. **/
	private static final String TITLE = "Dataset editor";

	/** Label used for the left side of the compare editor. **/
	private static final String LEFT_LABEL = "Input";

	/** Label used for the right side of the compare editor. **/
	private static final String RIGHT_LABEL = "Output";

	/** Dataset input file. **/
	private final IFile input;
	
	/** Dataset output file. **/
	private final IFile output;

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
	 * @param input Dataset input file.
	 * @param output Dataset output file.
	 */
	private DatasetEditorInput(final CompareConfiguration configuration, final IFile input, final IFile output) {
		super(configuration);
		setTitle(TITLE);
		this.input = input;
		this.output = output;
	}

	/** {@inheritDoc} **/
	@Override
	protected Object prepareInput(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		return new DiffNode(new Element(input), new Element(output));
	}

	/**
	 * Static factory method that creates a {@link DatasetEditorInput} from the given
	 * <tt>actual</tt> and <tt>expected</tt> file.
	 * 
	 * @param input Dataset input file to be edited.
	 * @param output Dataset output file to be edited.
	 * @return Created input instance.
	 */
	public static DatasetEditorInput create(final IFile input, final IFile output) {
		final CompareConfiguration configuration = new CompareConfiguration();
		configuration.setLeftEditable(true);
		configuration.setRightEditable(true);
		configuration.setLeftLabel(LEFT_LABEL);
		configuration.setRightLabel(RIGHT_LABEL);
		return new DatasetEditorInput(configuration, input, output);
	}

}
