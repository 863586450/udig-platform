  /* uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.tools.edit.behaviour;

import org.locationtech.udig.project.ui.render.displayAdapter.MapMouseEvent;
import org.locationtech.udig.tools.edit.EditState;
import org.locationtech.udig.tools.edit.EditToolHandler;
import org.locationtech.udig.tools.edit.EventType;
import org.locationtech.udig.tools.edit.support.EditBlackboard;
import org.locationtech.udig.tools.edit.support.Point;
import org.locationtech.udig.tools.edit.support.PrimitiveShape;
import org.locationtech.udig.tools.edit.support.Selection;

/**
 * <p>
 * Requirements:
 * <ul>
 * <li>Current Geom != null</li>
 * <li>Cursor within geom</li>
 * <li>ctrl-alt keys are down (unless all vertices are selected)</li>
 * </ul>
 * </p>
 * <p>
 * Action:
 * <ul>
 * <li>Move entire Geometry</li>
 * <li>Locks the EditTool handler until mouse is released so other behaviours won't interfere</li>
 * </ul>
 * </p>
 * 
 * @author jones
 * @since 1.1.0
 */
public class MoveGeometryBehaviour extends MoveVertexBehaviour {

    @Override
    public boolean isValid( EditToolHandler handler, MapMouseEvent e, EventType eventType ) {
        boolean isLegalState = handler.getCurrentState() == EditState.MODIFYING
                || handler.getCurrentState() == EditState.NONE
                || handler.getCurrentState() == EditState.MOVING;
        boolean isEventDragged = eventType == EventType.DRAGGED;
        boolean onlyButton1IsDown = (e.buttons ^ MapMouseEvent.BUTTON1) == 0;
        boolean currentGeomNotNull = handler.getCurrentGeom() != null;

        if( !( isLegalState && isEventDragged && onlyButton1IsDown
                && currentGeomNotNull) ) return false;

        boolean altDown = e.isAltDown() && e.isModifierDown(MapMouseEvent.CTRL_DOWN_MASK) || allVerticesSelectedAndWithinGeom(handler) ||  handler.isLockOwner(this);
        
        return  altDown && ( handler.isLockOwner(this) ||
                handler.getCurrentShape().contains(Point.valueOf(e.x, e.y), true) );
    }

    private boolean allVerticesSelectedAndWithinGeom( EditToolHandler handler ) {
        PrimitiveShape currentShape = handler.getCurrentShape();
        Selection selection = handler.getCurrentGeom().getEditBlackboard().getSelection();

        Point dragStarted = handler.getMouseTracker().getDragStarted();
        if( dragStarted==null )
            return false;
        for( Point point : currentShape ) {
            if( ! selection.contains(point) )
                return false;
        }
        
        return currentShape.contains(dragStarted, true);
        
    }
    @Override
    protected Selection getPointsToMove( EditToolHandler handler, EditBlackboard blackboard ) {
        return handler.getCurrentGeom().createSelection();
    }
    
    @Override
    protected boolean isSnappingValid() {
        return false;
    }
}
