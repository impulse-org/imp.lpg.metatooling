/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

package org.eclipse.imp.lpg;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.imp.runtime.PluginBase;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class LPGPlugin extends PluginBase {
    public static final String kPluginID= "org.eclipse.imp.lpg.metatooling";

    private static final String kLanguageID= "lpgMeta"; // Only used as a preference path prefix

    /**
     * The unique instance of this plugin class
     */
    protected static LPGPlugin sPlugin;

    public static LPGPlugin getInstance() {
        return sPlugin;
    }

    public LPGPlugin() {
        super();
        sPlugin= this;
    }

    public String getID() {
        return kPluginID;
    }

    @Override
    public String getLanguageID() {
        return kLanguageID;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    protected void initializeImageRegistry(ImageRegistry reg) {
    }

    public static ImageDescriptor createImageDescriptor(Bundle bundle, IPath path) {
    	// SMS 28 Apr 2008 (updated this in process of "porting" to 3.3
        //URL url= Platform.find(bundle, path);
        URL url = FileLocator.find(bundle, path, null);
        if (url != null) {
            return ImageDescriptor.createFromURL(url);
        }
        return null;
    }
}
