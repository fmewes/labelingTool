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
import gui.JPanelImage;
import gui.Options;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import db_Label.Ignore;
import db_Label.LabelHeader;
import db_Label.LabelPackage;
import db_Label.LabelBody;

import util.Shaper;

public class Visualizer{
	private boolean guiCreated;
	private JPanelImage ip;
	private boolean done;
	private boolean back;
	Point mouseFrom;
	Point mouseTo;
	private JFrameMain frame;
	private final Options o;
	
	public Visualizer(JFrameMain frame){
		this.o=frame.getOptions();
		this.frame=frame;
		guiCreated=false;
	}
	
	public void showImage(final BufferedImage bufferedImage, final String imageID, final LinkedList<LabelPackage> labelList){
		if(!guiCreated){
			frame.addMouseWheelListener(new MouseWheelListener(){
				@Override
				public void mouseWheelMoved(MouseWheelEvent e){
					if (e.getWheelRotation() < 0){
						back();
					}else{
						next();
					}
				}
			});
			createGUI(bufferedImage.getWidth(),bufferedImage.getHeight());
			guiCreated=true;
		}
		ip.setImage(bufferedImage,imageID,labelList);
	}

	private void createGUI(int width, int height){
		ip=new JPanelImage(width,height,frame.getLabelTypeController());
		ip.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mouseEntered(MouseEvent arg0){}
			public void mouseExited(MouseEvent arg0){}
			public void mousePressed(MouseEvent e){
				if (SwingUtilities.isRightMouseButton(e)){
					if (!ip.getLabelList().isEmpty()){
						ip.getLabelList().removeLast();
						ip.repaint();
					}
					return;
				}
				if (checkIgnore())
					return;
				mouseFrom = new Point(e.getX(),e.getY());
				LabelBody label=new LabelBody(o.getSelectedLabel().getType(), o.getSelectedLabel().getName(),
						Shaper.findShape(o.getSelectedLabel().getShape()));
				label.getShape().setPoints(mouseFrom,mouseFrom);
				LabelPackage pack = new LabelPackage(new LabelHeader(ip.getImageID()),label);
				ip.getLabelList().add(pack);
			}
			
			public void mouseReleased(MouseEvent e){
				if (SwingUtilities.isRightMouseButton(e) || ip.getLabelList().isEmpty() || checkIgnore())
					return;
				mouseTo = new Point(e.getX(),e.getY());
				ip.getLabelList().getLast().getLabel().getShape().setPoints(mouseFrom,mouseTo);
				ip.repaint();
				
				if(o.isFastMode())
					done=true;

			}
		});
		ip.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e){
				if (SwingUtilities.isRightMouseButton(e) || ip.getLabelList().isEmpty() || checkIgnore())
					return;
				mouseTo = new Point(e.getX(),e.getY());
				ip.getLabelList().getLast().getLabel().getShape().setPoints(mouseFrom, mouseTo);
				ip.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e){
			}
			
		});
		frame.add(ip);
		frame.pack();
	}
	
	public boolean waitForMouseEvent() throws InterruptedException{
		done=false;
		back=!o.getScrollMode();
		while(!done){
			sleep(10);
		}
		return back;
	}
	private void sleep(int time) throws InterruptedException{
		Thread.sleep(time);
	}
	
	public void disposePanel(){
		frame.remove(ip);
	}
	
	public boolean checkIgnore(){
		for (LabelPackage pack : ip.getLabelList()){
			if (pack.getLabel().getClass().getSimpleName().equals("Ignore")){
				return true;
			}
		}
		return false;
	}
	
	public void labelIgnore(){
		ip.getLabelList().clear();
		Ignore label=new Ignore();
		LabelPackage pack = new LabelPackage(new LabelHeader(ip.getImageID()),label);
		ip.getLabelList().add(pack);
		ip.repaint();
		done=true;
	}
	
	public void clearLabel(){
		if (ip.getLabelList() != null){
			ip.getLabelList().clear();
			ip.repaint();
		}
	}
	public void deleteLabel(){
		if (ip.getLabelList() != null){
			LinkedList<LabelPackage> labelListCopy=new LinkedList<LabelPackage>(ip.getLabelList());
			for (LabelPackage pack : labelListCopy){
				if(pack.getLabel().getName().equals(o.getSelectedLabel().getName()) &&
						pack.getLabel().getType().equals(o.getSelectedLabel().getType())){
					ip.getLabelList().remove(pack);
				}
			}
			ip.repaint();
		}
	}
	public void next(){
		done=true;
	}
	public void back(){
		back=o.getScrollMode();
		done=true;
	}
	
}
