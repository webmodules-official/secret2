package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import Encryption.Decryptor;

public class wtf implements Packet {

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
		Player curplayer = ((PlayerConnection)con).getPlayer();// go to Player.java tab (accounts etc)
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		// add mh points
		byte[] mhp = BitTools.intToByteArray(curplayer.getmhpoints());
		byte[] MHP = new byte[12];
		MHP[0] = (byte)0x0c;
		MHP[4] = (byte)0x03;
		MHP[6] = (byte)0x08;
		for(int i=0;i<4;i++){MHP[8+i] = mhp[i];}
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), MHP);
		
		
		// IDK???
		byte[] fury = new byte[24];
		fury[0] = (byte)0x18;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x2f;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i];
		}
		fury[16] = (byte)0x01;
		
		fury[23] = (byte)0x2a;
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);		
		
		//cur.CheckMail();
		if(cur.guild != null && cur.guild.getGuildtype() != 0){
		cur.guild.iniguild(cur);
		}
		
		
		Iterator<Entry<Integer, Long>> iter = cur.item_end_date.entrySet().iterator();
		Integer Itemid;
		Long time_end_date;
		while(iter.hasNext()) {
			Map.Entry<Integer, Long> pairs = iter.next();
		      Itemid = pairs.getKey(); time_end_date = pairs.getValue();
			 if (System.currentTimeMillis() > time_end_date ){ 
			 while(cur.InventorySLOT.containsValue(Itemid)){
				 int Key = BitTools.ValueToKey(Itemid, cur.InventorySLOT); // get Key from InventorySLOT by Value
				 cur.DeleteItemMESSAGE(Key); 
			 }
			 iter.remove(); 	 
		   }
	    }
		
		cur.getPlayer().Rarea = true;
		//System.out.println(cur.getLOGsetName()+"===WTF===");
		
		//character must not be in the beginning of the new spawn chain

		return null;
	}
	
}
