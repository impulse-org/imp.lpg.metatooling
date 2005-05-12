package org.jikespg.uide.builder;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2004  All Rights Reserved
 */

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author CLaffra
 */
public class XmlBuilder extends IncrementalProjectBuilder {
	
	static String lpg;
	static String outdir;
	static String incdir;
	
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) {
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
		try {
			delta.accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) {
					IResource resource = delta.getResource();
                    if (!resource.getProject().equals(getProject()))
                        return false;
					IPath path = resource.getRawLocation();
                    IProject project = resource.getProject();
					if (path != null) {
						String fileName = path.toString();
						if (fileName.indexOf("/bin/") != -1 && "xml".equals(path.getFileExtension())) {
							try {
								processXmlFile(project, fileName);
								project.refreshLocal(IResource.DEPTH_INFINITE, null);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					return true; // visit children too
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void processXmlFile(IProject project, final String fileName) {
		try {
            System.out.println("XML builder runs on "+getProject());
            System.out.println("Process LPG output in "+fileName);
			XmlOutputParser.parse(project, fileName);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void fullBuild(IProgressMonitor monitor) {
		System.out.println("full XML build");  // TODO: implement full XML builder
	}
}