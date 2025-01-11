package GameServer.GamePackets.UseItemList;

import Connections.Connection;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;


public class POTSone implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		byte[] buff = new byte[44];
		 buff[0] = (byte)0x2c; 
		 buff[4] = (byte)0x05;
		 buff[6] = (byte)0x1f;
		 buff[8] = (byte)0x01;
		 for(int i=0;i<4;i++){
		 buff[12+i] = chid[i]; 				
		 }	
		 buff[26] = (byte)0x01; 
		 buff[28] = (byte)0x05; 
		 buff[39] = (byte)0x01; 
		 buff[32] = (byte)0x05; 
		 buff[33] = (byte)0x01; 
		 buff[36] = (byte)0xcc; 
		 buff[38] = (byte)0xcc; 
		 buff[40] = (byte)0x98; 
		 buff[42] = (byte)0x98;
		 
		 
		 
		 if(ItemID == 213062530){ // FAD
				 byte[] bufftimez = BitTools.intToByteArray(9000); // 10 hrs
				 buff[16] = (byte)0x02;	 
				 buff[20] = (byte)0x53;	 
				 buff[24] = (byte)0x7d;  
					 for(int i=0;i<2;i++) {
					 buff[i+22] = bufftimez[i];  
					 }
				if(cur.PotSLOT.containsKey(2)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	 
				cur.PotSLOT.put(2, 2);	 
				cur.PotIconID.put(2, 83);	
				cur.PotTime.put(2, 9000); // x 4 = 36000 seconds = 600 minutes = 10 hours
				cur.PotValue.put(2, 125);	
				cur.FAD = 1;	
		} 
		if(ItemID == 283000080){ // jackpot tag
				 byte[] bufftimez = BitTools.intToByteArray(4500); // 5 hrs
				 buff[16] = (byte)0x00;	 
				 buff[20] = (byte)0x5c;	  
				 buff[24] = (byte)0x08; 
					 for(int i=0;i<2;i++) {
					 buff[i+22] = bufftimez[i];  
					 }
						cur.PotSLOT.put(0, 0);	 
						cur.PotIconID.put(0, 92);	
						cur.PotTime.put(0, 4500);	
						cur.PotValue.put(0, 8);	 
				cur.JACKPOT_TAG   = 1;	
		}
		if(ItemID == 283000079){ // Double item drop tag   
			 byte[] bufftimez = BitTools.intToByteArray(4500); // 5 hrs
			 buff[16] = (byte)0x01;	 
			 buff[20] = (byte)0x5b;	  
			 buff[24] = (byte)0x28; 
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(1)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	 
					cur.PotSLOT.put(1, 1);	 
					cur.PotIconID.put(1, 91);	
					cur.PotTime.put(1, 4500);	
					cur.PotValue.put(1, 40);	 
			cur.DOUBLE_ITEM_DROP_TAG   = 1;	
		} 
		if(ItemID == 213062709){ // fame 100%
				 byte[] bufftimez = BitTools.intToByteArray(1800); // 2 hrs
				 buff[16] = (byte)0x07;	   
				 buff[20] = (byte)0x5a;	 
				 //buff[22] = (byte)0x28;  
				 //buff[23] = (byte)0x23; 
				 buff[24] = (byte)0x64;    
					 for(int i=0;i<2;i++) {
					 buff[i+22] = bufftimez[i];  
					 }
						if(cur.PotSLOT.containsKey(7)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
						cur.PotSLOT.put(7, 7);	 
						cur.PotIconID.put(7, 90);	
						cur.PotTime.put(7, 1800);	
						cur.PotValue.put(7, 101);	
				cur.Fame_Tag_100 = 1;	
		}
		if(ItemID == 273001205){ // 10%
			 byte[] bufftimez = BitTools.intToByteArray(900); // 1 hrs
			 buff[16] = (byte)0x06;	   
			 buff[20] = (byte)0x52;	 
			 //buff[22] = (byte)0x28;  
			 //buff[23] = (byte)0x23; 
			 buff[24] = (byte)0x0a;    
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(6)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(6, 6);	 
					cur.PotIconID.put(6, 82);	
					cur.PotTime.put(6, 900);	
					cur.PotValue.put(6, 10);	
			cur.Exp_Tag_10 = 1;	
		}
		if(ItemID == 273001206){ // 15%
			 byte[] bufftimez = BitTools.intToByteArray(900); // 1 hrs
			 buff[16] = (byte)0x06;	   
			 buff[20] = (byte)0x52;	 
			 //buff[22] = (byte)0x28;  
			 //buff[23] = (byte)0x23; 
			 buff[24] = (byte)0xf;    
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(6)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(6, 6);	 
					cur.PotIconID.put(6, 82);	
					cur.PotTime.put(6, 900);	
					cur.PotValue.put(6, 15);
			cur.Exp_Tag_15 = 1;	
		}
		if(ItemID == 213062707){ // 20%
			 byte[] bufftimez = BitTools.intToByteArray(1800); // 2 hrs
			 buff[16] = (byte)0x06;	   
			 buff[20] = (byte)0x52;	
			 //buff[22] = (byte)0x28;  
			 //buff[23] = (byte)0x23; 
			 buff[24] = (byte)0x14;    
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(6)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(6, 6);	 
					cur.PotIconID.put(6, 82);	
					cur.PotTime.put(6, 1800);	
					cur.PotValue.put(6, 20);
			cur.Exp_Tag_20 = 1;	
		}
		if(ItemID == 283000129 ){ // 30% 
			 byte[] bufftimez = BitTools.intToByteArray(1800); // 2 hrs
				 buff[16] = (byte)0x06;	   
				 buff[20] = (byte)0x52;		 
				 //buff[22] = (byte)0x28;  
				 //buff[23] = (byte)0x23; 
				 buff[24] = (byte)0x1e;    
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(6)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(6, 6);	 
					cur.PotIconID.put(6, 82);	
					cur.PotTime.put(6, 1800);	
					cur.PotValue.put(6, 30);
			cur.Exp_Tag_30 = 1;	
		}
		if(ItemID == 213062772||ItemID == 283000130){ // 100%
			 byte[] bufftimez = BitTools.intToByteArray(900); // 1 hrs
			 buff[16] = (byte)0x06;	   
			 buff[20] = (byte)0x52;	 
			 //buff[22] = (byte)0x28;  
			 //buff[23] = (byte)0x23; 
			 buff[24] = (byte)0x64;    
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(6)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(6, 6);	 
					cur.PotIconID.put(6, 82);	
					cur.PotTime.put(6, 900);	
					cur.PotValue.put(6, 100);
			cur.Exp_Tag_100 = 1;	
		}
		
		 if(ItemID == 283000106){ // FD 50% bonus dmg
			 byte[] bufftimez = BitTools.intToByteArray(9000); // 10 hrs
			 buff[16] = (byte)0x08;	 
			 buff[20] = (byte)0x56;	 
			 buff[24] = (byte)0x32;  
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(8)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(8, 8);	 
					cur.PotIconID.put(8, 86);	
					cur.PotTime.put(8, 9000);	
					cur.PotValue.put(8, 50);
			cur.FD = 1;	
		 } 
		 
		 if(ItemID == 283000094){ // SAP M
			 byte[] bufftimez = BitTools.intToByteArray(4500); // 5 hrs
			 buff[16] = (byte)0x07;	 
			 buff[20] = (byte)0x5e;	 
			 buff[24] = (byte)0x3c;  
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(7)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(7, 7);	 
					cur.PotIconID.put(7, 94);	
					cur.PotTime.put(7, 4500);	
					cur.PotValue.put(7, 60);
			cur.SAP = 1;	
		 } 
		 
		 if(ItemID == 283000095){ // SAP L
			 byte[] bufftimez = BitTools.intToByteArray(9000); // 10 hrs
			 buff[16] = (byte)0x07;	 
			 buff[20] = (byte)0x5e;	 
			 buff[24] = (byte)0x3c;  
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(7)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(7, 7);	 
					cur.PotIconID.put(7, 94);	
					cur.PotTime.put(7, 9000);	
					cur.PotValue.put(7, 60);
			cur.SAP = 1;	
		 } 
		 
		 if(ItemID == 283000089){ // New Status Sundan L
			 byte[] bufftimez = BitTools.intToByteArray(9000); // 10 hrs
			 buff[16] = (byte)0x09;	 
			 buff[20] = (byte)0x5d;	 
			 buff[24] = (byte)0x0a;  
				 for(int i=0;i<2;i++) {
				 buff[i+22] = bufftimez[i];  
				 }
					if(cur.PotSLOT.containsKey(9)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(9, 9);	 
					cur.PotIconID.put(9, 93);	
					cur.PotTime.put(9, 9000);	
					cur.PotValue.put(9, 11);
			cur.NEW_STATUS_SUNDAN = 1;	
		 } 
		cur.statlist();
	ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), buff); 
	return true;
   }
}