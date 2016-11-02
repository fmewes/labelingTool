/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package db_Label;

import db_Shape.IgnoreShape;

public class Ignore extends LabelBody{
	protected String type = "Ignore";
	
	public Ignore(){
		super.setType(type);
		super.setShape(new IgnoreShape());
	}

	@Override
	public String[] getSerialization() {
		return null;
	}

}
