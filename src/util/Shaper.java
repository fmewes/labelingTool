/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package util;

import db_Shape.Circle;
import db_Shape.Line;
import db_Shape.NoShape;
import db_Shape.Rectangle;
import db_Shape.ShapeEnum;
import db_Shape.ShapeInfo;

public class Shaper {
	public static ShapeInfo findShape(ShapeEnum e){
		switch(e){
		case NoShape:
			return new NoShape();

		case Line:
			return new Line();

		case Circle:
			return new Circle();

		case Rectangle:
			return new Rectangle();

		default:
			return new NoShape();
		}
	}
}
