package GameServer.GamePackets.UseItemList;

import Connections.Connection;
import GameServer.GamePackets.UseItemCommandExecutor;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;


public class POTStwo implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
			Character cur = ((PlayerConnection)source).getActiveCharacter();
			byte[] chid = BitTools.intToByteArray(cur.getCharID());
			byte[] buff = new byte[88];
			 buff[0] = (byte)0x2c; 
			 buff[4] = (byte)0x05;
			 buff[6] = (byte)0x1f;
			 buff[8] = (byte)0x01;
			 for(int i=0;i<4;i++){
			 buff[12+i] = chid[i]; 
			 buff[56+i] = chid[i]; 				
			 }	
			 buff[26] = (byte)0x01; 
			 buff[28] = (byte)0x89; 
			 buff[32] = (byte)0x89; 
			 buff[36] = (byte)0x7e; 
			 buff[38] = (byte)0x7e; 
			 buff[40] = (byte)0x60; 
			 buff[42] = (byte)0x60; 
			 
			 buff[44] = (byte)0x2c; 
			 buff[48] = (byte)0x05;
		 	 buff[50] = (byte)0x1f;
			 buff[52] = (byte)0x01;
			 buff[70] = (byte)0x01; 
			 
			 buff[72] = (byte)0x89; 
			 buff[76] = (byte)0x89; 
			 buff[80] = (byte)0x7e; 
			 buff[82] = (byte)0x7e; 
			 buff[84] = (byte)0x60; 
			 buff[86] = (byte)0x60; 
			 
				if(ItemID == 283000242){ // BIG BJT
					 byte[] bufftimez = BitTools.intToByteArray(4500); // 5 hrs
						 buff[16] = (byte)0x00;	 // buffslot
						 buff[20] = (byte)0x5c;	 // buff id
						 //buff[22] = (byte)0x28;  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
						 //buff[23] = (byte)0x23; 
						 buff[24] = (byte)0x0c; // Value XXXXX
						 
						 buff[60] = (byte)0x01;	 
						 buff[64] = (byte)0x5b;	 
						 //buff[66] = (byte)0x28;  
						 //buff[67] = (byte)0x23; 
						 buff[68] = (byte)0x3c; 
						 
						 for(int i=0;i<2;i++) {
						 buff[i+22] = bufftimez[i]; 
						 buff[i+66] = bufftimez[i];  
						 }
							if(cur.PotSLOT.containsKey(1)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
							cur.PotSLOT.put(0, 0);	 
							cur.PotIconID.put(0, 92);	
							cur.PotTime.put(0, 4500);	
							cur.PotValue.put(0, 12);
							
							cur.PotSLOT.put(1, 1);	 
							cur.PotIconID.put(1, 91);	
							cur.PotTime.put(1, 4500);	
							cur.PotValue.put(1, 60);
					cur.GREATER_JACKPOT_TAG   = 1;
					cur.GREATER_DOUBLE_ITEM_DROP_TAG   = 1;	
				}
			
			if(ItemID == 283000209 || ItemID == 213062764){ // GJT
				 byte[] bufftimez = BitTools.intToByteArray(4500); // 5 hrs
					 buff[16] = (byte)0x00;	 // buffslot
					 buff[20] = (byte)0x5c;	 // buff id
					 //buff[22] = (byte)0x28;  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
					 //buff[23] = (byte)0x23; 
					 buff[24] = (byte)0x08; // Value XXXXX
					 
					 buff[60] = (byte)0x01;	 
					 buff[64] = (byte)0x5b;	 
					 //buff[66] = (byte)0x28;  
					 //buff[67] = (byte)0x23; 
					 buff[68] = (byte)0x28; 
					 
					 for(int i=0;i<2;i++) {
					 buff[i+22] = bufftimez[i]; 
					 buff[i+66] = bufftimez[i];  
					 }
						if(cur.PotSLOT.containsKey(1)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
						cur.PotSLOT.put(0, 0);	 
						cur.PotIconID.put(0, 92);	
						cur.PotTime.put(0, 4500);	
						cur.PotValue.put(0, 8);
						
						cur.PotSLOT.put(1, 1);	 
						cur.PotIconID.put(1, 91);	
						cur.PotTime.put(1, 4500);	
						cur.PotValue.put(1, 40);
				cur.JACKPOT_TAG   = 1;
				cur.DOUBLE_ITEM_DROP_TAG   = 1;	
			}
			if(ItemID == 283000207 || ItemID == 213062767){ // fdd fasr
					 buff[16] = (byte)0x02;	 
					 buff[20] = (byte)0x57;	 
					 buff[22] = (byte)0x28;  
					 buff[23] = (byte)0x23; 
					 buff[24] = (byte)0x19; 
					 
					 buff[60] = (byte)0x03;	 
					 buff[64] = (byte)0x54;	 
					 buff[66] = (byte)0x28;  
					 buff[67] = (byte)0x23; 
					 buff[68] = (byte)0x7d; 
						if(cur.PotSLOT.containsKey(2)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
						if(cur.PotSLOT.containsKey(3)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
						cur.PotSLOT.put(2, 2);	 
						cur.PotIconID.put(2, 84);	
						cur.PotTime.put(2, 9000);	
						cur.PotValue.put(2, 25);
					 
						cur.PotSLOT.put(3, 3);	 
						cur.PotIconID.put(3, 87);	
						cur.PotTime.put(3, 9000);	
						cur.PotValue.put(3, 125);
					 cur.FDD = 1;	
					 cur.CASR = 1;	
				
			}
			if(ItemID == 283000208 || ItemID == 213062768){ // fad fasr
				 buff[16] = (byte)0x04;	 
				 buff[20] = (byte)0x53;	 
				 buff[22] = (byte)0x28;  
				 buff[23] = (byte)0x23; 
				 buff[24] = (byte)0x7d; 
				 
				 buff[60] = (byte)0x05;	 
				 buff[64] = (byte)0x55;	 
				 buff[66] = (byte)0x28;  
				 buff[67] = (byte)0x23; 
				 buff[68] = (byte)0x19; 
					if(cur.PotSLOT.containsKey(4)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					if(cur.PotSLOT.containsKey(5)){cur.respondguildTIMED("This slot is already in use by another pot.", cur.GetChannel());return false;}	
					cur.PotSLOT.put(4, 4);	 
					cur.PotIconID.put(4, 83);	
					cur.PotTime.put(4, 9000);	
					cur.PotValue.put(4, 125);
				 
					cur.PotSLOT.put(5, 5);	 
					cur.PotIconID.put(5, 85);	
					cur.PotTime.put(5, 9000);	
					cur.PotValue.put(5, 25);
				 cur.FAD = 1;	
				 cur.FASR = 1;
			}
			cur.statlist();
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), buff); 
		return true;
   }
}