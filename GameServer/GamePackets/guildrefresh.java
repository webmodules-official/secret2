package GameServer.GamePackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class guildrefresh implements Packet {
	public long TimestampIniGuild;  
	private WMap wmap = WMap.getInstance();
	
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling guildrefresh ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		

		Character cur = ((PlayerConnection)con).getActiveCharacter();
		
		
		if(cur.guild == null){return null;}
		if(cur.getGuildID() == 0){return null;}
		 if(System.currentTimeMillis() - TimestampIniGuild > 2000){
		 cur.guild.RefreshOnlyForMe(cur);
		 this.TimestampIniGuild = System.currentTimeMillis();
		 }
		 
		return null;
	}

}
