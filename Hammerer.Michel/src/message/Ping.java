package message;

import java.io.IOException;
import java.net.InetAddress;

import messages.engine.Engine;

public class Ping{

	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			Engine ping = new OwnEngine();
			ping.connect(InetAddress.getByName("localhost"), port, new ConnectCallbackTest());
			ping.mainloop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
