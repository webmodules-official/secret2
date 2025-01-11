package GameServer.GamePackets;

import java.nio.ByteBuffer;

import timer.SystemTimer;
import timer.furyoff;
import timer.globalsave;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Encryption.Decryptor;

public class putcargofrominv implements Packet {

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
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		
		
		int one = BitTools.byteToInt(decrypted[1]);   // from item SLOT ( can be from anywhre like equip etc. )   
		int two = BitTools.byteToInt(decrypted[2]); // to inventory slot
		int three = BitTools.byteToInt(decrypted[3]);  // to inventory weight
		int four = BitTools.byteToInt(decrypted[4]);  // to inventory weight
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[24];
		fury[0] = (byte)0x18;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x2c;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		fury[16] = (byte)0x01;

		if(decrypted[0] == 1) 
		{
			if(cur.getInventorySLOT(one) == 0){
				cur.DeleteItemNOMESSAGE(one); 
				Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
				return null;
			}	
			
		//cur.cargo = cur.getCargoSLOT(two);	
		//cur.cargostack = cur.getCargoSTACK(two);
		if(cur.getCargoSLOT(two) != 0){cur.respondguildTIMED("Select an empty cargo slot.", cur.GetChannel()); cur.KillInvFreeze(); return null;}
		if(cur.CargoSLOT.size() >= 118){cur.respondguildTIMED("Cargo is Full.", cur.GetChannel()); cur.KillInvFreeze(); return null;}
		
		cur.setCargoSLOT(two, cur.getInventorySLOT(one)); 
		cur.setCargoHEIGHT(two, three);
		cur.setCargoWEIGHT(two, four);
		
		int finalstacktwo;
		//if(cur.cargo == cur.getCargoSLOT(two)){
		// finalstacktwo = cur.getCargoSTACK(two) + cur.getInventorySTACK(one);// <-- problem moving item
		//}else{
		 finalstacktwo = cur.getInventorySTACK(one); 	
		//}
		cur.setCargoSTACK(two, finalstacktwo);
		
		cur.DeleteInvItem(one); 
		}
		
		if(decrypted[0] == 2) // move 2nd ITEM inventory slot ( LIKE REPLACE ITEM BLUE blockje je weet )
		{
			//Charstuff.getInstance().respondguildTIMED("Double switching to cargo is currently disabled.", cur.GetChannel());cur.ClearInv();
			return null;
			/*System.out.println("cur.inventory == "+cur.inventory);
			System.out.println("cur.inventory2 == "+cur.inventory2);
			System.out.println("cur.cargo == "+cur.cargo);
			System.out.println("cur.cargo2 == "+cur.cargo2);*/
			// replace code here
			/*int finalstacktwo = 0;
		if (cur.inventory != 0){ 
			cur.cargo = cur.getCargoSLOT(two); 
			cur.setCargoSLOT(two, cur.inventory); 
			if(cur.inventory == cur.cargo){
			 finalstacktwo = cur.getCargoSTACK(two) + cur.invstack;// <-- problem moving item
			}else{
				cur.cargostack2 = cur.getCargoSTACK(two);
			 finalstacktwo = cur.invstack; 	
			}
			 cur.invstack = 0;
			cur.inventory = 0;
			}
		else 
		if (cur.inventory2 != 0){ 
			cur.cargo = cur.getCargoSLOT(two); 
			cur.setCargoSLOT(two, cur.inventory2);
			if(cur.inventory2 == cur.cargo){
			 finalstacktwo = cur.getCargoSTACK(two) + cur.invstack2;// <-- problem moving item
			}else{
				cur.cargostack2 = cur.getCargoSTACK(two);
			 finalstacktwo = cur.invstack2; 	
			}
			 cur.invstack2 = 0;
			cur.inventory2 = 0;
			}
		else 
		if(cur.cargo != 0) {
			cur.cargo2 = cur.getCargoSLOT(two); 
			cur.setCargoSLOT(two, cur.cargo); 
			System.out.println(cur.cargo+" == "+cur.cargo2);
			if(cur.cargo == cur.cargo2){
			 finalstacktwo = cur.getCargoSTACK(two) + cur.cargostack;// <-- problem moving item
			}else{
				cur.cargostack2 = cur.getCargoSTACK(two);
			 finalstacktwo = cur.cargostack; 
			}
			 cur.cargostack = 0;
			cur.cargo = 0;
			}// SET inevntory slot -> equip itemid by equip slot
		else											
		if(cur.cargo2 != 0){ 
			cur.cargo = cur.getCargoSLOT(two); 
			cur.setCargoSLOT(two, cur.cargo2);
			System.out.println(cur.cargo2+" == "+cur.cargo);
			if(cur.cargo2 == cur.cargo){ 
			 finalstacktwo = cur.getCargoSTACK(two) + cur.cargostack2;// <-- problem moving item
			}else{
				cur.cargostack = cur.getCargoSTACK(two);
			 finalstacktwo = cur.cargostack2;
			}
			cur.cargostack2 = 0;
			cur.cargo2 = 0;
			}
		
			cur.setCargoHEIGHT(two, three);
			cur.setCargoWEIGHT(two, four);
			cur.setCargoSTACK(two, finalstacktwo);
			//cur.DeleteInvItem(one); */
		}

		fury[18] = decrypted[0];
		fury[19] = decrypted[1]; 
		fury[20] = decrypted[2]; 
		fury[21] = decrypted[3];
		fury[22] = decrypted[4];
		fury[23] = (byte)0x2e;
		
		return fury;
	}
	
}
