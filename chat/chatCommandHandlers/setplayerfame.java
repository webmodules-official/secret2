package chat.chatCommandHandlers;


import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import World.WMap;
import Player.Character;
import Connections.Connection;
import Database.CharacterDAO;

import chat.ChatCommandExecutor;



public class setplayerfame implements ChatCommandExecutor {
	 private Map<String, Character> names = new HashMap<String, Character>();
	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling change character Class command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
				String CharName = (parameters[0]);
				boolean isValid = false;
			
	
				//---------change player face----------\\
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				
				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				
				if(CharName.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(CharName); // select player from string
				//System.out.println("Name: " + anyplayer.getLOGsetName());
				isValid = true;
				
				anyplayer.setFame(Integer.parseInt(parameters[1]));
				anyplayer.sendToMap(anyplayer.extCharPacket());	
						//TODO: resond party chat that to player that his gm level has been changed
				
				}		 
				}
				
				
				if(!isValid) {
					//System.out.println("Name doesnt exist!");
					
					//TODO: resond party chat that name doesnt exist
				}
				
				//System.out.println("DONE");
		 }
   }

	