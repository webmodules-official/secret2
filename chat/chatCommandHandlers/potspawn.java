package chat.chatCommandHandlers;

import item.Item;
import Player.PlayerConnection;
import Player.Character;
import Tools.StringTools;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class potspawn implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
				Character cur = ((PlayerConnection)source).getActiveCharacter();;
				
				source.addWrite(Item.itemSpawnPacket(cur.getCharID(), 213010002, 9001, cur.getlastknownX(), cur.getlastknownY(), source));
				source.addWrite(Item.itemSpawnPacket(cur.getCharID(), 213020002, 9001, cur.getlastknownX(), cur.getlastknownY(), source));
	}
}
		


