/*
 *    uDig - User Friendly Desktop Internet GIS client
 *    http://udig.refractions.net
 *    (C) 2012, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package net.refractions.udig.mapgraphic.internal;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.refractions.udig.catalog.ServiceExtension;

public class MapGraphicServiceExtension implements ServiceExtension {
    /**
     * Key used to record the URL indicating a local map graphic service.
     */
	public static final String KEY = "graphic"; //$NON-NLS-1$
	
    /**
     * TODO summary sentence for createService ...
     * 
     * @see net.refractions.udig.catalog.ServiceExtension#createService(java.net.URL, java.util.Map)
     * @param id
     * @param params
     * @return
     */
    public MapGraphicService createService( URL id, Map<String,Serializable> params ) {
        if( params.containsKey(KEY)){
            return new MapGraphicService();
        }
        return null;
    }

    /**
     * TODO summary sentence for createParams ...
     * 
     * @see net.refractions.udig.catalog.ServiceExtension#createParams(java.net.URL)
     * @param url
     * @return
     */
    public Map<String,Serializable> createParams( URL url ) {
        if( url != null && url.toExternalForm().startsWith( MapGraphicService.SERVICE_URL.toExternalForm())){
            Map<String,Serializable> map = new HashMap<String,Serializable>();
            map.put( KEY, url );
            return map;            
        }
        return null;
    }
    
}