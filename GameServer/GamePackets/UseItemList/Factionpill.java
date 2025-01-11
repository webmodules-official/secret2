package GameServer.GamePackets.UseItemList;

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


public class Factionpill implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		if (cur.getFaction() == 1){cur.setFaction(2); CharacterDAO.changeplayerfaction(2, cur.getLOGsetName());}
		else
		if (cur.getFaction() == 2){cur.setFaction(1); CharacterDAO.changeplayerfaction(1, cur.getLOGsetName());}	
		return true;
	}
}