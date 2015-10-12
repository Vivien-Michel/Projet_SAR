package message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import messages.engine.AcceptCallback;
import messages.engine.Channel;
import messages.engine.ConnectCallback;
import messages.engine.Engine;
import messages.engine.Server;


public class OwnEngine extends Engine{
	
	Selector m_selector;
	Map<SelectionKey, Channel> listKey= new HashMap<SelectionKey, Channel>();
	SelectionKey m_sk;
	int m_port;
	// Automata to write message
	WriteAutomata writeAutomata;
	// Automata to read message
	ReadAutomata readAutomata;
	// The message to send to the server
	String msg="tesrgqfvrssrgrgrqgrqggzrZRGSAAst";
	
	public OwnEngine() throws IOException {
		 m_selector = SelectorProvider.provider().openSelector();
	}

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
		SocketChannel socketChannel = (SocketChannel) key.channel();

		try {
			socketChannel.finishConnect();
		} catch (IOException e) {
			// cancel the channel's registration with our selector
			System.out.println(e);
			key.cancel();
			return;
		}
		
		// when connected, send a message to the server 
		Channel channel = listKey.get(key);
		channel.send(msg.getBytes(), 0, msg.getBytes().length);
		key.interestOps(SelectionKey.OP_WRITE);
		
	}

	private void handleWrite(SelectionKey key) {
		try {
			writeAutomata.handleWrite();			
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		key.interestOps(SelectionKey.OP_READ);
	}

	private void handleRead(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
	    int nbread = 0;
	    try {
	    	msg=readAutomata.handleRead();
	    } catch (IOException e) {
	      // the connection as been closed unexpectedly, cancel the selection and close the channel
	      key.cancel();
	      try {
			socketChannel.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      return;
	    }
	    if (nbread == -1) {
	      // the socket has been shutdown remotely cleanly"
	      try {
			key.channel().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      key.cancel();
	      return;
	    }
	    if(msg != null){
	    	System.out.println("Client: " +msg);
	    	Channel channel = listKey.get(key);
			channel.send(msg.getBytes(), 0, msg.getBytes().length);
	    }
		
	}

	private void handleAccept(SelectionKey key) {
		SocketChannel socketChannel = null;
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		try {
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			
		} catch (IOException e) {
			// as if there was no accept done
			return;
		}

		// be notified when there is incoming data 
		try {
			SelectionKey m_key = socketChannel.register(this.m_selector, SelectionKey.OP_READ);
			Channel channel = new ChannelTest(socketChannel);
			listKey.put(m_key, channel);
		} catch (ClosedChannelException e) {
			handleClose(socketChannel);
		}
		
		
	}

	private void handleClose(SocketChannel socketChannel) {
		socketChannel.keyFor(m_selector).cancel();
		try{
			socketChannel.close();
		} catch (IOException e) {
			//nothing to do, the channel is already closed
		}
		
	}

	public Server listen(int port, AcceptCallback callback) throws IOException {
		ServerTest server = new ServerTest(port);
		m_sk = server.getSocket().register(m_selector, SelectionKey.OP_ACCEPT);
		//callback.accepted(server, channel);
		return server;
	}

	public void connect(InetAddress hostAddress, int port,
			ConnectCallback callback) throws UnknownHostException,
			SecurityException, IOException {
		SocketChannel m_ch;
		m_ch = SocketChannel.open();
	    m_ch.configureBlocking(false);
	    m_ch.socket().setTcpNoDelay(true);
	    
	    SelectionKey m_key;
	    // be notified when the connection to the server will be accepted
	    m_key = m_ch.register(m_selector, SelectionKey.OP_CONNECT);
	    
	    // request to connect to the server
	    m_ch.connect(new InetSocketAddress(hostAddress, port));
	    Channel channel = new ChannelTest(m_ch);
	    listKey.put(m_key, channel);
	    callback.connected(channel);
	}

}
