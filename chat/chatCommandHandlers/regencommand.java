package chat.chatCommandHandlers;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class regencommand implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Regen command activated");
	
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

		
		
		int regenhp = 160;
		while(regenhp < 3250)
		{
			try {
				Thread.sleep(2000);
				
				
				
				
				Character currenthcamp = ((PlayerConnection)source).getActiveCharacter();
				currenthcamp.setHp(regenhp);
				currenthcamp.setMana(regenhp);
				currenthcamp.setStamina(regenhp);
				
				
				
				if(isValid) {
					byte[] hp = BitTools.shortToByteArray((short)regenhp);  // put intstring here
					byte[] mana = BitTools.shortToByteArray((short)regenhp); // put intstring here
					byte[] stam = BitTools.shortToByteArray((short)regenhp); // put intstring here
					
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

			
				//System.out.println("Adding + 10 health"+ regenhp);
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;// <-- +20 total
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;// <-- +20 total
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;// <-- +20 total
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;// <-- +20 total
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;// <-- +100 total
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;// <-- +20 total
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;// <-- +20 total
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;
				regenhp++;// <-- +20 total
				
				
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
			}
		}

		
		
	}
	
}
