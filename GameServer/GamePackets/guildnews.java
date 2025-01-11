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

public class guildnews implements Packet {
	public long TimestampIniGuild;  
	private WMap wmap = WMap.getInstance();
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling guildinvite ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//System.out.println(" | ");
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}

		Character cur = ((PlayerConnection)con).getActiveCharacter();
		//byte[] chid = BitTools.intToByteArray(cur.getCharID());

		if(cur.guild == null){return null;}
		
		//Due to we have to make eveybyte count and its in String form we will place 0x00 inhere as spacebar in database so it all matches 
		
				// Guild News
				byte[] iniguild5 = new byte[212];
				byte[] Guildnews = new byte[196];
				iniguild5[0] = (byte)0xd4; 
				iniguild5[4] = (byte)0x04; 
				iniguild5[6] = (byte)0x67; 
				iniguild5[8] = (byte)0x01; 
				//for(int a=0;a<4;a++) {
				//	iniguild5[12+a] = chid[a];
				//}
				
				for(int a=0;a<decrypted.length;a++) {
					iniguild5[16+a] = decrypted[a]; // guild news
					Guildnews[a] = decrypted[a]; 
				}

				cur.guild.setguildnews(Guildnews);
				
				//send to guild
				cur.guild.SendToGuildWithTheirCharID(iniguild5);
				
		
		return null;
	}

}
