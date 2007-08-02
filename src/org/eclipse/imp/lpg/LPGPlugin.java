package org.eclipse.imp.lpg.uide;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.imp.runtime.SAFARIPluginBase;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class JikesPGPlugin extends SAFARIPluginBase {
    public static final String kPluginID= "org.jikespg.uide";

    /**
     * The unique instance of this plugin class
     */
    protected static JikesPGPlugin sPlugin;

    public static JikesPGPlugin getInstance() {
        return sPlugin;
    }

    public JikesPGPlugin() {
        super();
        sPlugin= this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    protected void initializeImageRegistry(ImageRegistry reg) {
    }

    public static ImageDescriptor createImageDescriptor(Bundle bundle, IPath path) {
        URL url= Platform.find(bundle, path);
        if (url != null) {
            return ImageDescriptor.createFromURL(url);
        }
        return null;
    }

    public String getID() {
        return kPluginID;
    }
}
