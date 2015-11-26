package review.classdesign.jammy.ui.view;

import io.faylixe.googlecodejam.client.common.NamedObject;
import io.faylixe.googlecodejam.client.webservice.ContestInfo;
import io.faylixe.googlecodejam.client.webservice.Problem;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;
import org.eclipse.ui.part.ViewPart;

import review.classdesign.jammy.core.Jammy;
import review.classdesign.jammy.core.command.OpenSolverCommand;
import review.classdesign.jammy.core.common.EclipseUtils;
import review.classdesign.jammy.core.listener.IContestSelectionListener;
import review.classdesign.jammy.core.listener.IProblemSelectionListener;
import review.classdesign.jammy.ui.internal.FunctionalContentProvider;
import review.classdesign.jammy.ui.internal.FunctionalLabelProvider;

/**
 * View that exposes currently selected contest's available problem.
 * 
 * @author fv
 */
public final class ContestExplorer extends ViewPart implements IContestSelectionListener, ISelectionChangedListener {

	/** Identifier of this view. **/
	public static final String VIEW_ID = "review.classdesign.jammy.view.contest";

	/** Identifier of the contextual menu registered. **/
	public static final String MENU_ID = "review.classdesign.jammy.menu.contest";

	/** Menu contribution identifier. **/
	public static final String MENU_CONTRIBUTION = "contest.contribution";

	/** **/
	private final IProblemSelectionListener listener;

	/** Viewer instance this view expose. **/
	private TableViewer viewer;

	/** Current contest information. **/
	private ContestInfo contestInfo;

	/**
	 * Default constructor.
	 * Setups the required listener.
	 */
	public ContestExplorer() {
		this.listener = Jammy.getDefault();
	}

	/**
	 * Functional method that acts as a {@link Supplier} of available
	 * probem.
	 * 
	 * @return List of contest available.
	 */
	private List<Problem> getProblems() {
		return (contestInfo == null ? Collections.emptyList() : contestInfo.getProblems());
	}

	/**
	 * Functional method that acts as a {@link Supplier} of {@link Image}
	 * to display for a given problem instance.
	 * 
	 * @param element Problem instance to retrieve image from.
	 * @return Image instance for the given problem.
	 */
	private Image getImage(final Object element) { // NOPMD (Used as functional interface)
		final IWorkbench workbench = PlatformUI.getWorkbench();
		// TODO : Consider using problem status (info -> discovery, error -> failed, task -> succsess)
		return workbench.getSharedImages().getImage(SharedImages.IMG_OBJ_PROJECT);
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	private static String getName(final Object object) {
		if (object instanceof NamedObject) {
			return ((NamedObject) object).getName();
		}
		return null;
	}

	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent);
		viewer.setContentProvider(new FunctionalContentProvider(this::getProblems));
		viewer.setLabelProvider(new FunctionalLabelProvider(ContestExplorer::getName, this::getImage));
		viewer.addDoubleClickListener(event -> {
			EclipseUtils.executeCommand(OpenSolverCommand.ID);
		});
		viewer.addSelectionChangedListener(this);
		createContextualMenu();
		Jammy.getDefault().addContestSelectionListener(this);
	}

	/**
	 * Creates and registers the contextual menu associated to the internal viewer.
	 */
	private void createContextualMenu() {
		final MenuManager manager = new MenuManager();
		manager.addMenuListener(menu -> {
			menu.removeAll();
			menu.add(new GroupMarker(MENU_CONTRIBUTION));
		});
		final Menu menu = manager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(MENU_ID, manager, viewer);
	}
	
	/** {@inheritDoc} **/
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		final Object object = selection.getFirstElement();
		if (object instanceof Problem) {
			final Problem problem = (Problem) object;
			listener.problemSelected(problem);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void contestSelected(final ContestInfo contestInfo) {
		this.contestInfo = contestInfo;
		if (viewer != null && !viewer.getTable().isDisposed()) {			
			viewer.setInput(contestInfo);
			viewer.setSelection(new StructuredSelection(contestInfo.getProblems().get(0)));
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void setFocus() {
		if (viewer != null) {
			viewer.getControl().setFocus();
		}
	}

}
