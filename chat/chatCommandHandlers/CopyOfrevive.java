package chat.chatCommandHandlers;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;


import World.WMap;
import Player.Character;
import Player.Charstuff;
import Player.PlayerConnection;
import Player.buffdata;
import Player.skilleffects;
import Connections.Connection;
import ServerCore.ServerFacade;
import Tools.BitTools;
import chat.ChatCommandExecutor;

public class CopyOfrevive implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		 Character cur = ((PlayerConnection)source).getActiveCharacter();
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 int setit = Integer.parseInt(parameters[0]);
		 int seqway = 22200071;
		 byte[] skid = BitTools.intToByteArray(seqway);
		 byte[] target = BitTools.intToByteArray(cur.getCharID());
		 byte[] buff = new byte[96];
		 buff[0] = (byte)0x2c; 
		 buff[4] = (byte)0x05;
		 buff[6] = (byte)0x1f;
		 buff[8] = (byte)0x01;
		 buff[9] = (byte)0x99;
		 buff[10] = (byte)0x0f;
		 buff[11] = (byte)0xbf;
		
		for(int i=0;i<4;i++) {
			buff[12+i] = target[i]; // target id 4 bytes
			buff[56+i] = chid[i]; 	
			buff[64+i] = skid[i]; 	
			buff[76+i] = target[i]; // target id 4 bytes
		}	
		 	
		 int buffid = buffdata.getbuffid(seqway); 		//buff id -> 07 = hp | 15 = offence 
		 byte[] buffidz = BitTools.intToByteArray(buffid); 
		 
		 int bufftime = 9999; 	// time  Btw: 1800 = 2 hours.
		 byte[] bufftimez = BitTools.intToByteArray(bufftime);
		 
		 int buffvalue = buffdata.getbuffvalue(seqway); // value
		 byte[] buffvaluez = BitTools.intToByteArray(buffvalue);
		 
		 int buffslot = buffdata.getbuffslot(seqway); 	// buffslot
		 byte[] buffslotz = BitTools.intToByteArray(buffslot);
		 
		 Character Tplayer;
		 Tplayer = WMap.getInstance().getCharacter(BitTools.byteArrayToInt(target)); 
		 if(Tplayer == null){
		 Tplayer = cur;
		 if(seqway == 310201){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 310202){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 310203){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 310204){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 310151){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 310152){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 310153){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 310154){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 32100101){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 if(seqway == 31100131){Tplayer.Check_ForHiding(buffslot, buffid, bufftime, buffvalue);}	
		 }else{
		 Tplayer.setDots(buffslot, buffid, bufftime, buffvalue);
		 }
			if(skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 1;//player	
				int DotsIconID = Integer.valueOf(Passive[0]); 
				int DotsValue = Integer.valueOf(Passive[1]); 
				int DotsTime = Integer.valueOf(Passive[2]); 
				int DotsSLOT = Integer.valueOf(Passive[3]); 
				int DotsIconID2 = Integer.valueOf(Passive[4]); 
				int DotsValue2 = Integer.valueOf(Passive[5]); 
				int DotsTime2 = Integer.valueOf(Passive[6]); 
				int DotsSLOT2 = Integer.valueOf(Passive[7]); 
				int DotsIconID3 = Integer.valueOf(Passive[8]); 
				int DotsValue3 = Integer.valueOf(Passive[9]); 
				int DotsTime3 = Integer.valueOf(Passive[10]); 
				int DotsSLOT3 = Integer.valueOf(Passive[11]);
				//int Rate = Integer.valueOf(Passive[12]); 
				//int Limit = Integer.valueOf(Passive[13]); 
				
				//boolean SlicenDice = Charstuff.getInstance().Dice(Rate, Limit);
				
				Charstuff.getInstance().AddDot(cur.getCharID(), DotsIconID, DotsValue, 9999, DotsSLOT, DETERMINER, cur);
				if(DotsIconID2 != 0){Charstuff.getInstance().AddDot(cur.getCharID(), DotsIconID2, DotsValue2, 9999, DotsSLOT2, DETERMINER, cur);}
				if(DotsIconID3 != 0){Charstuff.getInstance().AddDot(cur.getCharID(), DotsIconID3, DotsValue3, 9999, DotsSLOT3, DETERMINER, cur);}
			}
		
			 for(int i=0;i<2;i++) {
				 buff[i+16] = buffslotz[i];	 // buffslot
				 buff[i+20] = buffidz[i];	 // buff id
				 buff[i+22] = bufftimez[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
				 buff[i+24] = buffvaluez[i]; // Value XXXXX
			 }
			 buff[26] = (byte)0x01; 
			 buff[28] = (byte)0xdd; 
			 buff[32] = (byte)0xdd; 
			 buff[36] = (byte)0xcc; 
			 buff[38] = (byte)0xcc; 
			 buff[40] = (byte)0x98; 
			 buff[42] = (byte)0x98;
			 
			 buff[44] = (byte)0x34; // 2nd packet contains buff animation
			 buff[48] = (byte)0x05; 
			 buff[50] = (byte)0x34; 
			 buff[52] = (byte)0x01; 
			
		   	 buff[60] = (byte)0x01; 

			 buff[69] = (byte)0x07; 
			
			 buff[71] = (byte)0x01; 
			 buff[72] = (byte)0x01; 

			 buff[80] = (byte)0x03;
			
			/* buff[84] = (byte)0xb2; // hp ?
			 buff[85] = (byte)0xb2; 
			
			 buff[92] = (byte)0x45; // mana?
			 buff[93] = (byte)0x01; */
			 
			 byte[] newhpz = BitTools.intToByteArray(Tplayer.getHp()); 
			 byte[] newmanaz = BitTools.intToByteArray(Tplayer.getMana()); 
			 
			 for(int i=0;i<2;i++) {
				 	buff[i+84] = newhpz[i];		
					buff[i+92] = newmanaz[i];		

			 }
			 
		 Tplayer.statlist(); // refresh dat statlist of target when i buffed the shit out of him!
		 cur.sendToMap(buff);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), buff);
	}

}
