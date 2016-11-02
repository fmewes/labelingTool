/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


import controller.LabelTypeController;
import controller.LabelingThread;

import db_Label.LabelType;

@SuppressWarnings("serial")
public class JFrameMain extends JFrame {
	private JMenuBar menuBar;
	private JMenu file, image, label, test;
	private JMenuItem folder, labelTypeFolder, list,
			  next, back, clear, delete, ignore,
			  modType, modName, group;
	private JCheckBoxMenuItem fast, nolabel, save, scrollMode;
	
	final JComboBox<String> typeBox = new JComboBox<String>();
	final JComboBox<LabelType> detailBox = new JComboBox<LabelType>();
	
	private Preferences pref = Preferences.userRoot();
	
	private final Options o;
	private LabelTypeController ltCtrl;
	private Thread t; 
	private LabelingThread ic;
	
	public JFrameMain(){
		super();
		
		o = new Options();
		ltCtrl = new LabelTypeController(System.getProperty("user.dir"),false);
		initMenuBar();
		add(initPanel(),BorderLayout.WEST);
		
		this.setTitle("LabelTool");
		this.setMinimumSize(new Dimension(640,85));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}
	
	public Options getOptions() {
		return o;
	}
	public LabelTypeController getLabelTypeController() {
		return ltCtrl;
	}
	
	private void initMenuBar(){
		menuBar = new JMenuBar();
		file = new JMenu("File");
		folder = new JMenuItem("Open Image..");
		file.add(folder);
		labelTypeFolder = new JMenuItem("Open Label-Type..");
		file.add(labelTypeFolder);
		list = new JMenuItem("list label");
		//file.add(list);
		menuBar.add(file);
		
		image = new JMenu("Image");
		next = new JMenuItem("Next");
		next.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
		image.add(next);
		back = new JMenuItem("Back");
		back.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
		image.add(back);
		clear = new JMenuItem("Clear");
		clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
		image.add(clear);
		delete = new JMenuItem("Delete");
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0));
		image.add(delete);
		ignore = new JMenuItem("Ignore");
		ignore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0));
		image.add(ignore);
		image.setEnabled(false);
		
		image.addSeparator();
		save = new JCheckBoxMenuItem("Show Save Warning");
		save.setSelected(true);
		image.add(save);
		scrollMode = new JCheckBoxMenuItem("Reverse Scroll Mode");
		image.add(scrollMode);
		image.addSeparator();
		nolabel = new JCheckBoxMenuItem("Without Label First");
		nolabel.setSelected(true);
		//image.add(nolabel);
		fast = new JCheckBoxMenuItem("One Label Mode");
		fast.setSelected(false);
		image.add(fast);
		menuBar.add(image);
		
		label = new JMenu("Label");
		modType = new JMenuItem("Type Modification");
		label.add(modType);
		modName = new JMenuItem("Name Modification");
		label.add(modName);
		group = new JMenuItem("Group");
		//label.add(group);
		menuBar.add(label);
		
		test = new JMenu("");
		test.setEnabled(false);
		menuBar.add(test);
		setJMenuBar(menuBar);
		
		folder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				selectFolder();
			}
		});
		labelTypeFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				selectFolderType();
			}
		});
		list.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("not implemented yet");
			}
		});
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ic.getVis().next();
			}
		});
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ic.getVis().back();
			}
		});
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ic.getVis().clearLabel();
			}
		});
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ic.getVis().deleteLabel();
			}
		});
		ignore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ic.getVis().labelIgnore();
			}
		});
		modType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				openLabelFrame(true);
			}
		});
		modName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				openLabelFrame(false);
			}
		});
		group.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("not implemented yet");
			}
		});
		fast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				o.setFastMode(fast.getState());
			}
		});
		nolabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				o.setNoLabel(nolabel.getState());
				System.out.println("not implemented yet");
			}
		});
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				o.setSaveWarning(save.getState());
			}
		});
		scrollMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				o.setScrollMode(scrollMode.getState());
			}
		});
	}
	
	private void selectFolder(){
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select a Folder or Image");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		String path = pref.get("DEFAULT_PATH_IMAGE", "");
		chooser.setCurrentDirectory(new File(path));
		
		if (chooser.showOpenDialog(folder) == JFileChooser.APPROVE_OPTION){
			pref.put("DEFAULT_PATH_IMAGE", chooser.getCurrentDirectory().toString());
			if (chooser.getSelectedFile().isFile()){
				o.setDirectory(chooser.getCurrentDirectory());
				o.setFile(chooser.getSelectedFile());
			}else{
				o.setDirectory(chooser.getSelectedFile());
				o.setFile(null);
			}

			if (t == null)
				t = new Thread(ic=new LabelingThread(this));
			
//			System.out.println("1: State:" + t.getState());
//			System.out.println("1: Is alive?:" + t.isAlive());
//			System.out.println("1: ID:" + t.getId());
			//t.stop();
			t.interrupt();
			t = new Thread(ic=new LabelingThread(this));
			t.start();
			image.setEnabled(true);
			pack();
		}
	}
	
	private void selectFolderType(){
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select a Folder or .config");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		FileFilter filter = new FileNameExtensionFilter(".config", new String[] {"config"});
		chooser.setFileFilter(filter);
		chooser.addChoosableFileFilter(filter);
		String path = pref.get("DEFAULT_PATH_TYPE", "");
		chooser.setCurrentDirectory(new File(path));
		
		if (chooser.showOpenDialog(labelTypeFolder) == JFileChooser.APPROVE_OPTION){
			pref.put("DEFAULT_PATH_TYPE", chooser.getCurrentDirectory().toString());
			ltCtrl = new LabelTypeController(chooser.getSelectedFile().toString(),chooser.getSelectedFile().isFile());
			fillTypeBox();
			fillDetailBox(typeBox.getItemAt(0));
		}
	}

	private JPanel initPanel(){
		JPanel p = new JPanel();
		typeBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
				 if (event.getStateChange() == ItemEvent.SELECTED) {
					 fillDetailBox(typeBox.getSelectedItem().toString());
					 pack();
			     }
			}
		});
		
		detailBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				 if (event.getStateChange() == ItemEvent.SELECTED) {
					 o.setSelectedLabel((LabelType) detailBox.getSelectedItem());
					 pack();
			     }
			}
		});
		fillTypeBox();

		p.add(typeBox);
		p.add(detailBox);
		return p;
	}
	public void openLabelFrame(boolean flag){
		if (flag){
			new JDialogLabelType(this);
		}else{
			new JDialogLabelName(this);
		}
	}
	
	public void fillTypeBox(){
		typeBox.removeAllItems();
		if (ltCtrl==null || ltCtrl.emptyMap()){
			detailBox.removeAllItems();
			this.pack();
			return;
		}
		for (String s : ltCtrl.getKeys()){
			typeBox.addItem(s);
		}
		fillDetailBox(typeBox.getItemAt(0));
	}

	public void fillDetailBox(String type){
		detailBox.removeAllItems();
		if (ltCtrl==null || ltCtrl.emptyMap()){
			this.pack();
			return;
		}
		for (LabelType lt : ltCtrl.getDetail(type)){
			detailBox.addItem(lt);
		}
		o.setSelectedLabel(detailBox.getItemAt(0));
		this.pack();
	}
	
	public void setImageName(String imageID){
		test.setText(imageID);
	}
}
