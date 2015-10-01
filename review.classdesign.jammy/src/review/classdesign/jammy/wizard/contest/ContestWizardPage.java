package review.classdesign.jammy.wizard.contest;

import java.util.function.Consumer;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import review.classdesign.jammy.model.JammyContext;
import review.classdesign.jammy.model.TitledEntity;
import review.classdesign.jammy.model.contest.Contest;
import review.classdesign.jammy.model.provider.FunctionalContentProvider;
import review.classdesign.jammy.model.provider.FunctionalLabelProvider;

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
		viewer.setContentProvider(new FunctionalContentProvider(JammyContext::getContests));
		viewer.setLabelProvider(new FunctionalLabelProvider(TitledEntity::getText));
		viewer.setInput(ContestWizard.CHILDLESS);
		viewer.addSelectionChangedListener(this);
		setControl(viewer.getControl());
		setPageComplete(false);
	}

}
