/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004-2012, Refractions Research Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 * 
 */
package net.refractions.udig.project.internal;

import java.util.List;
import net.refractions.udig.project.IFolder;

import net.refractions.udig.project.ILegendItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Folder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.refractions.udig.project.internal.Folder#getItems <em>Items</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.refractions.udig.project.internal.ProjectPackage#getFolder()
 * @model superTypes="net.refractions.udig.project.internal.IFolder net.refractions.udig.project.internal.LegendItem"
 * @generated
 */
public interface Folder extends IFolder, LegendItem {

    /**
     * Returns the value of the '<em><b>Items</b></em>' containment reference list.
     * The list contents are of type {@link net.refractions.udig.project.ILegendItem}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Items</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Items</em>' containment reference list.
     * @see net.refractions.udig.project.internal.ProjectPackage#getFolder_Items()
     * @model type="net.refractions.udig.project.internal.ILegendItem" containment="true"
     * @generated
     */
    List<ILegendItem> getItems();
} // Folder
