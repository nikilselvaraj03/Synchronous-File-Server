package root.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import root.Server;
import root.model.FileUpdateInfoModel;

public class FileService {

	final String path = System.getProperty("user.home")+"/Desktop/ADSE/Server";
	final int linesPerPage = 40;
	
	public void getFileList() {
		List<String> fileList = new ArrayList<String>();
		try {
			File[] folder = new File(path).listFiles();
			for(File file: folder) {
				System.out.println(file.getName());
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public void updateFile(FileUpdateInfoModel fileUpdateObj) {
		//System.out.println("update: "+fileUpdateObj);
		try {
			File fOld = new File(path+"/"+fileUpdateObj.getFileName());
			File fNew = new File(path+"/"+fileUpdateObj.getFileName());
			if(fOld.exists()) {
				FileReader fReader = new FileReader(fOld);
				BufferedReader bReader = new BufferedReader(fReader);
				String line;
				int start = (fileUpdateObj.getPageIndex()*linesPerPage)-(linesPerPage-1);
				int end = start+linesPerPage;
				//System.out.println("update starts at line: "+start+" finishes at: "+(end-1));
				List<String> fileDataList = new ArrayList<>();
			
				String[] ori = fileUpdateObj.getOriginal().split("\n");
				String[] newC = fileUpdateObj.getNewContent().split("\n");
				List<String> changesList = new ArrayList<>();
				for(int i=0;i<newC.length;i++){
					//System.out.println(i);
					if(i<ori.length) {
						if(!ori[i].equals(newC[i])) {
							//System.out.println("----> "+newC[i]);
							changesList.add(newC[i]);
							System.out.println("---> line "+i+": "+newC[i]);
						}
					}
					else {
						//System.out.println("----> "+newC[i]);
						changesList.add(newC[i]);
						System.out.println("---> line "+i+": "+newC[i]);
					}
				}
				
				for(int i=1;(line=bReader.readLine())!=null;i++) {
					if(i>=start && i<end) {
						//System.out.println("dont add line: "+i+" "+line);
					}
					else {
						fileDataList.add(line);
						//System.out.println("add line: "+i+" "+line);
					}
				}
				//System.out.println(start-1+" "+fileDataList.size());
				fileDataList.addAll(start-1, Arrays.asList(fileUpdateObj.getNewContent().split("\n")));
				FileWriter writer = new FileWriter(fNew, false);
				for(String str: fileDataList) {
					//System.out.println("~~ "+str);
					writer.write(str+"\n");
				}
				writer.close();
				fReader.close();
			}
		}
		catch(Exception e) {
			System.out.println("Error: "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void createFile(String fileName) {
		try {
			File file = new File(path+"/"+fileName);
			if(!file.exists()) {
				Server.console("Creating new file: "+fileName,"~");
				file.createNewFile();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createFile(String fileName, String newContent) {
		try {
			File file = new File(path+"/"+fileName);
			if(!file.exists()) {
				Server.console("Creating new file: "+fileName,"~");
				file.createNewFile();
				
				FileWriter writer = new FileWriter(file, false);
				for(String str: newContent.split("\n")) {
					System.out.println("----------"+str);
					writer.write(str+"\n");
				}
				writer.close();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFile(String fileName) {
		try {
			File file = new File(path+"/"+fileName);
			if(file.exists()) {
				Server.console("Deleting file: "+fileName,"~");
				file.delete();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
