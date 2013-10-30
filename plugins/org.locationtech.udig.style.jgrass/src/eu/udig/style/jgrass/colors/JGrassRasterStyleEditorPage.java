/*
 * uDig - User Friendly Desktop Internet GIS client
 * (C) HydroloGIS - www.hydrologis.com 
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the HydroloGIS BSD
 * License v1.0 (http://udig.refractions.net/files/hsd3-v10.html).
 */
package org.locationtech.udig.style.jgrass.colors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.locationtech.udig.catalog.IGeoResource;
import org.locationtech.udig.project.internal.Layer;
import org.locationtech.udig.style.sld.editor.StyleEditorPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.geotools.gce.grassraster.JGrassConstants;
import org.geotools.gce.grassraster.core.color.ColorRule;

import org.locationtech.udig.catalog.jgrass.core.JGrassMapGeoResource;
import org.locationtech.udig.catalog.jgrass.utils.JGrassCatalogUtilities;
import org.locationtech.udig.style.jgrass.JGrassrasterStyleActivator;
import org.locationtech.udig.style.jgrass.core.GrassColorTable;

public class JGrassRasterStyleEditorPage extends StyleEditorPage {

    public static String ID = "org.locationtech.udig.style.jgrass.color";
    private ColorEditor colorRulesEditor = null;
    private boolean editorSupported = false;
    private String type = "unknown";

    public JGrassRasterStyleEditorPage() {
        super();
        setSize(new Point(500, 450));
    }

    public void createPageContent( Composite parent ) {
        Layer layer = getSelectedLayer();
        IGeoResource resource = layer.getGeoResource();

        if (resource.canResolve(JGrassMapGeoResource.class)) {
            try {
                JGrassMapGeoResource grassMapGeoResource = resource.resolve(
                        JGrassMapGeoResource.class, null);
                if (grassMapGeoResource.getType().equals(JGrassConstants.GRASSBINARYRASTERMAP)) {
                    editorSupported = true;
                } else {
                    editorSupported = false;
                }
                type = grassMapGeoResource.getType();

            } catch (IOException e) {
                JGrassrasterStyleActivator.log("JGrassrasterStyleActivator problem", e); //$NON-NLS-1$
                e.printStackTrace();
            }
        } else {
            editorSupported = false;
        }

        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginBottom = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginWidth = 0;
        parent.setLayout(gridLayout);

        if (editorSupported) {
            colorRulesEditor = new ColorEditor(parent, SWT.NONE);

            String[] mapsetPathAndMapName = JGrassCatalogUtilities
                    .getMapsetpathAndMapnameFromJGrassMapGeoResource(resource);

            GrassColorTable ctable = null;
            Enumeration<ColorRule> rules = null;

            try {
                while( rules == null || !rules.hasMoreElements() ) {

                    try {
                        ctable = new GrassColorTable(mapsetPathAndMapName[0],
                                mapsetPathAndMapName[1], null);
                    } catch (IOException e1) {
                        JGrassrasterStyleActivator.log("JGrassrasterStyleActivator problem", e1); //$NON-NLS-1$
                        e1.printStackTrace();
                    }
                    rules = ctable.getColorRules();

                    // create a default color file
                    if (rules == null || !rules.hasMoreElements()) {
                        ctable.createDefaultColorRulesString(null, true);
                    }

                }
            } catch (Exception e) {
                JGrassrasterStyleActivator
                        .log(
                                "JGrassrasterStyleActivator problem: eu.hydrologis.jgrass.style.jgrassraster.colors#JGrassRasterStyleEditorPage#createPageContent", e); //$NON-NLS-1$

                e.printStackTrace();
            }

            ArrayList<Rule> listOfRules = new ArrayList<Rule>();

            while( rules.hasMoreElements() ) {
                ColorRule element = (ColorRule) rules.nextElement();

                float lowvalue = element.getLowCategoryValue();
                float highvalue = element.getLowCategoryValue() + element.getCategoryRange();
                byte[] lowcatcol = element.getColor(lowvalue);
                byte[] highcatcol = element.getColor(highvalue);

                float[] lowHigh = new float[]{lowvalue, highvalue};
                Color lowColor = new Color(Display.getDefault(), (int) (lowcatcol[0] & 0xff),
                        (int) (lowcatcol[1] & 0xff), (int) (lowcatcol[2] & 0xff));
                Color highColor = new Color(Display.getDefault(), (int) (highcatcol[0] & 0xff),
                        (int) (highcatcol[1] & 0xff), (int) (highcatcol[2] & 0xff));

                listOfRules.add(new Rule(lowHigh, lowColor, highColor, true));
            }

            colorRulesEditor.setLayer(layer);

            colorRulesEditor.setAlphaValue(ctable.getAlpha());

            colorRulesEditor.setRulesList(listOfRules);
        } else {
            Label problemLabel = new Label(parent, SWT.NONE);
            problemLabel.setText("No support for map styling of map type: \"" + type + "\"");
        }

    }

    public String getErrorMessage() {
        return null;
    }

    public String getLabel() {
        return null;
    }

    public void gotFocus() {
        System.out.println("colr got focus");

    }

    public boolean performCancel() {
        return false;
    }

    public boolean okToLeave() {
        return true;
    }

    public boolean performApply() {
        if (editorSupported) {
            colorRulesEditor.makePersistent();
        }
        return true;
    }

    public boolean performOk() {
        if (editorSupported) {
            colorRulesEditor.makePersistent();
        }
        return false;
    }

    public void refresh() {
    }

    public void dispose() {
        if (editorSupported) {
            colorRulesEditor = null;
        }
        super.dispose();
    }

    public void styleChanged( Object source ) {

    }

}
