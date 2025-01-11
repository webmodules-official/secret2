package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
import Tools.StringTools;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class getheal implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("putting hp mana stamina!");
		byte[] healpckt = new byte[32];
		boolean isValid = true;
		healpckt[0] = (byte)healpckt.length;
		healpckt[4] = (byte)0x05;
		healpckt[6] = (byte)0x35;
		healpckt[8] = (byte)0x08; 
		healpckt[9] = (byte)0x60; 
		healpckt[10] = (byte)0x22;
		healpckt[11] = (byte)0x45;
		
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] cid = BitTools.intToByteArray(cur.getCharID());
		
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
			if(!StringTools.isInteger(parameters[i])) {
				isValid = false;
			}
		}
		
		
		
		// ---  load stats from  db --\\
		Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
		int currenthp = currenthcamp.getHp();
		//System.out.println("current hp:" + currenthp);
		int currentmana = currenthcamp.getMana();
		//System.out.println("current mana:" + currentmana);
		int currentstamina = currenthcamp.getStamina();
		//System.out.println("current stamina:" + currentstamina);
		
		
		if(isValid) {
			//System.out.println("Healing the character");
			byte[] hp = BitTools.shortToByteArray((short)currenthp);  // put intstring here
			byte[] mana = BitTools.shortToByteArray((short)currentmana); // put intstring here
			byte[] stam = BitTools.shortToByteArray((short)currentstamina); // put intstring here
			
			healpckt[24] = hp[0];
			healpckt[25] = hp[1];
			
			healpckt[28] = mana[0];
			healpckt[29] = mana[1];
			
			healpckt[30] = stam[0];
			healpckt[31] = stam[1];			
		}
		
		healpckt[16] = (byte)0x03;
		healpckt[18] = (byte)0x02;
		
		for(int i=0;i<4;i++) {
			healpckt[12+i] = cid[i];
		}
		
		source.addWrite(healpckt);
	}
	
}
