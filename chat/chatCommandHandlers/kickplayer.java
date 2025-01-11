package chat.chatCommandHandlers;


import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Connections.Connection;
import Database.CharacterDAO;
import ServerCore.ServerFacade;

import chat.ChatCommandExecutor;



public class kickplayer implements ChatCommandExecutor {
	 private Map<String, Character> names = new HashMap<String, Character>();
	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling Kick command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
				String CharName = (parameters[0]);
			
				Character cur = ((PlayerConnection)source).getActiveCharacter();
				
				//---------kick player----------\\
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;

				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				if(CharName.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(CharName); // select player from string
				if(anyplayer.getChargm().equals("az")){return;}
				//System.out.println("Name: " + anyplayer.getLOGsetName());
				String namei = anyplayer.getLOGsetName();
						 anyplayer.leaveGameWorld();
						 ServerFacade.getInstance().finalizeConnection(anyplayer.GetChannel()); 
						
						 Charstuff.getInstance().respondguild("Character name "+namei+" kicked.", cur.GetChannel());
						 CharacterDAO.setconsole(System.currentTimeMillis(), "Character name "+namei+" kicked."+" by "+cur.getLOGsetName() , "Server");
							
						 
					}
				}
				
				//System.out.println("DONE");
		 }
   }

	