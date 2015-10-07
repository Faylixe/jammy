package review.classdesign.jammy.ui.wizard.contest;

import java.util.function.Consumer;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.model.Contest;
import review.classdesign.jammy.ui.common.FunctionalLabelProvider;

/**
 * Page implementation for {@link Contest} selection.
 * 
 * @author fv
 */
public final class ContestWizardPage extends WizardPage implements ISelectionChangedListener {

	/** Page name. **/
	private static final String NAME = "Round selection";

	/** Page description. **/
	private static final String DESCRIPTION = "Please select a Jam contest";

	/** {@link Contest} consumer that will handle selected consumer. **/
	private final Consumer<Contest> consumer;

	/**
	 * Default constructor.
	 * 
	 * @param consumer {@link Contest} consumer that will handle selected consumer.
	 */
	protected ContestWizardPage(final Consumer<Contest> consumer) {
		super(NAME);
		setDescription(DESCRIPTION);
		this.consumer = consumer;
	}

	/** {@inheritDoc} **/
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		final Contest contest = (Contest) selection.getFirstElement();
		consumer.accept(contest);
		setPageComplete(true);
	}
	
	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		final ListViewer viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		// TODO : Provide a delayed loading view.
		//viewer.setContentProvider(new FunctionalContentProvider(Contest::get));
		viewer.setLabelProvider(new FunctionalLabelProvider(null));
		viewer.setInput(Jammy.CHILDLESS);
		viewer.addSelectionChangedListener(this);
		setControl(viewer.getControl());
		setPageComplete(false);
	}

}
