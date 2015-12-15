package fr.faylixe.jammy.ui.wizard.submission;

import java.util.ArrayList;
import java.util.List;

import fr.faylixe.googlecodejam.client.common.NamedObject;
import fr.faylixe.googlecodejam.client.webservice.ProblemInput;
import fr.faylixe.jammy.core.service.OnlineSubmission;

/**
 * <p>Adapter class that allows a given delegate {@link ProblemInput}
 * instance to be acting as a valid {@link NamedObject}.</p>
 * 
 * @author fv
 */
public final class NamedProblemInput extends NamedObject {

	/** Serialization index. **/
	private static final long serialVersionUID = 1L;

	/** Delegate problem input instance. **/
	private final ProblemInput input;

	/**
	 * Default constructor.
	 *
	 * @param input Delegate problem input instance.
	 */
	private NamedProblemInput(final ProblemInput input) {
		super(OnlineSubmission.buildName(input));
		this.input = input;
	}
	
	/**
	 * Getter for the delegate instance.
	 * 
	 * @return Delegate problem input instance.
	 */
	public ProblemInput getProblemInput() {
		return input;
	}
	
	/**
	 * Static factory that builds adapted {@link ProblemInput} instance
	 * into valid {@link NamedProblemInput} from the given <tt>inputs</tt> list.
	 * 
	 * @param inputs Input to adapt.
	 * @return Adapted instance.
	 */
	public static List<NamedProblemInput> adapt(final List<ProblemInput> inputs) {
		final List<NamedProblemInput> adapted = new ArrayList<>(inputs.size());
		inputs.stream().map(NamedProblemInput::new).forEach(adapted::add);
		return adapted;
	}

}
