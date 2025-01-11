package npc;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import ServerCore.ServerFacade;
import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;
import World.Waypoint;




public class NpcPackets {
	private static int uniq = 49;
	public NpcPackets(){
	}
	
	public static byte[] npcspawn(Character cur, int npcID, int uid, int module, Waypoint wp, String name) {
				// dafuq is this shit
				// start coords					   // end coords
			  /*float x = cur.getlastknownX();     float dx = wp.getX();
				float y = cur.getlastknownY();     float dy = wp.getY();
				
				float deltax = WMap.distance(x, dx); // distance between here  X and there X
				float deltay = WMap.distance(y, dy); // distance between here  Y and there Y
				
				float cosa  = deltax / 2; // /2 is 50%
				float sina  = deltay / 2; // /2 is 50%
				
				float mpx = 1;
				float mpy = 1;
				float New_x = 0;
				float New_y = 0;
				if (dx < x) mpx = -1;
				if (dy < y) mpy = -1;
				if (cosa == 1) cosa = 0;
				if (sina == 1) sina = 0;
				
				New_x += mpx * cosa ;
				New_y += mpy * sina ;
				
				float dNew_x = x + New_x;
				float dNew_y = y + New_y;;
				
				WONT WORK WITHOUT AREA ID's
			 */
				
		byte[] npc = new byte[610]; // janux got 611
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		
        byte[] Xcur = BitTools.floatToByteArray(cur.getlastknownX());
        byte[] Ycur = BitTools.floatToByteArray(cur.getlastknownY());
    	byte[] npcid = BitTools.intToByteArray(npcID);
        byte[] Xcords = BitTools.floatToByteArray(wp.getX());
        byte[] Ycords = BitTools.floatToByteArray(wp.getY());
		byte[] npcname = BitTools.stringToByteArray(name);
		byte[] uid1 = BitTools.intToByteArray(uid);
		byte[] AreaID = BitTools.intToByteArray(0); // as long its the same gg
		//System.out.println("Spawning npc"+" npcID:"+npcID+" uid:"+uid+" npcNAME:"+name+" " + wp.getX() + "-" +wp.getY());

		npc[0]  = (byte)0x62; 
		npc[1]  = (byte)0x02;
		
		npc[4]  = (byte)0x04;
		npc[6]  = (byte)0x04;
		npc[8]  = (byte)0x01;
		
		npc[25] = (byte)0x03;
		npc[68] = (byte)module; // determines also proffessiosn 21 = graveyard / 20  = fishing / 19 = herbing / 18 = mining
		
		for(int i=0;i<npcname.length;i++) {
			npc[i+34] = npcname[i];
		}
		for(int i=0;i<2;i++) {
			npc[i+82] = npcid[i];
		}
		for(int i=0;i<4;i++) {
			npc[i+9] = chid[i];
			npc[i+13] = AreaID[i];
			npc[i+26] = uid1[i];
			npc[i+102] = Xcords[i];   
			npc[i+106] = Ycords[i];	
			
			// area x ,y ? if this is the same then it somehow lets the npc's flash
			npc[i+17] = Xcur[i];
		    npc[i+21] = Ycur[i];
		}

        return npc;
}
	
	public static byte[] packeteer(float x, float y, int nid, String n, int plid) {
	    uniq++;
        byte[] buff = new byte[611];
        byte[] name = n.getBytes();
        byte[] Xcords = BitTools.floatToByteArray(x);
        byte[] Ycords = BitTools.floatToByteArray(y);
        byte[] id = BitTools.intToByteArray(nid);
        byte[] chid = BitTools.intToByteArray(plid);
       
        byte[] uid = BitTools.intToByteArray(uniq);

                for(int i=0;i<name.length;i++) {
                        buff[i+34] = name[i];
                }
               
                buff[0]  = (byte)0x63;
                buff[1]  = (byte)0x02;
                buff[4]  = (byte)0x04;
                buff[6]  = (byte)0x04;
               
                buff[8] = (byte)0x01;
               
                buff[68] = (byte)0x05;
               
               
                buff[25] = (byte)0x03;
            	for(int i=0;i<2;i++) {
            		buff[i+82] = id[i];
        		}
                   
                for(int i=0;i<4;i++) {
                        buff[i+9] = chid[i];
                        buff[i+102] = Xcords[i];  
                        buff[i+106] = Ycords[i];
                        buff[i+17] = Xcords[i];
                        buff[i+21] = Ycords[i];
                        buff[i+26] = uid[i];
                }
               
           return buff;
}
}
		
		

