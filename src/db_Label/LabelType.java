/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package db_Label;

import java.awt.Color;

import db_Shape.ShapeEnum;

public class LabelType {
	private String type;
	private String name;
	private ShapeEnum shape;
	private Color color;
	
	public LabelType(){
		this.type = "not found";
		this.name = "not found";
		this.shape = ShapeEnum.NoShape;
		this.color = Color.RED;
	}
	public LabelType(String type, String name, ShapeEnum e, Color color) {
		this.type = type;
		this.name = name;
		this.shape = e;
		this.color = color;
		//System.out.println(type+"-"+name+"-"+e+"-"+color);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ShapeEnum getShape() {
		return shape;
	}

	public void setShape(ShapeEnum shape) {
		this.shape = shape;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
