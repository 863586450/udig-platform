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
package net.refractions.udig.core.filter;

import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;

@SuppressWarnings("deprecation")
class AdaptingBBOX extends AdaptingFilter<BBOX> implements BBOX {

    AdaptingBBOX( BBOX filter ) {
        super(filter);
    }

    public double getMaxX() {
        return wrapped.getMaxX();
    }

    public double getMaxY() {
        return wrapped.getMaxY();
    }

    public double getMinX() {
        return wrapped.getMinX();
    }

    public double getMinY() {
        return wrapped.getMinY();
    }

    public String getPropertyName() {
        return wrapped.getPropertyName();
    }

    public String getSRS() {
        return wrapped.getSRS();
    }

    public Expression getExpression1() {
        return wrapped.getExpression1();
    }

    public Expression getExpression2() {
        return wrapped.getExpression2();
    }

    public MatchAction getMatchAction() {
        return wrapped.getMatchAction();
    }

    @Override
    public BoundingBox getBounds() {
        return wrapped.getBounds();
    }    
}