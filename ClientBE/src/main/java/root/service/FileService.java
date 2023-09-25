package root.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Service;

import root.ClientBeApplication;
import root.model.FileInfoModel;
import root.model.FileUpdateInfoModel;

@Service
public class FileService {

	final String path = System.getProperty("user.home")+"/Desktop/ADSE/Clients/"+ClientBeApplication.clientId;
	final int linesPerPage = 40;
	
	DataInputStream input;
	DataOutputStream output;
	ObjectOutputStream objOutput;
	ObjectInputStream objInput;
	
	public List<FileInfoModel> getFileNameList() {
		List<FileInfoModel> fileList = new ArrayList<>();
		try {
			File[] folder = new File(path).listFiles();
			for(File file: folder) {
				fileList.add(new FileInfoModel(file));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error: "+e.getLocalizedMessage());
		}

		return fileList;
	}
	
	public Map<String, Object> fileRead(String fileName, Integer pageIndex, Integer totalPages) { //big.txt, page 2, total 1000 pages with 20 lines in each page
		Map<String,Object> map = new HashMap<>();
		String content="";
		if(pageIndex==null)
			pageIndex=1;
		
		try {
			System.out.println(fileName+" "+pageIndex+" "+totalPages);
			File file = new File(path+"/"+fileName);
			if(file.exists()) {
				FileReader fReader = new FileReader(file);
				BufferedReader bReader = new BufferedReader(fReader);
				String line;
				int start = (pageIndex*linesPerPage)-(linesPerPage-1);
				float tempTp = 0;
				if(totalPages==null) {
					for(tempTp=1;(line=bReader.readLine())!=null;tempTp++) {
						if(tempTp<=linesPerPage)
							content += line+"\n";
					}
					tempTp /= linesPerPage;
					totalPages = 1;
					for(int i=1;tempTp-i>0;i++,totalPages++);
				}
				else {
					for(int i=1;(line=bReader.readLine())!=null && i<=(pageIndex*linesPerPage);i++) {
						if(i>=start) {
							content += line+"\n";
						}
					}
				}
				map.put(fileName, content);
				map.put("page", pageIndex);
				map.put("totalPages", totalPages);
				
				fReader.close();
			}
		}
		catch(Exception e) {
			System.out.println("Error: "+e.getLocalizedMessage());
		}
		
		return map;
	}
	
	public Map<String, Object> updateFile(FileUpdateInfoModel fileUpdateObj, boolean updateToServer) {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			File fOld = new File(path+"/"+fileUpdateObj.getFileName());
			File fNew = new File(path+"/"+fileUpdateObj.getFileName());
			if(fOld.exists()) {
				if(!fileUpdateObj.getNewContent().equals(fileUpdateObj.getOriginal())) {
					FileReader fReader = new FileReader(fOld);
					BufferedReader bReader = new BufferedReader(fReader);
					String line;
					int start = (fileUpdateObj.getPageIndex()*linesPerPage)-(linesPerPage-1);
					int end = start+linesPerPage;
					List<String> fileDataList = new ArrayList<>();
				
					String[] ori = fileUpdateObj.getOriginal().split("\n");
					String[] newC = fileUpdateObj.getNewContent().split("\n");
					List<String> changesList = new ArrayList<>();
					MsgService msgServ = new MsgService();
					for(int i=0;i<newC.length;i++) {
						//System.out.println(i);
						if(i<ori.length) {
							if(!ori[i].equals(newC[i])) {
								//System.out.println("----> "+newC[i]);
								changesList.add(newC[i]);
								System.out.println("---> line "+i+": "+newC[i]+"\tcompressed: "+msgServ.compress(newC[i]));
							}
						}
						else {
							//System.out.println("----> "+newC[i]);
							changesList.add(newC[i]);
							System.out.println("---> line "+i+": "+newC[i]);
						}
					}
					
					for(int i=1;(line=bReader.readLine())!=null;i++) {
						if(!(i>=start && i<end)) {
							
							fileDataList.add(line);
						}
					}
					fileDataList.addAll(start-1, Arrays.asList(fileUpdateObj.getNewContent().split("\n")));
					FileWriter writer = new FileWriter(fNew, false);
					
					for(String str: fileDataList) {
						writer.write(str+"\n");
					}
					writer.close();
					fReader.close();
					
					
					if(updateToServer) {
						Map<String, Object> map = fileUpdateObj.covertToMap();
						map.put("do", "update");
						
						updateToServer(map);
					}
				}
				else {
					//System.out.println("print same content");
				}
				responseMap.put("code", 200);
			}
			else {
				responseMap.put("code", 400);
				responseMap.put("msg", "File does not exist");
			}
		}
		catch(Exception e) {
			System.out.println("Error: "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return responseMap;
	}
	
	public void updateToServer(Map<String, Object> map) {
		try {
			ConnectionService.sendData(SerializationUtils.serialize((Serializable) map));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Map<String, Object> createFile(String fileName, boolean updateToServer) {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			File file = new File(path+"/"+fileName);
			
			if(!file.exists()) {
				ClientBeApplication.console("Creating new file: "+fileName,"~");
				file.createNewFile();
				
				if(updateToServer) {
					Map<String, Object> map = new HashMap<>();
					map.put("do", "create");
					map.put("fileName", fileName);
					map.put("clientId", ClientBeApplication.clientId);
					ClientBeApplication.console("New file creation to server: "+fileName,">");
					updateToServer(map);
				}
				responseMap.put("code", 200);
			}
			else {
				responseMap.put("code", 400);
				responseMap.put("msg", "File already exists");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return responseMap;
	}
	
	public Map<String, Object> createFile(FileUpdateInfoModel newFile, boolean updateToServer) {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			File file = new File(path+"/"+newFile.getFileName());
			
			if(!file.exists()) {
				ClientBeApplication.console("Creating new file: "+newFile.getFileName(),"~");
				file.createNewFile();
				
				FileWriter writer = new FileWriter(file, false);
				for(String str: newFile.getNewContent().split("\n")) {
					System.out.println("----------"+str);
					writer.write(str+"\n");
				}
				writer.close();
					
				if(updateToServer) {
					Map<String, Object> map = new HashMap<>();
					map.put("do", "create");
					map.put("fileName", newFile.getFileName());
					map.put("newContent", newFile.getNewContent());
					map.put("clientId", ClientBeApplication.clientId);
					ClientBeApplication.console("New file creation to server: "+newFile.getFileName(),">");
					updateToServer(map);
				}
				responseMap.put("code", 200);
			}
			else {
				responseMap.put("code", 400);
				responseMap.put("msg", "File already exists");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return responseMap;
	}
	
	public Map<String, Object> deleteFile(String fileName, boolean updateToServer) {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			File file = new File(path+"/"+fileName);
			if(file.exists()) {
				file.delete();
				if(updateToServer) {
					Map<String, Object> map = new HashMap<>();
					map.put("do", "delete");
					map.put("fileName", fileName);
					map.put("clientId", ClientBeApplication.clientId);
					ClientBeApplication.console("file deletion to server: "+fileName,">");
					updateToServer(map);
				}
				responseMap.put("code", 200);
			}
			else {
				responseMap.put("code", 400);
				responseMap.put("msg", "File does not exist");
			}
			
			ReactConnector.broadcastMessage("delete");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return responseMap;
	}
	
	/*
	
	public void newClientInit(int clientId, ConnectionService conn) {
		System.out.println("new client");
		try {
			//String temp = newPath+"Client"+Integer.toString(++ClientBeApplication.clientCount);
			//System.out.println(temp);
			File folder = new File(newPath+"Client"+Integer.toString(clientId));
			if(!folder.exists())
				folder.mkdir();
				conn.sendCommand(clientId, ClientBeApplication.newClConn);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	*/
}
