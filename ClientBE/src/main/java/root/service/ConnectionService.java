package root.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import root.handler.UdpHandler;
import root.model.MsgBody;

@Service
public class ConnectionService {

	private static final int outPort = 9453; //send
	public static final int inPort = 9555; //receive

	static DataInputStream input;
	static DataOutputStream output;
	static DatagramSocket udpSocket, udpSocket2;
	static DatagramPacket packet;
	static InetAddress ip;
	static Socket socket;
	static ObjectMapper mapper = new ObjectMapper();
	MsgService msgService = new MsgService();

	public static void init() {
		sender();
		listener();
	}
	
	public static void sender() {
		try {
			socket = new Socket("localhost", outPort);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());	
			udpSocket = new DatagramSocket();
			packet = null;
			ip = InetAddress.getLocalHost();
			
			sendCommand(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void listener() {
		try {
			udpSocket2 = new DatagramSocket(inPort);
			Thread listener = new Thread(new UdpHandler(udpSocket2));
			listener.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//for udp
	public static void sendData(String msg) { //obj -> string -> byte
		byte msgBuffer[] = null;
		try {
			msgBuffer = msg.getBytes(); //msgService.compress2(msg);
			packet = new DatagramPacket(msgBuffer, msgBuffer.length, ip, outPort);

			udpSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void sendData(byte[] msg) { //obj -> string -> byte
		try {
			packet = new DatagramPacket(msg, msg.length, ip, outPort);

			udpSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	//for tcp
	public static void sendCommand(int command) {
		try {
			MsgBody msg = new MsgBody(command);
			System.out.println("new "+msg);
			output.writeUTF(mapper.writeValueAsString(msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
