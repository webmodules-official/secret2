package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import Encryption.Decryptor;

public class ExternalGETVendingwindow implements Packet {
	private static int inc = 0;
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling externalget vending window ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);	
		Character Me = ((PlayerConnection)con).getActiveCharacter();
		byte[] target = new byte[4];
		for(int i=0;i<4;i++) {
			target[i] = decrypted[i]; 
		}
		Character Tplayer = WMap.getInstance().getCharacter(BitTools.byteArrayToInt(target));
		if(Tplayer != null && Tplayer.BoothStance == 1){
		Tplayer.ExternalGETVendingwindow(Me);
		}
		return null;
	}
	
}
