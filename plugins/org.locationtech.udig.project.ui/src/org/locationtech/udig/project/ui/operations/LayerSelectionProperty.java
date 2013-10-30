/*
 *    uDig - User Friendly Desktop Internet GIS client
 *    http://udig.refractions.net
 *    (C) 2004, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 *
 */
package net.refractions.udig.project.ui.operations;

import net.refractions.udig.project.ILayer;
import net.refractions.udig.ui.operations.AbstractPropertyValue;
import net.refractions.udig.ui.operations.PropertyValue;

import org.geotools.filter.Filter;

/**
 * Checks if a layer has a selection
 * 
 * @author Jesse
 */
public class LayerSelectionProperty extends AbstractPropertyValue<ILayer>
        implements PropertyValue<ILayer> {

    public boolean canCacheResult() {
        return false;
    }

    public boolean isBlocking() {
        return false;
    }

    public boolean isTrue(ILayer object, String value) {
        Boolean hasSelection = object.getFilter() != Filter.ALL;
        return hasSelection.toString().equalsIgnoreCase(value);
    }

}
