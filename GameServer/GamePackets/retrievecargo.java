package GameServer.GamePackets;

import item.Item;
import item.ItemCache;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.ResultSet;

import timer.SystemTimer;

import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;

import Connections.Connection;
import Database.CharacterDAO;
import Database.Queries;
import Database.SQLconnection;
import Encryption.Decryptor;

public class retrievecargo implements Packet {

	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}
	
	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling retrievecargo ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		decrypted = Decryptor.Decrypt(decrypted);
		Character cur = ((PlayerConnection)con).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
		 //PERFECT WAY TO KNOW WHATS WHAT !
		/*int onee = 0;
		for(int i=0;i<decrypted.length;i++){System.out.print(" "+onee+":"+decrypted[i]+" ");onee++; }
		
		System.out.println(" ");
		System.out.println(" ");
		System.out.print("WTF:");
		for(int i=0;i<decrypted.length;i++){System.out.printf("%02x ", (decrypted[i]&0xFF));}
		System.out.println(" ");*/
		if(cur.IsTrading){return null;}
		if(cur.IsVending){return null;}
		boolean Valid = false;
		byte[] fury = new byte[128];
		byte[] gold1 = new byte[8];
		byte[] MailID = new byte[4];
		byte[] test1 = new byte[4];
		byte[] test2 = new byte[4];
		byte[] test3 = new byte[4];
		byte[] test4 = new byte[4];
		byte[] test5 = new byte[4];
		fury[0] = (byte)0x80;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x46;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
			fury[12+i] = chid[i];
			test1[i] = decrypted[52+i]; // 1st item
			test2[i] = decrypted[64+i]; // 2st item
			test3[i] = decrypted[76+i]; // 3st item
			test4[i] = decrypted[88+i]; // 4st item
			test5[i] = decrypted[100+i]; // 5st item
			MailID[i] = decrypted[108+i]; // MAIL ID
		}
		fury[16] = (byte)0x01;
		fury[18] = decrypted[0];
		
		for(int i=0;i<gold1.length;i++) {
			gold1[i] = decrypted[20+i];;
		}
		long g = BitTools.ByteArrayToLong(gold1);
		
		//generate key for mail
		int key = BitTools.byteArrayToInt(MailID);
		
		// retrieve gold only                // Actually: if(decrypted[0] == 4){
		if((int)BitTools.byteArrayToInt(test1) == 273000229){ // if 1st item is empty and gold is not.
			byte[] gold = BitTools.LongToByteArrayREVERSE(-cur.getgold_REQUIRED(key));
			for(int i=0;i<gold.length;i++) {
				fury[36+i] = gold[i];
			}
			
			//Slots
			for(int i=0;i<20;i++) {
				fury[44+i] = decrypted[28+i];
			}

			// item x,y
		    fury[66] = (byte)0xff;
			fury[67] = (byte)0xff;
			
		    fury[78] = decrypted[62];
			fury[79] = decrypted[63];
			
			fury[90] = decrypted[74];
			fury[91] = decrypted[75];
			
			fury[102] = decrypted[86];
			fury[103] = decrypted[87];
			
			fury[114] = decrypted[98];
			fury[115] = decrypted[99];
			
			CharacterDAO.deletemail(key);
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury); 
			cur.setgold(cur.getgold() + cur.getgold_REQUIRED(key));
			cur.refreshmailbox();
			cur.savecharacter();
			return null;
		}else
			
		// Cancel mail, and send it back.
		if(decrypted[0] == 3){
			
			// check first if mail still exist
    		try{
    			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getmailbymailid(SQLconnection.getInstance().getaConnection(), key));
    			if(rs != null){
    			if(rs.next()){
    			if(rs.getInt("mailid") == key){Valid = true;
    			}else{Charstuff.getInstance().respond("The sender already canceled this mail before you could cancel it.",con); return null;}
    			}else{Charstuff.getInstance().respond("The sender already canceled this mail before you could cancel it.",con); return null;}
    			}else{Charstuff.getInstance().respond("The sender already canceled this mail before you could cancel it.",con); return null;}
    		    }catch (Exception e) {
    				 System.out.println(e.getMessage());
    			}
    	if(Valid == true){
		//check if already canncelded.
		if(cur.getmailCanceled(key) == 1){Charstuff.getInstance().respond("This mail has already been canceled.",con); return null;}	
			
			// set some jiz pvp saan
			int i1 = cur.getitem1(key);
			int i2 = cur.getitem2(key);
			int i3 = cur.getitem3(key);
			int i4 = cur.getitem4(key);
			int i5 = cur.getitem5(key);
			
			int s1 = cur.getstack1(key);
			int s2 = cur.getstack2(key);
			int s3 = cur.getstack3(key);
			int s4 = cur.getstack4(key);
			int s5 = cur.getstack5(key);

			// GET FROM MEMORY with key gg
	    CharacterDAO.sendmail(cur.getCharID(),"[Canceled] "+cur.getLOGsetName(), cur.getcharid_SENDER(key), cur.getname_SENDER(key), 0,i1,s1,i2,s2,i3,s3,i4,s4,i5,s5,1);
		CharacterDAO.deletemail(key);
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury); 	
		cur.refreshmailbox();
		}
		return null;
		}else
			
		// send mail
		if(decrypted[0] == 0){
			byte[] ITEMID1 = new byte[4];
			byte[] ITEMID2 = new byte[4];
			byte[] ITEMID3 = new byte[4];
			byte[] ITEMID4 = new byte[4];
			byte[] ITEMID5 = new byte[4];
			byte[] STACK1 = new byte[4];
			byte[] STACK2 = new byte[4];
			byte[] STACK3 = new byte[4];
			byte[] STACK4 = new byte[4];
			byte[] STACK5 = new byte[4];
			byte[] Name = new byte[17];
			
			// items for db
			for(int i=0;i<4;i++) {
				fury[36+i] = decrypted[20+i]; // -gold <-- BUGGGEDD doesnt substract 1G from inventory WTFF
				ITEMID1[i] = decrypted[52+i];
				ITEMID2[i] = decrypted[64+i];
				ITEMID3[i] = decrypted[76+i];
				ITEMID4[i] = decrypted[88+i];
				ITEMID5[i] = decrypted[100+i];
			}
			// stack for db
			for(int i=0;i<2;i++) {
				STACK1[i] = decrypted[56+i];
				STACK2[i] = decrypted[68+i];
				STACK3[i] = decrypted[80+i];
				STACK4[i] = decrypted[92+i];
				STACK5[i] = decrypted[104+i];
			}
			
			int i1 = cur.getInventorySLOT((int)decrypted[28]);
			int i2 = cur.getInventorySLOT((int)decrypted[32]);
			int i3 = cur.getInventorySLOT((int)decrypted[36]);
			int i4 = cur.getInventorySLOT((int)decrypted[40]);
			int i5 = cur.getInventorySLOT((int)decrypted[44]);
			
			if(g < 0){cur.respondguildTIMED("You cannot request for negative gold values.", cur.GetChannel());return null;}
			
			if(i1 == 1337){cur.respondguildTIMED("1st item == 1337, inform GM.", cur.GetChannel());return null;}
			if(i2 == 1337){cur.respondguildTIMED("2nd item == 1337, inform GM.", cur.GetChannel());return null;}
			if(i3 == 1337){cur.respondguildTIMED("3rd item == 1337, inform GM.", cur.GetChannel());return null;}
			if(i4 == 1337){cur.respondguildTIMED("4th item == 1337, inform GM.", cur.GetChannel());return null;}
			if(i5 == 1337){cur.respondguildTIMED("5th item == 1337, inform GM.", cur.GetChannel());return null;}
			
			if(i1 == 283000022 || i1 == 283000021 || i1 == 283000020){
				Charstuff.getInstance().respondguild("Can only Trade the MH Points.", cur.GetChannel());
				return null;
			}
			if(i2 == 283000022 || i2 == 283000021 || i2 == 283000020){
				Charstuff.getInstance().respondguild("Can only Trade the MH Points.", cur.GetChannel());
				return null;
			}
			if(i3 == 283000022 || i3 == 283000021 || i3 == 283000020){
				Charstuff.getInstance().respondguild("Can only Trade the MH Points.", cur.GetChannel());
				return null;
			}
			if(i4 == 283000022 || i4 == 283000021 || i4 == 283000020){
				Charstuff.getInstance().respondguild("Can only Trade the MH Points.", cur.GetChannel());
				return null;
			}
			if(i5 == 283000022 || i5 == 283000021 ||i5 == 283000020){
				Charstuff.getInstance().respondguild("Can only Trade the MH Points.", cur.GetChannel());
				return null;
			}

			if(Charstuff.getInstance().tryNon_Tradable_items(cur.getInventorySLOT((int)decrypted[28]))){cur.KillInvFreeze();return null;}
			if(Charstuff.getInstance().tryNon_Tradable_items(cur.getInventorySLOT((int)decrypted[32]))){cur.KillInvFreeze();return null;}
			if(Charstuff.getInstance().tryNon_Tradable_items(cur.getInventorySLOT((int)decrypted[36]))){cur.KillInvFreeze();return null;}
			if(Charstuff.getInstance().tryNon_Tradable_items(cur.getInventorySLOT((int)decrypted[40]))){cur.KillInvFreeze();return null;}
			if(Charstuff.getInstance().tryNon_Tradable_items(cur.getInventorySLOT((int)decrypted[44]))){cur.KillInvFreeze();return null;}
			
			int s1 = cur.getInventorySTACK((int)decrypted[28]);
			int s2 = cur.getInventorySTACK((int)decrypted[32]);
			int s3 = cur.getInventorySTACK((int)decrypted[36]);
			int s4 = cur.getInventorySTACK((int)decrypted[40]);
			int s5 = cur.getInventorySTACK((int)decrypted[44]);
	
			// to receiver name
			for(int i=0;i<17;i++) {
				fury[19+i] = decrypted[1+i];
				Name[i] = decrypted[1+i];
			}
			int finalCharID = 0; String finalnameparsed; 
			String TargetName = BitTools.byteArrayToString(Name);
			 byte[] hisname = new String(TargetName).getBytes();
		        byte arr[] = hisname;
		        try {
		        		int z;
		        		for (z = 0; z < arr.length && arr[z] != 0; z++) { }
		        		 finalnameparsed = new String(arr, 0, z, "UTF-8");
		        		try{
		        			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getCharacterByName(SQLconnection.getInstance().getaConnection(), finalnameparsed));
		        			if(rs != null){
		        			if(rs.next()){
		        			if(cur.getLOGsetName().equals(rs.getString("charname"))){Charstuff.getInstance().respond("You cannot send items to yourself!",con); return null;}
		        			else if(finalnameparsed.equals(rs.getString("charname"))){Valid = true; finalCharID = rs.getInt("CharacterID");}
		        			else{Charstuff.getInstance().respond("Character name does not exist!",con); return null;}
		        			}
		        			}else{Charstuff.getInstance().respond("Character name does not exist!",con); return null;}
		        		    }catch (Exception e) {
		        				 System.out.println(e.getMessage());
		        			}
		if(Valid == true){
			/*System.out.println("(int)decrypted[28]: "+(int)decrypted[28]);
			System.out.println("(int)decrypted[32]: "+(int)decrypted[32]);
			System.out.println("(int)decrypted[36]: "+(int)decrypted[36]);
			System.out.println("(int)decrypted[40]: "+(int)decrypted[40]);
			System.out.println("(int)decrypted[44]: "+(int)decrypted[44]);*/
			// inv slot 
			if((int)decrypted[28] == -1){}else
			if(cur.InventorySLOT.containsKey((int)decrypted[28]) && cur.getInventorySLOT((int)decrypted[28]) != 0 && cur.getInventorySLOT((int)decrypted[28]) == i1){	
			cur.DeleteInvItem((int)decrypted[28]);
			}else{/*Charstuff.getInstance().respondguildTIMED("1st is Fake item!", cur.GetChannel());*/ return null;}
			if((int)decrypted[32] == -1){}else
			if(cur.InventorySLOT.containsKey((int)decrypted[32]) && cur.getInventorySLOT((int)decrypted[32]) != 0 && cur.getInventorySLOT((int)decrypted[32]) == i2){
			cur.DeleteInvItem((int)decrypted[32]);
			}else{/*Charstuff.getInstance().respondguildTIMED("2nd is Fake item!", cur.GetChannel());*/ return null;}
			if((int)decrypted[36] == -1){}else
			if(cur.InventorySLOT.containsKey((int)decrypted[36]) && cur.getInventorySLOT((int)decrypted[36]) != 0 && cur.getInventorySLOT((int)decrypted[36]) == i3){
			cur.DeleteInvItem((int)decrypted[36]);
			}else{/*Charstuff.getInstance().respondguildTIMED("3rd is Fake item!", cur.GetChannel());*/ return null;}
			if((int)decrypted[40] == -1){}else
			if(cur.InventorySLOT.containsKey((int)decrypted[40]) && cur.getInventorySLOT((int)decrypted[40]) != 0 && cur.getInventorySLOT((int)decrypted[40]) == i4){
			cur.DeleteInvItem((int)decrypted[40]);
			}else{/*Charstuff.getInstance().respondguildTIMED("4th is Fake item!", cur.GetChannel());*/ return null;}
			if((int)decrypted[44] == -1){}else
			if(cur.InventorySLOT.containsKey((int)decrypted[44]) && cur.getInventorySLOT((int)decrypted[44]) != 0 && cur.getInventorySLOT((int)decrypted[44]) == i5){
			cur.DeleteInvItem((int)decrypted[44]);
			}else{/*Charstuff.getInstance().respondguildTIMED("5th is Fake item!", cur.GetChannel());*/ return null;}
			
			//Slots
			for(int i=0;i<20;i++) {
				fury[44+i] = decrypted[28+i];
			}
		/*	fury[44] = decrypted[28];
			fury[48] = decrypted[32];
			fury[52] = decrypted[36];
			fury[56] = decrypted[40];
			fury[60] = decrypted[44];*/

			// item x,y
		    fury[66] = decrypted[50];
			fury[67] = decrypted[51];
			
		    fury[78] = decrypted[62];
			fury[79] = decrypted[63];
			
			fury[90] = decrypted[74];
			fury[91] = decrypted[75];
			
			fury[102] = decrypted[86];
			fury[103] = decrypted[87];
			
			fury[114] = decrypted[98];
			fury[115] = decrypted[99];
			
			/*long newgold = cur.getgold() - g;
			if(newgold < 0){return null;};
			cur.setgold(newgold); // set new gold */

			// items
			for(int i=0;i<4;i++) {
				fury[68+i] = decrypted[52+i];
				fury[80+i] = decrypted[64+i];
				fury[92+i] = decrypted[76+i];
				fury[104+i] = decrypted[88+i];
				fury[116+i] = decrypted[100+i];
			}
			// stack
			for(int i=0;i<2;i++) {
				fury[72+i] = decrypted[56+i];
				fury[84+i] = decrypted[68+i];
				fury[96+i] = decrypted[80+i];
				fury[108+i] = decrypted[92+i];
				fury[120+i] = decrypted[104+i];
			}
			CharacterDAO.sendmail(cur.getCharID(),"[New] "+cur.getLOGsetName(), finalCharID, finalnameparsed, g, i1,s1,i2,s2,i3,s3,i4,s4,i5,s5,0);
			ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury); 
			cur.refreshmailbox();
			cur.savecharacter();
			}else{Charstuff.getInstance().respond("Character name does not exist!",con);return null;}
		        } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				////e.printStackTrace();
				}
		}else
		
		// receive mail
		if(decrypted[0] == 1){
			
			// check first if mail still exist
    		try{
    			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getmailbymailid(SQLconnection.getInstance().getaConnection(), key));
    			if(rs != null){
    			if(rs.next()){
    			if(rs.getInt("mailid") == key){Valid = true;
    			}else{Charstuff.getInstance().respond("The sender already canceled this mail before you could retrieve it.",con); return null;}
    			}else{Charstuff.getInstance().respond("The sender already canceled this mail before you could retrieve it.",con); return null;}
    			}else{Charstuff.getInstance().respond("The sender already canceled this mail before you could retrieve it.",con); return null;}
    		    }catch (Exception e) {
    				 System.out.println(e.getMessage());
    			}
			
    	if(Valid = true){
		byte[] name = cur.getname_SENDER(key).getBytes();
		for(int i=0;i<name.length;i++) {
			fury[19+i] = name[i];
		}
		
		long newgold = cur.getgold() - cur.getgold_REQUIRED(key);
		if(newgold < 0){Charstuff.getInstance().respond("You do have not enough gold to retrieve this mail.",con);return null;}
		cur.setgold(newgold); // set new gold 
		byte[] gold = BitTools.LongToByteArrayREVERSE(cur.getgold_REQUIRED(key));
		for(int i=0;i<gold.length;i++) {
			fury[36+i] = gold[i];
		}
		
		if(cur.getInventorySLOT((int)decrypted[28]) != 0){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", cur.GetChannel()); return null;}
		if(cur.getInventorySLOT((int)decrypted[32]) != 0){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", cur.GetChannel()); return null;}
		if(cur.getInventorySLOT((int)decrypted[36]) != 0){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", cur.GetChannel()); return null;}
		if(cur.getInventorySLOT((int)decrypted[40]) != 0){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", cur.GetChannel()); return null;}
		if(cur.getInventorySLOT((int)decrypted[44]) != 0){Charstuff.getInstance().respondguild("Tried to overlap on a different stackable item, Please relog.", cur.GetChannel()); return null;}
		
		
		byte[] item_1 = BitTools.intToByteArray(cur.getitem1(key));
		byte[] stack_1 = BitTools.intToByteArray(cur.getstack1(key));
		byte[] item_2 = BitTools.intToByteArray(cur.getitem2(key));
		byte[] stack_2 = BitTools.intToByteArray(cur.getstack2(key));
		byte[] item_3 = BitTools.intToByteArray(cur.getitem3(key));
		byte[] stack_3 = BitTools.intToByteArray(cur.getstack3(key));
		byte[] item_4 = BitTools.intToByteArray(cur.getitem4(key));
		byte[] stack_4 = BitTools.intToByteArray(cur.getstack4(key));
		byte[] item_5 = BitTools.intToByteArray(cur.getitem5(key));
		byte[] stack_5 = BitTools.intToByteArray(cur.getstack5(key));

		String e1 = null, e2 = null, e3 = null, e4 = null, e5 = null;
		if(ItemCache.Items.containsKey(cur.getitem1(key))){e1 = ItemCache.Items.get(cur.getitem1(key));}
		if(ItemCache.Items.containsKey(cur.getitem2(key))){e2 = ItemCache.Items.get(cur.getitem2(key));}
		if(ItemCache.Items.containsKey(cur.getitem3(key))){e3 = ItemCache.Items.get(cur.getitem3(key));}
		if(ItemCache.Items.containsKey(cur.getitem4(key))){e4 = ItemCache.Items.get(cur.getitem4(key));}
		if(ItemCache.Items.containsKey(cur.getitem5(key))){e5 = ItemCache.Items.get(cur.getitem5(key));}
		
		String[] item1 = null, item2 = null, item3 = null, item4 = null, item5 = null; 
		
		if(e1 != null){item1 = e1.split(",");}
		if(e2 != null){item2 = e2.split(",");}
		if(e3 != null){item3 = e3.split(",");}
		if(e4 != null){item4 = e4.split(",");}
		if(e5 != null){item5 = e5.split(",");}

		if(item1 != null){
		if(Long.valueOf(item1[22]) != 0 ){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item1[22])) + System.currentTimeMillis();
		cur.setitem_end_date(cur.getitem1(key), minato);
		cur.expireactive();
		}}
		if(item2 != null){
		if(Long.valueOf(item2[22]) != 0){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item2[22])) + System.currentTimeMillis();
		cur.setitem_end_date(cur.getitem2(key), minato);
		cur.expireactive();
		}}
		if(item3 != null){
		if(Long.valueOf(item3[22]) != 0){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item3[22])) + System.currentTimeMillis();
		cur.setitem_end_date(cur.getitem3(key), minato);
		cur.expireactive();
		}}
		if(item4 != null){
		if(Long.valueOf(item4[22]) != 0){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item4[22])) + System.currentTimeMillis();
		cur.setitem_end_date(cur.getitem4(key), minato);
		cur.expireactive();
		}}
		if(item5 != null){
		if(Long.valueOf(item5[22]) != 0){
		long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item5[22])) + System.currentTimeMillis();
		cur.setitem_end_date(cur.getitem5(key), minato);
		cur.expireactive();
		}}
		
		// inv slot 
		if(cur.getitem1(key) != 0){	
		cur.setInventory((int)decrypted[28], cur.getitem1(key), (int)decrypted[50], (int)decrypted[51], cur.getstack1(key));
		}
		if(cur.getitem2(key) != 0){
		cur.setInventory((int)decrypted[32], cur.getitem2(key), (int)decrypted[62], (int)decrypted[63], cur.getstack2(key));
		}
		if(cur.getitem3(key) != 0){
		cur.setInventory((int)decrypted[36], cur.getitem3(key), (int)decrypted[74], (int)decrypted[75], cur.getstack3(key));
		}
		if(cur.getitem4(key) != 0){
		cur.setInventory((int)decrypted[40], cur.getitem4(key), (int)decrypted[86], (int)decrypted[87], cur.getstack4(key));
		}
		if(cur.getitem5(key) != 0){
		cur.setInventory((int)decrypted[44], cur.getitem5(key), (int)decrypted[98], (int)decrypted[99], cur.getstack5(key));
		}
		
		//Slots
		for(int i=0;i<20;i++) {
			fury[44+i] = decrypted[28+i];
		}

		// item x,y
	    fury[66] = decrypted[50];
		fury[67] = decrypted[51];
		
	    fury[78] = decrypted[62];
		fury[79] = decrypted[63];
		
		fury[90] = decrypted[74];
		fury[91] = decrypted[75];
		
		fury[102] = decrypted[86];
		fury[103] = decrypted[87];
		
		fury[114] = decrypted[98];
		fury[115] = decrypted[99];
		
		// items
		for(int i=0;i<4;i++) {
			fury[68+i] = item_1[i];
			fury[80+i] = item_2[i];
			fury[92+i] = item_3[i];
			fury[104+i] = item_4[i];
			fury[116+i] = item_5[i];
		}
		// stack
		for(int i=0;i<2;i++) {
			fury[72+i] = stack_1[i];
			fury[84+i] = stack_2[i];
			fury[96+i] = stack_3[i];
			fury[108+i] = stack_4[i];
			fury[120+i] = stack_5[i];
		}
		if(cur.getgold_REQUIRED(key) != 0){
		CharacterDAO.sendmail(cur.getCharID(),"[Gold] "+cur.getLOGsetName(), cur.getcharid_SENDER(key), cur.getname_SENDER(key), cur.getgold_REQUIRED(key),273000229,1,0,0,0,0,0,0,0,0,0);
		}
		CharacterDAO.deletemail(key);
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury); 
		cur.refreshmailbox();
		cur.savecharacter();
		}
		}else
			// cancel selfmail
			if(decrypted[0] == 2){
				
				// check first if mail still exist
        		try{
        			ResultSet rs = SQLconnection.getInstance().executeQuery(Queries.getmailbymailid(SQLconnection.getInstance().getaConnection(), key));
        			if(rs != null){
        			if(rs.next()){
        			if(rs.getInt("mailid") == key){Valid = true;
        			}else{Charstuff.getInstance().respond("The retriever already retrieved this mail before you could cancel it.",con); return null;}
        			}else{Charstuff.getInstance().respond("The retriever already retrieved this mail before you could cancel it.",con); return null;}
        			}else{Charstuff.getInstance().respond("The retriever already retrieved this mail before you could cancel it.",con); return null;}
        		    }catch (Exception e) {
        				 System.out.println(e.getMessage());
        			}
				
				
				
				if(Valid = true){
				byte[] name = cur.getname_SENDER(key).getBytes();
				for(int i=0;i<name.length;i++) {
					fury[19+i] = name[i];
				}

				byte[] gold = BitTools.intToByteArray(0);
				for(int i=0;i<gold.length;i++) {
					fury[36+i] = gold[i];
				}
				
				
				byte[] item_1 = BitTools.intToByteArray(cur.getitem1(key));
				byte[] stack_1 = BitTools.intToByteArray(cur.getstack1(key));
				byte[] item_2 = BitTools.intToByteArray(cur.getitem2(key));
				byte[] stack_2 = BitTools.intToByteArray(cur.getstack2(key));
				byte[] item_3 = BitTools.intToByteArray(cur.getitem3(key));
				byte[] stack_3 = BitTools.intToByteArray(cur.getstack3(key));
				byte[] item_4 = BitTools.intToByteArray(cur.getitem4(key));
				byte[] stack_4 = BitTools.intToByteArray(cur.getstack4(key));
				byte[] item_5 = BitTools.intToByteArray(cur.getitem5(key));
				byte[] stack_5 = BitTools.intToByteArray(cur.getstack5(key));

				String e1 = null, e2 = null, e3 = null, e4 = null, e5 = null;
				if(ItemCache.Items.containsKey(cur.getitem1(key))){e1 = ItemCache.Items.get(cur.getitem1(key));}
				if(ItemCache.Items.containsKey(cur.getitem2(key))){e2 = ItemCache.Items.get(cur.getitem2(key));}
				if(ItemCache.Items.containsKey(cur.getitem3(key))){e3 = ItemCache.Items.get(cur.getitem3(key));}
				if(ItemCache.Items.containsKey(cur.getitem4(key))){e4 = ItemCache.Items.get(cur.getitem4(key));}
				if(ItemCache.Items.containsKey(cur.getitem5(key))){e5 = ItemCache.Items.get(cur.getitem5(key));}
				
				String[] item1 = null, item2 = null, item3 = null, item4 = null, item5 = null; 
				
				if(e1 != null){item1 = e1.split(",");}
				if(e2 != null){item2 = e2.split(",");}
				if(e3 != null){item3 = e3.split(",");}
				if(e4 != null){item4 = e4.split(",");}
				if(e5 != null){item5 = e5.split(",");}

				if(item1 != null){
				if(Long.valueOf(item1[22]) != 0 ){
				long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item1[22])) + System.currentTimeMillis();
				cur.setitem_end_date(cur.getitem1(key), minato);
				cur.expireactive();
				}}
				if(item2 != null){
				if(Long.valueOf(item2[22]) != 0){
				long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item2[22])) + System.currentTimeMillis();
				cur.setitem_end_date(cur.getitem2(key), minato);
				cur.expireactive();
				}}
				if(item3 != null){
				if(Long.valueOf(item3[22]) != 0){
				long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item3[22])) + System.currentTimeMillis();
				cur.setitem_end_date(cur.getitem3(key), minato);
				cur.expireactive();
				}}
				if(item4 != null){
				if(Long.valueOf(item4[22]) != 0){
				long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item4[22])) + System.currentTimeMillis();
				cur.setitem_end_date(cur.getitem4(key), minato);
				cur.expireactive();
				}}
				if(item5 != null){
				if(Long.valueOf(item5[22]) != 0){
				long minato = SystemTimer.MinuteToMiliseconds(Long.valueOf(item5[22])) + System.currentTimeMillis();
				cur.setitem_end_date(cur.getitem5(key), minato);
				cur.expireactive();
				}}
				
				// inv slot 
				if(cur.getitem1(key) != 0){	
				cur.setInventory((int)decrypted[28], cur.getitem1(key), (int)decrypted[50], (int)decrypted[51], cur.getstack1(key));
				}
				if(cur.getitem2(key) != 0){
				cur.setInventory((int)decrypted[32], cur.getitem2(key), (int)decrypted[62], (int)decrypted[63], cur.getstack2(key));
				}
				if(cur.getitem3(key) != 0){
				cur.setInventory((int)decrypted[36], cur.getitem3(key), (int)decrypted[74], (int)decrypted[75], cur.getstack3(key));
				}
				if(cur.getitem4(key) != 0){
				cur.setInventory((int)decrypted[40], cur.getitem4(key), (int)decrypted[86], (int)decrypted[87], cur.getstack4(key));
				}
				if(cur.getitem5(key) != 0){
				cur.setInventory((int)decrypted[44], cur.getitem5(key), (int)decrypted[98], (int)decrypted[99], cur.getstack5(key));
				}
				
				//Slots
				for(int i=0;i<20;i++) {
					fury[44+i] = decrypted[28+i];
				}

				// item x,y
			    fury[66] = decrypted[50];
				fury[67] = decrypted[51];
				
			    fury[78] = decrypted[62];
				fury[79] = decrypted[63];
				
				fury[90] = decrypted[74];
				fury[91] = decrypted[75];
				
				fury[102] = decrypted[86];
				fury[103] = decrypted[87];
				
				fury[114] = decrypted[98];
				fury[115] = decrypted[99];
				
				// items
				for(int i=0;i<4;i++) {
					fury[68+i] = item_1[i];
					fury[80+i] = item_2[i];
					fury[92+i] = item_3[i];
					fury[104+i] = item_4[i];
					fury[116+i] = item_5[i];
				}
				// stack
				for(int i=0;i<2;i++) {
					fury[72+i] = stack_1[i];
					fury[84+i] = stack_2[i];
					fury[96+i] = stack_3[i];
					fury[108+i] = stack_4[i];
					fury[120+i] = stack_5[i];
				}
				CharacterDAO.deletemail(key);
				ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury); 
				cur.refreshmailbox();
				cur.savecharacter();
				}
				}
		return null;
	}
	
}
