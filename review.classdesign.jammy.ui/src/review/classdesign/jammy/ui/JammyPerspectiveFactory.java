package review.classdesign.jammy.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import review.classdesign.jammy.ui.view.ContestExplorer;
import review.classdesign.jammy.ui.view.ProblemView;
import review.classdesign.jammy.ui.view.SubmissionView;

/**
 * Factory that is in charge of creating Jammy Perspective.
 * 
 * @author fv
 */
public final class JammyPerspectiveFactory implements IPerspectiveFactory {

	/** Identifier used for view section. **/
	private static final String VIEW_ID = "views";

	/** Identifier used for navigator section. **/
	private static final String NAVIGATOR_ID = "navigator";

	/** {@inheritDoc} **/
	@Override
	public void createInitialLayout(final IPageLayout layout) {
		addView(layout);
		addViewShortcut(layout);
	}

	/**
	 * Adds view associated to the created perspective.
	 * 
	 * @param layout Layout instance to use for configuring created perspective.
	 */
	private void addView(final IPageLayout layout) {
		final String editor = layout.getEditorArea();
		// Create left views.
		final IFolderLayout left = layout.createFolder(NAVIGATOR_ID, IPageLayout.LEFT, 0.26f, editor);
		left.addView(ContestExplorer.VIEW_ID);
		left.addView(SubmissionView.ID);
		// Create bottom views.
		final IFolderLayout bottom = layout.createFolder(VIEW_ID, IPageLayout.BOTTOM, 0.60f, editor);
		bottom.addView(ProblemView.ID);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
	}

	/**
	 * Adds view shortcut associated to the created perspective.
	 * 
	 * @param layout Layout instance to use for configuring created perspective.
	 */
	private void addViewShortcut(final IPageLayout layout) {
		layout.addShowViewShortcut(ContestExplorer.VIEW_ID);
		layout.addShowViewShortcut(ProblemView.ID);
	}

}
