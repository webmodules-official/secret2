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

public class Party implements Cloneable{
	public int inc = 1;
	private WMap wmap = WMap.getInstance();
	public ConcurrentMap<Integer, Integer> partymembers = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> KeyQueue = new ConcurrentHashMap<Integer, Integer>();
	
	public int getpartymembers(int one) {
		if(this.partymembers.containsKey(one)){
		int chIDs = this.partymembers.get(one);
		//System.out.println("party: " +one+" - " +chIDs);
		return chIDs;}else
		{ //System.out.println(one+" - null "); 
		return 0;}
	}

	public void setpartymembers(int one, int chIDs) {
		this.partymembers.put(Integer.valueOf(one), Integer.valueOf(chIDs)); 
	}
	
	
	public void addmember(Character cur) {
	if(cur != null){
	inc++;	
	setpartymembers(inc, cur.getCharID());
	}}
	
	
	//queue them players
	public void ReOrder(int line) {
		Iterator<Map.Entry<Integer, Integer>> iter = this.partymembers.entrySet().iterator();   
		while(iter.hasNext()) {
			Map.Entry<Integer, Integer> Member = iter.next();
			 int Key = Member.getKey();
			 int Value = Member.getValue();
			 //if key is above the line
			if(line < Key){
				this.KeyQueue.put(Key-1,Value);
			}else{this.KeyQueue.put(Key,Value);}
		}
	
		Iterator<Map.Entry<Integer, Integer>> iterr = this.KeyQueue.entrySet().iterator();  
		partymembers.clear();
		inc = 0;
		while(iterr.hasNext()) {
			Map.Entry<Integer, Integer> Reorder = iterr.next();
			 Character Value = wmap.getCharacter(Reorder.getValue());
			 this.addmember(Value);
		}
		KeyQueue.clear();
	}
	
	// create new party	
	public Party(int Uniqpartyid, byte[] decrypted ,Character cur) 
	{			
				byte[] newP = new byte[4];
				for(int i=0;i<4;i++) {
				newP[i] = decrypted[4+i];
				}
				
				int newp = BitTools.byteArrayToInt(newP);
				Character Leader = wmap.getCharacter(newp);
				
				if(Leader != null && cur != null){
				this.setpartymembers(1, Leader.getCharID());
				if(Leader.partyUID == 0){Leader.partyUID = Uniqpartyid;}
				inc++;
				this.setpartymembers(inc, cur.getCharID());
				if(cur.partyUID == 0){cur.partyUID = Leader.partyUID;}
				this.Refresh_Party(cur);
				}
		 }
	
	// return empty list to the one that is leaving on Kick OR Leave
	public byte[] EmptyList(Character cur) {
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] Elist = new byte[64];
		Elist[0] = (byte)0x40;
		Elist[4] = (byte)0x04; // 0x05 for other players
		Elist[6] = (byte)0x24;
		Elist[8] = (byte)0x01;
		Elist[16] = (byte)0x01;
		Elist[18] = (byte)0x00; //0 = leave | 1 = him leaving
		Elist[19] = (byte)0x08;
		
		Elist[24] = (byte)0x01; // this probably evicts the player
		
		for(int i=0;i<4;i++) {
			Elist[20+i] = chid[i]; //player leaveing the party
			Elist[12+i] = chid[i]; //player leaveing the party
			Elist[60+i] = chid[i]; //player leaveing the party
		}
			return Elist;
	}
	
	
	
	// Refresh_Party
			public void PromoteToLeader(byte[] decrypted, Character cur) 
			{	
						byte[] chid = BitTools.intToByteArray(cur.getCharID());
						byte[] fury = new byte[64];
						fury[0] = (byte)0x40;
						fury[4] = (byte)0x04; // 0x05 for other players
						fury[6] = (byte)0x25;
						fury[8] = (byte)0x01;
						fury[16] = (byte)0x01;
						fury[18] = decrypted[0]; // 0x00 = null | 0x01 = accepted | 0x02 is requesting party
						fury[19] = (byte)0x08;
					
						byte[] tplayer1 = BitTools.intToByteArray(this.getpartymembers(1));
						byte[] tplayer2 = BitTools.intToByteArray(this.getpartymembers(2));
						byte[] tplayer3 = BitTools.intToByteArray(this.getpartymembers(3));
						byte[] tplayer4 = BitTools.intToByteArray(this.getpartymembers(4));
						byte[] tplayer5 = BitTools.intToByteArray(this.getpartymembers(5));
						byte[] tplayer6 = BitTools.intToByteArray(this.getpartymembers(6));
						byte[] tplayer7 = BitTools.intToByteArray(this.getpartymembers(7));
						byte[] tplayer8 = BitTools.intToByteArray(this.getpartymembers(8));
						
						for(int i=0;i<4;i++) {
							fury[20+i] = chid[i]; //player getting switched
							fury[12+i] = tplayer1[i];
							fury[28+i] = tplayer1[i]; 
							fury[32+i] = tplayer2[i];
							fury[36+i] = tplayer3[i];
							fury[40+i] = tplayer4[i];
							fury[44+i] = tplayer5[i];
							fury[48+i] = tplayer6[i];
							fury[52+i] = tplayer7[i];
							fury[56+i] = tplayer8[i];
							
							fury[60+i] = chid[i];  //player getting switched
						}
						
						
						Character Tplayer1 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer1));
					Iterator<Map.Entry<Integer, Integer>> iter1 = this.partymembers.entrySet().iterator();   
						Character tmp1;
						while(iter1.hasNext()){
							Map.Entry<Integer, Integer> pairs1 = iter1.next();
							tmp1 = wmap.getCharacter(pairs1.getValue());
							if(tmp1.partyUID == Tplayer1.partyUID)
							{
								ServerFacade.getInstance().addWriteByChannel(tmp1.GetChannel(), fury);
							}
						}
			}
	
	
	// Refresh_Party
		public void leave_kicK(byte[] decrypted, Character cur) 
		{	
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), this.EmptyList(cur));		
			
					byte[] chid = BitTools.intToByteArray(cur.getCharID());
					byte[] fury = new byte[64];
					fury[0] = (byte)0x40;
					fury[4] = (byte)0x04; // 0x05 for other players
					fury[6] = (byte)0x24;
					fury[8] = (byte)0x01;
					fury[16] = (byte)0x01;
					fury[18] = decrypted[0]; // 0x00 = null | 0x01 = accepted | 0x02 is requesting party
					fury[19] = (byte)0x08;
				
					byte[] tplayer1 = BitTools.intToByteArray(this.getpartymembers(1));
					byte[] tplayer2 = BitTools.intToByteArray(this.getpartymembers(2));
					byte[] tplayer3 = BitTools.intToByteArray(this.getpartymembers(3));
					byte[] tplayer4 = BitTools.intToByteArray(this.getpartymembers(4));
					byte[] tplayer5 = BitTools.intToByteArray(this.getpartymembers(5));
					byte[] tplayer6 = BitTools.intToByteArray(this.getpartymembers(6));
					byte[] tplayer7 = BitTools.intToByteArray(this.getpartymembers(7));
					byte[] tplayer8 = BitTools.intToByteArray(this.getpartymembers(8));
					
					for(int i=0;i<4;i++) {
						fury[20+i] = chid[i]; //player leaveing the party
						fury[12+i] = tplayer1[i];
						fury[28+i] = tplayer1[i]; 
						fury[32+i] = tplayer2[i];
						fury[36+i] = tplayer3[i];
						fury[40+i] = tplayer4[i];
						fury[44+i] = tplayer5[i];
						fury[48+i] = tplayer6[i];
						fury[52+i] = tplayer7[i];
						fury[56+i] = tplayer8[i];
						
						fury[60+i] = chid[i]; //player leaveing the party
					}
					
					
					Character Tplayer1 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer1));
					if(Tplayer1 == null){
					ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);		
					}else{
					
				Iterator<Map.Entry<Integer, Integer>> iter1 = this.partymembers.entrySet().iterator();   
					Character tmp1;
					while(iter1.hasNext()){
						Map.Entry<Integer, Integer> pairs1 = iter1.next();
						tmp1 = wmap.getCharacter(pairs1.getValue());
						if(tmp1.partyUID == Tplayer1.partyUID)
						{
							ServerFacade.getInstance().addWriteByChannel(tmp1.GetChannel(), fury);
						}
					}
				}
		}
	
	
	// Refresh_Party
	public void Refresh_Party(Character cur) 
	{	
				byte[] chid = BitTools.intToByteArray(cur.getCharID());
				byte[] fury = new byte[64];
				fury[0] = (byte)0x40;
				fury[4] = (byte)0x04;
				fury[6] = (byte)0x23;
				fury[8] = (byte)0x01;
				for(int i=0;i<4;i++) {
				fury[12+i] = chid[i];
				}
				fury[16] = (byte)0x01;
				fury[18] = (byte)0x01; // 0x00 = null | 0x01 = accepted | 0x02 is requesting party
				fury[19] = (byte)0x08;
			
				byte[] tplayer1 = BitTools.intToByteArray(this.getpartymembers(1));
				byte[] tplayer2 = BitTools.intToByteArray(this.getpartymembers(2));
				byte[] tplayer3 = BitTools.intToByteArray(this.getpartymembers(3));
				byte[] tplayer4 = BitTools.intToByteArray(this.getpartymembers(4));
				byte[] tplayer5 = BitTools.intToByteArray(this.getpartymembers(5));
				byte[] tplayer6 = BitTools.intToByteArray(this.getpartymembers(6));
				byte[] tplayer7 = BitTools.intToByteArray(this.getpartymembers(7));
				byte[] tplayer8 = BitTools.intToByteArray(this.getpartymembers(8));
				
				for(int i=0;i<4;i++) {
					fury[20+i] = chid[i]; //To the new player joining the party
					
					fury[28+i] = tplayer1[i]; 
					fury[32+i] = tplayer2[i];
					fury[36+i] = tplayer3[i];
					fury[40+i] = tplayer4[i];
					fury[44+i] = tplayer5[i];
					fury[48+i] = tplayer6[i];
					fury[52+i] = tplayer7[i];
					fury[56+i] = tplayer8[i];
					
					fury[60+i] = chid[i]; // New player Character has joined the party message
				}
				
				
				Character Tplayer1 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer1));
				Character Tplayer2 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer2));
				Character Tplayer3 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer3));
				Character Tplayer4 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer4));
				Character Tplayer5 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer5));
				Character Tplayer6 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer6));
				Character Tplayer7 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer7));
				Character Tplayer8 = wmap.getCharacter(BitTools.byteArrayToInt(tplayer7));
				
				byte[] hpnmana1 = new byte[60];
				byte[] hpnmana2 = new byte[60];
				byte[] hpnmana3 = new byte[60];
				byte[] hpnmana4 = new byte[60];
				byte[] hpnmana5 = new byte[60];
				byte[] hpnmana6 = new byte[60];
				byte[] hpnmana7 = new byte[60];
				byte[] hpnmana8 = new byte[60];
				
				if(Tplayer1 != null){
					byte[] chid1 = BitTools.intToByteArray(Tplayer1.getCharID());
					Tplayer1.partyUID = Tplayer1.partyUID;
					hpnmana1[0] = (byte)0x3c;
					hpnmana1[4] = (byte)0x05;
					hpnmana1[6] = (byte)0x26;
					hpnmana1[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						hpnmana1[12+i] = chid1[i]; // charid
					}
					hpnmana1[16] = (byte)0x01;
					
					hpnmana1[42] = (byte)0x0f;
					hpnmana1[43] = (byte)0xbf;
					
					
					byte[] TP1 = BitTools.intToByteArray(Tplayer1.getCharID());
					for(int i=0;i<4;i++) {
						hpnmana1[16+i] = TP1[i]; 
					}	
					byte[] msg = Tplayer1.getLOGsetName().getBytes();
					for(int i=0;i<msg.length;i++) {
						hpnmana1[20+i] = msg[i];
					}
					byte[] TP1hp = BitTools.intToByteArray(Tplayer1.getHp());
					byte[] TP1mana = BitTools.intToByteArray(Tplayer1.getMana());
					byte[] TP1MAXhp = BitTools.intToByteArray(Tplayer1.getMaxHp());
					byte[] TP1MAXmana = BitTools.intToByteArray(Tplayer1.getMAXMana());
					for(int i=0;i<2;i++) {
						hpnmana1[44+i] = TP1hp[i]; // current mana
						hpnmana1[48+i] = TP1mana[i]; // current hp
						hpnmana1[52+i] = TP1MAXhp[i]; //maxmana
						hpnmana1[56+i] = TP1MAXmana[i];// maxhp
					}	
					hpnmana1[40] = (byte)Tplayer1.getLevel();
					
				ServerFacade.getInstance().addWriteByChannel(Tplayer1.GetChannel(), fury);
				} 		 
				if(Tplayer2 != null && Tplayer1 != null){
					byte[] chid2 = BitTools.intToByteArray(Tplayer2.getCharID());
					Tplayer2.partyUID = Tplayer1.partyUID;
					hpnmana2[0] = (byte)0x3c;
					hpnmana2[4] = (byte)0x05;
					hpnmana2[6] = (byte)0x26;
					hpnmana2[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						hpnmana2[12+i] = chid2[i]; // charid
					}
					hpnmana2[16] = (byte)0x01;

					
					hpnmana2[42] = (byte)0x0f;
					hpnmana2[43] = (byte)0xbf;

					

					byte[] TP1 = BitTools.intToByteArray(Tplayer2.getCharID());
					for(int i=0;i<4;i++) {
						hpnmana2[16+i] = TP1[i]; 
					}	
					byte[] msg = Tplayer2.getLOGsetName().getBytes();
					for(int i=0;i<msg.length;i++) {
						hpnmana2[20+i] = msg[i];
					}
					byte[] TP2hp = BitTools.intToByteArray(Tplayer2.getHp());
					byte[] TP2mana = BitTools.intToByteArray(Tplayer2.getMana());
					byte[] TP2MAXhp = BitTools.intToByteArray(Tplayer2.getMaxHp());
					byte[] TP2MAXmana = BitTools.intToByteArray(Tplayer2.getMAXMana());
					for(int i=0;i<2;i++) {
						hpnmana2[44+i] = TP2hp[i]; // current mana
						hpnmana2[48+i] = TP2mana[i]; // current hp
						hpnmana2[52+i] = TP2MAXhp[i]; //maxmana
						hpnmana2[56+i] = TP2MAXmana[i];// maxhp 
					}	
					hpnmana2[40] = (byte)Tplayer2.getLevel();
					
				ServerFacade.getInstance().addWriteByChannel(Tplayer2.GetChannel(), fury);
				} 		 
				if(Tplayer3 != null && Tplayer1 != null){
					byte[] chid3 = BitTools.intToByteArray(Tplayer3.getCharID());
					Tplayer3.partyUID = Tplayer1.partyUID;
					hpnmana3[0] = (byte)0x3c;
					hpnmana3[4] = (byte)0x05;
					hpnmana3[6] = (byte)0x26;
					hpnmana3[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						hpnmana3[12+i] = chid3[i]; // charid
					}
					hpnmana3[16] = (byte)0x01;

					
					hpnmana3[42] = (byte)0x0f;
					hpnmana3[43] = (byte)0xbf;

					

					byte[] TP1 = BitTools.intToByteArray(Tplayer3.getCharID());
					for(int i=0;i<4;i++) {
						hpnmana3[16+i] = TP1[i]; 
					}	
					byte[] msg = Tplayer3.getLOGsetName().getBytes();
					for(int i=0;i<msg.length;i++) {
						hpnmana3[20+i] = msg[i];
					}
					byte[] TP3hp = BitTools.intToByteArray(Tplayer3.getHp());
					byte[] TP3mana = BitTools.intToByteArray(Tplayer3.getMana());
					byte[] TP3MAXhp = BitTools.intToByteArray(Tplayer3.getMaxHp());
					byte[] TP3MAXmana = BitTools.intToByteArray(Tplayer3.getMAXMana());
					for(int i=0;i<2;i++) {
						hpnmana3[44+i] = TP3hp[i]; // current mana
						hpnmana3[48+i] = TP3mana[i]; // current hp
						hpnmana3[52+i] = TP3MAXhp[i]; //maxmana
						hpnmana3[56+i] = TP3MAXmana[i];// maxhp 
					}	
					hpnmana3[40] = (byte)Tplayer3.getLevel();
					
				ServerFacade.getInstance().addWriteByChannel(Tplayer3.GetChannel(), fury);
				} 		 
				if(Tplayer4 != null && Tplayer1 != null){
					byte[] chid4 = BitTools.intToByteArray(Tplayer4.getCharID());
					Tplayer4.partyUID = Tplayer1.partyUID;
					hpnmana4[0] = (byte)0x3c;
					hpnmana4[4] = (byte)0x05;
					hpnmana4[6] = (byte)0x26;
					hpnmana4[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						hpnmana4[12+i] = chid4[i]; // charid
					}
					hpnmana4[16] = (byte)0x01;

					
					hpnmana4[42] = (byte)0x0f;
					hpnmana4[43] = (byte)0xbf;

					

					byte[] TP1 = BitTools.intToByteArray(Tplayer4.getCharID());
					for(int i=0;i<4;i++) {
						hpnmana4[16+i] = TP1[i]; 
					}	
					byte[] msg = Tplayer4.getLOGsetName().getBytes();
					for(int i=0;i<msg.length;i++) {
						hpnmana4[20+i] = msg[i];
					}
					byte[] TP4hp = BitTools.intToByteArray(Tplayer4.getHp());
					byte[] TP4mana = BitTools.intToByteArray(Tplayer4.getMana());
					byte[] TP4MAXhp = BitTools.intToByteArray(Tplayer4.getMaxHp());
					byte[] TP4MAXmana = BitTools.intToByteArray(Tplayer4.getMAXMana());
					for(int i=0;i<2;i++) {
						hpnmana4[44+i] = TP4hp[i]; // current mana
						hpnmana4[48+i] = TP4mana[i]; // current hp
						hpnmana4[52+i] = TP4MAXhp[i]; //maxmana
						hpnmana4[56+i] = TP4MAXmana[i];// maxhp 
					}	
					hpnmana4[40] = (byte)Tplayer4.getLevel();
					
				ServerFacade.getInstance().addWriteByChannel(Tplayer4.GetChannel(), fury);
				} 		 
				if(Tplayer5 != null && Tplayer1 != null){
					byte[] chid5 = BitTools.intToByteArray(Tplayer5.getCharID());
					Tplayer5.partyUID = Tplayer1.partyUID;
					hpnmana5[0] = (byte)0x3c;
					hpnmana5[4] = (byte)0x05;
					hpnmana5[6] = (byte)0x26;
					hpnmana5[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						hpnmana5[12+i] = chid5[i]; // charid
					}
					hpnmana5[16] = (byte)0x01;

					
					hpnmana5[42] = (byte)0x0f;
					hpnmana5[43] = (byte)0xbf;

					

					byte[] TP1 = BitTools.intToByteArray(Tplayer5.getCharID());
					for(int i=0;i<4;i++) {
						hpnmana5[16+i] = TP1[i]; 
					}	
					byte[] msg = Tplayer5.getLOGsetName().getBytes();
					for(int i=0;i<msg.length;i++) {
						hpnmana5[20+i] = msg[i];
					}
					byte[] TP5hp = BitTools.intToByteArray(Tplayer5.getHp());
					byte[] TP5mana = BitTools.intToByteArray(Tplayer5.getMana());
					byte[] TP5MAXhp = BitTools.intToByteArray(Tplayer5.getMaxHp());
					byte[] TP5MAXmana = BitTools.intToByteArray(Tplayer5.getMAXMana());
					for(int i=0;i<2;i++) {
						hpnmana5[44+i] = TP5hp[i]; // current mana
						hpnmana5[48+i] = TP5mana[i]; // current hp
						hpnmana5[52+i] = TP5MAXhp[i]; //maxmana
						hpnmana5[56+i] = TP5MAXmana[i];// maxhp 
					}	
					hpnmana5[40] = (byte)Tplayer5.getLevel();
					
				ServerFacade.getInstance().addWriteByChannel(Tplayer5.GetChannel(), fury);
				} 		 
				if(Tplayer6 != null && Tplayer1 != null){
					byte[] chid6 = BitTools.intToByteArray(Tplayer6.getCharID());
					Tplayer6.partyUID = Tplayer1.partyUID;
					hpnmana6[0] = (byte)0x3c;
					hpnmana6[4] = (byte)0x05;
					hpnmana6[6] = (byte)0x26;
					hpnmana6[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						hpnmana6[12+i] = chid6[i]; // charid
					}
					hpnmana6[16] = (byte)0x01;

					
					hpnmana6[42] = (byte)0x0f;
					hpnmana6[43] = (byte)0xbf;

					

					byte[] TP1 = BitTools.intToByteArray(Tplayer6.getCharID());
					for(int i=0;i<4;i++) {
						hpnmana6[16+i] = TP1[i]; 
					}	
					byte[] msg = Tplayer6.getLOGsetName().getBytes();
					for(int i=0;i<msg.length;i++) {
						hpnmana6[20+i] = msg[i];
					}
					byte[] TP6hp = BitTools.intToByteArray(Tplayer6.getHp());
					byte[] TP6mana = BitTools.intToByteArray(Tplayer6.getMana());
					byte[] TP6MAXhp = BitTools.intToByteArray(Tplayer6.getMaxHp());
					byte[] TP6MAXmana = BitTools.intToByteArray(Tplayer6.getMAXMana());
					for(int i=0;i<2;i++) {
						hpnmana6[44+i] = TP6hp[i]; // current mana
						hpnmana6[48+i] = TP6mana[i]; // current hp
						hpnmana6[52+i] = TP6MAXhp[i]; //maxmana
						hpnmana6[56+i] = TP6MAXmana[i];// maxhp 
					}	
					hpnmana6[40] = (byte)Tplayer6.getLevel();
					
				ServerFacade.getInstance().addWriteByChannel(Tplayer6.GetChannel(), fury);
				}
				if(Tplayer7 != null && Tplayer1 != null){
					byte[] chid7 = BitTools.intToByteArray(Tplayer7.getCharID());
					Tplayer7.partyUID = Tplayer1.partyUID;
					hpnmana7[0] = (byte)0x3c;
					hpnmana7[4] = (byte)0x05;
					hpnmana7[6] = (byte)0x26;
					hpnmana2[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						hpnmana7[12+i] = chid7[i]; // charid
					}
					hpnmana7[16] = (byte)0x01;

					
					hpnmana7[42] = (byte)0x0f;
					hpnmana7[43] = (byte)0xbf;

					

					byte[] TP1 = BitTools.intToByteArray(Tplayer7.getCharID());
					for(int i=0;i<4;i++) {
						hpnmana7[16+i] = TP1[i]; 
					}	
					byte[] msg = Tplayer7.getLOGsetName().getBytes();
					for(int i=0;i<msg.length;i++) {
						hpnmana7[20+i] = msg[i];
					}
					byte[] TP7hp = BitTools.intToByteArray(Tplayer7.getHp());
					byte[] TP7mana = BitTools.intToByteArray(Tplayer7.getMana());
					byte[] TP7MAXhp = BitTools.intToByteArray(Tplayer7.getMaxHp());
					byte[] TP7MAXmana = BitTools.intToByteArray(Tplayer7.getMAXMana());
					for(int i=0;i<2;i++) {
						hpnmana7[44+i] = TP7hp[i]; // current mana
						hpnmana7[48+i] = TP7mana[i]; // current hp
						hpnmana7[52+i] = TP7MAXhp[i]; //maxmana
						hpnmana7[56+i] = TP7MAXmana[i];// maxhp 
					}	
					hpnmana7[40] = (byte)Tplayer7.getLevel();
					
				ServerFacade.getInstance().addWriteByChannel(Tplayer7.GetChannel(), fury);
				} 
				if(Tplayer8 != null && Tplayer1 != null){
					byte[] chid8 = BitTools.intToByteArray(Tplayer8.getCharID());
					Tplayer8.partyUID = Tplayer1.partyUID;
					hpnmana8[0] = (byte)0x3c;
					hpnmana8[4] = (byte)0x05;
					hpnmana8[6] = (byte)0x26;
					hpnmana8[8] = (byte)0x01;
					for(int i=0;i<4;i++) {
						hpnmana8[12+i] = chid8[i]; // charid
					}
					hpnmana8[16] = (byte)0x01;

					
					hpnmana8[42] = (byte)0x0f;
					hpnmana8[43] = (byte)0xbf;

					

					byte[] TP1 = BitTools.intToByteArray(Tplayer8.getCharID());
					for(int i=0;i<4;i++) {
						hpnmana8[16+i] = TP1[i]; 
					}	
					byte[] msg = Tplayer8.getLOGsetName().getBytes();
					for(int i=0;i<msg.length;i++) {
						hpnmana8[20+i] = msg[i];
					}
					byte[] TP8hp = BitTools.intToByteArray(Tplayer8.getHp());
					byte[] TP8mana = BitTools.intToByteArray(Tplayer8.getMana());
					byte[] TP8MAXhp = BitTools.intToByteArray(Tplayer8.getMaxHp());
					byte[] TP8MAXmana = BitTools.intToByteArray(Tplayer8.getMAXMana());
					for(int i=0;i<2;i++) {
						hpnmana8[44+i] = TP8hp[i]; // current mana
						hpnmana8[48+i] = TP8mana[i]; // current hp
						hpnmana8[52+i] = TP8MAXhp[i]; //maxmana
						hpnmana8[56+i] = TP8MAXmana[i];// maxhp 
					}	
					hpnmana8[40] = (byte)Tplayer8.getLevel();
					
				ServerFacade.getInstance().addWriteByChannel(Tplayer8.GetChannel(), fury);
				} 
				
				Iterator<Map.Entry<Integer, Integer>> iter1 = partymembers.entrySet().iterator();
				while(iter1.hasNext()){
					Map.Entry<Integer, Integer> pairs1 = iter1.next();
					Integer tmp1 = pairs1.getValue();
					Character tmpz = wmap.getCharacter(tmp1);
					if(tmpz != null && Tplayer1 != null && tmpz.partyUID == Tplayer1.partyUID && ServerFacade.getInstance().getCon().getConnection(tmpz.GetChannel()) != null && ServerFacade.getInstance().getCon().getConnection(Tplayer1.GetChannel()) != null){
					if(Tplayer1 != null && ServerFacade.getInstance().getCon().getConnection(Tplayer1.GetChannel()) != null){ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), hpnmana1);}
					if(Tplayer2 != null && ServerFacade.getInstance().getCon().getConnection(Tplayer2.GetChannel()) != null){ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), hpnmana2);}
					if(Tplayer3 != null && ServerFacade.getInstance().getCon().getConnection(Tplayer3.GetChannel()) != null){ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), hpnmana3);}
					if(Tplayer4 != null && ServerFacade.getInstance().getCon().getConnection(Tplayer4.GetChannel()) != null){ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), hpnmana4);}
					if(Tplayer5 != null && ServerFacade.getInstance().getCon().getConnection(Tplayer5.GetChannel()) != null){ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), hpnmana5);}
					if(Tplayer6 != null && ServerFacade.getInstance().getCon().getConnection(Tplayer6.GetChannel()) != null){ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), hpnmana6);}
					if(Tplayer7 != null && ServerFacade.getInstance().getCon().getConnection(Tplayer7.GetChannel()) != null){ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), hpnmana7);}
					if(Tplayer8 != null && ServerFacade.getInstance().getCon().getConnection(Tplayer8.GetChannel()) != null){ServerFacade.getInstance().addWriteByChannel(tmpz.GetChannel(), hpnmana8);}
					}
				}
			}	
}
