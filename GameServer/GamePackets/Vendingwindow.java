package GameServer.GamePackets;

import java.nio.ByteBuffer;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.itemprice;
import Tools.BitTools;

import Connections.Connection;
import Encryption.Decryptor;

public class Vendingwindow implements Packet {
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling Vending window");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
		decrypted = Decryptor.Decrypt(decrypted);
		
		//for(int i=0;i<decrypted.length;i++) {System.out.printf("%02x ", (decrypted[i]&0xFF));}
		//System.out.println("  ");
		
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		int one = BitTools.byteToInt(decrypted[1]);   // from inventory SLOT ( can be from anywhre like equip etc. )
		int two = BitTools.byteToInt(decrypted[2]);   // to vendingwindow slot 
		int three = BitTools.byteToInt(decrypted[3]); // to vendingwindow X
		int four = BitTools.byteToInt(decrypted[4]);  // to vendingwindow Y
		int six = BitTools.byteToInt(decrypted[6]);  // to vendingwindow stack
	
		if(cur.getInventorySLOT(one) == 0){
			cur.DeleteItemNOMESSAGE(one); 
			Charstuff.getInstance().respondguild("Fake Item!", cur.GetChannel());
			return null;
		}
		
		if(cur.getInventorySLOT(one) == 283000022 || cur.getInventorySLOT(one) == 283000021 || cur.getInventorySLOT(one) == 283000020){
			Charstuff.getInstance().respondguild("Can only Trade the MH Points.", cur.GetChannel());
			return null;
		}

		
		if(Charstuff.getInstance().tryNon_Tradable_items(cur.getInventorySLOT(one))){cur.KillInvFreeze();return null;}
		if(six > cur.getInventorySTACK(one)){six = cur.getInventorySTACK(one);}
		if(cur.getInventorySTACK(one) <= 0){return null;}
		//if(six <= 0){return null;}
		if(!Charstuff.getInstance().tryStackable_items(cur.getInventorySLOT(one))){/*System.out.println("npcshop1");*/cur.KillInvFreeze();return null;}
		int stackable = Charstuff.getInstance().getStackable_items(cur.getInventorySLOT(one));
		if(stackable == 0){
			//System.out.println("stackable = 0");
			// if target inv slot is not 0 then return null;
			//if(cur.getInventorySLOT((int)decrypted[5]) != 0){System.out.println("npcshop2");cur.ClearInv();return null;}
			six = 1;
			cur.setInventorySTACK(one, 1);
		}

		//System.out.println("six, curinvstack"+six+" - "+cur.getInventorySTACK(one));
		
		byte[] Itemprice = new byte[8];
		byte[] booth = new byte[36];
		booth[0] = (byte)booth.length;
		booth[4] = (byte)0x04;
		booth[6] = (byte)0x39;
		booth[8] = (byte)0x01;
		booth[9] = (byte)0x04;
		
		booth[10] = (byte)0x47; 
		booth[11] = (byte)0x28;
		
		for(int i=0;i<4;i++) {
			booth[12+i] = chid[i]; 
		}
		booth[16] = (byte)0x01;
		
		booth[18] = decrypted[0];	
		for(int i=0;i<4;i++) {
			booth[19+i] = decrypted[i+1];
		}
		booth[24] =  (byte)six; 
		booth[26] =  (byte)0x0f;
		booth[27] =  (byte)0xbf;
		
		
		for(int i=0;i<6;i++) {
			booth[28+i] = decrypted[8+i];
			Itemprice[i] = decrypted[8+i];
			//Itemprice[i] = decrypted[12+i];
		}
		
		long itemprice = BitTools.ByteArrayToLong(Itemprice);
		
		if(decrypted[0] == 0)
		{
			cur.boothwindowinvtobooth.remove(Integer.valueOf(two));// REMOVE vendingSLOT - inventorySLOT
			cur.boothwindowSLOT.remove(Integer.valueOf(two));
			cur.boothwindowX.remove(Integer.valueOf(two));
			cur.boothwindowY.remove(Integer.valueOf(two));
			cur.boothwindowSTACK.remove(Integer.valueOf(two));
			cur.boothwindowPRICE.remove(Integer.valueOf(two));
		}
		if(decrypted[0] == 1)
		{
			cur.seboothwindowinvtobooth(two, one); // put vendingSLOT - inventorySLOT
			cur.setboothwindowSLOT(two, cur.getInventorySLOT(one)); 
			cur.setboothwindowX(two, three);
			cur.setboothwindowY(two, four);
			cur.setboothwindowSTACK(two, Integer.valueOf(six));
			cur.setboothwindowPRICE(two, itemprice);
		}
	
		
		//System.out.println("DONE");
		return booth;
	}
	
}
