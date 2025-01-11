package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class DeleteItem implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
		
	}

	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling delete");
		
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}

		int zero = BitTools.byteToInt(decrypted[0]);
		int one = BitTools.byteToInt(decrypted[1]);
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(zero == 2){cur.KillInvFreeze(); return null;}
		cur.DeleteItemNOMESSAGE(one);
		return null;
	}
	
}
