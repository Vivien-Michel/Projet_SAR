package test;

import java.io.IOException;
import java.net.InetAddress;

import message.ConnectCallbackTest;
import message.OwnEngine;
import messages.engine.Engine;

public class Ping{

	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			Engine ping = new OwnEngine();
			ping.connect(InetAddress.getByName("localhost"), port, new ConnectCallbackTest());
			ping.startEcho();
			ping.mainloop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
