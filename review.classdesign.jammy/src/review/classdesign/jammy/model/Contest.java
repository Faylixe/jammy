package review.classdesign.jammy.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import review.classdesign.jammy.common.Constants;
import review.classdesign.jammy.common.NamedEntity;
import review.classdesign.jammy.common.RequestUtils;

/**
 * POJO class that represents a Google Jam {@link Contest}.
 * A {@link Contest} is represented by a title and a
 * collection of {@link Round}.
 * 
 * @author fv
 */
public final class Contest extends NamedEntity {

	/** {@link Round} that belongs to this contest. **/
	private final List<Round> rounds;

	/**
	 * Default constructor.
	 * 
	 * @param name Title of this contest.
	 * @param rounds {@link Round} that belongs to this contest.
	 */
	private Contest(final String name, final List<Round> rounds) {
		super(name);
		this.rounds = rounds;
	}
	
	/**
	 * Getter that returns a immutable view
	 * of the {@link Round} list.
	 * 
	 * @return List of the round of this contest.
	 * @see Collections#unmodifiableList(List)
	 */
	public List<Round> getRounds() {
		return Collections.unmodifiableList(rounds);
	}

	/**
	 * Static factory method that retrieves contest title
	 * from a given HTML contest element.
	 * 
	 * @param element JSoup element to retrieve title from.
	 * @return Optional reference of a contest title.
	 */
	private static Optional<String> getTitle(final Element element) {
		final Elements candidates = element.getElementsByTag(Constants.HTML.H3);
		if (!candidates.isEmpty()) {
			return Optional.of(candidates.first().text());
		}
		return Optional.empty();
	}

	/**
	 * Static factory method that retrieves all contest available
	 * on Google Jam index page.
	 * 
	 * @return List of contest found.
	 * @throws Exception If any error occurs while retrieving or parsing document.
	 */
	public static List<Contest> get() throws Exception {
		final Document document = Jsoup.parse(RequestUtils.get(Constants.CONTEST_INDEX));
		final Elements years = document.getElementsByClass(Constants.CONTEST_CLASS_NAME);
		final List<Contest> contests = new ArrayList<Contest>(years.size());
		for (final Element contest : years) {
			getTitle(contest).ifPresent(title -> {
				contests.add(new Contest(title, Round.getRounds(contest)));
			});
		}
		return contests;
	}

}
