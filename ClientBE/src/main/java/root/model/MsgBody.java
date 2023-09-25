package root.model;

import root.ClientBeApplication;

public class MsgBody {

	private String clientId = ClientBeApplication.clientId;
	private int command; //every number denotes a command
	
	public MsgBody(){}
	
	public MsgBody(int command) {
		super();
		this.command = command;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public void parse() {
		
	}

	@Override
	public String toString() {
		return "{clientId=" + clientId + ",command=" + command + "}";
	}
	
	
}
