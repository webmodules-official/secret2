package GameServer.GamePackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import Encryption.Decryptor;

public class guildpromote implements Packet {
	 private WMap wmap = WMap.getInstance();
	 
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
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" "); }
		//System.out.println(" | ");
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		
		
        Character cur = ((PlayerConnection)con).getActiveCharacter();
        byte[] charid = new byte[4];
        charid[0] = decrypted[4];
        charid[1] = decrypted[5];
        charid[2] = decrypted[6];
        charid[3] = decrypted[7];
        
        int Key = BitTools.ValueToKey(BitTools.byteArrayToInt(charid), cur.guild.guildids); 
        if(!wmap.CharacterExists(cur.guild.getguildids(Key))){Charstuff.getInstance().respondguild("That character is not in guild.", cur.GetChannel());return null;}
        Character Member = wmap.getCharacter(cur.guild.getguildids(Key));	

        if(Member != null && Member.guild != null){	
		byte[] chid = BitTools.intToByteArray(Member.getCharID());

		byte[] egdata = new byte[28];
		egdata[0] = (byte)0x1c;
		egdata[4] = (byte)0x04;
		egdata[6] = (byte)0x40;
		egdata[8] = (byte)0x01;
		
		egdata[16] = (byte)0x01;
		egdata[18] = (byte)0x45;
		egdata[19] = (byte)0x28;
		for(int i=0;i<4;i++){
			egdata[20+i] = chid[i];
		}
		egdata[24] = decrypted[0];
		if((int)decrypted[0] == 7){// if setting master then set me to member
		int curKey = BitTools.ValueToKey(cur.charID, cur.guild.guildids); 
		cur.guild.setguildranks(curKey, 3);
		cur.sendToMap(cur.extCharGuild());
		}
		Member.guild.setguildranks(Key, (int)decrypted[0]);
		Member.guild.SendToGuildWithTheirCharID(egdata);
		cur.guild.RefreshOnlyForMe(cur);
		Member.guild.RefreshOnlyForMe(Member);
		Member.sendToMap(Member.extCharGuild());
    	return null;
	}
		return null;
  }
}
