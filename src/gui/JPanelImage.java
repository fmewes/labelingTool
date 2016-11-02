/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;

import controller.LabelTypeController;

import db_Label.LabelPackage;
import db_Label.LabelType;


@SuppressWarnings("serial")
public class JPanelImage extends JPanel{
	private Dimension preferredSize;
	public BufferedImage img;
	public BufferedImage shapeLayer;
	private String imageID;
	private LinkedList<LabelPackage> labelList;
	private final LabelTypeController ltCtrl;
	
	public JPanelImage(int width, int height, LabelTypeController ltCtrl){
		super();
		this.ltCtrl=ltCtrl;
		img=new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		preferredSize=new Dimension(width,height);
	}
	
	@Override
	public void paint(Graphics g){
	    BufferedImage shapeLayer = copyImage(img);
	    drawShapes(shapeLayer.createGraphics());
	    g.drawImage(shapeLayer, 0, 0, null);
	}

	@Override
	public Dimension getPreferredSize(){
		return preferredSize;
	}

	public void setImage(BufferedImage img, String imageID, LinkedList<LabelPackage> labelList){
		this.img=img;
		preferredSize=new Dimension(img.getWidth(),img.getHeight());
		this.labelList = labelList;
		this.imageID = imageID;
		this.repaint();
	}
	
	public void drawShapes(Graphics g){
		if (labelList.isEmpty())
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (LabelPackage pack: labelList){
			g2.setColor(choseColor(pack.getLabel().getType(),pack.getLabel().getName()));
			pack.getLabel().getShape().drawTo(g2,img.getWidth(),img.getHeight(),pack.getLabel().getName());
		}
	}
	
	public BufferedImage copyImage(BufferedImage source){
	    BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics2D g = copy.createGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return copy;
	}
	
	private Color choseColor(String type, String info){
		Color c = Color.WHITE;
		for (LabelType lt : ltCtrl.getDetail(type)){
			if (lt.getName().equals(info)){
				c=lt.getColor();
				break;
			}
		}
		return c;
	}

	public LinkedList<LabelPackage> getLabelList(){
		return labelList;
	}
	
	public String getImageID(){
		return imageID;
	}
 
}