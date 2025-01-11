package chat.chatCommandHandlers;

import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import Database.CharacterDAO;
import Player.Character;
import Player.PlayerConnection;
import World.WMap;
import chat.ChatCommandExecutor;


public class masssummon implements ChatCommandExecutor {

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling teleport command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}	
		Character getmyplrcon = ((PlayerConnection)source).getActiveCharacter();
		
		
		Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue();
		
		
				float gox = getmyplrcon.getlastknownX();
				float goy = getmyplrcon.getlastknownY();
				int gomap = getmyplrcon.getCurrentMap();
				String getcharnamewhomadethecommand = tmp.getLOGsetName();
			
				
				//---------tele player----------\\
				CharacterDAO.telexymapfloat(gox, goy, gomap, getcharnamewhomadethecommand);	
				//System.out.println("Summoned: " + getcharnamewhomadethecommand);
				//TODO: respond party chat "summoned everyone"
		}		
				
		}	
	}
		
		
  





	