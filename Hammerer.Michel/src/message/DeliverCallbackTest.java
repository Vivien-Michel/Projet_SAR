package message;

import messages.engine.Channel;
import messages.engine.DeliverCallback;

public class DeliverCallbackTest implements DeliverCallback {

	public void deliver(Channel channel, byte[] bytes) {
		String msg=new String(bytes);
		System.out.println("Message : "+msg +" on channel " + channel.toString());
	}

}
