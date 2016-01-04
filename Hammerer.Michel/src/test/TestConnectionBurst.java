package test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import message.AcceptCallbackTest;
import message.ConnectCallbackTest;
import message.OwnEngine;

public class TestConnectionBurst {
	private static final int NB_CLIENT = 5;
	private static final int NB_MESSAGE = 10000;
	private static List<OwnEngine> clients=new ArrayList<OwnEngine>(); 
	public static void main(String[] args) {
		try {
			int portInitial=1234;


			//Il est possible de changer le port de départ
			if(args !=null && args.length != 0){
				portInitial = Integer.parseInt(args[0]);
			}
			int port = portInitial;
			//Boucle de connexion de tous les clients entre eux
			for(int i = 0 ; i<NB_CLIENT;i++){
				int portTemp=port;
				OwnEngine client = new OwnEngine();
				client.listen(port, new AcceptCallbackTest());
				for(int j=0 ; j<i;j++){
					portTemp--;
					client.connect(InetAddress.getByName("localhost"), portTemp, new ConnectCallbackTest());
				}
				client.startEcho();
				new Thread(client).start();
				port++;
				clients.add(client);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Simulation de l'envoie de message par chaque client
			int k =0;
			while(k<NB_MESSAGE){
				int ID=0;
				for(OwnEngine client : clients){
					String msg = "client "+ID+" : "+k;
					System.out.println("[ENVOIE] "+msg);
					client.send(msg,0,msg.length());
					ID++;				
				}
				k++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
