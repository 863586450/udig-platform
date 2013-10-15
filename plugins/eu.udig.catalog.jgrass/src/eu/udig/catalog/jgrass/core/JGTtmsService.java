/*
 * uDig - User Friendly Desktop Internet GIS client
 * (C) HydroloGIS - www.hydrologis.com 
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the HydroloGIS BSD
 * License v1.0 (http://udig.refractions.net/files/hsd3-v10.html).
 */
package eu.udig.catalog.jgrass.core;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import net.refractions.udig.catalog.ID;
import net.refractions.udig.catalog.IGeoResource;
import net.refractions.udig.catalog.IResolve;
import net.refractions.udig.catalog.IService;
import net.refractions.udig.catalog.IServiceInfo;
import net.refractions.udig.catalog.URLUtils;
import net.refractions.udig.catalog.IResolve.Status;
import net.refractions.udig.catalog.ui.CatalogUIPlugin;
import net.refractions.udig.catalog.ui.ISharedImages;
import net.refractions.udig.ui.graphics.AWTSWTImageUtils;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * <p>
 * The service handle for the JGrasstools tms 
 * </p>
 * 
 * @author Andrea Antonello - www.hydrologis.com
 * @since 1.3.2
 */
public class JGTtmsService extends IService {

    /** jgrass location url field */
    private URL url = null;

    /** connection params field */
    private Map<String, Serializable> params = null;

    /** metadata info field */
    private JGTtmsServiceInfo info = null;

    /** the resources members field */
    private volatile List<IResolve> mapsetMembers = null;

    /** error message field */
    private Throwable msg = null;

    private ID id;

    private File tmsPropertiesFile;

    private CoordinateReferenceSystem tmsCrs;

    public static final String EPSG_MERCATOR = "EPSG:3857";

    public JGTtmsService( Map<String, Serializable> params ) {
        this.params = params;

        // get the file url from the connection parameters
        url = (URL) this.params.get(JGTtmsServiceExtension.KEY);
        id = new ID(url);

        tmsPropertiesFile = URLUtils.urlToFile(url);
        try {
            tmsCrs = CRS.decode(EPSG_MERCATOR);
        } catch (Exception e) {
            msg = e;
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Serializable> getConnectionParams() {
        return params;
    }

    /**
     * check if the passed adaptee can resolve the file. Checks on location
     * consistency were already done in the service extention.
     */
    public <T> boolean canResolve( Class<T> adaptee ) {
        // garbage in, garbage out
        if (adaptee == null)
            return false;

        /*
         * in this case our resource is a folder, therefore of type File
         */
        return adaptee.isAssignableFrom(IServiceInfo.class) || // getInfo
                adaptee.isAssignableFrom(File.class) || super.canResolve(adaptee);
    }

    /**
     * resolve the adaptee to the location folder file
     */
    public <T> T resolve( Class<T> adaptee, IProgressMonitor monitor ) throws IOException {
        if (adaptee == null)
            return null;

        if (adaptee.isAssignableFrom(IServiceInfo.class)) {
            return adaptee.cast(getInfo(monitor));
        }
        if (adaptee.isAssignableFrom(File.class)) {
            return adaptee.cast(tmsPropertiesFile);
        }
        // bad call to resolve
        return super.resolve(adaptee, monitor);
    }

    public List< ? extends IGeoResource> resources( IProgressMonitor monitor ) throws IOException {
        // seed the potentially null field
        List<JGTtmsGeoResource> children = new ArrayList<JGTtmsGeoResource>();
        JGTtmsGeoResource resource = new JGTtmsGeoResource(this, url);
        children.add(resource);
        return children;
    }

    public URL getIdentifier() {
        return id.toURL();
    }

    public ID getID() {
        return id;
    }

    /**
     * Getter for the  {@link CoordinateReferenceSystem}.
     * 
     * @return the crs.
     */
    public CoordinateReferenceSystem getCrs() {
        return tmsCrs;
    }

    public Throwable getMessage() {
        return msg;
    }

    public Status getStatus() {
        if( mapsetMembers == null ){
            // if the folder hasn't been scanned yet
            return super.getStatus();
        }
        return Status.CONNECTED;
    }

    public File getFile() {
        return tmsPropertiesFile;
    }

    class JGTtmsServiceInfo extends IServiceInfo {
        public JGTtmsServiceInfo() {
            File serviceFile = getFile();
            String baseName = FilenameUtils.getBaseName(serviceFile.getName());
            this.title = baseName;
            this.description = "JGrassTools TMS service (" + this.title + ")";
        }

        public Icon getIcon() {
            ImageDescriptor imgD = CatalogUIPlugin.getDefault().getImageDescriptor(ISharedImages.GRID_OBJ);
            return AWTSWTImageUtils.imageDescriptor2awtIcon(imgD);
        }

        public String getTitle() {
            return title;
        }
    }

    protected IServiceInfo createInfo( IProgressMonitor monitor ) throws IOException {
        // lazy creation
        if (info == null) {
            info = new JGTtmsServiceInfo();
        }
        return info;
    }

}
