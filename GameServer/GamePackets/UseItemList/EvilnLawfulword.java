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


public class EvilnLawfulword implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		cur.recfame(1000);
		if(ItemID == 299010017){source.addWrite(Item.itemSpawnPacket(cur.getCharID(), 213062203, 1, cur.getlastknownX(), cur.getlastknownY(), source));}
		if(ItemID == 299010018){source.addWrite(Item.itemSpawnPacket(cur.getCharID(), 213062205, 1, cur.getlastknownX(), cur.getlastknownY(), source));}
		return true;
	}
}