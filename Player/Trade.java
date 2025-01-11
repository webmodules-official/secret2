package Player;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Connections.Connection;
import Encryption.Decryptor;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


/*
 * party.class
 * Stores all party data 
 */

public class Trade implements Cloneable{
	private int Player1 = 0;
	private int Player2 = 0;
	private long Player1gold = 0;
	private long Player2gold = 0;
	private boolean Player1ready = false;
	private boolean Player2ready = false;
	private byte[] Player1decrypted;
	private byte[] Player2decrypted;
	private WMap wmap = WMap.getInstance();
	public ConcurrentMap<Integer, Integer> Player1Invslot = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> Player1TradeinvITEMID = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> Player1TradeinvSTACK = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> Player2Invslot = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> Player2TradeinvITEMID = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> Player2TradeinvSTACK = new ConcurrentHashMap<Integer, Integer>();


	public void setPlayer1Tradeinv(int Invslot, int SLOT, int itemid, int stack) {
		this.Player1Invslot.put(Integer.valueOf(Invslot), Integer.valueOf(Invslot)); 
		this.Player1TradeinvITEMID.put(Integer.valueOf(SLOT), Integer.valueOf(itemid)); 
		this.Player1TradeinvSTACK.put(Integer.valueOf(SLOT), Integer.valueOf(stack)); 
	}
	
	public void setPlayer2Tradeinv(int Invslot, int SLOT, int itemid, int stack) {
		this.Player2Invslot.put(Integer.valueOf(Invslot), Integer.valueOf(Invslot)); 
		this.Player2TradeinvITEMID.put(Integer.valueOf(SLOT), Integer.valueOf(itemid)); 
		this.Player2TradeinvSTACK.put(Integer.valueOf(SLOT), Integer.valueOf(stack)); 
	}
	
	// create new Trade
	public Trade(int Player1, int Player2) {				
		this.Player1 = Player1;
		this.Player2 = Player2;
		
		//send opening windows to both players
		Character Plr1 = wmap.getCharacter(this.Player1);
		Character Plr2 = wmap.getCharacter(this.Player2);
		
		
		if(Plr1 != null && Plr2 != null){
			Plr1.IsTrading = true;
			Plr2.IsTrading = true;
			
		//p1
		byte[] P1chid = BitTools.intToByteArray(Plr1.getCharID());
		byte[] fury = new byte[28];
		fury[0] = (byte)0x1c;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x17;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
		fury[12+i] = P1chid[i]; // charid
		}
		fury[16] = (byte)0x01;
		fury[18] = (byte)0x03; //3 = open window
		fury[19] = (byte)0x08;
		for(int i=0;i<4;i++) {
			fury[12+i] = P1chid[i];
			fury[20+i] = P1chid[i]; 
			fury[24+i] = P1chid[i]; 
		}

		
		//p2
		byte[] P2chid = BitTools.intToByteArray(Plr2.getCharID());
		byte[] fury1 = new byte[28];
		fury1[0] = (byte)0x1c;
		fury1[4] = (byte)0x04;
		fury1[6] = (byte)0x17;
		fury1[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury1[12+i] = P2chid[i]; // charid
		}
		fury1[16] = (byte)0x01;
		fury1[18] = (byte)0x03; //3 = open window
		fury1[19] = (byte)0x08;
		for(int i=0;i<4;i++) {
			fury1[12+i] = P2chid[i];
			fury1[20+i] = P2chid[i]; 
			fury1[24+i] = P2chid[i]; 
		}
		ServerFacade.getInstance().addWriteByChannel(Plr1.GetChannel(), fury1);
		ServerFacade.getInstance().addWriteByChannel(Plr2.GetChannel(), fury);
		}
	}
	
	
	// put golds and items
		public void ReadyandFinish(int CharID, byte[] decrypted) {				
			//send opening windows to both players
			Character Plr1 = wmap.getCharacter(this.Player1);
			Character Plr2 = wmap.getCharacter(this.Player2);
			
			
			if(Plr1 != null && Plr2 != null){
				
			// store their byte array if they clicked ok
			if(CharID == this.Player1){
				this.Player1decrypted = decrypted;
				this.Player1ready = true;
			}else
			if(CharID == this.Player2){
				this.Player2decrypted = decrypted;
				this.Player2ready = true;
			}else{
				Character cur = wmap.getCharacter(CharID);
				if(cur != null){
				Charstuff.getInstance().respondguild("You are in the wrong Trade window.", cur.GetChannel());
				}
				return;
			}
			
			//if both r ready then end
			if(Player1ready == true && Player2ready == true){
				
				// test first if all target slots r empty;
				byte[] Plr1stuff = new byte[this.Player2decrypted.length-2];
				byte[] Plr2stuff = new byte[this.Player1decrypted.length-2];
				for(int i=0;i<Plr2stuff.length;i++){
					i++;
					int Invslot = (int)this.Player1decrypted[2+i];
					i++;
					i++;
					//System.out.println(" Plr1.getInventorySLOT(Invslot) : "+Plr1.getInventorySLOT(Invslot)); 
					if(Plr1.getInventorySLOT(Invslot) != 0){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", Plr1.GetChannel()); return;}
				}
				for(int i=0;i<Plr1stuff.length;i++){
					i++;
					int Invslot = (int)this.Player2decrypted[2+i];
					i++;
					i++;
					//System.out.println(" Plr2.getInventorySLOT(Invslot) : "+Plr2.getInventorySLOT(Invslot)); 
					if(Plr2.getInventorySLOT(Invslot) != 0){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", Plr2.GetChannel()); return;}
				}
				
				//remove both items from their inventorys becaus its already stored in temp Trade.
				Iterator<Integer> it1 = this.Player1Invslot.keySet().iterator();
				while(it1.hasNext()){
				Integer i = it1.next();
				Plr1.DeleteInvItem(i);
				}
				
				Iterator<Integer> it2 = this.Player2Invslot.keySet().iterator();
				while(it2.hasNext()){
				Integer i = it2.next();
				Plr2.DeleteInvItem(i);
				}
				
				// checkgold
				if(Plr1.getgold() - this.Player1gold < 0){return;};
				if(Plr2.getgold() - this.Player2gold < 0){return;};
				
				
				//if all set then googogo
				//plr1
				byte[] P1chid = BitTools.intToByteArray(Plr1.getCharID());
				byte[] Result1 = new byte[36];
				byte[] Tempstore1;
				int inc2 = 0;
				for(int i=0;i<Plr2stuff.length;i++){
					inc2++;
					int Tradeslot = (int)this.Player1decrypted[2+i];
					i++;
					int Invslot = (int)this.Player1decrypted[2+i];
					i++;
					int InvY = (int)this.Player1decrypted[2+i];
					i++;
					int InvX = (int)this.Player1decrypted[2+i];
					
					//put in inv and add extra byte array per item
					Tempstore1 = Result1;
					Result1 = new byte[Result1.length+12];
					for(int w=0;w<Tempstore1.length;w++){
						Result1[w] = Tempstore1[w];
					}
					Result1[Result1.length-10] = (byte)InvX;
					Result1[Result1.length-9] = (byte)InvY;
					byte[] itemID1 = BitTools.intToByteArray(this.Player2TradeinvITEMID.get(Tradeslot));
					byte[] stack1 = BitTools.intToByteArray(this.Player2TradeinvSTACK.get(Tradeslot));
					for(int w=0;w<4;w++){
						Result1[Result1.length-8+w] = itemID1[w];
					}
					for(int w=0;w<2;w++){
						Result1[Result1.length-4+w] = stack1[w];
					}
					Plr1.setInventory(Invslot, this.Player2TradeinvITEMID.get(Tradeslot), InvY, InvX,this.Player2TradeinvSTACK.get(Tradeslot));
					//System.out.println("Result1:"+Plr1.getLOGsetName()+" : "+Result1.length+" : "+this.Player2TradeinvITEMID.get(Tradeslot)+" - "+this.Player2TradeinvSTACK.get(Tradeslot)+" to "+Invslot+" - "+InvX+" - "+InvY);
				}
				
				byte[] indibyte2 = BitTools.intToByteArray(Result1.length);
				for(int i=0;i<2;i++){
					Result1[i] = indibyte2[i];
				}
				Result1[4] = (byte)0x04;
				Result1[6] = (byte)0x19;
				Result1[8] = (byte)0x01;
				for(int i=0;i<4;i++) {
					Result1[12+i] = P1chid[i];
					Result1[28+i] = P1chid[i]; 
				}
				Result1[16] = (byte)0x04;
				Plr1.setgold(Plr1.getgold() + this.Player2gold);
				Plr2.setgold(Plr2.getgold() - this.Player2gold);
				Result1[32] = (byte)inc2;

				//plr2
				byte[] P2chid = BitTools.intToByteArray(Plr2.getCharID());
				byte[] Result2 = new byte[36];
				byte[] Tempstore2;
				int inc1 = 0;
				for(int i=0;i<Plr1stuff.length;i++){
					inc1++;
					int Tradeslot = (int)this.Player2decrypted[2+i];
					i++;
					int Invslot = (int)this.Player2decrypted[2+i];
					i++;
					int InvY = (int)this.Player2decrypted[2+i];
					i++;
					int InvX = (int)this.Player2decrypted[2+i];
					
					//put in inv and add extra byte array per item
					Tempstore2 = Result2;
					Result2 = new byte[Result2.length+12];
					for(int w=0;w<Tempstore2.length;w++){
						Result2[w] = Tempstore2[w];
					}
					Result2[Result2.length-10] = (byte)InvX;
					Result2[Result2.length-9] = (byte)InvY;
					byte[] itemID1 = BitTools.intToByteArray(this.Player1TradeinvITEMID.get(Tradeslot));
					byte[] stack1 = BitTools.intToByteArray(this.Player1TradeinvSTACK.get(Tradeslot));
					for(int w=0;w<4;w++){
						Result2[Result2.length-8+w] = itemID1[w];
					}
					for(int w=0;w<2;w++){
						Result2[Result2.length-4+w] = stack1[w];
					}
					Plr2.setInventory(Invslot, this.Player1TradeinvITEMID.get(Tradeslot), InvY, InvX, this.Player1TradeinvSTACK.get(Tradeslot));
					//System.out.println("Result2:"+Plr2.getLOGsetName()+" : "+Result2.length+" : "+this.Player1TradeinvITEMID.get(Tradeslot)+" - "+this.Player1TradeinvSTACK.get(Tradeslot)+" to "+Invslot+" - "+InvX+" - "+InvY);
				}
				
				byte[] indibyte1 = BitTools.intToByteArray(Result2.length);
				for(int i=0;i<2;i++){
					Result2[i] = indibyte1[i];
				}
				Result2[4] = (byte)0x04;
				Result2[6] = (byte)0x19;
				Result2[8] = (byte)0x01;
				for(int i=0;i<4;i++) {
					Result2[12+i] = P2chid[i];
					Result2[28+i] = P2chid[i]; 
				}
				Result2[16] = (byte)0x04;
				Plr2.setgold(Plr2.getgold() + this.Player1gold);
				Plr1.setgold(Plr1.getgold() - this.Player1gold);
				Result2[32] = (byte)inc1;
				
				//final gold last
				byte[] gold2 = BitTools.LongToByteArrayREVERSE(Plr1.getgold());
				for(int i=0;i<gold2.length;i++){
					Result1[20+i] = gold2[i];
				}
				byte[] gold = BitTools.LongToByteArrayREVERSE(Plr2.getgold());
				for(int i=0;i<gold.length;i++){
					Result2[20+i] = gold[i];
				}
				
				ServerFacade.getInstance().addWriteByChannel(Plr1.GetChannel(), Result1);	
				ServerFacade.getInstance().addWriteByChannel(Plr2.GetChannel(), Result2);	
				
				//ended now:
				//Handell ALL removement
				this.Player1 = 0;
				this.Player2 = 0;
				this.Player1gold = 0;
				this.Player2gold = 0;
				this.Player1ready = false;
				this.Player2ready = false;
				this.Player1decrypted = null;
				this.Player2decrypted = null;
				this.Player1Invslot.clear();
				this.Player1TradeinvITEMID.clear();
				this.Player1TradeinvSTACK.clear();
				this.Player2Invslot.clear();
				this.Player2TradeinvITEMID.clear();
				this.Player2TradeinvSTACK.clear();
				Plr1.TradeUID = 0;
				Plr2.TradeUID = 0;
				wmap.Trade.remove(this);
				Plr1.IsTrading = false;
				Plr2.IsTrading = false;
			}else{
			//else push ready.
			byte[] chid = BitTools.intToByteArray(CharID);
			byte[] fury = new byte[36];
			fury[0] = (byte)0x24;
			fury[4] = (byte)0x04;
			fury[6] = (byte)0x19;
			fury[8] = (byte)0x01;
			for(int i=0;i<4;i++) {
				fury[12+i] = chid[i];
				fury[28+i] = chid[i]; 
			}
			fury[16] = (byte)0x01;
			fury[17] = (byte)0x9d;
			fury[18] = (byte)0x0f;
			fury[19] = (byte)0xbf;
			fury[33] = (byte)0x9d;
			fury[34] = (byte)0x0f;
			fury[35] = (byte)0xbf;
			
			
			ServerFacade.getInstance().addWriteByChannel(Plr1.GetChannel(), fury);
			ServerFacade.getInstance().addWriteByChannel(Plr2.GetChannel(), fury);
			//System.out.println("Setting Ready");
			}
			}
		}
	
	// put golds
	public void PutGold(int CharID, long gold) {				
		//send opening windows to both players
		Character Plr1 = wmap.getCharacter(this.Player1);
		Character Plr2 = wmap.getCharacter(this.Player2);
		
		if(Plr1 != null && Plr2 != null){
		
		if(CharID == this.Player1){
			this.Player1gold = gold;
		}else
		if(CharID == this.Player2){
			this.Player2gold = gold;
		}else{
			Character cur = wmap.getCharacter(CharID);
			if(cur != null){
			Charstuff.getInstance().respondguild("You are in the wrong Trade window.", cur.GetChannel());
			}
			return;
		}
		
		byte[] golds = BitTools.LongToByteArrayREVERSE(gold);
		//System.out.println("gold :"+gold);
		// can be plr 1 or 2
		byte[] chid = BitTools.intToByteArray(CharID);
		byte[] fury = new byte[48];
		fury[0] = (byte)0x30;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x18;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i];
			fury[44+i] = chid[i]; 
		}
		fury[16] = (byte)0x01;
		fury[18] = (byte)0xff;
		fury[20] = (byte)0x1c;
		fury[21] = (byte)0x30;
		fury[22] = (byte)0x5c;
		fury[23] = (byte)0x08;
		
		// check if player has gold
		for(int i=0;i<golds.length;i++) {
			fury[32+i] = golds[i]; 

		}
		fury[43] = (byte)0x08;
		
		ServerFacade.getInstance().addWriteByChannel(Plr1.GetChannel(), fury);
		ServerFacade.getInstance().addWriteByChannel(Plr2.GetChannel(), fury);
		//System.out.println("WTF");
		}
	}
	
	// put item
	public void PutItem(int CharID, int one, int two, int ItemID, int Stack, int X, int Y) {				
		//send opening windows to both players
		Character Plr1 = wmap.getCharacter(this.Player1);
		Character Plr2 = wmap.getCharacter(this.Player2);
		
		if(Plr1 != null && Plr2 != null){
		
		if(CharID == this.Player1){
			this.setPlayer1Tradeinv(one, two, ItemID, Stack);
		}else
		if(CharID == this.Player2){
			this.setPlayer2Tradeinv(one, two, ItemID, Stack);
		}else{
			Character cur = wmap.getCharacter(CharID);
			if(cur != null){
			Charstuff.getInstance().respondguild("You are in the wrong Trade window.", cur.GetChannel());
			}
			return;
		}
		
		byte[] Itemid = BitTools.intToByteArray(ItemID);
			
		// can be plr 1 or 2
		byte[] chid = BitTools.intToByteArray(CharID);
		byte[] fury = new byte[48];
		fury[0] = (byte)0x30;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x18;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i];
			fury[24+i] = Itemid[i]; 
			fury[44+i] = chid[i]; 
		}
		fury[16] = (byte)0x01;
		fury[18] = (byte)0x01;
		fury[19] = (byte)one;
		//fury[22] = (byte)two;
		
		fury[28] = (byte)Stack;//stack
		fury[40] = (byte)two;
		fury[41] = (byte)X;//x
		fury[42] = (byte)Y;//y 
		
		ServerFacade.getInstance().addWriteByChannel(Plr1.GetChannel(), fury);
		ServerFacade.getInstance().addWriteByChannel(Plr2.GetChannel(), fury);
		}
	}
	
	public void CancelTrade() {
				Character Plr1 = wmap.getCharacter(this.Player1);
				Character Plr2 = wmap.getCharacter(this.Player2);
				
				if(Plr1 != null && Plr2 != null){
				//p1
				byte[] P1chid = BitTools.intToByteArray(Plr1.getCharID());
				byte[] fury = new byte[36];
				fury[0] = (byte)0x24;
				fury[4] = (byte)0x04;
				fury[6] = (byte)0x19;
				fury[8] = (byte)0x01;
				for(int i=0;i<4;i++) {
				fury[12+i] = P1chid[i]; // charid
				fury[28+i] = P1chid[i]; // charid
				}
				fury[17] = (byte)0x9d;
				fury[18] = (byte)0x0f; 
				fury[19] = (byte)0xbf;
				fury[33] = (byte)0x9d;
				fury[34] = (byte)0x0f; 
				fury[35] = (byte)0xbf;


				
				//p2
				byte[] P2chid = BitTools.intToByteArray(Plr2.getCharID());
				byte[] fury1 = new byte[36];
				fury1[0] = (byte)0x24;
				fury1[4] = (byte)0x04;
				fury1[6] = (byte)0x19;
				fury1[8] = (byte)0x01;
				for(int i=0;i<4;i++) {
				fury1[12+i] = P2chid[i]; // charid
				fury1[28+i] = P2chid[i]; // charid
				}
				fury1[17] = (byte)0x9d;
				fury1[18] = (byte)0x0f; 
				fury1[19] = (byte)0xbf;
				fury1[33] = (byte)0x9d;
				fury1[34] = (byte)0x0f; 
				fury1[35] = (byte)0xbf;
				ServerFacade.getInstance().addWriteByChannel(Plr1.GetChannel(), fury1);
				ServerFacade.getInstance().addWriteByChannel(Plr2.GetChannel(), fury);
				

				this.Player1 = 0;
				this.Player2 = 0;
				this.Player1gold = 0;
				this.Player2gold = 0;
				this.Player1ready = false;
				this.Player2ready = false;
				this.Player1decrypted = null;
				this.Player2decrypted = null;
				this.Player1Invslot.clear();
				this.Player1TradeinvITEMID.clear();
				this.Player1TradeinvSTACK.clear();
				this.Player2Invslot.clear();
				this.Player2TradeinvITEMID.clear();
				this.Player2TradeinvSTACK.clear();
				Plr1.TradeUID = 0;
				Plr2.TradeUID = 0;
				wmap.Trade.remove(this);
				Plr1.IsTrading = false;
				Plr2.IsTrading = false;
				}
	}
			
		
}
