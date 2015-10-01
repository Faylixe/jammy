package review.classdesign.jammy.model.contest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import review.classdesign.jammy.common.TitledEntity;

/**
 * 
 * @author fv
 */
public final class Round implements TitledEntity {

	/** **/
	private static final String ANCHOR = "a";

	/** **/
	private static final String HREF = "href";

	/** **/
	private static final String ROUND_TAG_NAME = "tr";

	/** **/
	private static final String DESCRIPTION_CLASS_NAME = "desc";

	/** URL of this round dashboard. **/
	private final String url;

	/** Title of this round. **/
	private final String title;

	/**
	 * Default constructor.
	 * 
	 * @param title Title of this round.
	 * @param url URL of this round dashboard.
	 */
	private Round(final String title, final String url) {
		this.url = url;
		this.title = title;
	}

	/**
	 * Getter for round dashboard URL.
	 * 
	 * @return URL of this round dashboard.
	 */
	public String getURL() {
		return url;
	}
	
	/** {@inheritDoc} **/
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param round
	 * @return
	 */
	private static Optional<Round> getRound(final Element round) {
		final Elements links = round.getElementsByTag(ANCHOR);
		if (links.isEmpty()) {
			return Optional.empty();
		}
		final Element link = links.first();
		final String title = link.text();
		final String url = link.attr(HREF);
		return Optional.of(new Round(title, url));
	}

	/**
	 * Static factory 
	 * @param contest
	 * @return
	 */
	public static List<Round> getRounds(final Element contest) {
		final Elements rows = contest.getElementsByTag(ROUND_TAG_NAME);
		final List<Round> rounds = new ArrayList<Round>(rows.size());
		for (final Element row : rows) {
			final Elements cells = row.getElementsByClass(DESCRIPTION_CLASS_NAME);
			if (!cells.isEmpty()) {
				final Element cell = cells.first();
				getRound(cell).ifPresent(rounds::add);
			}
		}
		return rounds;
	}

}
