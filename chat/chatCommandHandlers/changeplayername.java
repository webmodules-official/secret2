package chat.chatCommandHandlers;


import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import World.WMap;
import Player.Character;
import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;

import chat.ChatCommandExecutor;



public class changeplayername implements ChatCommandExecutor {
	 private Map<String, Character> names = new HashMap<String, Character>();
	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling Kick command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
				String CharName = (parameters[0]);
				String wtf = CharName + " has changed his name to " + parameters[1];
				
	
				
			
				byte[] gmsg = new byte[45+wtf.length()];
				byte[] msg = wtf.getBytes(); // <-- real shou message
				boolean isValid = false;
				
				gmsg[0] = (byte)gmsg.length;
				gmsg[4] = (byte)0x05;
				gmsg[6] = (byte)0x07;
				gmsg[8] = (byte)0x01;
				gmsg[17] = (byte)0x01;
				gmsg[18] = (byte)0x06;
				gmsg[20] = (byte)0xa4;
				gmsg[21] = (byte)0xd1;


				byte[] name = "[Server]".getBytes();
				
				for(int b=0;b<name.length;b++) {
					gmsg[b+22] = name[b];
				}
				
				gmsg[40] = (byte)0x44; // = " : "
				
				for(int b=0;b<msg.length;b++) {
					gmsg[b+44] = msg[b];
				}
				
			


				
				//---------change player name----------\\
				Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;

				while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), gmsg); // global message
				if(CharName.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(CharName); // select player from string
				//System.out.println("Name: " + anyplayer.getLOGsetName());
				isValid = true;
				
				anyplayer.setName(parameters[1]);
				anyplayer.setLOGsetName(parameters[1]);
				CharacterDAO.changeplayername(parameters[1], parameters[0]);
				anyplayer.sendToMap(anyplayer.extCharPacket());
						
						//TODO: resond party chat that to player that his name has been changed
						 
					}
				}
				
				if(!isValid) {
					//System.out.println("Name doesnt exist!");
					
					//TODO: resond party chat that name doesnt exist
				}
				
				//System.out.println("DONE");
		 }
   }

	