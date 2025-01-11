package GameServer.GamePackets;

import java.nio.ByteBuffer;

import timer.SystemTimer;
import timer.furyoff;
import timer.globalsave;

import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;

import Connections.Connection;
import Encryption.Decryptor;

public class Herbstart implements Packet {

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
		//System.out.println("");
		/*for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		System.out.println("");*/
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		cur.Proffession(decrypted);
		return null;
	}
	
}
