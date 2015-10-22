package message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class WriteAutomata {

	SocketChannel sock;
	ArrayList<ByteBuffer> messages = new ArrayList<ByteBuffer>(); // messages to
																	// send"
	ByteBuffer lenBuf = ByteBuffer.allocate(4); // for writing the length of a
												// message"
	ByteBuffer msgBuf = null; // for writing a message"
	static final int WRITING_LENGTH = 1;
	static final int WRITING_MSG = 2;
	int currentState = WRITING_LENGTH; // initial state "

	public WriteAutomata(SocketChannel socketChannel) {
		this.sock = socketChannel;
	}

	public SocketChannel getSock() {
		return sock;
	}

	public void write(byte[] data, int offset, int length) {
		messages.add(ByteBuffer.wrap(data));
	}

	public void handleWrite() throws IOException {
		if(!messages.isEmpty()){
			if (currentState == WRITING_LENGTH) {
				msgBuf = messages.get(0);
				lenBuf.position(0);
				lenBuf=lenBuf.putInt(0,msgBuf.remaining());
				sock.write(lenBuf);
				if (lenBuf.remaining() == 0) {
					currentState = WRITING_MSG;
				}
			}
			if (currentState == WRITING_MSG) {
				if (msgBuf.remaining() > 0) {
					sock.write(msgBuf);
				}
				if (msgBuf.remaining() == 0) { // the message has been fully sent"
					msgBuf = messages.remove(0);
					currentState = WRITING_LENGTH;
				}
			}
		}
	}

}
