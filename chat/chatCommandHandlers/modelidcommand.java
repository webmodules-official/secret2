package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Player.Character;
import World.WMap;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class modelidcommand implements ChatCommandExecutor {
	private Map<String, Character> names = new HashMap<String, Character>();
	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Received chat command to set model ID!");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
				String CharName = (parameters[0]);
				boolean isValid = false;
			
	
				//---------change player model id----------\\
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				if(CharName.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(CharName); // select player from string
				//System.out.println("Name: " + anyplayer.getLOGsetName());
				isValid = true;
				
				anyplayer.setmodelid(Integer.parseInt(parameters[1]));
				anyplayer.sendToMap(anyplayer.extCharPacket());
						//TODO: Make query to mysql to save the MODELID into the DB
						//CharacterDAO.changeplayerface(Integer.parseInt(parameters[1]), anyplayer.getLOGsetName());
				}
				
				
				if(!isValid) {
					//System.out.println("Name doesnt exist!");
					
					//TODO: resond party chat that name doesnt exist
				}
				
				//System.out.println("DONE");
			
		}
	}

