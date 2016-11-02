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

public class Circle extends ShapeInfo{
	private Point p1;
	private Point p2;
	
	public Circle(){
		this.p1 = new Point(0,0);
		this.p2 = new Point(0,0);
	}
	
	@Override
	public void setParameter(String[] data) {
		int x1=Integer.parseInt(data[0]);
		int y1=Integer.parseInt(data[1]);
		int x2=Integer.parseInt(data[2]);
		int y2=Integer.parseInt(data[3]);
		p1=new Point(x1,y1);
		p2=new Point(x2,y2);
	}
	
	@Override
	public void drawTo(Graphics2D g, int width, int height, String name) {
		Point mp=getCenter();
		int len=getSize();
		g.drawOval(mp.x-len/2,mp.y-len/2,len,len);
		//show infoText (int width, int height, String infoText)
		mp.y=mp.y-len/2-1;
		if(mp.x<10)mp.x=10;
		if(mp.y<10)mp.y=10;
		if(mp.x>=width-30)mp.x=width-30;
		if(mp.y>=height-15)mp.y=height-15;
		g.drawString(name,mp.x,mp.y);
	}

	@Override
	public Point getCenter() {
		int x=(p2.x+p1.x)/2;
		int y=(p2.y+p1.y)/2;
		return new Point(x,y);
	}

	@Override
	public int getSize() {
		int dx=p2.x-p1.x;
		int dy=p2.y-p1.y;
		int len=(int)Math.sqrt(dx*dx+dy*dy);
		return len;
	}

	@Override
	public LinkedList<Point> getPoints() {
		LinkedList<Point> list = new LinkedList<Point>();
		list.add(p1);
		list.add(p2);
		return list;
	}

	/*@Override
	public void setPoints(LinkedList<Point> list) {
		this.p1 = list.get(0);
		this.p2 = list.get(1);
	}*/

	@Override
	public void setPoints(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
}
