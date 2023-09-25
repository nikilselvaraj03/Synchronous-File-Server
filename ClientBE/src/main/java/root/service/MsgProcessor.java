package root.service;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import root.ClientBeApplication;
import root.model.FileUpdateInfoModel;

public class MsgProcessor {

	FileService fileService;
	
	public void udpProcess(Map<String, Object> map) {
		switch((String)map.get("do")) {
			case "update":
				FileUpdateInfoModel fileObj = new FileUpdateInfoModel(map);
				fileService = new FileService();
				fileService.updateFile(fileObj, false);
				ClientBeApplication.console("File Update from: "+fileObj.getClientId(), "<");
				break;
				
			case "create":
				fileService = new FileService();
				//fileService.createFile((String)map.get("fileName"), false);
				FileUpdateInfoModel obj = new FileUpdateInfoModel(map);
				
				fileService.createFile(obj, false);
				break;
				
			case "delete":
				fileService = new FileService();
				fileService.deleteFile((String)map.get("fileName"), false);
				break;
		}
	}
}
