package root.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import root.ClientBeApplication;
import root.service.ConnectionService;
import root.service.FileService;

@RestController
public class HomeController {
	
	@Autowired
	FileService fileService;
	
	@Autowired
	ConnectionService conn;
	/*
	@GetMapping("/clientcount")
	public int getClientList() {
		
		return ClientBeApplication.clientCount;
	}
	
	@PostMapping("/new")
	public int addNewClient() {
		int clientId = ++ClientBeApplication.clientCount;
		fileService.newClientInit(clientId, conn);
		
		return clientId;
	}
	*/
}
