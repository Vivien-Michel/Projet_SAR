package message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import messages.engine.AcceptCallback;
import messages.engine.ConnectCallback;
import messages.engine.Engine;
import messages.engine.Server;


public class OwnEngine extends Engine{
	
	InetAddress m_localhost;
	Selector m_selector;
	SocketChannel m_ch;
	SelectionKey m_key;
	int m_port;
	
	public void mainloop() {
		System.out.println("NioClient running");
		while (true) {
			try {

				m_selector.select();

				Iterator<?> selectedKeys = this.m_selector.selectedKeys().iterator();

				while (selectedKeys.hasNext()) {

					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					} else if (key.isAcceptable()) {
						handleAccept(key);

					} else if (key.isReadable()) {
						handleRead(key);

					} else if (key.isWritable()) {
						handleWrite(key);

					} else if (key.isConnectable()) {
						handleConnect(key);
					} else 
						System.out.println("  ---> unknown key=");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	private void handleConnect(SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	private void handleWrite(SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	private void handleRead(SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	private void handleAccept(SelectionKey key) {
		// TODO Auto-generated method stub
		
	}

	public Server listen(int port, AcceptCallback callback) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void connect(InetAddress hostAddress, int port,
			ConnectCallback callback) throws UnknownHostException,
			SecurityException, IOException {
		// TODO Auto-generated method stub
		
	}

}
