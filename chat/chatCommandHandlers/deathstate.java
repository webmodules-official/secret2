package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Player.lookuplevel;
import Tools.BitTools;
import Tools.StringTools;
import Connections.Connection;
import Database.CharacterDAO;
import chat.ChatCommandExecutor;

public class deathstate implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("LEVEL TEST!");
		

		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] cid = BitTools.intToByteArray(cur.getCharID());
		
		
		
		int setit = Integer.parseInt(parameters[0]);
		CharacterDAO.setlevel(setit,cur.charID); // save it DB
		byte[] msg = BitTools.shortToByteArray((short)setit);
		byte[] healpckt = new byte[40];
		healpckt[0] = (byte)0x28;
		healpckt[4] = (byte)0x05;
		healpckt[6] = (byte)0x20;
		healpckt[8] = (byte)0x01; 
		healpckt[9] = (byte)0x39; 
		healpckt[10] = (byte)0x07;
		healpckt[11] = (byte)0x08;
		
		for(int i=0;i<4;i++) {
			healpckt[12+i] = cid[i];
		}
	
		for(int i=0;i<msg.length;i++) {
			healpckt[16+i] = msg[i]; // level
		}
		
		
		healpckt[30] = (byte)0xa0; 
		healpckt[31] = (byte)0x41;

		healpckt[36] = (byte)0x00;// new exp upon lvl up??
		
		
		int statP = cur.getStatPoints();
		int skillP = cur.getSkillPoints();
		
		cur.setLevel(Integer.valueOf(setit));
		
		int lookuplevelstp = lookuplevel.getstatP(setit); 
		int lookuplevelskp = lookuplevel.getskillP(setit);
	
	final int setstp = 	statP + lookuplevelstp; // final value of statpoints
	final int setskp = 	skillP + lookuplevelskp; // final value of skillpoints
	 int hp = cur.getHp() + cur.getHp(); 
	 int mana = cur.getMana() + cur.getMana();
	 
	 
	 
	 cur.setStatPoints(Integer.valueOf(setstp));
	 cur.setSkillPoints(Integer.valueOf(setskp));
	 cur.setHp(Integer.valueOf(hp));
	 cur.setMana(Integer.valueOf(mana));
	
	 
	 byte[] fstp = BitTools.intToByteArray(setstp);
	 byte[] fskp = BitTools.intToByteArray(setskp);
	 byte[] fhp = BitTools.intToByteArray(hp);
	 byte[] fmana = BitTools.intToByteArray(mana);
	 
	 
		
	 for(int i=0;i<2;i++) {

			healpckt[18+i] = fstp[i]; // stat points
			
			healpckt[20+i] = fskp[i]; // skill points
			
			healpckt[24+i] = fhp[i]; // hp
			
			healpckt[28+i] = fmana[i]; // mana
		}
	 
	 	CharacterDAO.setstatpoints(setstp, cur.getCharID());
		CharacterDAO.setskillpoints(setskp,cur.getCharID());
		
		cur.sendToMap(healpckt);
		source.addWrite(healpckt);
		
		//System.out.println("DONE");
		cur.setexp(Integer.valueOf(0)); // has to be last to redo the whole damn thing
	}
	
}
