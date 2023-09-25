package root.handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import root.ClientBeApplication;

public class UdpHandler implements Runnable{

	DatagramSocket udpSocket;
	final ExecutorService udpExecutor = Executors.newCachedThreadPool();
	
	public UdpHandler(DatagramSocket udpSocket) {
		this.udpSocket = udpSocket;
	}
	
	public UdpHandler(){}
	
	
	public void run() {
		byte[] incomingMsg = new byte[100000];
        DatagramPacket packet = null;
        ClientBeApplication.console("UDP LISTENER THREAD BEGIN", "*");
        while (true) { //listens for incoming msgs
            try {
            	packet = new DatagramPacket(incomingMsg, incomingMsg.length);
            	udpSocket.receive(packet);
				ClientBeApplication.console("INCOMING UDP MSG", "~");
				Thread udpT = new Thread(new MsgHandler(incomingMsg));				
				udpExecutor.execute(udpT);
				
	            incomingMsg = new byte[100000];
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}

}
