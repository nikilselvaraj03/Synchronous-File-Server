package root.handler;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import root.ClientBeApplication;
import root.service.MsgProcessor;

//import root.service.MsgProcessor;

public class MsgHandler implements Runnable {

	public byte[] msgBuffer;
	
	public MsgHandler(byte[] msgBuffer) {
		this.msgBuffer = msgBuffer;
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
		//System.out.println("msg handler");
		MsgProcessor processor = new MsgProcessor();
		try {
			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(msgBuffer));
			Map<String, Object> map = (Map) SerializationUtils.deserialize(msgBuffer);
			String client = (String) map.get("clientId");
			if(!((String) map.get("clientId")).equals(ClientBeApplication.clientId)) {
				processor.udpProcess(map);
			}
			else {
				//System.out.println("do nothing bruh");
				//processor.udpProcess(map);
			}
			
							
			iStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
