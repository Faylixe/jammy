package fr.faylixe.jammy.ui.internal;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import fr.faylixe.googlecodejam.client.webservice.ContestInfo;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.ui.view.ContestExplorer;

/**
 * Custom {@link ITreeContentProvider} for the {@link ContestExplorer}.
 * 
 * @author fv
 */
public final class ContestExplorerContentProvider implements ITreeContentProvider {

	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof ContestInfo) {
			final ContestInfo info = (ContestInfo) inputElement;
			final List<Problem> problems = info.getProblems();
			return problems.toArray();
		}
		return null;
	}

	/** {@inheritDoc} **/
	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof Problem) {
			final Problem problem = (Problem) parentElement;
			final List<ProblemInput> inputs = problem.getProblemInputs();
			return inputs.toArray();
		}
		return null;
	}

	/** {@inheritDoc} **/
	@Override
	public Object getParent(final Object element) {
		if (element instanceof ProblemInput) {
			final ProblemInput input = (ProblemInput) element;
			return input.getProblem();
		}
		return null;
	}

	/** {@inheritDoc} **/
	@Override
	public boolean hasChildren(final Object element) {
		return (element instanceof Problem);
	}

}
