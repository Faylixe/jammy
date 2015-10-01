package review.classdesign.jammy.model;

/**
 * 
 * @author fv
 */
public interface TitledEntity  {

	/**
	 * 
	 * @return
	 */
	String getTitle();

	/**
	 * 
	 * @param element
	 * @return
	 */
	public static String getText(final Object element) {
		if (element instanceof TitledEntity) {
			final TitledEntity entity = (TitledEntity) element;
			return entity.getTitle();
		}
		return null;
	}

}
