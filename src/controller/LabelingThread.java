/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package controller;

import gui.JFrameMain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import db_Label.LabelPackage;

import util.JDirectoryScanner;


public class LabelingThread implements Runnable{
	private final JFrameMain frame;
	private final CSVController csv;
	private final Visualizer vis;
	private String imageID="";
	private LinkedList<LabelPackage> labelList;
	private BufferedImage image;
	
	public LabelingThread(JFrameMain frame){
		super();
		this.frame=frame;
		csv = new CSVController(frame.getOptions().getDirectory().toString(), frame.getLabelTypeController());
		vis = new Visualizer(frame);
	}
	
	@Override
	public void run() {
		final String path = frame.getOptions().getDirectory().toString();
		final JDirectoryScanner jds=new JDirectoryScanner(path,"png",100000);
		if (jds.fileList.size()<1){
			JOptionPane.showMessageDialog(
					frame,
				    "No .png Images found!",
				    "Image Error",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		int i=0;	
		while(true){
			image=openImage(jds.fileList.get(i));
			frame.setImageName(imageID);
			labelList=new LinkedList<LabelPackage>(csv.getAnnotations(imageID));
			
			vis.showImage(image,imageID,labelList);
			boolean scroll=false;
			try {
				scroll = vis.waitForMouseEvent();
			} catch (InterruptedException e) {
				frame.setImageName("");
				vis.disposePanel();
				return;
			}
			save();
			i=calcNextImage(scroll,i,jds.fileList.size());
		}
	}
	
	private void save(){
		if (frame.getOptions().isSaveWarning()){
			if (labelList.equals(csv.getAnnotations(imageID))){
				return;
			}else{
				int out = JOptionPane.showConfirmDialog(
						frame,
					    "Do you want to save changes?",
					    "Save Dialog",
					    JOptionPane.YES_NO_OPTION);
				if (out != JOptionPane.YES_OPTION)
					return;
			}
		}
		csv.saveChanges(labelList,imageID);
	}
	
	private int calcNextImage(boolean backwards, int curIt, int size){
		int i = curIt;
		if(!backwards){
			if(i==size-1){
				i=0;
			}else{
				i++;
			}
		}else{
			if(i==0){
				i=size-1;
			}else{
				i--;
			}
		}
		return i;
	}
	
	private BufferedImage openImage(final String filename){
		BufferedImage image = null;
		try{
			File file = new File(filename);
			image = ImageIO.read(file);
			imageID = file.getName();
			csv.setFilename(file.getParent());
		}catch (Exception e){
			e.printStackTrace();
		}
		return image;
	}
	
	public void printLabel(){
		if (labelList.isEmpty())
			System.out.println("labelList => empty");
		for (LabelPackage pack : labelList){
			System.out.println(pack.getHead().getImageID()+": "+pack.getLabel().toString());
		}
	}
	
	public String printID(){
		return imageID;
	}

	public Visualizer getVis() {
		return vis;
	}
}
