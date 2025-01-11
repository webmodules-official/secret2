package chat.chatCommandHandlers;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import World.WMap;
import Player.Character;
import Player.Charstuff;
import Connections.Connection;

import chat.ChatCommandExecutor;



public class flagred implements ChatCommandExecutor {
	 private Map<String, Character> names = new HashMap<String, Character>();
	
	public void execute(String[] parameters, Connection source) {
				String CharName = (parameters[0]);
			
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				
				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				if(CharName.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(CharName); 
				//System.out.println("Name: " + anyplayer.getLOGsetName());
				anyplayer.flagred(); 
					}
				}
		 }
   }

	