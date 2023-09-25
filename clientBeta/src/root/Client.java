package root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		Scanner sc = new Scanner(System.in);
		
		Socket socket = new Socket("localhost", 9453);
		DataInputStream input = new DataInputStream(socket.getInputStream());
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		
		DatagramSocket udpSocket = new DatagramSocket();
		DatagramPacket packet = null;
		InetAddress ip = InetAddress.getLocalHost();
		byte msgBuffer[] = null;
		
		output.writeUTF("client 2 connected");
		
		while (true) {
			String msg = sc.nextLine();
			msgBuffer = msg.getBytes();
			packet = new DatagramPacket(msgBuffer, msgBuffer.length, ip, 9453);
			
			udpSocket.send(packet);
		}
	}
}
