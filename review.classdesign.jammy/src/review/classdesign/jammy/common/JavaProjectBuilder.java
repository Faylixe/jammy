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
 * 
 * @author fv
 */
public class JavaProjectBuilder {

	/** **/
	private final IProject project;

	/** **/
	private final IProgressMonitor monitor;

	/**
	 * 
	 * @param project
	 * @param monitor
	 */
	private JavaProjectBuilder(final IProject project, final IProgressMonitor monitor) {
		this.project = project;
		this.monitor = monitor;
	}

	
	/**
	 * 
	 * @throws CoreException
	 */
	private void setNature() throws CoreException {
		final IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, monitor);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws CoreException 
	 */
	private IFolder createFolder(final String name) throws CoreException {
		final IFolder folder = project.getFolder(name);
		folder.create(false, true, monitor);
		return folder;
	}
	
	/**
	 * 
	 * @param sourcePath
	 * @return
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
	 * 
	 * @throws CoreException
	 */
	private void build() throws CoreException {
		final IFolder sourceFolder = createFolder("src");
		final IFolder binaryFolder = createFolder("bin");
		final IJavaProject javaProject = JavaCore.create(project);
		final IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
		javaProject.setOutputLocation(binaryFolder.getFullPath(), monitor);
		javaProject.setRawClasspath(createClasspath(root.getPath()), monitor);
	}

	/**
	 * 
	 * @param project
	 * @param monitor
	 * @throws CoreException 
	 */
	public static void build(final IProject project, final IProgressMonitor monitor) throws CoreException {
		project.create(monitor);
		project.open(monitor);
		final JavaProjectBuilder builder = new JavaProjectBuilder(project, monitor);
		builder.setNature();
		builder.build();
	}

}
