package root.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import root.handler.TcpHandler;
import root.handler.UdpHandler;

public class ConnectionService {

	public static final int inPort1 = 9453; //receive
	public static final int outPort1 = 9555; //send
	
	public static final int inPort2 = 9582; //receive
	public static final int outPort2 = 9557; //send
	

	private static DatagramSocket udpSocket, udpSocket2;
	private static ServerSocket tcpSocket;
	public static ExecutorService mainExecutor;
	static DatagramPacket packet;
	static InetAddress ip;
	static Socket socket;
	static DataInputStream input;
	static DataOutputStream output;

	public static void init() {
		listener();
		sender();
	}

	public static void sender() {
		try {
			/*
			socket = new Socket("localhost", port2);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());	
			*/
			udpSocket2 = new DatagramSocket();
			packet = null;
			ip = InetAddress.getLocalHost();
			
			sendData("hi from server");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void listener() {
		try {
			mainExecutor = Executors.newFixedThreadPool(4);
			mainExecutor.execute(startListener(inPort1, false));
			mainExecutor.execute(startListener(inPort1, true));
			mainExecutor.execute(startListener(inPort2, false));
			mainExecutor.execute(startListener(inPort2, true));
			
			mainExecutor.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Thread startListener(int port, boolean isUdp) {
		Thread T = null;
		try {
			if(isUdp) {
				udpSocket = new DatagramSocket(port);
				T = new Thread(new UdpHandler(udpSocket));
			}
			else {
				tcpSocket = new ServerSocket(port);
				T = new Thread(new TcpHandler(tcpSocket));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return T;
	}

	//for udp
	public static void sendData(String msg) { //obj -> string -> byte
		byte msgBuffer[] = null;
		try {
			msgBuffer = msg.getBytes(); //msgService.compress2(msg);
			//packet = new DatagramPacket(msgBuffer, msgBuffer.length, ip, outPort1);

			udpSocket2.send(new DatagramPacket(msgBuffer, msgBuffer.length, ip, outPort1));
			udpSocket2.send(new DatagramPacket(msgBuffer, msgBuffer.length, ip, outPort2));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public static void sendData(byte[] msg) { //obj -> string -> byte
		try {
			//packet = new DatagramPacket(msg, msg.length, ip, outPort1);

			udpSocket2.send(new DatagramPacket(msg, msg.length, ip, outPort1));
			udpSocket2.send(new DatagramPacket(msg, msg.length, ip, outPort2));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
