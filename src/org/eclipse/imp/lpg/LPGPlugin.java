package org.jikespg.uide;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.uide.runtime.UIDEPluginBase;

public class JikesPGPlugin extends UIDEPluginBase {
    public static final String kPluginID= "org.jikespg.uide";

    public JikesPGPlugin() {
    }

    public String getID() {
	return kPluginID;
    }
}
