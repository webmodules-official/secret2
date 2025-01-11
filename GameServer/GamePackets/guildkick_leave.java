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

public class guildkick_leave implements Packet {
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
		
		
		byte[] decryptedname = new byte[17];
		for(int i=0;i<17;i++) {
			decryptedname[i] = decrypted[i];
		}
		String CharName = BitTools.byteArrayToString(decryptedname); 
        byte[] hisname = new String(CharName).getBytes();
        byte arr[] = hisname;
        try {
        		int T;
        		for (T = 0; T < arr.length && arr[T] != 0; T++) { }
        		String finalnameparsed = new String(arr, 0, T, "UTF-8");
        
        Character cur = ((PlayerConnection)con).getActiveCharacter();
        int Key = BitTools.StringToKey(finalnameparsed, cur.guild.guildnames); 
        if(Key == 1337){Charstuff.getInstance().respondguild("That character is not in guild.", cur.GetChannel());return null;}
        if(cur.guild.getguildranks(Key) == 7){Charstuff.getInstance().respondguild("The Guildmaster cannot leave the guild.", cur.GetChannel());return null;}
        Character Member = wmap.getCharacter(cur.guild.getguildids(Key));
        
		
      		//bring change to guildmemory
  		    CharacterDAO.putguildinchartable(0, cur.guild.getguildids(Key));
      		cur.guild.removemember(Key);	
      		
      	// if member is online then strip him live and show it to him grey-kun icey blastuuhh
        if(Member != null){
        //System.out.println("guildMember : "+Member.getLOGsetName());
        Member.setGuildID(0);	
        if(Member.guild != null){Member.guild = null;}		
		byte[] chid = BitTools.intToByteArray(Member.getCharID());
		byte[] fury = new byte[1452];
		byte[] fury1 = new byte[296];
		byte[] egdata = new byte[40];
		egdata[0] = (byte)0x28;
		egdata[4] = (byte)0x05;
		egdata[6] = (byte)0x41;
		egdata[8] = (byte)0x01;
		
		fury[0] = (byte)0x20;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x61;
		fury[8] = (byte)0x01;
		
		fury[32] = (byte)0x48;
		fury[36] = (byte)0x04;
		fury[38] = (byte)0x3f;
		fury[40] = (byte)0x01;
		
		fury[48] = (byte)0x01;
		
		fury[56] = (byte)0x35;
		fury[57] = (byte)0x35;
		fury[58] = (byte)0x35;
		
		fury[73] = (byte)0xa5;
		fury[74] = (byte)0x14;
		fury[75] = (byte)0x08;
		
		fury[80] = (byte)0x35;
		fury[81] = (byte)0x35;
		fury[82] = (byte)0x35;
		
		fury[97] = (byte)0xd3;
		fury[98] = (byte)0x94;
		fury[99] = (byte)0x2e;
		
		fury[104] = (byte)0x20;
		fury[108] = (byte)0x04;
		fury[110] = (byte)0x61;
		fury[112] = (byte)0x01;
		
		fury[136] = (byte)0x4c;
		fury[137] = (byte)0x06;
		fury[140] = (byte)0x04;
		fury[142] = (byte)0x41;
		fury[144] = (byte)0x01;
		
		
		for(int i=0;i<4;i++){
			fury[12+i] = chid[i];
			fury[44+i] = chid[i];
			fury[52+i] = chid[i];
			fury[76+i] = chid[i];
			fury[116+i] = chid[i];
			fury[148+i] = chid[i];
			
			egdata[12+i] = chid[i]; // charid
		}

		Member.sendToMap(egdata);
		ServerFacade.getInstance().addWriteByChannel(Member.GetChannel(), fury);	
		ServerFacade.getInstance().addWriteByChannel(Member.GetChannel(), fury1);	
		Member.setgold(Member.getgold() - 10000000);
		double sub = Member.getFame() * 0.020;
		Member.setFame(Member.getFame() -(int)sub);
		}
		return null;
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
    	return null;
	}

}
