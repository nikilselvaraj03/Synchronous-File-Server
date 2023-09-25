package root.model;

public class TcpCommandModel {
	
	private String clientId;
	private int command; //every number denotes a command
	
	public TcpCommandModel(){}

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
	
}
// {"clientId":"client_1","command":23}