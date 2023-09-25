package root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import root.service.ConnectionService;
import root.service.ReactConnector;

@SpringBootApplication
public class ClientBeApplication {
	
	public static final String clientId = "Client_1";

	public static final int reqDirCopy = 1; //request a copy of the server directory
	
	public static void console(String op, String character) {
		for(int i=0;i<10;i++)
			System.out.print(character);
		System.out.print(" "+op+" ");
		for(int i=0;i<10;i++)
			System.out.print(character);
		System.out.println("\n");
	}
	
	public static void main(String[] args) {
		System.setProperty("server.port", "8080");
		SpringApplication.run(ClientBeApplication.class, args);
		ConnectionService.init();
		try {
			new ReactConnector(9987).start();
		} catch (Exception e) {
		}
		
	}

}
