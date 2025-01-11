package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import chat.ChatCommandExecutor;

public class removeitem implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		int zero = Integer.valueOf(parameters[0]);
		int one = Integer.valueOf(parameters[1]);
		int SLOT = Integer.valueOf(parameters[2]);
				System.out.println("delete item: "+zero +" "+one +" "+ SLOT);
				byte[] chid = BitTools.intToByteArray(cur.getCharID());

				byte[] delete = new byte[20];
				delete[0] = (byte)delete.length;
				delete[4] = (byte)0x04;
				delete[6] = (byte)0x15;
					
				for(int i=0;i<4;i++) {
					delete[12+i] = chid[i];
				}
					
				delete[16] = (byte)0x01; 
				delete[17] = (byte)0x01;
				delete[18] = (byte)one;
				delete[19] = (byte)SLOT;
				 // remove inventory item by slot
				/*cur.InventorySLOT.remove(Integer.valueOf(one)); 
				cur.InventoryHEIGHT.remove(Integer.valueOf(one));
				cur.InventoryWEIGHT.remove(Integer.valueOf(one));
				cur.InventorySTACK.remove(Integer.valueOf(one));*/
				ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), delete); 
	
	}

}
