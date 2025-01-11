package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

import timer.SystemTimer;
import timer.furyoff;
import timer.globalsave;

import Player.Character;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import Encryption.Decryptor;

public class vendinglist implements Packet {
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
		//Character cur = ((PlayerConnection)con).getActiveCharacter();
		//cur.getPlayer().EL = 0;
		return null;
	}
	
}
