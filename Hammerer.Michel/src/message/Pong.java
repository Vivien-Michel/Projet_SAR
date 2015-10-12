package message;

import java.io.IOException;
import java.net.InetAddress;

import messages.engine.Engine;
import messages.engine.Server;

public class Pong{

	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			Engine pong = new OwnEngine();
			Server contract = pong.listen(port, new AcceptCallbackTest());
			pong.mainloop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
