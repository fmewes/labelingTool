/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package controller;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import util.JDirectoryScanner;

import db_Label.LabelType;
import db_Shape.ShapeEnum;

public class LabelTypeController {
	private final  static String seperator=";";
	private final  static String splitRegex="(;|,)";
	private String filename;
	private HashMap<String,LinkedList<LabelType>> mapDetail;
	private HashMap<String,ShapeEnum> mapType;

	public LabelTypeController(String path, boolean isFile){
		mapDetail=new HashMap<String,LinkedList<LabelType>>();
		mapType=new HashMap<String,ShapeEnum>();
		if(isFile){
			loadGroup(path);
			this.filename=path;
		}else{
			JDirectoryScanner jds=new JDirectoryScanner(path,"config",0);
			for(String fileJDS:jds.fileList){
				loadGroup(fileJDS);
			}
			this.filename=path+"/label.config";
		}
	}
	
	@SuppressWarnings("resource")
	private void loadGroup(String file){
		try{
			BufferedReader input=new BufferedReader(new FileReader(file));
			String zeile=null;
			while((zeile=input.readLine())!=null){
				String[] split=zeile.split(splitRegex);
				String type = split[0];
				String info = split[1];
				ShapeEnum e = checkShape(split[2]);
				int r=Integer.parseInt(split[3]);
				int g=Integer.parseInt(split[4]);
				int b=Integer.parseInt(split[5]);
				Color color = new Color(r,g,b);
				addToMapType(type,e);
				addToMapDetail(type,new LabelType(type,info,e,color));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private ShapeEnum checkShape(String shape){
		for (ShapeEnum e : ShapeEnum.values()){
			if (shape.equals(e.toString()))
				return e;
		}
		return ShapeEnum.NoShape;
	}

//-----------MapDetail
	private void addToMapDetail(String type, LabelType lc){
		LinkedList<LabelType> list=mapDetail.get(type);
		if(list==null){
			list=new LinkedList<LabelType>();
		}
		list.add(lc);
		mapDetail.put(type,list);
	}
	
	private synchronized void addToFile(LabelType lt){
		File file=new File(filename);
		try{
			FileWriter writer=new FileWriter(file,true);
			StringBuffer sBuf=new StringBuffer();
			sBuf.append(getDataString(lt));
			sBuf.append(System.getProperty("line.separator"));
			writer.write(sBuf.toString());
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void addLabelType(LabelType lt){
		addToMapType(lt.getType(), lt.getShape());
		addToMapDetail(lt.getType(),lt);
		addToFile(lt);
	}
	
	public void removeType(String type){
		mapType.remove(type);
		mapDetail.remove(type);
		saveAll();
	}
	public void removeName(LabelType lt){
		LinkedList<LabelType> list = mapDetail.get(lt.getType());
		if(list!=null){
			for (LabelType mapLt : list){
				if (mapLt.equals(lt)){
					list.remove(lt);
					break;
				}
			}
		}
		saveAll();
	}
	
	private synchronized void saveAll(){
		File file=new File(filename);
		try{
			FileWriter writer=new FileWriter(file,false);
			StringBuffer sBuf=new StringBuffer();
			for(Entry<String,LinkedList<LabelType>> entry : mapDetail.entrySet()){
				LinkedList<LabelType> list=entry.getValue();
				for(LabelType lt : list){
					try{
						sBuf.append(getDataString(lt));
						sBuf.append(System.getProperty("line.separator"));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			writer.write(sBuf.toString());
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void editName(LabelType ltOld, LabelType ltNew){
		addLabelType(ltNew);
		removeName(ltOld);
	}
	
	public void editType(String type, LabelType lt){
		LinkedList<LabelType> list = new LinkedList<LabelType>(mapDetail.get(type));
		removeType(type);
		addLabelType(lt);
		list.removeFirst();
		for (LabelType mlt : list){
			mlt.setType(lt.getType());
			mlt.setShape(lt.getShape());
			addLabelType(mlt);
		}
	}
	
	public LinkedList<LabelType> getDetail(String type){
		LinkedList<LabelType> list=mapDetail.get(type);
		if(list==null)
			return new LinkedList<LabelType>();
		
		return new LinkedList<LabelType>(list);
	}
	
	public LabelType getDetail(String type, String name){
		LinkedList<LabelType> list=mapDetail.get(type);
		if(list==null){
			System.out.println("ERROR: NO Element "+type+" has been found");
			return new LabelType();
		}
		for (LabelType lt : list){
			lt.getName().equals(name);
			return lt;
		}
		return new LabelType();
	}
	
	public LinkedList<String> getKeys(){
		LinkedList<String> list=new LinkedList<String>();
		for(Entry<String,LinkedList<LabelType>> entry : mapDetail.entrySet()){
			list.add(entry.getKey());
		}
		return list;
	}
	
//------------------ MapType	
	private void addToMapType(String type, ShapeEnum e){
		if (mapType.containsKey(type))
			return;
		mapType.put(type, e);
	}
	
	public ShapeEnum getShape(String type){
		ShapeEnum e = mapType.get(type);
		if (e == null)
			return ShapeEnum.NoShape;
		return e;
	}
	
	public void printContent(String type){
		LinkedList<LabelType> list=mapDetail.get(type);
		if(list==null || list.isEmpty()){
			System.out.println("Map => leer");
			return;
		}
		System.out.println(type+"="+mapType.get(type).toString());
		for(LabelType lt : list){
			System.out.println(lt.getType() +": "+ lt.getName() +","+ lt.getShape().toString() +","+ lt.getColor().toString());
		}
	}
	
	public boolean emptyMap(){
		return mapDetail.isEmpty();
	}
	
	private String getDataString(LabelType lt){
		return lt.getType()+seperator+
			   lt.getName()+seperator+
			   lt.getShape()+seperator+
			   lt.getColor().getRed()+seperator+
			   lt.getColor().getGreen()+seperator+
			   lt.getColor().getBlue();
	}
}
