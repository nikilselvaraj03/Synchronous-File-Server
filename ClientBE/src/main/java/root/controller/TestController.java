package root.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import root.ClientBeApplication;
import root.model.MsgBody;
import root.service.ConnectionService;

@RestController
@CrossOrigin(origins ="http://localhost:3000")
public class TestController {
	
	@GetMapping("/tcp/{command}")
	public void testMe2(@PathVariable("command") int command) 
			throws IOException {
	      
		ConnectionService.sendCommand(command);
	}
	
	@GetMapping("/udp/{command}")
	public void testMe(@PathVariable("command") String command) 
			throws IOException {
	      
		ConnectionService.sendData(command);
	}
	
}
