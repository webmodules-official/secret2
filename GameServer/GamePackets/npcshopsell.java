package GameServer.GamePackets;

import item.ItemCache;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;

import npc.Npc;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.itemprice;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import Encryption.Decryptor;

public class npcshopsell implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling npcshop SELLING ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		
		//PERFECT WAY TO KNOW WHATS WHAT !
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" ");}
		//System.out.println("");
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println("");
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		int five = BitTools.byteToInt(decrypted[5]);
		int eight = cur.getInventorySTACK(five);
		/*
		 * decrypted[5] = inventorySLOT
		 * 
		 * decrypted[8] = inventorySTACK
		*/
		long sellprice = 0;
		if(cur.getInventorySLOT(five) == 0){
			cur.DeleteItemNOMESSAGE(five); 
			Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
			return null;
		}
		
		if(cur.getInventorySLOT(five) == 292000006 ||cur.getInventorySLOT(five) == 217000035 ||cur.getInventorySLOT(five) == 217000242 || cur.getInventorySLOT(five) == 217000243 || cur.getInventorySLOT(five) == 217000029 || cur.getInventorySLOT(five) == 217000028){sellprice =  itemprice.getBuyprice(cur.getInventorySLOT(five));}
		else{sellprice =  itemprice.getBuyprice(cur.getInventorySLOT(five)) / 3;}
		
		if(!Charstuff.getInstance().tryStackable_items(cur.getInventorySLOT(five))){/*System.out.println("npcshop1");*/cur.KillInvFreeze();return null;}
		int stackable = Charstuff.getInstance().getStackable_items(cur.getInventorySLOT(five));
		if(stackable == 0){
			//System.out.println("stackable = 0");
			// if target inv slot is not 0 then return null;
			//if(cur.getInventorySLOT((int)decrypted[5]) != 0){System.out.println("npcshop2");cur.ClearInv();return null;}
			eight = 1;
		}
		
		//System.out.println("sellprice: "+sellprice+" * "+eight);
		long sellpricee = sellprice * eight;
		//System.out.println("new sellprice: "+sellprice);
		long newgoldz = cur.getgold() + sellpricee;
		//System.out.println("new final gold: "+newgoldz+" = "+cur.getgold()+" + "+sellprice);
		cur.setgold(newgoldz);
		byte[] newgold = BitTools.LongToByteArrayREVERSE(newgoldz);
		byte[] fury = new byte[32];
		fury[0] = (byte)0x20;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x14;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i];
		}
		for(int i=0;i<newgold.length;i++) {
			fury[24+i] = newgold[i]; // total new gold
		}
		fury[16] = (byte)0x01;
		
		fury[18] = (byte)0x01;
		fury[19] = decrypted[5];
		fury[20] = decrypted[8];
		
		cur.DeleteInvItem(Integer.valueOf(five)); // remove inventory item by slot
		CharacterDAO.setgold(newgoldz, cur.charID);
		//System.out.println("DONE");
		return fury;
		
	}
	
}
