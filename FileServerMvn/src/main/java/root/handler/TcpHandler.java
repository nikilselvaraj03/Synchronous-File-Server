package root.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import root.Server;
import root.service.ConnectionService;

public class TcpHandler implements Runnable{

	ServerSocket tcpSocket;
	DataInputStream input;
	DataOutputStream output;

	final ExecutorService tcpEecutor = Executors.newCachedThreadPool();

	public TcpHandler(ServerSocket tcpSocket) {
		this.tcpSocket = tcpSocket;
	}

	public TcpHandler() {}

	public void run() {
		Server.console("TCP THREAD BEGIN", "*");
		try {
			Socket socket = tcpSocket.accept();
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			while(true) { //listens for incoming commands
				try {
					String inputString = ""; 
					inputString = input.readUTF();
					Server.console("INCOMING TCP COMMAND", "~");
					Thread tcpT = new Thread(new MsgHandler(inputString, false));
					tcpEecutor.execute(tcpT);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
