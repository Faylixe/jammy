package review.classdesign.jammy.ui.view;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.handler.ContestSelectionHandler;
import review.classdesign.jammy.listener.ProblemSelectionListener;
import review.classdesign.jammy.model.Problem;

/**
 * Jammy problem view that only consists in a web browser
 * that display a {@link Problem} instance body content.
 * 
 * TODO : Add menu bar with a reloading button.
 * @author fv
 */
public final class ProblemView extends ViewPart implements ProblemSelectionListener, LocationListener {

	/** Link used for triggering {@link ProblemSelectionHandler}. **/
	private static final String ACTION_URL = "action://problem.selection.handler";

	/** HTML content displayed when problem is not selected. **/
	private static final String CONTEST_NOT_SELECTED_CONTENT = "<a href=\"" + ACTION_URL + "\">Please select a contest first.</a>";

	/** View ID.**/
	public static final String ID = "review.classdesign.jammy.view.problem";

	/**
	 * Optional reference to the internal browser displayed.
	 * This reference is initialized when control are created
	 * through the {@link #createPartControl(Composite)} method.
	 */
	private Browser browser;

	/** {@inheritDoc} **/
	@Override
	public void createPartControl(final Composite parent) {
		final Jammy jammy = Jammy.getDefault();
		browser = new Browser(parent, SWT.NONE);
		browser.addLocationListener(this);
		jammy.addProblemSelectionListener(this);
		final Optional<Problem> problem = jammy.getCurrentProblem();
		if (problem.isPresent()) {
			problemSelected(problem.get());
		}
		else {
			browser.setText(CONTEST_NOT_SELECTED_CONTENT);
		}
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
			ContestSelectionHandler.execute();
			event.doit = false;
			// TODO : Implements browser redirection.
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void changed(final LocationEvent event) {
		// Do nothing.
	}

}
