/*
 * Created on Nov 1, 2005
 */
package org.jikespg.uide.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.uide.core.ProjectNatureBase;
import org.eclipse.uide.runtime.IPluginLog;
import org.jikespg.uide.JikesPGPlugin;

public class JikesPGNature extends ProjectNatureBase {
    public static final String	k_natureID = JikesPGPlugin.kPluginID + ".jikesPGNature";

    public String getNatureID() {
	return k_natureID;
    }

    public String getBuilderID() {
	return JikesPGBuilder.BUILDER_ID;
    }

    protected void refreshPrefs() {
	// TODO implement preferences and hook in here
    }

    public IPluginLog getLog() {
	return JikesPGPlugin.getInstance();
    }

    protected String getDownstreamBuilderID() {
	// TODO Auto-generated method stub
	return null;
    }
}
