package GameServer.GamePackets;

import item.Item;

import java.nio.ByteBuffer;
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

public class buyfromEXvendingwindow implements Packet {
	private static int inc = 0;
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling buyfromEXvendingwindow ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		//for(int i=0;i<decrypted.length;i++) { System.out.print(decrypted[i]+" "); }
		
		int zero = BitTools.byteToInt(decrypted[0]);  // target char id
		int four = BitTools.byteToInt(decrypted[4]);  // external window ITEMSLOT
		int five = BitTools.byteToInt(decrypted[5]);  // inv slot
		int six = BitTools.byteToInt(decrypted[6]);   // y
		int seven = BitTools.byteToInt(decrypted[7]); // x
		int eight = BitTools.byteToInt(decrypted[8]); // item stack
		
		byte[] target = new byte[4];
		for(int i=0;i<4;i++) {
			target[i] = decrypted[i]; 
		}
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		Character Tplayer = WMap.getInstance().getCharacter(BitTools.byteArrayToInt(target));
		if(Tplayer == null){return null;}
		if(ServerFacade.getInstance().getConnectionByChannel(Tplayer.GetChannel()) == null){return null;}
		// item doesnt exist
		if(Tplayer.getboothwindowSLOT(four) == 1337){
			Tplayer.boothwindowinvtobooth.remove(Integer.valueOf(four));
			Tplayer.boothwindowSLOT.remove(Integer.valueOf(four));
			Tplayer.boothwindowX.remove(Integer.valueOf(four));
			Tplayer.boothwindowY.remove(Integer.valueOf(four));
			Tplayer.boothwindowSTACK.remove(Integer.valueOf(four));
			Tplayer.boothwindowPRICE.remove(Integer.valueOf(four));
			Charstuff.getInstance().respondguild("Item doesnt exist in vending window, report to GM!", cur.GetChannel());
			return null;
		}
		
		
		
		
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] Tchid = BitTools.intToByteArray(Tplayer.getCharID());
		
		byte[] fury0 = new byte[40];
		fury0[0] = (byte)0x28;
		fury0[4] = (byte)0x04;
		fury0[6] = (byte)0x3a;
		fury0[8] = (byte)0x01;
	
		fury0[16] = (byte)0x01;
		fury0[23] = (byte)0x01;
		fury0[24] = decrypted[4]; 
		fury0[25] = decrypted[5];  // inv slot
		fury0[26] = decrypted[6];  // inv y
		fury0[27] = decrypted[7];  // inv x
		
		
		if(!Charstuff.getInstance().tryStackable_items(Tplayer.getboothwindowSLOT(four))){/*System.out.println("npcshop1");*/cur.KillInvFreeze();return null;}
		if(eight > Tplayer.getboothwindowSTACK(four)){/*System.out.println("cheater");*/cur.KillInvFreeze();return null;}
		if(eight <= 0){return null;}
		if(Tplayer.getboothwindowSTACK(four) <= 0){return null;}
		int stackable = Charstuff.getInstance().getStackable_items(Tplayer.getboothwindowSLOT(four));
		if(stackable == 0){
			//System.out.println("stackable = 0");
			// if target inv slot is not 0 then return null;
			//if(cur.getInventorySLOT((int)decrypted[5]) != 0){System.out.println("npcshop2");cur.ClearInv();return null;}
			eight = 1;
		}
		
		if(cur.getInventorySLOT(five) != 0 && stackable == 0){
			cur.respondguildTIMED("Select an empty Inventory slot.", cur.GetChannel());
			cur.KillInvFreeze();
			return null;
		}
		
		if(stackable != 0 && cur.getInventorySLOT(five) != 0 && cur.getInventorySLOT(five) != Tplayer.getboothwindowSLOT(four)){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", cur.GetChannel()); return null;}
		
		
		long price = Tplayer.getboothwindowPRICE(four) * eight;
		long newgold = cur.getgold() - price; 				// my cur gold - price = new gold
		if(newgold < 0){return null;}
		cur.setgold(newgold); 
		CharacterDAO.setgold(newgold, cur.charID);
		long Tplayernewgold = Tplayer.getgold() + price;		// his gold + price = new gold
		Tplayer.setgold(Tplayernewgold);
		CharacterDAO.setgold(Tplayernewgold, Tplayer.charID);
		
		byte[] Tplayernewgoldz = BitTools.LongToByteArrayREVERSE(Tplayernewgold); 
		byte[] mynewgold = BitTools.LongToByteArrayREVERSE(newgold); //
		
		for(int i=0;i<4;i++){
			fury0[12+i] = chid[i];  // my charid
			fury0[20+i] = Tchid[i]; // target charid
		}
		
		for(int i=0;i<mynewgold.length;i++){
			fury0[32+i] = mynewgold[i]; // new gold
		}

		
		fury0[28] = decrypted[8]; // item count (QUANTITY)
		
		int invslot = BitTools.byteToInt(decrypted[5]);
		int invheight = BitTools.byteToInt(decrypted[6]);
		int invweight = BitTools.byteToInt(decrypted[7]);
		int invSTACK = cur.getInventorySTACK(invslot) + eight;
		if(stackable == 0){
			//System.out.println("stackable = 0");
			// if target inv slot is not 0 then return null;
			//if(cur.getInventorySLOT((int)decrypted[5]) != 0){System.out.println("npcshop2");cur.ClearInv();return null;}
			invSTACK = 1;
		}
		int finalstack = Tplayer.getboothwindowSTACK(four) - eight;
		Tplayer.setboothwindowSTACK(four, finalstack);
		cur.setInventory(invslot, Tplayer.getboothwindowSLOT(four), invheight, invweight, invSTACK);
		
		if(stackable == 0){
			Tplayer.DeleteInvItem(Tplayer.getboothwindowinvtobooth(four));
			
			Tplayer.boothwindowinvtobooth.remove(Integer.valueOf(four));
			Tplayer.boothwindowSLOT.remove(Integer.valueOf(four));
			Tplayer.boothwindowX.remove(Integer.valueOf(four));
			Tplayer.boothwindowY.remove(Integer.valueOf(four));
			Tplayer.boothwindowSTACK.remove(Integer.valueOf(four));
			Tplayer.boothwindowPRICE.remove(Integer.valueOf(four));
		}else
		if(finalstack <= 0){
		Tplayer.setInventorySTACK(Tplayer.getboothwindowinvtobooth(four), Tplayer.getInventorySTACK(Tplayer.getboothwindowinvtobooth(four)) - eight);	
		if(Tplayer.getInventorySTACK(Tplayer.getboothwindowinvtobooth(four)) <= 0){
			Tplayer.DeleteInvItem(Tplayer.getboothwindowinvtobooth(four));
		}
		
		Tplayer.boothwindowinvtobooth.remove(Integer.valueOf(four));
		Tplayer.boothwindowSLOT.remove(Integer.valueOf(four));
		Tplayer.boothwindowX.remove(Integer.valueOf(four));
		Tplayer.boothwindowY.remove(Integer.valueOf(four));
		Tplayer.boothwindowSTACK.remove(Integer.valueOf(four));
		Tplayer.boothwindowPRICE.remove(Integer.valueOf(four));
		}else{
		Tplayer.setInventorySTACK(Tplayer.getboothwindowinvtobooth(four), Tplayer.getInventorySTACK(Tplayer.getboothwindowinvtobooth(four)) - eight);
		}
		
		byte[] fury1 = new byte[40];
		fury1[0] = (byte)0x28;
		fury1[4] = (byte)0x04;
		fury1[6] = (byte)0x3a;
		fury1[8] = (byte)0x01;
	
		fury1[16] = (byte)0x01;
		fury1[23] = (byte)0x01;
		fury1[24] = decrypted[4]; 
		fury1[25] = decrypted[5];  // inv slot
		fury1[26] = decrypted[6];  // inv y
		fury1[27] = decrypted[7];  // inv x
		
		for(int i=0;i<4;i++){
			fury1[12+i] = Tchid[i];  // my charid
			fury1[20+i] = Tchid[i]; // target charid
		}
		
		for(int i=0;i<Tplayernewgoldz.length;i++){
			fury1[32+i] = Tplayernewgoldz[i]; // new gold
		}
		
		fury1[28] = decrypted[8]; // item count (QUANTITY)
		
		ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), fury1); 
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury0); 
		
		//System.out.println("DONE");
		return null;
	}
	
}
