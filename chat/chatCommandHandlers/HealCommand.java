package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
import Tools.StringTools;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class HealCommand implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Received chat command to heal up!");
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
		
		
				// ---  set stats to current  db --\\
				Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
				int setcurrenthp = Integer.parseInt(parameters[0]);
				//System.out.println("current hp:" + setcurrenthp);
				currenthcamp.setHp(setcurrenthp);
				
				int setcurrentmana = Integer.parseInt(parameters[1]);
				//System.out.println("current mana:" + setcurrentmana);
				currenthcamp.setMana(setcurrentmana);
				
				int setcurrentstamina = Integer.parseInt(parameters[2]);
				//System.out.println("current stamina:" + setcurrentstamina);
				currenthcamp.setStamina(setcurrentstamina);
				
				

		if(isValid) {
			//System.out.println("Healing the character");
			byte[] hp = BitTools.shortToByteArray((short)setcurrenthp);
			byte[] mana = BitTools.shortToByteArray((short)setcurrentmana);
			byte[] stam = BitTools.shortToByteArray((short)setcurrentstamina);
			
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
