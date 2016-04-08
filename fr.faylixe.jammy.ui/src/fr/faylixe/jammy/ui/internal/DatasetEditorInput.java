package fr.faylixe.jammy.ui.internal;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.BufferedContent;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPage;

import fr.faylixe.jammy.core.ProblemSampleDataset;
import fr.faylixe.jammy.core.ProblemSolver;
import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * {@link CompareEditorInput} implementation for comparing
 * problem output files.
 * 
 * TODO : Check for setMessage() and setAncestorLabel() content.
 * 
 * @author fv
 */
public final class DatasetEditorInput extends CompareEditorInput {

	/** Error title displayed when sample dataset can not be found. **/
	private static final String DATASET_NOT_FOUND_TITLE = "Sample dataset not found";

	/** Error message displayed when sample dataset can not be found. **/
	private static final String DATASET_NOT_FOUND_MESSAGE = "The sample dataset is not available for this problem";

	/** Editor title. **/
	private static final String TITLE = "%s - Dataset";

	/** Label used for the left side of the compare editor. **/
	private static final String LEFT_LABEL = "Input";

	/** Label used for the right side of the compare editor. **/
	private static final String RIGHT_LABEL = "Output";

	/** Dataset input file. **/
	private final IFile input;
	
	/** Dataset output file. **/
	private final IFile output;

	/** Input editor element. **/
	private Element inputElement;

	/** Output editor element. **/
	private Element outputElement;

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

		/** {@inheritDoc} **/
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
	private DatasetEditorInput(final CompareConfiguration configuration, final ProblemSolver solver) {
		super(configuration);
		final ProblemSampleDataset dataset = solver.getSampleDataset();
		this.input = dataset.getInput();
		this.output = dataset.getOutput();
		setTitle(String.format(TITLE, solver.getName()));
	}

	/** {@inheritDoc} **/
	@Override
	protected Object prepareInput(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		this.inputElement = new Element(input);
		this.outputElement = new Element(output);
		return new DiffNode(null, Differencer.NO_CHANGE, null, inputElement, outputElement);
	}

	/** {@inheritDoc} **/
	@Override
	public void saveChanges(final IProgressMonitor monitor) throws CoreException {
		if (inputElement != null) {
			input.setContents(inputElement.getContents(), true, true, monitor);
		}
		if (outputElement != null) {
			output.setContents(outputElement.getContents(), true, true, monitor);
		}
	}

	
	/**
	 * Static factory method that creates a {@link DatasetEditorInput} from the given
	 * <tt>actual</tt> and <tt>expected</tt> file.
	 * 
	 * @param solver Target solver to edit dataset for.
	 * @return Created input instance.
	 */
	public static void openFrom(final ProblemSolver solver) {
		if (solver.getSampleDataset() == null) {
			MessageDialog.openError(EclipseUtils.getActiveShell(), DATASET_NOT_FOUND_TITLE, DATASET_NOT_FOUND_MESSAGE);
			return;
		}
		final CompareConfiguration configuration = new CompareConfiguration();
		configuration.setLeftEditable(true);
		configuration.setRightEditable(true);
		configuration.setLeftLabel(LEFT_LABEL);
		configuration.setRightLabel(RIGHT_LABEL);
		configuration.setChangeIgnored(RangeDifference.CHANGE, true);
		final DatasetEditorInput editorInput = new DatasetEditorInput(configuration, solver);
		synchronized (DatasetEditorInput.class) {
			final IReusableEditor editor = EditorCache.getInstance().getEditor(editorInput);
			if (editor == null) {
				CompareUI.openCompareEditor(editorInput, true);
			}
			else {
				final IWorkbenchPage page = EclipseUtils.getActivePage();
				page.activate(editor);
			}
		}
	}
	

}
