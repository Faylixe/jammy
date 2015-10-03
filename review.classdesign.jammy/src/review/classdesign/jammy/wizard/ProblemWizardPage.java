package review.classdesign.jammy.wizard;

import java.util.List;
import java.util.Optional;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.TitledEntity;
import review.classdesign.jammy.common.provider.FunctionalContentProvider;
import review.classdesign.jammy.common.provider.FunctionalLabelProvider;
import review.classdesign.jammy.model.Problem;
import review.classdesign.jammy.model.Round;

/**
 * 
 * @author fv
 */
public final class ProblemWizardPage extends WizardPage implements ISelectionChangedListener {

	/** Page name. **/
	private static final String NAME = "Problem selection";

	/** Page description. **/
	private static final String DESCRIPTION = "Please select a problem :";
	
	/**
	 * Default constructor.
	 */
	public ProblemWizardPage() {
		super(NAME);
		setDescription(DESCRIPTION);
	}

	/** {@inheritDoc} **/
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		setPageComplete(true);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	private Object [] getProblems(final Object input) {
		final Optional<Round> round = Jammy.getDefault().getCurrentRound();
		if (round.isPresent()) {
			final List<Problem> problems = Problem.get(round.get());
			return problems.toArray();
		}
		return Jammy.CHILDLESS;
	
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		final ListViewer viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new FunctionalContentProvider(this::getProblems));
		viewer.setLabelProvider(new FunctionalLabelProvider(TitledEntity::getText));
		viewer.setInput(Jammy.CHILDLESS);
		viewer.addSelectionChangedListener(this);
		setControl(viewer.getControl());
		setPageComplete(false);
	}

}
