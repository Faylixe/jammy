package fr.faylixe.jammy.ui.view;

import java.util.Optional;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import fr.faylixe.googlecodejam.client.webservice.ContestInfo;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.listener.IContestSelectionListener;
import fr.faylixe.jammy.core.listener.IProblemStateListener;

/**
 * Contest explorer navigator.
 * 
 * @author fv
 */
public final class ContestExplorer extends CommonNavigator implements IContestSelectionListener, ISelectionChangedListener, IProblemStateListener {

	/** View identifier. **/
	public static final String ID = "fr.faylixe.jammy.view.contest";

	/** Explorer root input. **/
	private Object input;

	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		super.dispose();
		Jammy.getInstance().removeContestSelectionListener(this);
		ProblemSolverFactory.getInstance().removeListener(this);
	}
	
	/** {@inheritDoc} **/	
	@Override
	protected CommonViewer createCommonViewer(Composite aParent) {
		final CommonViewer viewer = super.createCommonViewer(aParent);
		viewer.addSelectionChangedListener(this);
		Jammy.getInstance().addContestSelectionListener(this);
		ProblemSolverFactory.getInstance().addListener(this);
		return viewer;
	}
	
	/** {@inheritDoc} **/
	@Override
	protected Object getInitialInput() {
		return input;
	}

	/** {@inheritDoc} **/
	@Override
	public void problemStateChanged() {
		final CommonViewer viewer = getCommonViewer();
		if (viewer != null) {
			Display.getDefault().asyncExec(() -> {
				viewer.refresh();
			});
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {	
		final Optional<Problem> problem = EclipseUtils.getSelection(event.getSelection(), Problem.class);
		if (problem.isPresent()) {
			Jammy.getInstance().setSelectedProblem(problem.get());
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void contestSelected(final ContestInfo contest) {
		this.input = contest;
		final CommonViewer viewer = getCommonViewer();
		if (viewer != null && !viewer.getTree().isDisposed()) {
			viewer.setInput(contest);
			viewer.setSelection(new StructuredSelection(contest.getProblems().get(0)));
		}
	}

}
