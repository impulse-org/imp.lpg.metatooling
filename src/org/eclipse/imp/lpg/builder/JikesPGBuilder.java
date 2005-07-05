package org.jikespg.uide.builder;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2004  All Rights Reserved
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jikespg.uide.views.JikesPGView;
import org.osgi.framework.Bundle;

/**
 * @author CLaffra
 */
public class JikesPGBuilder extends IncrementalProjectBuilder {
	
	String lpg;
	String outdir;
	String incdir;
    private static final String PLUGIN_ID = "org.jikespg.uide";
	
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
					if (path != null) {
						String fileName = path.toString();
						if (fileName.indexOf("/bin/") == -1 && "g".equals(path.getFileExtension())) {
							try {
								resource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
							} catch (CoreException e) {
							}
							compile(fileName, resource);
						}
					}
					return true; // visit children too
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void compile(String fileName, final IResource currentResource) {
		if (!currentResource.exists())
			return;
//		IPath projectRelativePath = currentResource.getProjectRelativePath();
        //System.out.println("JikesPG builder runs on "+currentResource.getProject()+". Compiling "+fileName);
		try {
			File includeDir = new File(fileName).getParentFile();
//			String includeDirName = includeDir.getAbsolutePath();
			String cmd[] = new String[] {
					getLpgExecutable(),
					"-list",
					"-table=java",
					"-escape=$",
					//"-scopes",		// generates GPF
					"-em",
					"-parseTable=org.jikes.lpg.runtime.*",
					"-dat-directory="+getOutputDirectory(currentResource.getProject()),
					fileName.replace('/','\\')
			};
//            System.out.println("Running: ");
//            for (int n = 0; n < cmd.length; n++) {
//                System.out.print(" "+cmd[n]);
//            }
//            System.out.println();
			Process process = Runtime.getRuntime().exec(cmd,new String[0],includeDir);
			InputStream is = process.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
//			String projectLocation = currentResource.getProject().getLocation().toOSString();
			String line = null;
			JikesPGView view = JikesPGView.getDefault();
			while ((line = in.readLine()) != null) {
				if (view != null)
					JikesPGView.println(line);
				else {
					System.out.println(line);
				}
				final String msg = line;
				if (line.startsWith("<error")) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openError(new Shell(), "JikesPG: Grammar Error", msg);
						}
					});
				}
			}
			is.close();
			is = process.getErrorStream();
			in = new BufferedReader(new InputStreamReader(is));
			while ((line = in.readLine()) != null) {
				if (view != null)
					JikesPGView.println(line);
				System.err.println(line);
			}
			new Thread() {
				public void run() {
					try {
						currentResource.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String getOutputDirectory(IProject project) throws IOException {
		if (outdir == null) {
			outdir = new File(project.getLocation().toFile(), "/src").getAbsolutePath().replace('\\','/');
        }
		return outdir;
				
	}

	String getInputDirectory(IProject project) throws IOException {
		if (incdir == null) {
			File file = new File(project.getLocation().toFile(), "/src");
			file.mkdirs();
			file.mkdir();
			incdir = file.getAbsolutePath().replace('\\','/');
		}
		return incdir;
				
	}

	String getLpgExecutable() throws IOException {
		if (lpg == null) {
            Bundle bundle = Platform.getBundle(PLUGIN_ID);
            Path path = new Path("lpg.exe");
            URL url = Platform.resolve(Platform.find(bundle, path));
			lpg = url.getFile();
			if (lpg.startsWith("/")) lpg = lpg.substring(1);
			lpg = lpg.replace('/','\\');
		}
		return lpg;
	}

	private void fullBuild(IProgressMonitor monitor) {
		System.out.println("full LPG build"); // TODO: implement full Lpg builder
	}
}