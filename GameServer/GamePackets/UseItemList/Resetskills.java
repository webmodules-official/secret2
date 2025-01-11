package GameServer.GamePackets.UseItemList;

import item.Item;

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


public class Resetskills implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
				////System.out.println("Handling skill reset");
			Character cur = ((PlayerConnection)source).getActiveCharacter();
			
			
			if(ItemID == 273001227 && cur.getLevel() >= 12 && cur.getLevel() <= 80){}
			else
			if(ItemID == 213060101 && cur.getLevel() >= 12 && cur.getLevel() <= 80){}
			else
			if(ItemID == 213062383 && cur.getLevel() >= 81 && cur.getLevel() <= 160){}
			else
			if(ItemID == 283000005){}
			else{return false;}		
			
			cur.skills.clear();
			cur.skillbar.clear();
			cur.TempPassives.clear();
			cur.setSkillPoints(7);
			for(int i=1;i<=cur.getLevel();i++){
				//System.out.println("Count"+i);
				int lookuplevelskp = lookuplevel.getskillP(i);
				int setskp = cur.getSkillPoints() + lookuplevelskp;
				cur.setSkillPoints(setskp);	
			}	
			cur.statlist();
			////System.out.println("FINAL SKILL P"+	cur.getSkillPoints());
			CharacterDAO.setskillpoints(cur.getSkillPoints(),cur.getCharID());
			return true;
	}
}