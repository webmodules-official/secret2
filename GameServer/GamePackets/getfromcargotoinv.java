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

public class getfromcargotoinv implements Packet {

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
		
		int one = BitTools.byteToInt(decrypted[0]);   // from item SLOT ( can be from anywhre like equip etc. )
		int two = BitTools.byteToInt(decrypted[1]);   // to inventory slot 
		int three = BitTools.byteToInt(decrypted[2]); // to inventory height
		int four = BitTools.byteToInt(decrypted[3]);  // to inventory weight
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[24];
		fury[0] = (byte)0x18;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x2d;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		fury[16] = (byte)0x01;
		
		//cur.inventory = cur.getInventorySLOT(two);
		//cur.invstack = cur.getInventorySTACK(two);
		
		if(cur.getInventorySLOT(two) != 0){cur.respondguildTIMED("Select an empty inventory slot.", cur.GetChannel()); cur.KillInvFreeze();return null;}
		if(cur.getCargoSLOT(one) == 0){
			cur.KillInvFreeze();
			Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
			return null;
		}
		int finalstacktwo;
		//if(cur.inventory == cur.getInventorySLOT(two)){
		 //finalstacktwo = cur.getCargoSTACK(one) + cur.invstack;// <-- problem moving item
		//}else{
		 finalstacktwo = cur.getCargoSTACK(one); 	
		//}
		cur.setInventorySTACK(two, finalstacktwo);	
		cur.setInventory(two, cur.getCargoSLOT(one), three, four, finalstacktwo);
		
		cur.CargoSLOT.remove(one); 
		cur.CargoHEIGHT.remove(one);
		cur.CargoWEIGHT.remove(one);
		cur.CargoSTACK.remove(one);

		fury[18] = decrypted[0];
		fury[19] = decrypted[1]; // itemid
		fury[20] = decrypted[2];
		fury[21] = decrypted[3];
		
		fury[22] = (byte)0x0f;
		fury[23] = (byte)0xbf;
		
		return fury;
	}
	
}
