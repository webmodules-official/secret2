package GameServer.GamePackets.UseItemList;

import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import Database.CharacterDAO;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Player.lookuplevel;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class Cleanse implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		Charstuff.getInstance().AddDot(cur.charID, 48, 0, 1, 15, 1, cur);
		
		return true;
	}
}