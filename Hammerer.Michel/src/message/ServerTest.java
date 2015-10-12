package message;

import messages.engine.Server;

public class ServerTest extends Server {
	private int port;
	
	public ServerTest(int port) {
		this.port=port;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void close() {
	
	}

}
