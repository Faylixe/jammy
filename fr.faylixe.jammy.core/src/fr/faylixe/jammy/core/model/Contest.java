package fr.faylixe.jammy.core.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.faylixe.jammy.core.JammyPreferences;
import fr.faylixe.jammy.core.common.HTMLConstant;
import fr.faylixe.jammy.core.common.NamedObject;
import fr.faylixe.jammy.core.common.RequestUtils;

/**
 * POJO class that represents a Google Jam {@link Contest}.
 * A {@link Contest} is represented by a name and a
 * collection of {@link Round}.
 * 
 * @author fv
 */
public final class Contest extends NamedObject {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** URL of the contest index page. **/
	public static final String CONTEST_INDEX = "/codejam/contests.html";

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
		final Elements candidates = element.getElementsByTag(HTMLConstant.H3);
		if (!candidates.isEmpty()) {
			return Optional.of(candidates.first().text());
		}
		return Optional.empty();
	}

	/**
	 * 
	 * @param year
	 * @return
	 */
	private static Optional<Contest> createContest(final Element year) {
		final Optional<String> name = getName(year);
		if (name.isPresent()) {
			final String contestName = name.get();
			final List<Round> rounds = Round.getRounds(contestName, year);
			final Contest contest = new Contest(contestName, rounds);
			return Optional.of(contest);
		}
		return Optional.empty();
	}

	/**
	 * Static factory method that retrieves all contest available
	 * on Google Jam index page.
	 * 
	 * @return List of contest found.
	 * @throws IOException If any error occurs while retrieving or parsing document.
	 */
	public static List<Contest> get() throws IOException {
		final Document document = Jsoup.parse(RequestUtils.get(JammyPreferences.getHostname() + CONTEST_INDEX));
		final Elements years = document.getElementsByClass(CONTEST_CLASS_NAME);
		final List<Contest> contests = new ArrayList<Contest>(years.size());
		years
			.stream()
			.map(Contest::createContest)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(contests::add);
		return contests;
	}

}
