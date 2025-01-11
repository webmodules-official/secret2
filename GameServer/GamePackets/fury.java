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

public class fury implements Packet {

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
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] fury = new byte[20];
		fury[0] = (byte)fury.length;
		fury[4] = (byte)0x05;
		fury[6] = (byte)0x6a;
		fury[8] = (byte)0x01;
		
		if(cur.furyactive == 1)
		{
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i]; 
		}
		fury[16] = (byte)0x01;
		cur.TimestampFury = System.currentTimeMillis();		
		cur.furycheck = 1;// allow fury to be used in attack.java
		cur.furyvalue = 0;// reset fury value to 0
		cur.furyactive= 0;// cant be re-activated
		cur.sendToMap(fury);
		}
		return fury;
	}
	
}
