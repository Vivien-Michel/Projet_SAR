package message;

import java.io.IOException;

import messages.engine.Engine;
import messages.engine.Server;

public class Pong{

	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);;
			Engine pong = new OwnEngine();
			int i;
			for(i=0; i< 10 ;i++){
				Server contract = pong.listen(port, new AcceptCallbackTest());
				port++;
			}
			pong.startEcho();
			pong.mainloop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
