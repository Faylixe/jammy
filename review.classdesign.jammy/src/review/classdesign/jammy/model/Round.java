package review.classdesign.jammy.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import review.classdesign.jammy.common.Constants;
import review.classdesign.jammy.common.NamedEntity;
import review.classdesign.jammy.common.RequestUtils;

/**
 * 
 * @author fv
 */
public final class Round extends NamedEntity {

	/** URL of this round dashboard. **/
	private final String url;

	/**
	 * Default constructor.
	 * 
	 * @param name Title of this round.
	 * @param url URL of this round dashboard.
	 */
	private Round(final String name, final String url) {
		super(name);
		this.url = url;
	}

	/**
	 * Getter for round dashboard URL.
	 * 
	 * @return URL of this round dashboard.
	 */
	public String getURL() {
		return url;
	}
	
	/** **/
	private static final String REQUEST = "/ContestInfo";

	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public ContestInfo getInfo() throws IOException {
		final StringBuilder builder = new StringBuilder();
		builder.append(getURL());
		builder.append(REQUEST);
		final String json = RequestUtils.get(builder.toString());
		final Gson parser = new Gson();
		return parser.fromJson(json, ContestInfo.class);
	}

	/**
	 * 
	 * @param round
	 * @return
	 */
	private static Optional<Round> getRound(final Element round) {
		final Elements links = round.getElementsByTag(Constants.HTML.ANCHOR);
		if (links.isEmpty()) {
			return Optional.empty();
		}
		final Element link = links.first();
		final String title = link.text();
		final String url = Constants.HOST + link.attr(Constants.HTML.HREF);
		return Optional.of(new Round(title, url));
	}

	/**
	 * Static factory 
	 * @param contest
	 * @return
	 */
	public static List<Round> getRounds(final Element contest) {
		final Elements rows = contest.getElementsByTag(Constants.HTML.TR);
		final List<Round> rounds = new ArrayList<Round>(rows.size());
		for (final Element row : rows) {
			final Elements cells = row.getElementsByClass(Constants.DESCRIPTION_CLASS_NAME);
			if (!cells.isEmpty()) {
				final Element cell = cells.first();
				getRound(cell).ifPresent(rounds::add);
			}
		}
		return rounds;
	}

}
