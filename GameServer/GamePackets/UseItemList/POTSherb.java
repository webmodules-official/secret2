package GameServer.GamePackets.UseItemList;

import Connections.Connection;
import GameServer.GamePackets.UseItemCommandExecutor;
import Mob.Mob;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;


public class POTSherb implements UseItemCommandExecutor {
	

	public boolean execute(int ItemID, int DETERMINER, Connection source) {
			Character cur = ((PlayerConnection)source).getActiveCharacter();
			
			
			int bufficonid = 0; int buffvalue = 0; int bufftime = 0; int buffslot = 0;
			
			//defence 
			if(ItemID == 215003131){
				bufficonid = 16;
				buffvalue = 20;
				bufftime = 15;
				buffslot = 3;	
			}else
			if(ItemID == 215003132){
				bufficonid = 16;
				buffvalue = 40;
				bufftime = 15;
				buffslot = 3;	
			}else
			if(ItemID == 215003131){
				bufficonid = 16;
				buffvalue = 120;
				bufftime = 30;
				buffslot = 3;		
			}else
			if(ItemID == 215003134){
				bufficonid = 16;
				buffvalue = 300;
				bufftime = 45;
				buffslot = 3;			
			}else
			if(ItemID == 215003135){
				bufficonid = 16;
				buffvalue = 1000;
				bufftime = 60;
				buffslot = 3;					
			}else
				
			//offence
			if(ItemID == 215003136){
				bufficonid = 15;
				buffvalue = 20;
				bufftime = 15;
				buffslot = 2;					
			}else
			if(ItemID == 215003137){
				bufficonid = 15;
				buffvalue = 40;
				bufftime = 15;
				buffslot = 2;						
			}else
			if(ItemID == 215003138){
				bufficonid = 15;
				buffvalue = 120;
				bufftime = 15;
				buffslot = 2;								
			}else
			if(ItemID == 215003139){
				bufficonid = 15;
				buffvalue = 300;
				bufftime = 30;
				buffslot = 2;								
			}else
			if(ItemID == 215003140){
				bufficonid = 15;
				buffvalue = 1000;
				bufftime = 45;
				buffslot = 2;									
			}else
				
			//HP
			if(ItemID == 215003141){
				bufficonid = 7;
				buffvalue = 200;
				bufftime = 75;
				buffslot = 1;										
			}else
			if(ItemID == 215003142){
				bufficonid = 7;
				buffvalue = 400;
				bufftime = 75;
				buffslot = 1;											
			}else
			if(ItemID == 215003143){
				bufficonid = 7;
				buffvalue = 700;
				bufftime = 150;
				buffslot = 1;												
			}else
			if(ItemID == 215003144){
				bufficonid = 7;
				buffvalue = 1200;
				bufftime = 150;
				buffslot = 1;													
			}else
			if(ItemID == 215003145){
				bufficonid = 7;
				buffvalue = 2000;
				bufftime = 150;
				buffslot = 1;														
			}else{return false;}
																	
				
			this.AddDot(cur.charID, bufficonid, buffvalue, bufftime, buffslot);
			cur.statlist(); 
			return true;
   }
	
	public void AddDot(int Target, int DotsIconID, int DotsValue, int DotsTime, int DotsSLOT){//DETERMINE: 1 = player | 2 = mob
		Character Tplayer = WMap.getInstance().getCharacter(Target); 	
		if(Tplayer != null){
		if(DotsIconID == 43 && !Tplayer.DotsIconID.containsValue(43)
		||DotsIconID == 46 && !Tplayer.DotsIconID.containsValue(46)
		||DotsIconID == 47 && !Tplayer.DotsIconID.containsValue(47)
		||DotsIconID == 49 && !Tplayer.DotsIconID.containsValue(49)
		||DotsIconID == 58 && !Tplayer.DotsIconID.containsValue(58)
		||DotsIconID == 6||DotsIconID == 7||DotsIconID == 12||DotsIconID == 13||DotsIconID == 15||DotsIconID == 16||DotsIconID == 21||DotsIconID == 22
		||DotsIconID == 42||DotsIconID == 44||DotsIconID == 45||DotsIconID == 48||DotsIconID == 52||DotsIconID == 56||DotsIconID == 64
		){
		byte[] chid = BitTools.intToByteArray(Tplayer.getCharID());
		byte[] buff = new byte[44];
		 buff[0] = (byte)0x2c; 
		 buff[4] = (byte)0x05;
		 buff[6] = (byte)0x1f;
		 buff[8] = (byte)0x01;// 1 = player | 2 = mob
		 for(int i=0;i<4;i++){
		 buff[12+i] = chid[i]; 				
		 }	
		 buff[26] = (byte)0x01; 
		 buff[28] = (byte)0x89; 
		 buff[32] = (byte)0x89; 
		 buff[36] = (byte)0x7e; 
		 buff[38] = (byte)0x7e; 
		 buff[40] = (byte)0x60; 
		 buff[42] = (byte)0x60;
		 
			if(DotsIconID == 43){DotsTime = DotsTime / 2;}
			if(DotsIconID == 46){DotsTime = DotsTime / 2;}
			if(DotsIconID == 49){DotsTime = DotsTime / 2;}
		 
		 Tplayer.setDots(DotsSLOT, DotsIconID, DotsTime, DotsValue);
		  
		 byte[] buffidz1 = BitTools.intToByteArray(DotsIconID); 
		 byte[] bufftimez1 = BitTools.intToByteArray(DotsTime);
		 byte[] buffvaluez1 = BitTools.intToByteArray(DotsValue);
		 byte[] buffslotz1 = BitTools.intToByteArray(DotsSLOT);
		 
			 for(int i=0;i<2;i++) {
				 buff[i+16] = buffslotz1[i];	 // buffslot
				 buff[i+20] = buffidz1[i];	 // buff id
				 buff[i+22] = bufftimez1[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
				 buff[i+24] = buffvaluez1[i]; // Value XXXXX
			 }	
			 
			if(DotsIconID == 16||DotsIconID == 15){Tplayer.statlist();} 
			ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), buff); 
			 if(DotsIconID == 43||DotsIconID == 46||DotsIconID == 49||DotsIconID == 44||DotsIconID == 45||DotsIconID == 47||DotsIconID == 48){Tplayer.sendToMap(buff);}
			}
	    }
	}
}