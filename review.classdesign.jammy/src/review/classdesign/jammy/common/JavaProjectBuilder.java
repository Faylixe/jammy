package review.classdesign.jammy.common;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
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

/**
 * A {@link JavaProjectBuilder} provides tools for creating
 * and / or configuring Java project into the current workspace
 * instance.
 * 
 * @author fv
 */
public class JavaProjectBuilder {

	/** Path to use as source folder. **/
	public static final String SOURCE_PATH = "src";

	/** Path to use as binary folder. **/
	private static final String BINARY_PATH = "bin";

	/** Target java project to be created. **/
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
	 * Creates a folder using the given <tt>name</tt> inside the internal project.
	 * 
	 * @param name Name of the folder to be created.
	 * @return Created folder instance.
	 * @throws CoreException If any error occurs while creating folder.
	 */
	private IFolder createFolder(final String name) throws CoreException {
		final IFolder folder = project.getFolder(name);
		folder.create(false, true, monitor);
		return folder;
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
		final IFolder sourceFolder = createFolder(SOURCE_PATH);
		final IFolder binaryFolder = createFolder(BINARY_PATH);
		final IJavaProject javaProject = JavaCore.create(project);
		final IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
		javaProject.setOutputLocation(binaryFolder.getFullPath(), monitor);
		javaProject.setRawClasspath(createClasspath(root.getPath()), monitor);
	}

	/**
	 * Creates if not exist, and configures the given
	 * <tt>project</tt> as a valid Java project instance.
	 * 
	 * @param project Project instance to create and / or configures as a Java project.
	 * @param monitor Monitor instance used for project creation.
	 * @throws CoreException If any error occurs during project creation and / or configuration.
	 */
	public static void build(final IProject project, final IProgressMonitor monitor) throws CoreException {
		if (!project.exists()) {
			project.create(monitor);
		}
		project.open(monitor);
		final JavaProjectBuilder builder = new JavaProjectBuilder(project, monitor);
		builder.setNature();
		builder.build();
	}

}
