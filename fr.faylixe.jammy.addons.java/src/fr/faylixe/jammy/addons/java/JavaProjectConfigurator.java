package fr.faylixe.jammy.addons.java;

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

import fr.faylixe.jammy.core.addons.IProjectConfigurator;
import fr.faylixe.jammy.core.common.EclipseUtils;

/**
 * {@link IProjectConfigurator} implementation for Java addon.
 * Adds Java nature for created project, and configures Java classpath.
 * 
 * @author fv
 */
public final class JavaProjectConfigurator implements IProjectConfigurator {

	/** Path to use as source folder. **/
	private static final String SOURCE_PATH = "src";

	/** Path to use as binary folder. **/
	private static final String BINARY_PATH = "bin";

	/** {@inheritDoc} 
	 * @throws CoreException **/
	@Override
	public void configure(final IProject project, final IProgressMonitor monitor) throws CoreException {
		final IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, monitor);
		final IFolder sourceFolder = EclipseUtils.getFolder(project, SOURCE_PATH, monitor);
		final IFolder binaryFolder = EclipseUtils.getFolder(project, BINARY_PATH, monitor);
		final IJavaProject javaProject = JavaCore.create(project);
		final IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
		javaProject.setOutputLocation(binaryFolder.getFullPath(), monitor);
		javaProject.setRawClasspath(createClasspath(root.getPath()), monitor);		
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

}
