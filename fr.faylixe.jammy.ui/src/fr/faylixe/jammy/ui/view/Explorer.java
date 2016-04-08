package fr.faylixe.jammy.ui.view;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
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
 * 
 * @author fv
 */
public final class Explorer extends CommonNavigator  implements IContestSelectionListener, ISelectionChangedListener, IProblemStateListener {

	/**
	 * 
	 */
	public Explorer() {
		super();
		Jammy.getInstance().addContestSelectionListener(this);
		ProblemSolverFactory.getInstance().addListener(this);
	}

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
		return viewer;
	}

	/** {@inheritDoc} **/
	@Override
	public void problemStateChanged() {
		final CommonViewer viewer = getCommonViewer();
		if (viewer != null) {
			viewer.refresh();
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {	
		final Object object = EclipseUtils.getFirstSelection(event.getSelection());
		if (object instanceof Problem) {
			final Problem problem = (Problem) object;
			Jammy.getInstance().setSelectedProblem(problem);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void contestSelected(final ContestInfo contest) {
		final CommonViewer viewer = getCommonViewer();
		if (viewer != null && !viewer.getTree().isDisposed()) {
			viewer.setInput(contest);
			viewer.setSelection(new StructuredSelection(contest.getProblems().get(0)));
		}
	}

}
