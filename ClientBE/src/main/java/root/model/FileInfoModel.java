package root.model;

import java.io.File;
import java.text.SimpleDateFormat;

public class FileInfoModel {

	public String fileName;
	public String lastMod;
	public String size;
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	public FileInfoModel() {}
	
	public FileInfoModel(File file) {
		this.fileName = file.getName();
		this.lastMod = sdf.format(file.lastModified());
        this.size = Long.toString(file.length() / 1024) + " KB";
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLastMod() {
		return lastMod;
	}

	public void setLastMod(String lastMod) {
		this.lastMod = lastMod;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "FileInfoModel [fileName=" + fileName + ", lastMod=" + lastMod + ", size=" + size + "]";
	}	
	
}
