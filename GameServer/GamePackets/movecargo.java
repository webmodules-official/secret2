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

public class movecargo implements Packet {

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
		int two = BitTools.byteToInt(decrypted[3]);   // to cargo slot 
		int three = BitTools.byteToInt(decrypted[4]); // to cargo height
		int four = BitTools.byteToInt(decrypted[5]);  // to cargo weight
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[24];
		fury[0] = (byte)0x18;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x2e;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		fury[16] = (byte)0x01;
		
		//cur.Cargo = cur.getCargSLOT(two);
		//cur.Cargostack = cur.getCargoSTACK(two);
		
		if(one != two){cur.respondguildTIMED("Select an empty cargo slot.", cur.GetChannel()); cur.KillInvFreeze();return null;}
		if(cur.getCargoSLOT(one) == 0){
			cur.KillInvFreeze();
			Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
			return null;
		}
		int finalstacktwo;
		//if(cur.cargo == cur.getcargoSLOT(two)){
		 //finalstacktwo = cur.getCargoSTACK(one) + cur.cargostack;// <-- problem moving item
		//}else{
		 finalstacktwo = cur.getCargoSTACK(one); 	
		//}
		cur.setCargoSTACK(two, finalstacktwo);	
		cur.setCargo(two, cur.getCargoSLOT(one), three, four, finalstacktwo);

		fury[18] = decrypted[0]; // from itemid
		fury[19] = decrypted[1]; 
		fury[20] = decrypted[2];
		
		fury[21] = decrypted[3]; // to itemid
		fury[22] = decrypted[4];
		fury[23] = decrypted[5];
		
		return fury;
	}
	
}
