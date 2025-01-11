package GameServer.GamePackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import chat.ChatParser;

import Connections.Connection;
import ServerCore.ServerFacade;
import Encryption.Decryptor;
import Player.Charstuff;
import Player.Party;
import Player.Player;
import Player.PlayerConnection;
import Player.Character;
import Tools.BitTools;
import World.WMap;


public class Chat implements Packet {
	 private Map<String, Character> names = new HashMap<String, Character>();
	 private WMap wmap = WMap.getInstance();
	 
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}


	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		
		/*//PERFECT WAY TO KNOW WHATS WHAT !
		for(int i=0;i<decrypted.length;i++) { //System.out.print(decrypted[i]+" "); }
		//System.out.println(" | ");
		for(int i=0;i<decrypted.length;i++) {//System.out.printf("%02x ", (decrypted[i]&0xFF)); }*/

		Player tmplayer = ((PlayerConnection)con).getPlayer();
		Character current = tmplayer.getActiveCharacter();
		boolean isValid = false;
		
		byte[] name = current.getName().getBytes();
		byte[] chatRelay = new byte[decrypted[19]+44];
		byte[] chid = BitTools.intToByteArray(current.getCharID());
		
		chatRelay[0] = (byte)chatRelay.length;
		chatRelay[4] = (byte)0x05;
		chatRelay[6] = (byte)0x07;
		chatRelay[8] = (byte)0x05;
		
		for(int i=0;i<name.length;i++) {
			chatRelay[i+20] = name[i];
		}
		
		byte[] stuffz = new byte[] { (byte)0x01, (byte)0xfe, (byte)0x14, (byte)0x08, (byte)0x30, (byte)0x12, (byte)0x0c, (byte)0x00,  
	       (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00};
	       
		for(int i=0;i<stuffz.length;i++) {
			chatRelay[i+8] = stuffz[i];
		}
		
		for(int i=0;i<4;i++) {
			chatRelay[12+i] = chid[i]; // c5 = charID , aka this[12] <-----------------
		}
		
		chatRelay[17] = (byte)0x01;
		chatRelay[18] = decrypted[0]; // indicates chat type ( public , guild etc )
		
		byte[] msgbytes = new byte[decrypted[19]];
		
		for(int i=0;i<decrypted[19];i++) {
			chatRelay[i+44] = decrypted[i+23];
			msgbytes[i] = decrypted[i+23];
		}
		
		chatRelay[40] = decrypted[19];
		
	
		// Censor Protection system :DDDDD fuck them advertisers n watnot
		// checks a word all uppercase & lowercase, so no worrys about capitals
		String string = new String(msgbytes);
		String finalstring = string.toLowerCase();
		Iterator<Entry<String, String>> iterw = Charstuff.getInstance().CensoredWords.entrySet().iterator();
		while(iterw.hasNext()) {
			Entry<String, String> pairsw = iterw.next();
			//check if any of the words in hashmap is in this string.
			if(finalstring.contains(pairsw.getKey())){
				current.respondguildTIMED("Censor Protection System has dectected foul language : "+pairsw.getKey(), current.GetChannel()); return null;
			}

		}		
		
		if (6 == decrypted[0]) {	 // karma shout chat
			if(current.getLevel() < 48){current.respondguildTIMED("Only level 48+ can shout.", current.GetChannel());return null;}
			Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character tmp;
			while(iter.hasNext()) {
				Map.Entry<Integer, Character> pairs = iter.next();
				tmp = pairs.getValue();
				if(tmp != null && ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null){
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), chatRelay);
				}
			}
		}	
		
		if (5 == decrypted[0]) {	 // vending chat from external
			current.sendToMap(chatRelay); 
		}
		
		if (4 == decrypted[0]) {	 // vending chat from internal
			current.sendToMap(chatRelay); 
		}
		
		
		if (2 == decrypted[0]) { 	// party chat
			if(current.partyUID != 0){	
			Party pt = wmap.getParty(current.partyUID);
			Iterator<Map.Entry<Integer, Integer >> iter = pt.partymembers.entrySet().iterator();
			Character tmp;
			while(iter.hasNext()) {
			Map.Entry<Integer, Integer> pairs = iter.next();
			tmp = current.getWmap().getCharacter(pairs.getValue());
			if (tmp != null && tmp.getCharID() != current.getCharID() && tmp.partyUID == current.partyUID && tmp.partyUID != 0 && !tmp.ignorelist.containsValue(current.getLOGsetName()) && ServerFacade.getInstance().getConnectionByChannel(tmp.GetChannel()) != null) { //[ Prevents spam in party Chat ]
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), chatRelay); 	 	
			}else if(tmp.ignorelist.containsValue(current.getLOGsetName())){
				// if has on ignore list, do nothing.
			}
		  }
	     }
		}
		
		
		if (1 == decrypted[0]) {	 // whisper chat
			
			byte[] decryptedname = new byte[17];
			for(int i=0;i<17;i++) {
				decryptedname[i] = decrypted[2+i];
			}
			String CharName = BitTools.byteArrayToString(decryptedname); 

	        byte[] hisname = new String(CharName).getBytes();
	        
	        byte arr[] = hisname;
	        try {
	        		int i;
	        		for (i = 0; i < arr.length && arr[i] != 0; i++) { }
	        		String finalnameparsed = new String(arr, 0, i, "UTF-8");
	        		
	        		//System.out.println(" PARSED : " + finalnameparsed);
	        		
			
			Iterator<Map.Entry<Integer, Character >> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
			Character tmp;
			
			while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			tmp = pairs.getValue(); 
		
			if(finalnameparsed.equals(tmp.getLOGsetName()) && !tmp.ignorelist.containsValue(current.getLOGsetName())) {
			names.put(tmp.getLOGsetName(), tmp);
			Character anyplayer = names.get(finalnameparsed); // select player from string
			isValid = true;
					
			ServerFacade.getInstance().addWriteByChannel(anyplayer.GetChannel(), chatRelay); // send whisper					 
			}else if(finalnameparsed.equals(tmp.getLOGsetName()) && tmp.ignorelist.containsValue(current.getLOGsetName())){
				// do nothing
			}
			}
	        } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
			
			if(!isValid) {
				//System.out.println("Name doesnt exist!");
				
				//TODO: resond RED chat that name doesnt exist / not online
			}	
		}	
		
		if (0 == decrypted[0]) {	 // public chat
				Iterator<Integer> iter = current.getIniPackets().keySet().iterator();
					while(iter.hasNext()) {
						Integer plUid = iter.next();               
						if (plUid.intValue() != current.charID){
							Character ch = wmap.getCharacter(plUid.intValue());
							if(ch != null){
								if(!ch.ignorelist.containsValue(current.getLOGsetName())){
								ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), chatRelay);
								}else 
								if(ch.ignorelist.containsValue(current.getLOGsetName())){
									// dont send if is on ignore
								}
							}
						}
					}
			return null;
		}
		
		// guild chat
		if (current.guild != null && 3 == decrypted[0]) {	 // guild chat
		Iterator<Entry<Integer, Integer>> iter = current.guild.guildids.entrySet().iterator();
		while(iter.hasNext()) {
		Entry<Integer, Integer> pairs = iter.next();
		int CharID = pairs.getValue(); 
		if(wmap.CharacterExists(CharID)){
			Character tmp = wmap.getCharacter(CharID);
			if(tmp != null && tmp.getCharID() != current.getCharID()){
				ServerFacade.getInstance().addWriteByChannel(tmp.GetChannel(), chatRelay);
			}}
		  }
		}
		

		Character getplrforcheckifisgm = ((PlayerConnection)con).getActiveCharacter();  // go to Player.java tab (accounts etc)
		String meowisGM = getplrforcheckifisgm.getChargm();   			 // get gm from Player.java
		
		
			// Administrator \\
		/*{if(getplrforcheckifisgm.funcommands == 1)
		{
			 //System.out.print(" =====>Character: funcommands = " + getplrforcheckifisgm.funcommands +" : ");
			ChatParser.getInstance1().parseAndExecuteChatCommand(new String(msgbytes), con);
		}  */
		if(meowisGM.equals ("az"))
		{
			////System.out.print(" =====>Character: isgm = " + meowisGM +" : ");
		ChatParser.getInstance().parseAndExecuteChatCommand(new String(msgbytes), con);
		}  
			// Developer \\
		/*else if(meowisGM.equals ("3"))
		{
			////System.out.print(" =====>Character: isgm = " + meowisGM +" : ");
			ChatParser.getInstance().parseAndExecuteChatCommand(new String(msgbytes), con); // same as admin commands dafuq?
		}*/
			// Gamemaster \\
		else if(meowisGM.equals ("2"))
		{
			////System.out.print(" =====>Character: isgm = " + meowisGM +" : ");
			ChatParser.getInstance1().parseAndExecuteChatCommand(new String(msgbytes), con);
		}
			// GMH@ \\
		else if(meowisGM.equals ("1"))
		{
			////System.out.print(" =====>Character: isgm = " + meowisGM +" : ");
			ChatParser.getInstancegmh().parseAndExecuteChatCommand(new String(msgbytes), con);
		}
		else if(meowisGM.equals ("0"))
		{
			////System.out.print(" =====>Character: isgm = " + meowisGM +" : ");
			ChatParser.getInstanceplayer().parseAndExecuteChatCommand(new String(msgbytes), con);
		}
		else if(meowisGM.equals ("vince"))
		{
			////System.out.print(" =====>Character: isgm = " + meowisGM +" : ");
			ChatParser.getInstanceVINCE().parseAndExecuteChatCommand(new String(msgbytes), con);
		}
		
		return null;
	}

}
