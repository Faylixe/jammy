package fr.faylixe.jammy.ui.view;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import fr.faylixe.googlecodejam.client.webservice.Problem;
import fr.faylixe.jammy.core.Jammy;
import fr.faylixe.jammy.core.common.EclipseUtils;
import fr.faylixe.jammy.core.listener.IProblemSelectionListener;
import fr.faylixe.jammy.ui.command.ContestSelectionCommand;

/**
 * Jammy problem view that only consists in a web browser
 * that display a {@link Problem} instance body content.
 * 
 * @author fv
 */
public final class ProblemView extends ViewPart implements IProblemSelectionListener, LocationListener {

	/** Link used for triggering {@link ProblemSelectionHandler}. **/
	private static final String ACTION_URL = "action://problem.selection.handler";

	/** HTML content displayed when problem is not selected. **/
	private static final String CONTEST_NOT_SELECTED_CONTENT = "<a href=\"" + ACTION_URL + "\">Please select a contest first.</a>";

	/** View ID.**/
	public static final String ID = "review.classdesign.jammy.view.problem";

	/** Menu contribution identifier. **/
	public static final String MENU_CONTRIBUTION = "problem.contribution";

	/**
	 * Optional reference to the internal browser displayed.
	 * This reference is initialized when control are created
	 * through the {@link #createPartControl(Composite)} method.
	 */
	private Browser browser;

	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		browser.addLocationListener(this);
		browser.setText(CONTEST_NOT_SELECTED_CONTENT);
		final IActionBars bars = getViewSite().getActionBars();
		final IToolBarManager manager = bars.getToolBarManager();
		manager.add(new GroupMarker(MENU_CONTRIBUTION));
		Jammy.getDefault().addProblemSelectionListener(this);
	}

	/** {@inheritDoc} **/
	@Override
	public void dispose() {
		super.dispose();
		Jammy.getDefault().removeProblemSelectionListener(this);
	}

	/** {@inheritDoc} **/
	@Override
	public void setFocus() {
		// Do nothing.
	}

	/** {@inheritDoc} **/
	@Override
	public void problemSelected(final Problem problem) {
		browser.setText(problem.getBody());
	}

	/** {@inheritDoc} **/
	@Override
	public void changing(final LocationEvent event) {
		final String location = event.location;
		if (ACTION_URL.equals(location)) {
			EclipseUtils.executeCommand(ContestSelectionCommand.ID);
			event.doit = false;
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void changed(final LocationEvent event) {
		// Do nothing.
	}

}
