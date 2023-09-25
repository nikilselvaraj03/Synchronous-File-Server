package root.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import root.model.FileInfoModel;
import root.model.FileUpdateInfoModel;
import root.service.FileService;

@RestController
@RequestMapping("/file")
@CrossOrigin(origins ="http://localhost:3000")
public class FileController {

	@Autowired
	FileService fileService;
	
	@GetMapping("/list")
	public List<FileInfoModel> getFileNames() {
		System.out.println("file list");
		return fileService.getFileNameList();
	}

	@GetMapping({"/read/{fileName}", "/read/{fileName}/{pageIndex}/{totalPages}"})
	public Map<String, Object> getFileData(@PathVariable("fileName") String fileName, 
			@PathVariable(name="pageIndex", required=false) Integer pageIndex, 
			@PathVariable(name="totalPages", required=false) Integer totalPages) {
		
		return fileService.fileRead(fileName, pageIndex, totalPages);
	}
	
	@PostMapping(value="/update", consumes= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> updateFile(@RequestBody FileUpdateInfoModel fileUpdateObj) {
		//System.out.println(">>> "+fileUpdateObj);
		return fileService.updateFile(fileUpdateObj, true);
	}
	
	/*
	@PostMapping(value="/create/{fileName}")
	public void createFile(@PathVariable String fileName) {

		fileService.createFile(fileName, true);
	}
	*/
	
	@PostMapping(value="/create")
	public Map<String, Object> createFile2(@RequestBody FileUpdateInfoModel fileUpdateObj) {
		//System.out.println("cccccrrrr "+fileUpdateObj);
		return fileService.createFile(fileUpdateObj, true);
	}
	
	@PostMapping(value="/delete/{fileName}")
	public Map<String, Object> deleteFile(@PathVariable String fileName) {

		return fileService.deleteFile(fileName, true);
	}
}
