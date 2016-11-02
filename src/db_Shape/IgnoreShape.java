/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package db_Shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;

public class IgnoreShape extends ShapeInfo{
	
	public IgnoreShape(){
		
	}

	@Override
	public void setParameter(String[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawTo(Graphics2D g, int width, int height, String infoText) {
		g.setColor(new Color(255,0,0));
		g.drawLine(0,0,width,height);
		g.drawLine(width,0,0,height);
	}

	@Override
	public Point getCenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LinkedList<Point> getPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPoints(Point p1, Point p2) {
		// TODO Auto-generated method stub
		
	}

}
