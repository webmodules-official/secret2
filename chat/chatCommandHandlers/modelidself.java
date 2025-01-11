package chat.chatCommandHandlers;


import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class modelidself implements ChatCommandExecutor {
	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Received chat command to set model ID FOR SELF!");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		cur.setmodelid(Integer.parseInt(parameters[0]));	
		cur.sendToMap(cur.extCharPacket());
						//TODO: Make query to mysql to save the MODELID into the DB
						//CharacterDAO.changeplayerface(Integer.parseInt(parameters[1]), anyplayer.getLOGsetName());
				
				//System.out.println("DONE");
			
		}
	}

