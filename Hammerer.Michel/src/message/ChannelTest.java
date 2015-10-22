package message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import messages.engine.Channel;
import messages.engine.DeliverCallback;

public class ChannelTest extends Channel {
	
	private SocketChannel m_ch;
	private ReadAutomata readAutomata;
	private WriteAutomata writeAutomata;
	private DeliverCallback callback;
	
	public WriteAutomata getWriteAutomata() {
		return writeAutomata;
	}

	public ReadAutomata getReadAutomata() {
		return readAutomata;
	}

	public ChannelTest(SocketChannel m_ch) {
		this.m_ch = m_ch;
		readAutomata = new ReadAutomata(m_ch);
		writeAutomata = new WriteAutomata(m_ch);
	}

	@Override
	public void setDeliverCallback(DeliverCallback callback) {
		this.callback=callback;
	}
	
	public DeliverCallback getCallback() {
		return callback;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		try {
			return (InetSocketAddress) m_ch.getRemoteAddress();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void send(byte[] bytes, int offset, int length) {
		writeAutomata.write(bytes,offset,length);
	}

	@Override
	public void close() {
		try {
			m_ch.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
