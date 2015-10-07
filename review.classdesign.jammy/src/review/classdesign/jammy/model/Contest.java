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
import review.classdesign.jammy.common.NamedObject;
import review.classdesign.jammy.common.RequestUtils;

/**
 * POJO class that represents a Google Jam {@link Contest}.
 * A {@link Contest} is represented by a name and a
 * collection of {@link Round}.
 * 
 * @author fv
 */
public final class Contest extends NamedObject {

	/** Class name of the element that contains contest data. **/
	public static final String CONTEST_CLASS_NAME = "year_row";

	/** {@link Round} that belongs to this contest. **/
	private final List<Round> rounds;

	/**
	 * Default constructor.
	 * 
	 * @param name Name of this contest.
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
	 * Static factory method that retrieves contest name
	 * from a given HTML contest element.
	 * 
	 * @param element JSoup element to retrieve title from.
	 * @return Optional reference of a contest title.
	 */
	private static Optional<String> getName(final Element element) {
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
		final Elements years = document.getElementsByClass(CONTEST_CLASS_NAME);
		final List<Contest> contests = new ArrayList<Contest>(years.size());
		for (final Element contest : years) {
			getName(contest).ifPresent(name -> {
				contests.add(new Contest(name, Round.getRounds(name, contest)));
			});
		}
		return contests;
	}

}
