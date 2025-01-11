package Mob;

import item.Item;

import java.nio.ByteBuffer;
import java.util.Random;

import Player.Character;
import Player.Charstuff;
import Tools.BitTools;
import World.WMap;
import World.Waypoint;

/*
 * MobPackets.class
 * Generates the necessary packets for the mobs
 */

public class MobPackets {
	private static int zerocheck = 1;
	private static int greencrit = 1;
	private static int fury = 1; // normal
	private static int Crit = 1; // put it to 1
	private static double Bytecritchance = 1;
	private static double TargetPlayerdefence = 0; // Target player DEF
	private static double PlayerAttack = 0; // player DEF
	private static double Mobexp = 0; // player DEF
	
	
	
	
	public static byte[]getMovePacket(int uid,int MOB_SPEED, float x, float y){
		byte[] moveBucket = new byte[48];
		byte[] uniqueID = BitTools.intToByteArray(uid);
		byte[] moveX = BitTools.floatToByteArray(x);
		byte[] moveY = BitTools.floatToByteArray(y);
		
		moveBucket[0] = (byte)moveBucket.length;
		moveBucket[4] = (byte)0x05;
		moveBucket[6] = (byte)0x0D;
		moveBucket[8] =  (byte)0x02;
		moveBucket[9] =  (byte)0x10;
		moveBucket[10] = (byte)0xa0; 
		moveBucket[11] = (byte)0x36;

		
		for(int i=0;i<4;i++) {
			moveBucket[i+12] = uniqueID[i];
			moveBucket[i+20] = moveX[i];
			moveBucket[i+24] = moveY[i];
			moveBucket[i+28] = moveX[i];
			moveBucket[i+32] = moveY[i];
		}
		if(MOB_SPEED == 1){
			moveBucket[36] = (byte)1; // 1 renne
			}else{
			moveBucket[36] = (byte)0;} // 0 lopen
		
		return moveBucket;
	}
	
    public static byte[] getDeathPacket(int uid) {
        byte[] deathPacket = new byte[20];
        byte[] unique = BitTools.intToByteArray(uid);
        byte[] bython = new byte[] {(byte)0x02, (byte)0x61, (byte)0x21, (byte)0x35};

        deathPacket[0] = (byte)0x14;
        deathPacket[4] = (byte)0x05;
        deathPacket[6] = (byte)0x0a;
       
       
        for(int i=0;i<4;i++) {
                deathPacket[i+12] = unique[i];
                deathPacket[i+8] = bython[i];
        }
        
        deathPacket[16] = (byte)0x00;
       
        return deathPacket;
}
    
    public static byte[] getDeathPacketSTAR(int uid) {
        byte[] deathPacket = new byte[20];
        byte[] unique = BitTools.intToByteArray(uid);
        byte[] bython = new byte[] {(byte)0x02, (byte)0x61, (byte)0x21, (byte)0x35};

        deathPacket[0] = (byte)0x14;
        deathPacket[4] = (byte)0x05;
        deathPacket[6] = (byte)0x0a;
       
       
        for(int i=0;i<4;i++) {
                deathPacket[i+12] = unique[i];
                deathPacket[i+8] = bython[i];
        }
        
        deathPacket[16] = (byte)0x01;
       
        return deathPacket;
}
    
    public static byte[] famepacket(int uid, Character Tplayer, int fame) {
        byte[] famepacket = new byte[28];
    	byte[] Uid = BitTools.intToByteArray(uid);
		byte[] Tp = BitTools.intToByteArray(Tplayer.getCharID());
		byte[] Fame = BitTools.intToByteArray(fame);
        famepacket[0] = (byte)0x1c;
        famepacket[4] = (byte)0x05;
        famepacket[6] = (byte)0x09;
        famepacket[8] = (byte)0x01;
		 for(int i=0;i<4;i++) {
			 famepacket[i+12] = Tp[i];	
			 famepacket[i+20] = Uid[i]; 
			 famepacket[i+24] = Fame[i];
		 }
	       famepacket[16] = (byte)0x02;
	       famepacket[17] = (byte)0x29;
	       famepacket[18] = (byte)0x37;
	       famepacket[19] = (byte)0x08;
       
        return famepacket;
}
	
	public static byte[] attackplayer(Mob mob, int uid, int uidAttack, int Targetplayer, int skill){
		 byte[] pckt = new byte[52];
		
		byte[] uniqueID = BitTools.intToByteArray(uid);
		byte[] Tp = BitTools.intToByteArray(Targetplayer);
		byte[] skid = BitTools.intToByteArray(skill);
		byte[] target1 = new byte[4];

		 pckt[0] = (byte)0x34;
		 pckt[4] = (byte)0x05;
		 pckt[6] = (byte)0x34;
		 pckt[8] = (byte)0x02; // 0x02 working from mob to players
		 
		 for(int i=0;i<4;i++) {
				pckt[i+12] = uniqueID[i];						// char ID
				pckt[i+32] = Tp[i];  		// target id SERVER
				target1[i] = Tp[i]; 		// target id CLIENT
				pckt[i+20] = skid[i]; 						// skill ID
		 }
		 
		 
		 pckt[16] = (byte)0x01;
		 pckt[25] = (byte)0x07;
		 pckt[27] = (byte)0x01;	// how many targets
		 pckt[28] = (byte)0x01; // 0x02 = mob | 0x01 = player
		 
		 // Player 1
		 Character Tplayer = WMap.getInstance().getCharacter(BitTools.byteArrayToInt(target1));  
		 if (Tplayer == null){return null;}
		 //===  RANDOMIZER  ===\\
		 long CritQ = mob.LevelDifference(Tplayer.getLevel(), 0, 2);
		 if (CritQ == 1){Bytecritchance = 2.00; Crit = 2;}


		 
		 if(Tplayer.FDD == 1){double DEF = Tplayer.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = Tplayer.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg = uidAttack - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 
		 AFdmg = Tplayer.Calcdmgtroughdefbuffpercent(AFdmg);
		 TargetPlayerdefence = 0;
		 double Fdmg = AFdmg * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int FinalDamage = (int)Fdmg * zerocheck;
		 
		 //System.out.println("Attack Final DMG = "+ FinalDamage);
		 if(Tplayer.FPCritAbsorb != 0 && Crit != 1 && Crit != 0){double meow = Tplayer.FPCritAbsorb * 2; double substractt = FinalDamage * meow;FinalDamage = FinalDamage - (int)substractt;}
		 //System.out.println("Attack Final DMG on crit = "+ FinalDamage);
		 
		 //System.out.println("Crit = "+ Crit);
		 //System.out.println("FinalDamage = "+ FinalDamage);
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt[36] = (byte)0x05;} // * 2 green crit
		
		 
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 	int newhp; int newmana;
			if(Tplayer.MANA_SHIELD_PROTECTION == 1){newmana = Tplayer.getMana() - FinalDamage;}
			else{newmana = Tplayer.getHp() - FinalDamage;}
			
			if(Tplayer.MANA_SHIELD_PROTECTION == 1 && newmana <= 0){newhp = Tplayer.getHp() + newmana;}
			else if(Tplayer.MANA_SHIELD_PROTECTION == 1){
			newhp = Tplayer.getHp();
			}else{newhp = newmana;}// FAAILL??
		 
		 byte[] finaldmg = BitTools.intToByteArray(FinalDamage); 
		 for(int i=0;i<4;i++) {
				pckt[i+44] = finaldmg[i];						
		 }
		 byte[] newmanaz; 
		 byte[] newhpz; 
		 if(Tplayer.MANA_SHIELD_PROTECTION == 1 && newmana <= 0){
			 newhpz = BitTools.intToByteArray(newhp);
			 newmanaz = BitTools.intToByteArray(newmana); 
			 Tplayer.setMana(newmana);
			 if(newhp <= 0){mob.resetDamage();}
			 Tplayer.setHp(newhp);
		 }else if(Tplayer.MANA_SHIELD_PROTECTION == 1){
			 newhpz = BitTools.intToByteArray(Tplayer.getHp());
			 newmanaz = BitTools.intToByteArray(newmana); 
			 Tplayer.setMana(newmana);
		 }else{
			 newhpz = BitTools.intToByteArray(newhp);  
			 newmanaz = BitTools.intToByteArray(Tplayer.getMana());
			 if(newhp <= 0){mob.resetDamage();}
			 Tplayer.setHp(newhp);
		 }
		 for(int i=0;i<2;i++) {
				pckt[i+40] = newhpz[i];		
				pckt[i+48] = newmanaz[i];
		 }
		 
		return pckt;
	}

	public static byte[]  getInitialPacket(int mobID, int uid, Waypoint wp, int curhp) {
        byte[] mobBucket = new byte[608];
        byte[] size = BitTools.shortToByteArray((short)mobBucket.length);
       
        byte[] mobid = BitTools.shortToByteArray((short)mobID);
        byte[] mobUid = BitTools.intToByteArray(uid);
        byte[] xCoords = BitTools.floatToByteArray(wp.getX());
        byte[] yCoords = BitTools.floatToByteArray(wp.getY());
        byte[] hp = BitTools.intToByteArray(curhp);
        
        for(int i=0;i<2;i++) {
                mobBucket[i] = size[i];
                mobBucket[i+64] = mobid[i];
        }
        for(int i=0;i<4;i++) {
                mobBucket[i+12] = mobUid[i];
                mobBucket[i+84] = xCoords[i];
                mobBucket[i+88] = yCoords[i];
                mobBucket[i+72] = hp[i];
        }
       
        mobBucket[4] = (byte)0x05;
        mobBucket[6] = (byte)0x03;
        mobBucket[8] = (byte)0x02;
        
       
        return mobBucket;
}
	
	// FOR MOBS
	public static byte[] mobitemSpawnPacket(int charID, int itemID, int stack, float x, float y) {
		
		 if(stack <= 0){stack = 1;}
		 if(stack >= 88000000){stack = 2;}
			Item.inc++;
		
		byte[] item = new byte[56];
		byte[] spawnX = BitTools.floatToByteArray(x);
		byte[] spawnY = BitTools.floatToByteArray(y);
		byte[] itid = BitTools.intToByteArray(itemID);
		byte[] chid = BitTools.intToByteArray(Item.inc);
		byte[] INVstack = BitTools.intToByteArray(stack);
		
		item[0] = (byte)item.length;
		item[4] = (byte)0x05;
		item[6] = (byte)0x0e;
		
		for(int i=0;i<4;i++) {
			item[36+i] = spawnX[i];
			item[40+i] = spawnY[i];
			item[20+i] = itid[i];
			item[32+i] = chid[i]; 
			item[28+i] = INVstack[i]; // drop stack
		}
		
		//System.out.println("Mob:"+uid+" - "+Integer.valueOf(itemID)+" - "+Integer.valueOf(stack));
		Item.iteMapTimedrop.put(Integer.valueOf(Item.inc), Long.valueOf(System.currentTimeMillis()));
		Item.iteMapcharid.put(Integer.valueOf(Item.inc), Integer.valueOf(charID));
		Item.iteMapSTACK.put(Integer.valueOf(Item.inc), Integer.valueOf(stack));
		Item.iteMap.put(Integer.valueOf(Item.inc), Integer.valueOf(itemID));
		return item;
	}

}
