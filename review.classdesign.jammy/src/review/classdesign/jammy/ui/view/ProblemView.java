package review.classdesign.jammy.ui.view;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import review.classdesign.jammy.Jammy;
import review.classdesign.jammy.JammyPreferences;
import review.classdesign.jammy.common.HTMLConstant;
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
		final String body = problem.getBody();
		final String source = format(normalize(body));
		browser.setText(source);
	}

	/** Problem HTML template. **/
	private static String TEMPLATE;

	/** Path of the template file resources used for problem display. **/
	private static final String TEMPLATE_PATH = "/problem.template.html";

	/**
	 * Normalizes the given HTML <tt>body</tt> text, by replacing
	 * images URI by absolute URI using preference hostname.
	 * 
	 * @param body HTML body to normalize.
	 * @return Normalized HTML content.
	 */
	private static String normalize(final String body) {
		final Document document = Jsoup.parse(body);
		final Elements images = document.getElementsByTag(HTMLConstant.IMG);
		for (final Element image : images) {
			final String original = image.attr(HTMLConstant.SRC);
			final String normalized = JammyPreferences.getHostname() + original.substring(1);
			images.attr(HTMLConstant.SRC, normalized);
		}
		return document.html();
	}

	/**
	 * Static method that formats the given <tt>body</tt>
	 * by using internal HTML template.
	 * 
	 * @param body Body of the text to format.
	 * @return Formatted HTML text.
	 */
	public static String format(final String body) {
		synchronized (ProblemView.class) {
			if (TEMPLATE == null) {
				// We are using another class to avoid synchronization issues.
				final Class<?> loader = Jammy.class;
				final InputStream stream = loader.getResourceAsStream(TEMPLATE_PATH);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				TEMPLATE = reader.lines().collect(Collectors.joining("\n"));
			}
		}
		return String.format(TEMPLATE, body);
	}

}
