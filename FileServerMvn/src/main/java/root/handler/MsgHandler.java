package root.handler;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import root.service.MsgProcessor;

public class MsgHandler implements Runnable {

	public String msg;
	public byte[] msgBuffer;
	public boolean isUdp;
	
	public MsgHandler(Object data, boolean isUdp) {
		this.isUdp = isUdp;
		if(isUdp) 
			this.msgBuffer = (byte[]) data;
		else
			this.msg = (String) data;
	}
	
	public String toString(byte[] incomingMsg)
    {
        if(incomingMsg==null)
            return null;
        
        StringBuilder message = new StringBuilder();
        int i = 0;        
        while(incomingMsg[i]!=0) {
        	message.append((char) incomingMsg[i]);
            i++;
        }
        
        return message.toString();
    }
	
	public void run() { //to test concurrency add while loop with thread sleep
		MsgProcessor processor = new MsgProcessor();
		try {
			if(!isUdp) { //tcp
				processor.tcpProcess(msg);
			}
			else { //udp
				
				ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(msgBuffer));
				Map<String, Object> map = (Map) SerializationUtils.deserialize(msgBuffer);
				processor.udpProcess(map);
								
				iStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
