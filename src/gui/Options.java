/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package gui;

import java.io.File;

import db_Label.LabelType;

public class Options {
	private File file;
	private File directory;
	
	private LabelType selectedLabel;
	private boolean fastMode;
	private boolean noLabel;
	private boolean saveWarning;
	private boolean scrollMode;

	public Options() {
		file=null;
		directory=new File(System.getProperty("user.home"));
		fastMode=false;
		noLabel=true;
		saveWarning=true;
		scrollMode=false;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public LabelType getSelectedLabel() {
		return selectedLabel;
	}

	public void setSelectedLabel(LabelType selectedLabel) {
		this.selectedLabel = selectedLabel;
	}

	public boolean isFastMode() {
		return fastMode;
	}

	public void setFastMode(boolean fastMode) {
		this.fastMode = fastMode;
	}

	public boolean isNoLabel() {
		return noLabel;
	}

	public void setNoLabel(boolean noLabel) {
		this.noLabel = noLabel;
	}
	
	public boolean isSaveWarning() {
		return saveWarning;
	}

	public void setSaveWarning(boolean saveWarning) {
		this.saveWarning = saveWarning;
	}
	
	public boolean getScrollMode() {
		return scrollMode;
	}

	public void setScrollMode(boolean scrollMode) {
		this.scrollMode = scrollMode;
	}
	
}
