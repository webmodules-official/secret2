package GameServer.GamePackets.UseItemList;

import item.Item;

import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import Database.CharacterDAO;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class Showtoothers implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		////System.out.println("Handling Show to others");
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] itemid = BitTools.intToByteArray(ItemID);
		 
		byte[] partychat = new byte[40];
		partychat[0] = (byte)0x28;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x05;
		partychat[8] = (byte)0x01;
		partychat[9] = (byte)0x98;
		partychat[10] = (byte)0x75;
		partychat[11] = (byte)0x2e;
		for(int i=0;i<4;i++) {
			partychat[12+i] = chid[i]; 
			partychat[16+i] = itemid[i];
		}
		partychat[37] = (byte)0x9e;
		partychat[38] = (byte)0x0f;
		partychat[39] = (byte)0xbf;
		cur.sendToMap(partychat);
		return true;
	}
}