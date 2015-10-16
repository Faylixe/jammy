package review.classdesign.jammy.ui.view;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;
import org.eclipse.ui.part.ViewPart;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.common.EclipseUtils;
import review.classdesign.jammy.common.NamedObject;
import review.classdesign.jammy.listener.ContestSelectionListener;
import review.classdesign.jammy.model.ContestInfo;
import review.classdesign.jammy.model.Problem;
import review.classdesign.jammy.ui.internal.FunctionalContentProvider;
import review.classdesign.jammy.ui.internal.FunctionalLabelProvider;

/**
 * View that exposes currently selected contest's available problem.
 * 
 * @author fv
 */
public final class ContestExplorer extends ViewPart implements ContestSelectionListener, IDoubleClickListener, ISelectionChangedListener {

	/** **/
	private static final String PROJECT_CREATION_ERROR = "An error occurs while creating project for solver %s : %s.";

	/** Identifier of this view. **/
	public static final String ID = "review.classdesign.jammy.view.contest";

	/** Viewer instance this view expose. **/
	private TableViewer viewer;

	/** Current contest information. **/
	private ContestInfo contestInfo;

	/**
	 * Functional method that acts as a {@link Supplier} of available
	 * probem.
	 * 
	 * @return List of contest available.
	 */
	private List<Problem> getProblems() {
		return (contestInfo != null ? contestInfo.getProblems() : Collections.emptyList());
	}

	/**
	 * Functional method that acts as a {@link Supplier} of {@link Image}
	 * to display for a given problem instance.
	 * 
	 * @param element Problem instance to retrieve image from.
	 * @return Image instance for the given problem.
	 */
	private Image getImage(final Object element) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		// TODO : Consider using problem status (info -> discovery, error -> failed, task -> succsess)
		return workbench.getSharedImages().getImage(SharedImages.IMG_OBJ_PROJECT);
	}

	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		Jammy.getDefault().addContestSelectionListener(this);
		viewer = new TableViewer(parent);
		viewer.setContentProvider(new FunctionalContentProvider(this::getProblems));
		viewer.setLabelProvider(new FunctionalLabelProvider(NamedObject::getName, this::getImage));
		viewer.addDoubleClickListener(this);
		viewer.addSelectionChangedListener(this);
		final Optional<ContestInfo> contest = Jammy.getDefault().getCurrentContest();
		if (contest.isPresent()) {
			contestSelected(contest.get());
		}
	}
	
	/** {@inheritDoc} **/
	@Override
	public void doubleClick(final DoubleClickEvent event) {
		if (contestInfo != null) {
			final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			final Problem problem = (Problem) selection.getFirstElement();
			final Job job = Job.create("", monitor -> {
				try {
					final IFile file = contestInfo.getProblemFile(problem, monitor);
					if (!file.exists()) {
						problem.createSolver(file, monitor);
					}
					EclipseUtils.openFile(file);
				}
				catch (final CoreException e) {
					e.printStackTrace();
					return new Status(IStatus.ERROR, Jammy.PLUGIN_ID, String.format(PROJECT_CREATION_ERROR, problem.getName(), e.getMessage()));
				}
				return Status.OK_STATUS;
			});
			job.schedule();
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		final Object object = selection.getFirstElement();
		if (object instanceof Problem) {
			final Problem problem = (Problem) object;
			Jammy.getDefault().setCurrentProblem(problem);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void contestSelected(final ContestInfo contestInfo) {
		this.contestInfo = contestInfo;
		if (viewer != null) {
			viewer.setInput(contestInfo);
			viewer.setSelection(new StructuredSelection(contestInfo.getProblems().get(0)));
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void setFocus() {
		// Do nothing.
	}

}
