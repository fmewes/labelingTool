/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package db_Label;

public class LabelHeader {
	public final String defaultComputerID=System.getProperty("user.name");
	private String computerID;
	private long timestamp;
	private String imageID;
	
	public LabelHeader(String imageID){
		this.computerID=defaultComputerID;
		this.timestamp=System.currentTimeMillis();
		this.imageID=imageID;
	}
	
	public String getComputerID() {
		return computerID;
	}
	public void setComputerID(String computerID) {
		this.computerID = computerID;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getImageID() {
		return imageID;
	}
	public void setImageID(String imageID) {
		this.imageID = imageID;
	}
}
