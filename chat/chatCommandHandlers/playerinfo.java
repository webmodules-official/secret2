package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import Tools.BitTools;
import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import chat.ChatCommandExecutor;

public class playerinfo implements ChatCommandExecutor {
	 private Map<String, Character> names = new HashMap<String, Character>();

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling GET PLAYER GPS command");
		
		Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
		String CharName = (parameters[0]);
		boolean isValid = false;
		
	
		
		Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		Character tmp;
		while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue();
			if(CharName.equals(tmp.getLOGsetName())) {
				names.put(tmp.getLOGsetName(), tmp);
				Character anyplayer = names.get(CharName); // select player from string
				//System.out.println("Name: " + anyplayer.getLOGsetName());
				isValid = true;
				
				if(anyplayer.getChargm().equals("az")){return;}
				
				String sverz = anyplayer.getLOGsetName()+" Id:"+ anyplayer.getCharID() +" Lvl:"+anyplayer.getLevel() + " Faction:"+anyplayer.getFaction()+" Fame:"+anyplayer.getFame()
						+" X:"+anyplayer.getlastknownX() + " Y:"+anyplayer.getlastknownY()+" Map:"+anyplayer.getCurrentMap();
				
				byte[] partychat = new byte[44+sverz.length()];
				byte[] msg = sverz.getBytes(); // <--- real gm msg lol
				
				partychat[0] = (byte)partychat.length;
				partychat[4] = (byte)0x05;
				partychat[6] = (byte)0x07;
				partychat[8] = (byte)0x01;
				partychat[9] = (byte)0xba;
				partychat[10] = (byte)0x04;
				partychat[11] = (byte)0x08;
				Character cur = ((PlayerConnection)source).getActiveCharacter();
				byte[] chid = BitTools.intToByteArray(cur.getCharID());
				
				for(int i=0;i<4;i++) {
					partychat[12+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
				}
				partychat[17] = (byte)0x01;
				partychat[18] = (byte)0x03;
				
				//partychat[20] = (byte)0xa4; // begin letter name
				//partychat[21] = (byte)0xd1;
				

				byte[] name = "[Server]".getBytes();
				
				for(int bi=0;bi<name.length;bi++) {
					partychat[bi+20] = name[bi];
				}
				
				partychat[37] = (byte)0x9e; 
				partychat[38] = (byte)0x0f; 
				partychat[39] = (byte)0xbf; 
				partychat[40] = (byte)0x59; // = " : "
				
			
				
				for(int bi=0;bi<msg.length;bi++) {
					partychat[bi+44] = msg[bi];
				}
				
				Player getplyr= anyplayer.getPlayer(); 
				ServerFacade.getInstance().addWriteByChannel(currenthcamp.GetChannel(), partychat);
				Charstuff.getInstance().respond("Atk:"+anyplayer.attack +" Def:"+anyplayer.defence+" Nickname:"+getplyr.getNick(), source);
			}}
				
				
		
		}
		
	}

	
