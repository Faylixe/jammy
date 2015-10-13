package review.classdesign.jammy.ui.view;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.listener.ProblemSelectionListener;
import review.classdesign.jammy.model.Problem;

/**
 * Jammy problem view that only consists in a web browser
 * that display a {@link Problem} instance body content.
 * 
 * @author fv
 */
public final class ProblemView extends ViewPart implements ProblemSelectionListener {

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
		jammy.addProblemSelectionListener(this);
		final Optional<Problem> problem = jammy.getCurrentProblem();
		if (problem.isPresent()) {
			problemSelected(problem.get());
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
		// TODO : Normalizes text.
		browser.setText(problem.getBody());
	}

}
