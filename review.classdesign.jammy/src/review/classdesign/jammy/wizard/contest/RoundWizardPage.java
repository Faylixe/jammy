package review.classdesign.jammy.wizard.contest;

import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import review.classdesign.jammy.common.TitledEntity;
import review.classdesign.jammy.common.provider.FunctionalContentProvider;
import review.classdesign.jammy.common.provider.FunctionalLabelProvider;
import review.classdesign.jammy.model.contest.Contest;
import review.classdesign.jammy.model.contest.Round;

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

	/**
	 * Optional reference to the selected {@link Contest}.
	 * This reference is updated when this page is used
	 * as a consumer by the previous contest selection page.
	 */
	private Optional<Contest> contest;

	/**
	 * Optional reference to the selected {@link Round}.
	 * This reference is updated when user change selection.
	 */
	private Optional<Round> selected;

	/** 
	 * Optional reference to the page viewer.
	 * This reference is updated when the control
	 * are create through {@link #createControl(Composite)}
	 * method.
	 */
	private Optional<ListViewer> viewer;

	/**
	 * Default constructor.
	 */
	protected RoundWizardPage() {
		super(NAME);
		setDescription(DESCRIPTION);
		this.viewer = Optional.empty();
		this.contest = Optional.empty();
		this.selected = Optional.empty();
	}

	/** {@inheritDoc} **/
	@Override
	public void accept(final Contest contest) {
		this.contest = Optional.of(contest);
		if (viewer.isPresent()) {
			viewer.get().setInput(new Object());
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		final Round round = (Round) selection.getFirstElement();
		selected = Optional.of(round);
		setPageComplete(true);
	}

	/** 
	 * Getter for the selected {@link Round}.
	 * 
	 * @return Optional instance of selected {@link Round}
	 */
	protected Optional<Round> getRound() {
		return selected;
	}
	

	/** 
	 * Getter for the selected {@link Contest}.
	 * 
	 * @return Optional instance of selected {@link Contest}
	 */
	protected Optional<Contest> getContest() {
		return contest;
	}

	/**
	 * Functional method that is used as content provider
	 * for the round list viewer.
	 * 
	 * @param element Input element provided by the {@link ITreeContentProvider}.
	 * @return Array of {@link Round} object.
	 */
	private Object[] getRound(final Object element) {
		if (contest.isPresent()) {
			return contest.get().getRounds().toArray();
		}
		return ContestWizard.CHILDLESS;
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		final ListViewer viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new FunctionalContentProvider(this::getRound));
		viewer.setLabelProvider(new FunctionalLabelProvider(TitledEntity::getText));
		viewer.setInput(ContestWizard.CHILDLESS);
		viewer.addSelectionChangedListener(this);
		setControl(viewer.getControl());
		setPageComplete(false);
		this.viewer = Optional.of(viewer);
	}

}
