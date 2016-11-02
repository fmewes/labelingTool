/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package util;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class JDirectoryScanner {
	public List<String> fileList;
	private String[] types;
	private File root;
	protected boolean stop = false;
	private FileFilter filter = new FileFilter() {

		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			String name = file.getName();
			for(String typ:types){
				if (name.endsWith("."+typ))
					return true;
			}
			return false;
		}
	};

	public JDirectoryScanner(String path, String typs, int maxFiles) {
		types=typs.split(";");
		fileList = new ArrayList<String>();
		root = new File(path);
		treeWalk(root,maxFiles);
	}

	//frei nach der dclj FAQ (www.dclj.de)
	public final void treeWalk(File root, int maxFiles) {
		if(fileList.size()>maxFiles&&maxFiles!=0)return;
		
		File[] files = root.listFiles(filter);
		if (files == null || files.length < 1)
			return;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				treeWalk(files[i],maxFiles);
			} else {
				fileList.add(files[i].getAbsolutePath());
			}
		}
	}
}