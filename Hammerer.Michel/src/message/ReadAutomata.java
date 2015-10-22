package message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import messages.engine.Util;

public class ReadAutomata {

	SocketChannel sock;
	ByteBuffer lenBuf = ByteBuffer.allocate(4); // for reading the length of a message
	ByteBuffer msgBuf = null; // for reading a message
	static final int READING_LENGTH = 1;
	static final int READING_MSG = 2;
	int state = READING_LENGTH; // initial state 
	SelectionKey key;
	
	public ReadAutomata(SocketChannel socketChannel){
		this.sock=socketChannel;
	}
	
	public byte[] handleRead() throws IOException {
		
		if (state == READING_LENGTH){
			int nbread = sock.read(lenBuf);
			 if (nbread == -1) {
				 sock.close();
				 return null;
			 }
			if (lenBuf.remaining() == 0) {
				msgBuf = ByteBuffer.allocate(Util.readInt32(lenBuf.array(), 0));
				lenBuf=(ByteBuffer) lenBuf.position(0);
				state = READING_MSG;
			}
		} 
		
		if (state == READING_MSG) {
			sock.read(msgBuf);
			if (msgBuf.remaining() == 0){ // the message has been fully received
				  // deliver it"
				byte[] msg =msgBuf.array();
				msgBuf = null;
				state = READING_LENGTH;
				return msg;
			}
		}
		return null;
	}

}
