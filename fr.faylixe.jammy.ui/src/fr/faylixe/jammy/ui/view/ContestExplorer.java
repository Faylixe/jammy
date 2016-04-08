package fr.faylixe.jammy.ui.view;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import fr.faylixe.googlecodejam.client.webservice.ContestInfo;
import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.ProblemSolverFactory;
import fr.faylixe.jammy.core.command.OpenSolverCommand;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.listener.IContestSelectionListener;
import fr.faylixe.jammy.core.listener.IProblemStateListener;
import fr.faylixe.jammy.ui.internal.ContestExplorerContentProvider;
import fr.faylixe.jammy.ui.internal.ContestExplorerLabelProvider;

/**
 * View that exposes currently selected contest's available problem.
 * 
 * @author fv
 */
public final class ContestExplorer extends ViewPart implements IContestSelectionListener, ISelectionChangedListener, IProblemStateListener {

	/** Identifier of this view. **/
	public static final String VIEW_ID = "fr.faylixe.jammy.view.contest";

	/** Identifier of the contextual menu registered. **/
	public static final String MENU_ID = "fr.faylixe.jammy.menu.contest";

	/** Menu contribution identifier. **/
	public static final String MENU_CONTRIBUTION = "contest.contribution";

	/** Viewer instance this view expose. **/
	private TreeViewer viewer;
	
	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent);
		viewer.setContentProvider(new ContestExplorerContentProvider());
		final ILabelDecorator decorator = PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator();
		viewer.setLabelProvider(new DecoratingLabelProvider(new ContestExplorerLabelProvider(), decorator));
		viewer.addDoubleClickListener(this::onDoubleClick);
		viewer.addSelectionChangedListener(this);
		createContextualMenu();
		Jammy.getInstance().addContestSelectionListener(this);
		ProblemSolverFactory.getInstance().addListener(this);
	}

	/**
	 * Callback method that handles double click.
	 * 
	 * @param event Event that trigger this call.
	 */
	private void onDoubleClick(final DoubleClickEvent event) {
		final Object object = EclipseUtils.getFirstSelection(event.getSelection());
		if (object != null && object instanceof Problem) {
			EclipseUtils.executeCommand(OpenSolverCommand.ID);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		super.dispose();
		Jammy.getInstance().removeContestSelectionListener(this);
		ProblemSolverFactory.getInstance().removeListener(this);
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
			Jammy.getInstance().setSelectedProblem(problem);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void contestSelected(final ContestInfo contestInfo) {
		if (viewer != null && !viewer.getTree().isDisposed()) {
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

	/** {@inheritDoc} **/
	@Override
	public void problemStateChanged() {
		if (viewer != null) {
			viewer.refresh();
		}
	}

}
