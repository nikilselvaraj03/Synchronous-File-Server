package root;

import java.io.IOException;

import root.service.ConnectionService;
import root.service.FileService;

public class Server {
	
	public static void console(String op, String character) {
		for(int i=0;i<10;i++)
			System.out.print(character);
		System.out.print(" "+op+" ");
		for(int i=0;i<10;i++)
			System.out.print(character);
		System.out.println("\n");
	}
	
	public static void main(String[] args) throws IOException {
		ConnectionService.init();
    }
	
}
