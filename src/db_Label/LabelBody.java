/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package db_Label;

import java.awt.Point;
import java.util.Arrays;


import db_Shape.ShapeInfo;

public class LabelBody {
	private String type="labeltype";
	private String name="labeltype";
	private ShapeInfo shape;
	
	public LabelBody(){
	}
	public LabelBody(String type, String info, ShapeInfo shape){
		this.type=type;
		this.name=info;
		this.shape=shape;
	}
	
	public void setName(String info){
		this.name=info;
	}
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type=type;
	}
	
	public ShapeInfo getShape(){
		return shape;
	}
	public void setShape(ShapeInfo shape){
		this.shape=shape;
	}
	
	public String toString(){
		return Arrays.toString(getSerialization());
	}
	
	public String[] getSerialization() {
		if (shape.getClass().getSimpleName().equals("NoShape"))
			return null;
		String[] data = new String[shape.getPoints().size()*2+1];
		int i=0;
		for (Point p : shape.getPoints()){
			data[i]=""+p.x;
			data[i+1]=""+p.y;
			i+=2;
		}
		data[data.length-1]=name;
		return data;
	}
}
