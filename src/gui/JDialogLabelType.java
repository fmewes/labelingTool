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
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import controller.LabelTypeController;

import db_Label.LabelType;
import db_Shape.ShapeEnum;

public class JDialogLabelType {
	private final JComboBox<String> typeBox  = new JComboBox<String>();
	private final JComboBox<ShapeEnum> shapeBoxE = new JComboBox<ShapeEnum>();
	private final JButton colorBoxE = new JButton("");
	private final JTextField typeFieldE = new JTextField("");
	private final JButton editButton = new JButton("Update");
	private final JButton deleteButton = new JButton("Delete");
	
	private final JComboBox<ShapeEnum> shapeBoxA = new JComboBox<ShapeEnum>();
	private final JButton colorBoxA = new JButton("");
	private final JTextField typeFieldA = new JTextField("");
	private final JButton addButton = new JButton("Add");
	private final JButton clearButton= new JButton("Clear");
	
	private final JButton closeButton= new JButton("Close");
	private final JFrameMain frame;
	private final LabelTypeController ltCtrl;
	private Color curColorE = Color.WHITE;
	private Color curColorA = Color.WHITE;
	private final JDialog dialog;
	
	public JDialogLabelType(JFrameMain frame){
		this.frame=frame;
		this.ltCtrl=frame.getLabelTypeController();
		
		dialog = new JDialog();
        dialog.setSize(new Dimension(500, 365));
        dialog.setTitle("Add/Modify Label-Type");
        dialog.setLocationRelativeTo(frame);
		dialog.setContentPane(buildFrame());
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		actionListenerEDIT();
		actionListenerADD();
		
        dialog.setModal(true);
        dialog.setVisible(true);
	}
	
	private JPanel buildFrame(){
		final FormLayout layout = new FormLayout("left:pref,0dlu,pref:grow","p, 75dlu");
        final DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        	builder.setDefaultDialogBorder();
        	builder.appendSeparator("Modify");
        	builder.append("",buildEdit());
        	builder.appendSeparator("Add");
        	builder.append("",buildAdd());
        return builder.getPanel();
	}
	
	private JPanel buildEdit(){
        final FormLayout layout = new FormLayout("left:pref, 3dlu, pref:grow, 20dlu, pref, 0dlu, pref");
        final DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        	builder.setDefaultDialogBorder();
        	builder.append("",typeBox);        builder.append("",editButton);
	        builder.append("Type",typeFieldE); builder.append("");builder.append("");
	        builder.append("Shape",shapeBoxE); builder.append("");builder.append("");
	        builder.append("Color",colorBoxE); builder.append("",deleteButton);
	        
	        for (ShapeEnum e : ShapeEnum.values()){
	        	shapeBoxE.addItem(e);
	        	shapeBoxA.addItem(e);
	        }
	        colorBoxE.setIcon(createIcon(Color.WHITE,32,16));
	        colorBoxE.setText(" r="+curColorE.getRed()+"  g="+curColorE.getGreen()+"  b="+curColorE.getBlue());
	        colorBoxA.setIcon(createIcon(Color.WHITE,32,16));
	        colorBoxA.setText(" r="+curColorA.getRed()+"  g="+curColorA.getGreen()+"  b="+curColorA.getBlue());
	        
	        fillTypeBox();
	        if (!ltCtrl.emptyMap())
	        	setEdit(typeBox.getItemAt(0),typeBox.getItemAt(0));
		return builder.getPanel();
	}
	
	private JPanel buildAdd(){
		final FormLayout layout = new FormLayout("left:pref, 3dlu, pref:grow, 20dlu, pref, 0dlu, pref");
        final DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        	builder.setDefaultDialogBorder();
	        builder.append("Type",typeFieldA); builder.append("",addButton);
	        builder.append("Shape",shapeBoxA); builder.append("");builder.append("");
	        builder.append("Color",colorBoxA); builder.append("",clearButton);
	        builder.appendSeparator();
	        builder.append("");builder.append("");builder.append("");builder.append("");
	        builder.append("");builder.append("");builder.append("",closeButton);
        return builder.getPanel();
	}
	
	private void actionListenerEDIT(){
		typeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setEdit(e.getItem().toString(), e.getItem().toString());
				}
			}
		});
		colorBoxE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				curColorE = colorChoose(curColorE,colorBoxE);
			}
		});
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (checkString(typeFieldE)){
					String type = typeFieldE.getText();
					ShapeEnum shape = (ShapeEnum) shapeBoxE.getSelectedItem();
					ltCtrl.editType(typeBox.getSelectedItem().toString(), new LabelType(type,type,shape,curColorE));
					fillTypeBox();
					typeBox.setSelectedItem(type);
				}
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				delete();
			}
		});
	}
	private void actionListenerADD(){
		colorBoxA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				curColorA = colorChoose(curColorA,colorBoxA);
			}
		});
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (checkString(typeFieldA)){
					String type = typeFieldA.getText();
					ShapeEnum shape = (ShapeEnum) shapeBoxA.getSelectedItem();
					ltCtrl.addLabelType(new LabelType(type,type,shape,curColorA));
					curColorA=clear(typeFieldA,colorBoxA);
					fillTypeBox();
				}
			}
		});
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				curColorA=clear(typeFieldA,colorBoxA);
			}
		});
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeFrame();
			}
		});
	}
	private Color colorChoose(Color cur, JButton b){
		Color c = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
		if (c==null)
			return cur;
		b.setIcon(createIcon(c,32,16));
		b.setText(" r="+c.getRed()+"  g="+c.getGreen()+"  b="+c.getBlue());
		return c;
	}
	
	private Color clear(JTextField text, JButton button){
		text.setText("");
		Color c = Color.WHITE;
		button.setIcon(createIcon(c,32,16));
		button.setText(" r="+c.getRed()+"  g="+c.getGreen()+"  b="+c.getBlue());
		return c;
	}
	
	private void fillTypeBox(){
		typeBox.removeAllItems();
		for (String s : ltCtrl.getKeys()){
			typeBox.addItem(s);
		}
		if (typeBox.getItemCount()==0){
			curColorE=clear(typeFieldE,colorBoxE);
			editButton.setEnabled(false);
			return;
		}
		editButton.setEnabled(true);
	}
	
	private void setEdit(String type, String name){
		LabelType lt = ltCtrl.getDetail(type,name);
		typeFieldE.setText(lt.getType());
		shapeBoxE.setSelectedItem(lt.getShape());
		curColorE = lt.getColor();
		colorBoxE.setIcon(createIcon(curColorE,32,16));
		colorBoxE.setText(" r="+curColorE.getRed()+"  g="+curColorE.getGreen()+"  b="+curColorE.getBlue());
	}
	
	private void closeFrame(){
		frame.fillTypeBox();
		dialog.dispose();
	}
	
	private void delete(){
		int out = JOptionPane.showConfirmDialog(
				dialog,
			    "Do you want to delete "+typeBox.getSelectedItem().toString()+"?",
			    "Delete Warning",
			    JOptionPane.YES_NO_OPTION);
		if (out != JOptionPane.YES_OPTION)
			return;
		ltCtrl.removeType(typeBox.getSelectedItem().toString());
		fillTypeBox();
	}
	
	private boolean checkString(JTextField text){
		String s = text.getText();
		s=s.replaceAll("\\W+", "_");
		s=s.replaceAll("^_+|^\\d+|_+$|;|,", "");
		if (s.equals("")){
			JOptionPane.showMessageDialog(
					dialog,
				    "Wrong Input from the Type-Field",
				    "User Input Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		text.setText(s);
		return true;
	}
	
    private ImageIcon createIcon(Color c, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(c);
        graphics.fillRect(0, 0, width, height);
        graphics.setXORMode(Color.DARK_GRAY);
        graphics.drawRect(0, 0, width-1, height-1);
        image.flush();
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }

}
