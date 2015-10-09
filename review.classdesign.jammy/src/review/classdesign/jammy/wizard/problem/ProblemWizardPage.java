package review.classdesign.jammy.wizard.problem;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.FunctionalContentProvider;
import review.classdesign.jammy.common.FunctionalLabelProvider;
import review.classdesign.jammy.common.NamedObject;
import review.classdesign.jammy.model.ContestInfo;
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

	/** **/
	private Problem selected;

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
		final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		final Object problem = selection.getFirstElement();
		if (problem != null) {
			selected = (Problem) problem;
			setPageComplete(true);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected Optional<Problem> getProblem() {
		return Optional.ofNullable(selected);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	private Object [] getProblems(final Object input) {
		final Optional<Round> round = Jammy.getDefault().getCurrentRound();
		if (round.isPresent()) {
			try {
				final ContestInfo info = ContestInfo.get(round.get());
				return info.getProblems().toArray();
			}
			catch (final IOException e) {
				// TODO : Handle error.
			}
		}
		return Jammy.CHILDLESS;
	
	}

	/** {@inheritDoc} **/
	@Override
	public void createControl(final Composite parent) {
		final ListViewer viewer = new ListViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new FunctionalContentProvider(this::getProblems));
		viewer.setLabelProvider(new FunctionalLabelProvider(NamedObject::getName));
		viewer.setInput(Jammy.CHILDLESS);
		viewer.addSelectionChangedListener(this);
		setControl(viewer.getControl());
		setPageComplete(false);
	}

}
