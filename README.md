FileServerMvn - Server code (moved from simple java project to maven structure)
Client beta - temporary code to check server connectivity
ClientBE - SpringBoot project where actual client code will be

Main thread (
	ExecutorService(UDP, TCP) - 2 fixed threads
)

. 2 fixed UDP and TCP threads keep listening for msgs 
. Every incoming msg/command will be handled by a new concurrent thread
. UDP & TCP handlers create multiple Thread(MsgHandler)


Paste ADSE folder on desktop

C1:
	private static final int outPort = 9453; //send
	public static final int inPort = 9555; //receive
C2:
	private static final int outPort = 9582; //send
	public static final int inPort = 9557; //receive


inside user interface folder
npm i
npm start

esc :wqexit