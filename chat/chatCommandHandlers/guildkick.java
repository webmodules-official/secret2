package chat.chatCommandHandlers;


import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Tools.BitTools;
import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;

import chat.ChatCommandExecutor;



public class guildkick implements ChatCommandExecutor {
	 private Map<String, Character> names = new HashMap<String, Character>();
	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling guildkick command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}
				String CharName = (parameters[0]);
				
				
				Character cur = ((PlayerConnection)source).getActiveCharacter();
				boolean isValid = false;
				

				//---------guild kick player----------\\
				/*Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
				Character tmp;
				if(cur.getGuildmasterid() == cur.getCharID()){	
				while(iter.hasNext()){
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue(); 
				if(tmp.getGuildID() == cur.getGuildID()){
				//Charstuff.getInstance().respond("Player:"+CharName+" has been kicked from the guild by "+ cur.getLOGsetName(), tmp, source);
				if(CharName.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(CharName); // select player from string
				////System.out.println("Name: " + anyplayer.getLOGsetName());
				isValid = true;
				
				anyplayer.setGuildID(0);	
				anyplayer.setGuildtype(0);
				anyplayer.setguildname("");
				anyplayer.setGuildfame(0);
				anyplayer.setGuildgold(0);
				anyplayer.setGuildhat(0);
				anyplayer.setGuildicon(0);
				anyplayer.setguildnews("");
				anyplayer.sendToMap(anyplayer.extCharGuild());
				byte[] chid = BitTools.intToByteArray(anyplayer.getCharID());
				byte[] fury = new byte[56];
				fury[0] = (byte)0x38;
				fury[4] = (byte)0x04;
				fury[6] = (byte)0x3d;
				fury[8] = (byte)0x01;
				for(int i=0;i<4;i++){
					fury[12+i] = chid[i]; // charid
				}
				fury[16] = (byte)0x01;
				fury[18] = (byte)0x00;
				fury[19] = (byte)0x00;
				fury[44] = (byte)0x68;
				byte[] newgold = BitTools.LongToByteArrayREVERSE(cur.getgold());
				for(int i=0;i<newgold.length;i++){
					fury[48+i] = newgold[i];
				}
				ServerFacade.getInstance().addWriteByChannel(anyplayer.GetChannel(), fury);	
				CharacterDAO.putguildinchartable(0, anyplayer.charID);
					}
				  }
				}
	
				if(!isValid) {
					Charstuff.getInstance().respond("Player doesnt exist", source);	
				}
			}
				else{ Charstuff.getInstance().respond("Only GuildMasters can kick players.", source); }	
				//System.out.println("DONE");
		 }*/
	}
   }

	