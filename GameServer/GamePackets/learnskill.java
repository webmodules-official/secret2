package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Player.skilldata;
import Player.skillpasives;
import Player.skillpointscost;
import Tools.BitTools;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class learnskill implements Packet {

	
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling learnskill");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		
	/*  //PERFECT WAY TO KNOW WHATS WHAT !
		for(int i=0;i<decrypted.length;i++) { //System.out.print(decrypted[i]+" "); }
		//System.out.println(" | ");
		for(int i=0;i<decrypted.length;i++) {//System.out.printf("%02x ", (decrypted[i]&0xFF)); }
		*/
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[32];
		
		fury[0] = (byte)fury.length;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x29;
		fury[8] = (byte)0x01;
		
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		fury[16] = (byte)0x01;
		
		fury[18] = (byte)0x06;
		fury[19] = (byte)0x08;
		fury[20] = decrypted[0];
		
		fury[24] = decrypted[3]; // skill ids i guess ??
		fury[25] = decrypted[4];
		fury[26] = decrypted[5];
		fury[27] = decrypted[6];
	
		fury[30] = (byte)0x39;
		fury[31] = (byte)0x08;

		byte[] skillid = new byte[4];
		for(int i=0;i<4;i++) {
			skillid[i] = decrypted[4+i];
		}
		
		int skillidz = BitTools.byteArrayToInt(skillid); 
		
		int skilllevel = skilldata.getskilllevel(skillidz);
		if(skilllevel > cur.getLevel()){		 /*Charstuff.getInstance().respondguild(learnskill: " +skilllevel+" is smaller then ", cur.GetChannel());*/return null;}

		int skillpointscostz = skillpointscost.getskillpointscost(skillidz); 
		int finalSPC = cur.getSkillPoints() - skillpointscostz;
		if(finalSPC < 0){;return null;}
	
		
		CharacterDAO.setskillpoints(finalSPC,cur.getCharID());
		cur.setSkillPoints(finalSPC);
		
	    byte[] finalSPCbyte = BitTools.intToByteArray(finalSPC);
		for(int i=0;i<2;i++) {
			fury[28+i] = finalSPCbyte[i]; // how many skill points are in the pot?
		}
		//System.out.println("skillidz:"+skillidz);
		 //if its a passive buff
		 if(skillpasives.tryskillpassives(skillidz)){
			 
			String e = skillpasives.getskillpassives(skillidz);
			String[] Passive = e.split(",");	
					
			//TypeID (what cathegory is this passive)
			int TypeID = Integer.valueOf(Passive[0]); 
			int Value1 = Integer.valueOf(Passive[1]); 
			int Value2 = Integer.valueOf(Passive[2]); 
			
			//System.out.println("TypeID:"+TypeID+" Value1:"+Value1+" Value2:"+Value2);
			
			// If type == HP
			if(TypeID == 1){cur.setTempPassives(1,Value1);}
			
			// If type == Damage
			if(TypeID == 2){cur.setTempPassives(2,Value1);}
			
			// If type == Life Recovery
			if(TypeID == 3){cur.setTempPassives(3,Value1);}
			
			// If type == Defence 
			if(TypeID == 4){cur.setTempPassives(4,Value1);}
			
			// If type == Critical Chance Rate
			if(TypeID == 6){cur.setTempPassives(6,Value1);}
			
			// If type == Attack Distance
			if(TypeID == 7){cur.setTempPassives(7,Value1);}
			
			// If type == Critical Bonus Damage
			if(TypeID == 8){cur.setTempPassives(8,Value1);}
			
			// If type == Mana
			if(TypeID == 9){cur.setTempPassives(9,Value1);}
			
			// If type == Defence + HP
			if(TypeID == 10){cur.setTempPassives(10,Value1); cur.setTempPassives(100,Value2);} // + Value 2
			
			// If type == Damage Succes Rate
			if(TypeID == 11){cur.setTempPassives(11,Value1);}
			
			// If type == Mana Recovery
			if(TypeID == 12){cur.setTempPassives(12,Value1);}	 
			
			cur.statlist();
		 }

		cur.setLearnedSkill((int)decrypted[0], skillidz); 
		// Leanring skills after 30sec - 1min = no bug on learning skills 
		
		//System.out.println("DONE");
		return fury;
	}
	
}
