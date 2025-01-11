package chat.chatCommandHandlers;


import java.util.Iterator;
import java.util.Map;

import World.WMap;
import Player.Character;
import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import chat.ChatCommandExecutor;


public class massbanplayer implements ChatCommandExecutor {

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling massBan command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
		//System.out.println(" ===>Banning everyone" );
		
		
				int banned = Integer.parseInt(parameters[0]);
				String banreason = (parameters[1]);
			
				
				//---------massban player----------\\
				Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				CharacterDAO.putbanlol(banned, banreason);
				while(iter.hasNext()) {
					Map.Entry<Integer, Character> pairs = iter.next();
					tmp = pairs.getValue();		
					tmp.leaveGameWorld();
					 ServerFacade.getInstance().finalizeConnection(tmp.GetChannel()); 
					//System.out.println(" ===>Banned player: " + tmp.getLOGsetName() );
				
			}
		}	
	}
		
		
  





	