package GameServer.GamePackets.UseItemList;

import Connections.Connection;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;


public class Potion implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
			Character cur = ((PlayerConnection)source).getActiveCharacter();
	
			// HEAL 40/80/140/220/300 \\
			if(ItemID == 213010001 ||ItemID == 213010007){
			int finalhp = cur.hp + 40;	
			cur.setHp(finalhp);
			HPpotanimation(source);
			}
			if(ItemID == 213010002){
				int finalhp = cur.hp + 80;	
				cur.setHp(finalhp);
				HPpotanimation(source);
			}
			if(ItemID == 213010003){
				int finalhp = cur.hp + 140;	
				cur.setHp(finalhp);
				HPpotanimation(source);
			}
			if(ItemID == 213010006){
				int finalhp = cur.hp + 220;	
				cur.setHp(finalhp);
				HPpotanimation(source);
			}
			if(ItemID == 273000242){ // pot
				int finalhp = cur.hp + 300;	
				cur.setHp(finalhp);
				HPpotanimation(source);
			}
			if(ItemID == 273000237){ // cake
				int finalhp = cur.hp + 300;	
				cur.setHp(finalhp);
				HPpotanimation(source);
			}
			
			// MANA 40/80/140/220/300 \\
			if(ItemID == 213020001 ||ItemID == 213020007){
			int finalmana = cur.mana + 40;	
			cur.setMana(finalmana);
			MANApotanimation(source);
			}
			if(ItemID == 213020002){
			int finalmana = cur.mana + 80;	
			cur.setMana(finalmana);
			MANApotanimation(source);
			}
			if(ItemID == 213020003){
			int finalmana = cur.mana + 140;	
			cur.setMana(finalmana);
			MANApotanimation(source);
			}
			if(ItemID == 213020006){
			int finalmana = cur.mana + 220;	
			cur.setMana(finalmana);
			MANApotanimation(source);
			}
			if(ItemID == 273000243){ // pot
			int finalmana = cur.mana + 300;	
			cur.setMana(finalmana);
			MANApotanimation(source);
			}
			if(ItemID == 273000241){ // cake
				int finalmana = cur.mana + 300;	
				cur.setMana(finalmana);
				MANApotanimation(source);
			}
			return true;
	}

	private  void HPpotanimation(Connection source) {
		//System.out.println("Handling HEAL ANIMATION");
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		byte[] partychat = new byte[40];
		partychat[0] = (byte)0x28;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x05;
		partychat[8] = (byte)0x01;
		partychat[9] = (byte)0x98;
		partychat[10] = (byte)0x75;
		partychat[11] = (byte)0x2e;
		for(int i=0;i<4;i++) {
			partychat[12+i] = chid[i]; 
		}
		partychat[16] = (byte)0x32;
		partychat[17] = (byte)0xa7;
		partychat[18] = (byte)0x45;
		partychat[19] = (byte)0x10;
	
		partychat[37] = (byte)0x9e;
		partychat[38] = (byte)0x0f;
		partychat[39] = (byte)0xbf;
		cur.sendToMap(partychat);
	}
	
	private  void MANApotanimation(Connection source) {
		//System.out.println("Handling MANA ANIMATION");
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		byte[] partychat = new byte[40];
		partychat[0] = (byte)0x28;
		partychat[4] = (byte)0x05;
		partychat[6] = (byte)0x05;
		partychat[8] = (byte)0x01;
		partychat[9] = (byte)0x98;
		partychat[10] = (byte)0x75;
		partychat[11] = (byte)0x2e;
		for(int i=0;i<4;i++) {
			partychat[12+i] = chid[i]; 
		}
		partychat[16] = (byte)0x33;
		partychat[17] = (byte)0xa7;
		partychat[18] = (byte)0x45;
		partychat[19] = (byte)0x10;
	
		partychat[37] = (byte)0x9e;
		partychat[38] = (byte)0x0f;
		partychat[39] = (byte)0xbf;
		cur.sendToMap(partychat);
	}	
}