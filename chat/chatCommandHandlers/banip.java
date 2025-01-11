package chat.chatCommandHandlers;


import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import timer.SystemTimer;

import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import Database.IPbanlistDAO;

import chat.ChatCommandExecutor;



public class banip implements ChatCommandExecutor {
	 private Map<String, Character> names = new HashMap<String, Character>();
	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling ban command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
				String CharName = (parameters[0]);
				boolean isValid = false;

				int banned = 1;
		
				
				Character cur = ((PlayerConnection)source).getActiveCharacter();
				
				
				//---------ban player----------\\
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
				isValid = true;
				
				String namei = anyplayer.getLOGsetName();
				
				Player getplyrusername = anyplayer.getPlayer();  // go to Player.java tab (accounts etc)
				String anycharactersIP = getplyrusername.getip(); 
				String anycharactersusername = getplyrusername.getUsername(); 
				String banreason = anyplayer.getPlayer().getip()+"-"+parameters[2];
				
				long banaxpire = System.currentTimeMillis() +SystemTimer.DaysToMiliseconds(Long.valueOf(parameters[1])); // how many days
						 CharacterDAO.putbanlolbyname(banned, banreason,banaxpire, anycharactersusername);
						 CharacterDAO.putipbanbyname(anycharactersIP, banreason);
						 anyplayer.leaveGameWorld();
						 ServerFacade.getInstance().finalizeConnection(anyplayer.GetChannel()); 
						 IPbanlistDAO.getipbanlist();

						 Charstuff.getInstance().respondguild("Character name "+namei+" banned for "+parameters[1]+"days.", cur.GetChannel());
						 CharacterDAO.setconsole(System.currentTimeMillis(), "Character "+namei+" banned for "+parameters[1]+" days. Reason: "+parameters[2]+". by "+cur.getLOGsetName() , "Server");
								
					}
				}
				
				if(!isValid) {
					//System.out.println("Name doesnt exist!");
					
					//TODO: resond party chat that name doesnt exist
				}
				
				//System.out.println("DONE");
		 }
   }

	