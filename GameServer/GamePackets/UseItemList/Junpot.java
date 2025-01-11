package GameServer.GamePackets.UseItemList;

import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import Database.CharacterDAO;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.PlayerConnection;
import Player.lookuplevel;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class Junpot implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		cur.setstrength(10);
		cur.setdextery(10);
		cur.setvitality(10);
		cur.setintelligence(10);
		cur.setagility(10);
		cur.setStatPoints(5);
		for(int i=1;i<=cur.getLevel();i++){
			int lookuplevelstp = lookuplevel.getstatP(i);
			int setstp = cur.getStatPoints() + lookuplevelstp;
			cur.setStatPoints(setstp);	
		}	
		//System.out.println("FINAL STATS P"+	cur.getStatPoints());
		CharacterDAO.setarributesz(cur.getstrength(),cur.getdextery(),cur.getvitality(),cur.getintelligence(),cur.getagility(),cur.getCharID());
		CharacterDAO.setstatpoints(cur.getStatPoints(),cur.getCharID());
		return true;
	}
}