/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package db_Label;

public class LabelPackage {
	private LabelHeader head;
	private LabelBody label;
	
	public LabelPackage(LabelHeader head, LabelBody label) {
		this.head = head;
		this.label = label;
	}

	public LabelHeader getHead() {
		return head;
	}

	public void setHead(LabelHeader head) {
		this.head = head;
	}

	public LabelBody getLabel() {
		return label;
	}

	public void setLabel(LabelBody label) {
		this.label = label;
	}
}
