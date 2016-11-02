/************************************************************************
 * Copyright (C) 2016  Florian Mewes <florian.mewes90@yahoo.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 ************************************************************************/
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import db_Label.Ignore;
import db_Label.LabelHeader;
import db_Label.LabelPackage;
import db_Label.LabelBody;

import util.JDirectoryScanner;
import util.Shaper;

public class CSVController {
	private final static String seperator=";";
	private final static String splitRegex="(;|,)";
	private String filename;
	private HashMap<String,LinkedList<LabelPackage>> labelMap;
	
	public CSVController(String path, LabelTypeController gc) {
		labelMap=new HashMap<String,LinkedList<LabelPackage>>();
		if(new File(path).exists()){
			JDirectoryScanner jds=new JDirectoryScanner(path,"csv",0);
			for(String filenameCSV:jds.fileList){
				String[] split=filenameCSV.split("/");
				if (split[split.length-1].equals("labels.csv")){
					loadCSV(filenameCSV, gc);
					this.filename=filenameCSV;
				}else{
					this.filename=path+"/labels.csv";
				}
			}
		}
	}
	
	@SuppressWarnings("resource")
	private synchronized void loadCSV(String fileCSV, LabelTypeController gc){
		try{
			BufferedReader input=new BufferedReader(new FileReader(fileCSV));
			String zeile=null;
			while((zeile=input.readLine())!=null){
				String[] split=zeile.split(splitRegex);
				//create - fill Header
				String imageID=split[0];
				LabelHeader head = new LabelHeader(imageID);
				head.setTimestamp(Long.parseLong(split[1]));
				head.setComputerID(split[2]);
				//create - fill Label
				String type=split[3];
				String name=split[split.length-1];
				LabelBody label=null;
				if (type.equals("Ignore")){
					label=new Ignore();
				}else{
					label=new LabelBody(type,name,Shaper.findShape(gc.getShape(type)));
					String[] labelData=new String[split.length-4];
					System.arraycopy(split,4,labelData,0,split.length-4);
					label.getShape().setParameter(labelData);
				}
				addToMap(imageID,new LabelPackage(head,label));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void addToMap(String imageID,LabelPackage pack){
		LinkedList<LabelPackage> imgLabels=labelMap.get(imageID);
		if(imgLabels==null){
			imgLabels=new LinkedList<LabelPackage>();
		}
		imgLabels.add(pack);
		labelMap.put(imageID,imgLabels);
	}
	
	public LinkedList<LabelPackage> getAnnotations(String imageID){
		return getAnnotations(imageID,null);
	}
	
	public LinkedList<LabelPackage> getAnnotations(String imageID, String filter){
		LinkedList<LabelPackage> imgLabels=labelMap.get(imageID);
		
		if(imgLabels==null)
			return new LinkedList<LabelPackage>();
		if(filter==null)
			return imgLabels;
		LinkedList<LabelPackage> result=new LinkedList<LabelPackage>();
		for(LabelPackage pack:imgLabels){
			if(pack.getLabel().getName().equals(filter)||pack.getLabel().getClass().getSimpleName().equals(filter)){
				result.add(pack);
			}
		}
		return result;
	}
	/*
	public LinkedList<LabelPackage> getAllAnnotations(String filter){
		LinkedList<LabelPackage> result=new LinkedList<LabelPackage>();
		for(LinkedList<LabelPackage> imgLabels:labelMap.values()){
			for(LabelPackage pack:imgLabels){
				if(pack.getLabel().getInfoText().equals(filter)||pack.getLabel().getClass().getSimpleName().equals(filter)){
					result.add(pack);
				}
			}
		}
		return result;
	}*/

	public void removeAnnotation(String imageID, LinkedList<LabelPackage> removeList){
		LinkedList<LabelPackage> imgLabels=labelMap.get(imageID);
		if(imgLabels!=null){
			for (LabelPackage pack : removeList){
				imgLabels.remove(pack);
			}
			saveAll();
		}
	}
	public void removeAllAnnotation(String imageID){
		labelMap.get(imageID).clear();
		saveAll();
	}

	public synchronized void saveAll(){
		File file=new File(filename);
		try{
			FileWriter writer=new FileWriter(file,false);
			StringBuffer sBuf=new StringBuffer();
			for(Entry<String,LinkedList<LabelPackage>> entry:labelMap.entrySet()){
				LinkedList<LabelPackage> imgLabels=entry.getValue();
				for(LabelPackage pack:imgLabels){
					try{
						sBuf.append(getDataString(pack.getHead(),pack.getLabel()));
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

	private String getDataString(LabelHeader head, LabelBody label){
		return head.getImageID()+seperator+
			   head.getTimestamp()+seperator+
			   head.getComputerID()+seperator+
			   label.getType()+
			   concat(label.getSerialization());
	}

	public void addAnnotation(String imageID, LinkedList<LabelPackage> labelList){
		for (LabelPackage pack : labelList){
			addToMap(imageID,pack);
		}
		addToFile(labelList);
	}

	private synchronized void addToFile(LinkedList<LabelPackage> labelList){
		File file=new File(filename);
		try{
			FileWriter writer=new FileWriter(file,true);
			StringBuffer sBuf=new StringBuffer();
			for (LabelPackage pack : labelList){
				sBuf.append(getDataString(pack.getHead(),pack.getLabel()));
				sBuf.append(System.getProperty("line.separator"));
			}
			writer.write(sBuf.toString());
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public String concat(String[] data){
		if(data==null)return "";
		String result="";
		for(String s:data){
			result+=seperator+s;
		}
		return result;
	}

	public String getFilename(){
		return filename;
	}

	public void saveChanges(LinkedList<LabelPackage> labelList, String imageID){
		LinkedList<LabelPackage> imgLabels=labelMap.get(imageID);
		//Case 0-0 => do nothing
		if ((imgLabels == null || imgLabels.isEmpty()) && labelList.isEmpty())
			return;
		//Case 0-1 => add full labelList
		if((imgLabels==null || imgLabels.isEmpty()) && !labelList.isEmpty()){
			addAnnotation(imageID,labelList);
			return;
		}
		//Case 1-0 => delete full imgLabels
		if (labelList.isEmpty()){
			removeAllAnnotation(imageID);
			return;
		}
		//Case 1-1 => compare => add/delete || do nothing
		LinkedList<LabelPackage> addList = compareList(labelList,imgLabels);
		LinkedList<LabelPackage> removeList = compareList(imgLabels,labelList);
		if(!addList.isEmpty())
			addAnnotation(imageID,addList);
		if(!removeList.isEmpty())
			removeAnnotation(imageID,removeList);
	}
	
	public LinkedList<LabelPackage> compareList(LinkedList<LabelPackage> list1, LinkedList<LabelPackage> list2){
		LinkedList<LabelPackage> result = new LinkedList<LabelPackage>();
		boolean found;
		for (LabelPackage pack1 : list1){
			found=false;
			for (LabelPackage pack2 : list2){
				if (pack1.equals(pack2)){
					found=true;
					break;
				}
			}
			if (!found)
				result.add(pack1);
		}
		return result;
	}
	
	public void printMap(String imageID){
		LinkedList<LabelPackage> mapLabels=labelMap.get(imageID);
		if(mapLabels==null || mapLabels.isEmpty()){
			System.out.println("Map => empty");
			return;
		}
		for(LabelPackage pack:mapLabels){
			System.out.println(pack.getHead().getImageID()+": "+pack.getLabel().toString());
		}
	}
	
	public void setFilename(String path){
		this.filename=path+"/labels.csv";
	}
}
