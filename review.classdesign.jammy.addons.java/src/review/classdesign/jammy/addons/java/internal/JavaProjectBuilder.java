package review.classdesign.jammy.addons.java.internal;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

import review.classdesign.jammy.common.EclipseUtils;

/**
 * A {@link JavaProjectBuilder} provides tools for creating
 * and / or configuring Java project into the current workspace
 * instance.
 * 
 * @author fv
 */
public final class JavaProjectBuilder {

	/** Path to use as source folder. **/
	public static final String SOURCE_PATH = "src";

	/** Path to use as binary folder. **/
	private static final String BINARY_PATH = "bin";

	/** Target project contribution is made for. **/
	private final IProject project;

	/** Monitor instance used for project creation. **/
	private final IProgressMonitor monitor;

	/**
	 * Default constructor.
	 * 
	 * @param project Target java project to be created.
	 * @param monitor Monitor instance used for project creation.
	 */
	private JavaProjectBuilder(final IProject project, final IProgressMonitor monitor) {
		this.project = project;
		this.monitor = monitor;
	}

	/**
	 * Sets the internal project nature.
	 * 
	 * @throws CoreException If any error occurs while setting project nature.
	 */
	private void setNature() throws CoreException {
		final IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, monitor);
	}
	
	/**
	 * Creates and returns an array of valid java class path entry
	 * including the given <tt>sourcePath</tt> folder.
	 * 
	 * @param sourcePath Path of the source folder to integrate into the created entry list.
	 * @return Array of class path entry created.
	 */
	private IClasspathEntry [] createClasspath(final IPath sourcePath) {
		final IVMInstall installation = JavaRuntime.getDefaultVMInstall();
		final LibraryLocation [] locations = JavaRuntime.getLibraryLocations(installation);
		final IClasspathEntry [] entries = new IClasspathEntry[locations.length + 1];
		int i = 0;
		for (final LibraryLocation location : locations) {
			entries[i++] = JavaCore.newLibraryEntry(location.getSystemLibraryPath(), null, null);
		}
		entries[i] = JavaCore.newSourceEntry(sourcePath);
		return entries;
	}

	/**
	 * Creates the java project.
	 * 
	 * @throws CoreException If any error occurs while creating or configuring project.
	 */
	private void build() throws CoreException {
		final IFolder sourceFolder = EclipseUtils.getFolder(project, SOURCE_PATH, monitor);
		final IFolder binaryFolder = EclipseUtils.getFolder(project, BINARY_PATH, monitor);
		final IJavaProject javaProject = JavaCore.create(project);
		final IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
		javaProject.setOutputLocation(binaryFolder.getFullPath(), monitor);
		javaProject.setRawClasspath(createClasspath(root.getPath()), monitor);
	}
	
	/** Task name for the Java project creation. **/
	private static final String CREATE_PROJECT_TASK = "Creates Java project for current round";

	/**
	 * Creates if not exist, and configures the given
	 * <tt>project</tt> as a valid Java project instance.
	 * 
	 * @param name Name of the project to create.
	 * @param monitor Monitor instance used for project creation.
	 * @throws CoreException If any error occurs during project creation and / or configuration.
	 */
	public static IProject build(final String name, final IProgressMonitor monitor) throws CoreException {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// TODO : Append "-java" to the project name in order to avoid project name conflict between language. 
		final IProject project = workspace.getRoot().getProject(name);
		if (!project.exists()) {
			monitor.subTask(CREATE_PROJECT_TASK);
			project.create(monitor);
			project.open(monitor);
			final JavaProjectBuilder builder = new JavaProjectBuilder(project, monitor);
			builder.setNature();
			builder.build();
		}
		return project;
	}

}
