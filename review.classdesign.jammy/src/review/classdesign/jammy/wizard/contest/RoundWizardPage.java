package review.classdesign.jammy.wizard.contest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import review.classdesign.jammy.common.FunctionalContentProvider;
import review.classdesign.jammy.common.FunctionalLabelProvider;
import review.classdesign.jammy.common.NamedObject;
import review.classdesign.jammy.model.Contest;
import review.classdesign.jammy.model.Round;

/**
 * Page implementation for {@link Round} selection.
 * 
 * @author fv
 */
public final class RoundWizardPage extends WizardPage implements ISelectionChangedListener, Consumer<Contest> {

	/** Page name. **/
	private static final String NAME = "Round selection";

	/** Page description. **/
	private static final String DESCRIPTION = "Please select any round";

	/** Default capacity for a round list. **/
	private static final int ROUND_CAPACITY = 8;

	/** List of round to display. **/
	private final List<Round> rounds;

	/** Selected round. **/
	private Round selected;

	/** 
	 * Reference to the wizard list view.
	 * This reference is updated when the control
	 * is created through {@link #createControl(Composite)}
	 * method.
	 */
	private ListViewer viewer;

	/**
	 * Default constructor.
	 */
	protected RoundWizardPage() {
		super(NAME);
		setDescription(DESCRIPTION);
		rounds = new ArrayList<Round>(ROUND_CAPACITY);
	}

	/** {@inheritDoc} **/
	@Override
	public void accept(final Contest contest) {
		rounds.clear();
		rounds.addAll(contest.getRounds());
		if (viewer != null) {
			viewer.setInput(rounds);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		final Object round = selection.getFirstElement();
		if (round != null) {
			selected = (Round) round;
			setPageComplete(true);
		}
	}

	/** 
	 * Getter for the selected {@link Round}.
	 * 
	 * @return Optional instance of selected {@link Round}
	 */
	protected Optional<Round> getRound() {
		return Optional.ofNullable(selected);
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new FunctionalContentProvider(() -> rounds));
		viewer.setLabelProvider(new FunctionalLabelProvider(NamedObject::getName));
		viewer.setInput(rounds);
		viewer.addSelectionChangedListener(this);
		setControl(viewer.getControl());
		setPageComplete(false);
	}

}
