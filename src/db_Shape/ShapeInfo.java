/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package db_Shape;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;

public abstract class ShapeInfo {
	
	public abstract void setParameter(String[] data);
	public abstract void drawTo(Graphics2D g2, int width, int height, String name);
	public abstract Point getCenter();
	public abstract int getSize();
	
	public abstract LinkedList<Point> getPoints();
	//public abstract void setPoints(LinkedList<Point2D> list);
	public abstract void setPoints(Point p1, Point p2);
}
