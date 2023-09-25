package root.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import root.ClientBeApplication;

public class FileUpdateInfoModel implements Serializable{
	
	private static final long serialVersionUID = 1001919453L;
	public String fileName;
	public Integer pageIndex;
	public String newContent;
	public String original;
	
	public String clientId = ClientBeApplication.clientId;
	
	public FileUpdateInfoModel() {}
	
	public FileUpdateInfoModel(Map<String, Object> map) {
		this.fileName = (String) map.get("fileName");
		this.pageIndex = (Integer) map.get("pageIndex");
		this.newContent = (String) map.get("newContent");
		this.original = (String) map.get("original");
		this.clientId = (String) map.get("clientId");
	}
	
	public String getNewContent() {
		return newContent;
	}
	public void setNewContent(String content) {
		this.newContent = content;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	
	public Map<String, Object> covertToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("clientId", clientId);
		map.put("fileName", fileName);
		map.put("pageIndex", pageIndex);
		map.put("newContent", newContent);
		map.put("original", original);
		
		return map;
	}
	
	@Override
	public String toString() {
		return "FileUpdateInfoModel [fileName=" + fileName + ", pageIndex=" + pageIndex + ", newContent=" + newContent
				+ ", original=" + original + ", clientId=" + clientId + "]";
	}
	
}
