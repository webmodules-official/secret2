package chat.chatCommandHandlers;

import java.util.Iterator;
import java.util.Map;

import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class furyoff implements ChatCommandExecutor {
 

	public void execute(String[] parameters, Connection source) {
		System.out.println("Handling furyoff");
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		String CharName = (parameters[0]);
		
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[20];
		fury[0] = (byte)fury.length;
		fury[4] = (byte)0x05;
		fury[6] = (byte)0x6a;
		fury[8] = (byte)0x01;
		
		cur.furycheck = 0;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		
		fury[16] = (byte)0x00;
		cur.sendToMap(fury);
	 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury); 	
		System.out.println("DONE");
	}

}
