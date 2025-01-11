package Player;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Connections.Connection;
import Mob.Mob;
import Mob.MobPackets;
import ServerCore.ServerFacade;
import Tools.BitTools;
import World.WMap;
import World.Waypoint;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class Summon implements Cloneable{
private Character cur;
public long CdInterval;
private WMap wmap = WMap.getInstance();
private Waypoint location;
private int CD = 30000;
private  double Bytecritchance = 1;
private  double TargetPlayerdefence = 0; // Target player DEF
private  double PlayerAttack = 0; // player DEF
private  int critdmg = 0, HidingCritChance = 0, HidingDamage = 0;
private  int zerocheck = 1;
private  int greencrit = 1;
private  int fury = 1; // normal
private  int Crit = 1; // put it to 1
private  int attacksuccesrate = 0;
private  double inc = 0;

	public Summon(Character cur) {
		//System.out.println("Summon");
		this.cur = cur;
		this.location = new Waypoint(cur.getLocation().getX(), cur.getLocation().getY());
		cur.Summonid = cur.charID + 1000000;
		wmap.Addsummon(cur.Summonid, this);
		this.CdInterval = System.currentTimeMillis();
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), this.extCharPacket());
		cur.sendToMap(this.extCharPacket());
		 if(cur.Running == 1){
			 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), this.Runnskill());
			 cur.sendToMap(this.Runnskill());
		 }
	}
	
	
	public boolean DistCheck(Mob TMob) {
		if (cur.getCharacterClass() == 1 && WMap.distance(this.location.getX(), this.location.getY(), TMob.getlastknownX(), TMob.getlastknownY()) <= 25 && TMob != null){
			//System.out.println("war: ");
			return true;
		}
		else
			if (cur.getCharacterClass() == 2 && WMap.distance(this.location.getX(), this.location.getY(), TMob.getlastknownX(), TMob.getlastknownY()) <= 35 && TMob != null){
				//System.out.println("assassin: ");
				return true;
			}
			else
				if (cur.getCharacterClass() == 3 && WMap.distance(this.location.getX(), this.location.getY(), TMob.getlastknownX(), TMob.getlastknownY()) <= 35 && TMob != null){
					//System.out.println("mage: ");
					return true;
				}
				else
					if (cur.getCharacterClass() == 4 && WMap.distance(this.location.getX(), this.location.getY(), TMob.getlastknownX(), TMob.getlastknownY()) <= 25 && TMob != null){
						//System.out.println("monk:");
						return true;
					}	
		//System.out.println("out of range: ");
		return false;
	}
	
	// attack target loc
	public void Attack(Mob TMob) {
		boolean DistCheck = DistCheck(TMob);
		//System.out.println("ME: x"+this.location.getX()+" - y"+this.location.getY()+" MOB: x"+TMob.getlastknownX()+" - y:"+TMob.getlastknownY());
		if(System.currentTimeMillis() - this.CdInterval < this.CD){
		if (DistCheck && TMob != null){

		 int seqway = 0;
		 Random r = new Random();
		 int R = 1+r.nextInt(5);
		 if(R == 1){seqway = cur.getlearnedskill(0);}
		 if(R == 2){seqway = cur.getlearnedskill(cur.getskillbar(2));}
		 if(R == 3){seqway = cur.getlearnedskill(cur.getskillbar(3));}
		 if(R == 4){seqway = cur.getlearnedskill(cur.getskillbar(4));}
		 if(R == 5){seqway = cur.getlearnedskill(cur.getskillbar(5));}
		 if(seqway == 0||seqway > 100000000){seqway = cur.getlearnedskill(0);}
		
		 

			// get skill Damage by skillid
			 int skilldmg = skilldata.getskilldmg(seqway); 
			 
			// get skill Crit by skillid
			 int skillcritchance = skilldata.getskillcritchance(seqway); 
			 
			// get PASSIVE skill crit
			 int passiveskillcritchance = cur.getTempPassives(6); 
			 
			// get crit bonus damage
			 critdmg = cur.getTempPassives(8);
			 
			// get skill attacksuccesrate by skillid ( Warrior buff )
			 attacksuccesrate = cur.gettempstore(21)/*WarrriorATTACKRATEbuff*/ + cur.getTempPassives(11);

			// get skill Mana consume by skillid
			 int skilldmanaconsume = skilldata.getskillmanaconsume(seqway); 
			 int finalmana = cur.mana - skilldmanaconsume;			 
			 cur.setMana(finalmana); // put new mana
			 
			 int Attack = cur.attack + skilldmg; 
			 if(cur.FAD == 1){double ATK = Attack * 1.25;  PlayerAttack = (int)ATK;}  
			 else { PlayerAttack = Attack; } //normal damage if i have no FAD
			 int Damage = (int)PlayerAttack; // TOTAL DAMAGE 
			 PlayerAttack = 0; // reset to 0;
			 
			 // Check if fury is ON or OFF
			 if (cur.furycheck == 1){ fury = 2;}// is * 2 
			 int checkfury = Damage * fury;//if fury is on then *20 else *1
			 fury = 1;
		 
		 // 1 target
		if(cur.getLevel() >= 10){cur.recfury(1);}
		this.AttackMOB1(seqway, checkfury, skillcritchance, passiveskillcritchance, TMob, cur);
		}else{	//System.out.println("OUT");
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), this.getMovePacket(cur.Summonid, 1, TMob.getlastknownX(), TMob.getlastknownY()));
		cur.sendToMap(this.getMovePacket(cur.Summonid, 1, TMob.getlastknownX(), TMob.getlastknownY()));
		}}else{
			// VANISH
			cur.sendToMap(cur.getVanishByID(cur.Summonid));
			cur.Summonid = 0;
		}
	}
	
	public boolean DistCheck(Character Tplayer) {
		if (cur.getCharacterClass() == 1 && WMap.distance(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) <= 25 && Tplayer != null){
			//System.out.println("war: ");
			return true;
		}
		else
			if (cur.getCharacterClass() == 2 && WMap.distance(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) <= 35 && Tplayer != null){
				//System.out.println("assassin: ");
				return true;
			}
			else
				if (cur.getCharacterClass() == 3 && WMap.distance(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) <= 35 && Tplayer != null){
					//System.out.println("mage: ");
					return true;
				}
				else
					if (cur.getCharacterClass() == 4 && WMap.distance(this.location.getX(), this.location.getY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) <= 25 && Tplayer != null){
						//System.out.println("monk:");
						return true;
					}	
		//System.out.println("out of range: ");
		return false;
	}
	
	// attack target loc
	public void Attack(Character Tplayer) {
		boolean DistCheck = DistCheck(Tplayer);
		//System.out.println("ME: x"+this.location.getX()+" - y"+this.location.getY()+" MOB: x"+TMob.getlastknownX()+" - y:"+TMob.getlastknownY());
		if(System.currentTimeMillis() - this.CdInterval < this.CD){
		if (DistCheck && Tplayer != null){

		 int seqway = 0;
		 Random r = new Random();
		 int R = 1+r.nextInt(5);
		 if(R == 1){seqway = cur.getlearnedskill(0);}
		 if(R == 2){seqway = cur.getlearnedskill(cur.getskillbar(2));}
		 if(R == 3){seqway = cur.getlearnedskill(cur.getskillbar(3));}
		 if(R == 4){seqway = cur.getlearnedskill(cur.getskillbar(4));}
		 if(R == 5){seqway = cur.getlearnedskill(cur.getskillbar(5));}
		 if(seqway == 0||seqway > 100000000){seqway = cur.getlearnedskill(0);}
		
		 

			// get skill Damage by skillid
			 int skilldmg = skilldata.getskilldmg(seqway); 
			 
			// get skill Crit by skillid
			 int skillcritchance = skilldata.getskillcritchance(seqway); 
			 
			// get PASSIVE skill crit
			 int passiveskillcritchance = cur.getTempPassives(6); 
			 
			// get crit bonus damage
			 critdmg = cur.getTempPassives(8);
			 
			// get skill attacksuccesrate by skillid ( Warrior buff )
			 attacksuccesrate = cur.gettempstore(21)/*WarrriorATTACKRATEbuff*/ + cur.getTempPassives(11);

			// get skill Mana consume by skillid
			 int skilldmanaconsume = skilldata.getskillmanaconsume(seqway); 
			 int finalmana = cur.mana - skilldmanaconsume;			 
			 cur.setMana(finalmana); // put new mana
			 
			 int Attack = cur.attack + skilldmg; 
			 if(cur.FAD == 1){double ATK = Attack * 1.25;  PlayerAttack = (int)ATK;}  
			 else { PlayerAttack = Attack; } //normal damage if i have no FAD
			 int Damage = (int)PlayerAttack; // TOTAL DAMAGE 
			 PlayerAttack = 0; // reset to 0;
			 
			 // Check if fury is ON or OFF
			 if (cur.furycheck == 1){ fury = 2;}// is * 2 
			 int checkfury = Damage * fury;//if fury is on then *20 else *1
			 fury = 1;
		 
		 // 1 target
		if(cur.getLevel() >= 10){cur.recfury(1);}
		this.AttackPLAYER1(seqway, checkfury, skillcritchance, passiveskillcritchance, Tplayer);
		}else{	//System.out.println("OUT");
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), this.getMovePacket(cur.Summonid, 1, Tplayer.getlastknownX(), Tplayer.getlastknownY()));
		cur.sendToMap(this.getMovePacket(cur.Summonid, 1, Tplayer.getlastknownX(), Tplayer.getlastknownY()));
		}}else{
			// VANISH
			cur.sendToMap(cur.getVanishByID(cur.Summonid));
			cur.Summonid = 0;
		}
	}
	
	public byte[] Runnskill() {
		 byte[] skill = BitTools.intToByteArray(cur.Runskill); 
		 byte[] chid = BitTools.intToByteArray(cur.Summonid);
		 byte[] buff1 = new byte[28];
		   buff1[0] = (byte)0x1c; 
		   buff1[4] = (byte)0x05;
		   buff1[6] = (byte)0x34;
		   buff1[8] = (byte)0x01;
		   for(int i=0;i<4;i++) {
			  buff1[12+i] = chid[i];	
			  buff1[20+i] = skill[i];
		   }
		   buff1[16] = (byte)0x01;
		   
		   if(cur.Running == 1){
		   buff1[24] = (byte)0xca;
		   buff1[25] = (byte)0xa0;
		   buff1[26] = (byte)0x04;
		   }
		   else
		   if(cur.Running == 0){
		   buff1[24] = (byte)0xcb;
		   buff1[25] = (byte)0x8e;
		   buff1[26] = (byte)0x0d;
		   }
		return buff1;	
	}
	
	public byte[]getMovePacket(int uid,int MOB_SPEED, float x, float y){
		this.location.setX(x); 
		this.location.setY(y);
		byte[] moveBucket = new byte[48];
		byte[] uniqueID = BitTools.intToByteArray(uid);
		byte[] moveX = BitTools.floatToByteArray(x);
		byte[] moveY = BitTools.floatToByteArray(y);
		
		moveBucket[0] = (byte)moveBucket.length;
		moveBucket[4] = (byte)0x05;
		moveBucket[6] = (byte)0x0D;
		moveBucket[8] =  (byte)0x01;// 01 = player | 02 = mob
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
	
	
	public void AttackMOB1(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, Mob TMob1, Character cur) {
		//System.out.println("AttackMOB1:"+cur.Summonid+" -> "+TMob1.getUid()+" seqway:"+seqway+" DMG:"+checkfury);
		 byte[] chid = BitTools.intToByteArray(cur.Summonid);
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target1_ = BitTools.intToByteArray(TMob1.getUid());

		 byte[] pckt = new byte[52];
		 pckt[0] = (byte)0x34;
		 pckt[4] = (byte)0x05;
		 pckt[6] = (byte)0x34;
		 pckt[8] = (byte)0x01;
		 
		 for(int i=0;i<4;i++) {
				pckt[i+12] = chid[i];						// char ID
				pckt[i+32] = target1_[i];		// target id SERVER
				pckt[i+20] = skid[i]; 						// skill ID
		 }
		
		 pckt[16] = (byte)0x01;
		 
		 pckt[27] = (byte)0x01;	// how many targets
		 pckt[28] = (byte)0x02; // 0x02 = mob
		 
		// Mob 1
		 //===  RANDOMIZER  ===\\
		 Random dice = new Random();	 
		 int random; // RANDOMIZER devided by 5% each!
		 
		 /*
			Crit == 1 is normal
			
			Crit == 2 is 1.5x Crit Damage 		 (Normal white Damage without any pot)
			Crit == 3 is 2x Crit Damage GREEN 	 (Normal GREEN DMG)
			 
			Crit == 2 is 2x Crit Damage FD       (FD pot increase crit damage 50%)
			Crit == 2 is 2x Crit Damage FD CCSR	 (FD pot increase crit damage 50%. CCSR Increase Skill Combo System Success Rate by 50%)
		 */
		 
		 random = 1+dice.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random == 16){} 
		 if (random == 17){} 
		 if (random == 18){}
		 if (random == 19){} 
		 if (random == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob1.FDD_FASR == 1 | TMob1.FDD == 1){double DEF = TMob1.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob1.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg = AFdmg * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck = (int)Fdmg * zerocheck;
		 
		 Random greendice = new Random();	 
		 int randomgreencrit;
		 randomgreencrit = 1+greendice.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}}

		 int FinalDamage = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit ) 
		 Random pdmgdice = new Random();	 
		 int randomdmg = 5;//1+pdmgdice.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg == 1){inc = FinalDamage * 1.050;} 
		 if (randomdmg == 2){inc = FinalDamage * 1.040;}
		 if (randomdmg == 3){inc = FinalDamage * 1.035;} 
		 if (randomdmg == 4){inc = FinalDamage * 1.025;}
		 if (randomdmg == 5){inc = FinalDamage * 1.000;} 
		 if (randomdmg == 6){inc = FinalDamage * 0.990;}
		 if (randomdmg == 7){inc = FinalDamage * 0.985;} 
		 if (randomdmg == 8){inc = FinalDamage * 0.975;}
		 if (randomdmg == 9){inc = FinalDamage * 0.960;} 
		 if (randomdmg == 10){inc = FinalDamage *0.950;}
		 //System.out.print("inc: " +inc+" + " +critdmg);
		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		 //System.out.println(" = "+inc);
		 if(cur.getLevel() < TMob1.getMobdata().getData().getLvl()){
		 Random missdice = new Random();
		 int randommiss = 1+missdice.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss == 6){}
		 if (randommiss == 7){}
		 if (randommiss == 8){}
		 if (randommiss == 9){}
		 if (randommiss == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss == 6){}
		 if (randommiss == 7){inc = 0; Crit = 0;}
		 if (randommiss == 8){inc = 0; Crit = 0;}
		 if (randommiss == 9){inc = 0; Crit = 0;}
		 if (randommiss == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss == 6){}
		 if (randommiss == 7){}
		 if (randommiss == 8){inc = 0; Crit = 0;}
		 if (randommiss == 9){inc = 0; Crit = 0;}
		 if (randommiss == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss == 6){}
		 if (randommiss == 7){}
		 if (randommiss == 8){}
		 if (randommiss == 9){inc = 0; Crit = 0;}
		 if (randommiss == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss == 6){}
		 if (randommiss == 7){}
		 if (randommiss == 8){}
		 if (randommiss == 9){}
		 if (randommiss == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss == 6){}
		 if (randommiss == 7){}
		 if (randommiss == 8){}
		 if (randommiss == 9){}
		 if (randommiss == 10){}
		 }else{
		 if (randommiss == 6){inc = 0; Crit = 0;}
		 if (randommiss == 7){inc = 0; Crit = 0;}
		 if (randommiss == 8){inc = 0; Crit = 0;}
		 if (randommiss == 9){inc = 0; Crit = 0;}
		 if (randommiss == 10){inc = 0; Crit = 0;}
		 }}
		 

		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt[36] = (byte)0x05;} // * 2 green crit
		
		  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;}
		  else
		  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;}
		  else
		  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;}
		  else
		  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;}
		  else
		  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;}
		  else
		  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp = TMob1.hp - (int)inc / 6;
		 
		 byte[] finaldmg = BitTools.intToByteArray((int)inc / 6); 
		 for(int i=0;i<4;i++) {
				pckt[i+44] = finaldmg[i];						
		 }
		;
		 
		 byte[] newhpz = BitTools.intToByteArray(newhp); 
		 byte[] newmanaz = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
				pckt[i+40] = newhpz[i];		
				pckt[i+48] = newmanaz[i];
		 }

		 TMob1.setHp(newhp, (int)inc, cur.charID);
		 inc = 0;
		 cur.sendToMap(pckt);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt);		 
			//System.out.println("DONE");
}	
	
	public void AttackPLAYER1(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, Character Tplayer) {
		 byte[] chid = BitTools.intToByteArray(cur.Summonid);
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target = BitTools.intToByteArray(Tplayer.charID);

		    //< ===== Attack target player ===== >
				 if(cur.getLevel() >= 10){cur.recfury(1);}
			   byte[] pckt = new byte[52];
				 pckt[0] = (byte)0x34;
				 pckt[4] = (byte)0x05;
				 pckt[6] = (byte)0x34;
				 pckt[8] = (byte)0x01;
				 
				 for(int i=0;i<4;i++) {
						pckt[i+12] = chid[i];						// char ID
						pckt[i+32] = target[i]; 		// target id SERVER
						pckt[i+20] = skid[i]; 						// skill ID
				 }
				 pckt[16] = (byte)0x01;
				 
				 pckt[25] = (byte)0x07;
				 
				 pckt[27] = (byte)0x01;	// how many targets
				 pckt[28] = (byte)0x01; // player 1 ( 0x01 is player | 0x02 is mob )
				 
				 
				 
				 // Player 1
				 byte[] Tpchid = BitTools.intToByteArray(Tplayer.Summonid);
				 
				 //===  RANDOMIZER  ===\\
				 Random dice = new Random();	 
				 int random; // RANDOMIZER devided by 5% each!
				 
				 /*
					Crit == 1 is normal
					
					Crit == 2 is 1.5x Crit Damage 		 (Normal white Damage without any pot)
					Crit == 3 is 2x Crit Damage GREEN 	 (Normal GREEN DMG)
					 
					Crit == 2 is 2x Crit Damage FD       (FD pot increase crit damage 50%)
					Crit == 2 is 2x Crit Damage FD CCSR	 (FD pot increase crit damage 50%. CCSR Increase Skill Combo System Success Rate by 50%)
				 */
				 
				 random = 1+dice.nextInt(20);
				 //System.out.println("Random number = "+ random);
				 
				 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
					 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 } 
				 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
					 if (random == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
					 if (random == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
					 if (random == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(passiveskillcritchance >= 23){ 
					 if (random == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 
				 if(cur.CASR == 1){ // if player has FDD CASR or CASR ( +25% crit chance)  then: 
				 if (random == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
				 if (random == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
				 }else{
				 if (random == 6) {} // just normal
				 if (random == 7) {} 
				 if (random == 8) {}
				 if (random == 9) {} 
				 if (random == 10){}  
				 }
				 
				 if(skillcritchance >= 3 && skillcritchance <= 7){  
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 } 
				 if(skillcritchance >= 8 && skillcritchance <= 12){  
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(skillcritchance >= 13 && skillcritchance <= 17){ 
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(skillcritchance >= 18 && skillcritchance <= 22){ 
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(skillcritchance >= 23){ 
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 	  
				 if(HidingCritChance >= 3 && HidingCritChance <= 7){  
					 if (random == 16) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 } 
				 if(HidingCritChance >= 8 && HidingCritChance <= 12){  
					 if (random == 16) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 17) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(HidingCritChance >= 13 && HidingCritChance <= 17){ 
					 if (random == 16) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 17) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 18) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(HidingCritChance >= 18 && HidingCritChance <= 22){ 
					 if (random == 16) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 17) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 18) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 19) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if (random == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
				 HidingCritChance = 0;
				 if(Tplayer.FDD == 1){double DEF = Tplayer.defence * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
				 else { TargetPlayerdefence = Tplayer.defence;} // normal
				 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
				 int AFdmg = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
				 TargetPlayerdefence = 0;
				 double Fdmg = AFdmg * Bytecritchance; // FINAL DAMAGE
				 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
				 int Fdmgzerochck = (int)Fdmg * zerocheck;
				 
				 Random greendice = new Random();	 
				 int randomgreencrit;
				 randomgreencrit = 1+greendice.nextInt(40);  // RANDOMIZER devided by 3.7% each!
				 if (randomgreencrit == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
				 int FinalDamage = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
				 Random pdmgdice = new Random();	 
				 int randomdmg = 1+pdmgdice.nextInt(10);  // RANDOMIZER devided by 20% each!
				 if (randomdmg == 1){inc = FinalDamage * 1.050;} 
				 if (randomdmg == 2){inc = FinalDamage * 1.040;}
				 if (randomdmg == 3){inc = FinalDamage * 1.035;} 
				 if (randomdmg == 4){inc = FinalDamage * 1.025;}
				 if (randomdmg == 5){inc = FinalDamage * 1.000;} 
				 if (randomdmg == 6){inc = FinalDamage * 0.990;}
				 if (randomdmg == 7){inc = FinalDamage * 0.985;} 
				 if (randomdmg == 8){inc = FinalDamage * 0.975;}
				 if (randomdmg == 9){inc = FinalDamage * 0.960;} 
				 if (randomdmg == 10){inc = FinalDamage *0.950;}
				 
				 int L = Tplayer.getLevel() - 8;
				 if(cur.getLevel() <= L){ 
				 Random missdice = new Random();
				 int randommiss = 1+missdice.nextInt(20);  // 1/4 on miss
				 if(cur.FASR == 1){
				 if (randommiss == 6){}
				 if (randommiss == 7){}
				 if (randommiss == 8){}
				 if (randommiss == 9){}
				 if (randommiss == 10){}
				 }
				 else
				 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
				 if (randommiss == 6){}
				 if (randommiss == 7){inc = 0; Crit = 0;}
				 if (randommiss == 8){inc = 0; Crit = 0;}
				 if (randommiss == 9){inc = 0; Crit = 0;}
				 if (randommiss == 10){inc = 0; Crit = 0;}
				 }
				 else
				 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
				 if (randommiss == 6){}
				 if (randommiss == 7){}
				 if (randommiss == 8){inc = 0; Crit = 0;}
				 if (randommiss == 9){inc = 0; Crit = 0;}
				 if (randommiss == 10){inc = 0; Crit = 0;}
				 }
				 else
				 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
				 if (randommiss == 6){}
				 if (randommiss == 7){}
				 if (randommiss == 8){}
				 if (randommiss == 9){inc = 0; Crit = 0;}
				 if (randommiss == 10){inc = 0; Crit = 0;}
				 }
				 else
				 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
				 if (randommiss == 6){}
				 if (randommiss == 7){}
				 if (randommiss == 8){}
				 if (randommiss == 9){}
				 if (randommiss == 10){inc = 0; Crit = 0;}
				 }
				 else
				 if(attacksuccesrate >= 23){ 
				 if (randommiss == 6){}
				 if (randommiss == 7){}
				 if (randommiss == 8){}
				 if (randommiss == 9){}
				 if (randommiss == 10){}
				 }else{
				 if (randommiss == 6){inc = 0; Crit = 0;}
				 if (randommiss == 7){inc = 0; Crit = 0;}
				 if (randommiss == 8){inc = 0; Crit = 0;}
				 if (randommiss == 9){inc = 0; Crit = 0;}
				 if (randommiss == 10){inc = 0; Crit = 0;}
				 }}
				 
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
					if(Tplayer.MANA_SHIELD_PROTECTION == 1){newmana = Tplayer.getMana() - (int)inc;}
					else{newmana = Tplayer.getHp() - (int)inc;}
					
					if(Tplayer.MANA_SHIELD_PROTECTION == 1 && newmana <= 0){newhp = Tplayer.getHp() + newmana;}
					else if(Tplayer.MANA_SHIELD_PROTECTION == 1){
					newhp = Tplayer.getHp();
					}else{newhp = newmana;}// FAAILL??
				 
				 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
				 for(int i=0;i<4;i++) {
						pckt[i+44] = finaldmg[i];						
				 }
				 inc = 0;
				 byte[] newmanaz; 
				 byte[] newhpz; 
				 if(Tplayer.MANA_SHIELD_PROTECTION == 1 && newmana <= 0){
					 newhpz = BitTools.intToByteArray(newhp);
					 newmanaz = BitTools.intToByteArray(newmana); 
					 Tplayer.setMana(newmana);
					 Tplayer.setHp(newhp);
				 }else if(Tplayer.MANA_SHIELD_PROTECTION == 1){
					 newhpz = BitTools.intToByteArray(Tplayer.getHp());
					 newmanaz = BitTools.intToByteArray(newmana); 
					 Tplayer.setMana(newmana);
				 }else{
					 newhpz = BitTools.intToByteArray(newhp);  
					 newmanaz = BitTools.intToByteArray(Tplayer.getMana());
					 Tplayer.setHp(newhp);
				 }
				 for(int i=0;i<2;i++) {
						pckt[i+40] = newhpz[i];		
						pckt[i+48] = newmanaz[i];
				 }

				 
				 if(Tplayer.Duel == 1 && newhp <= 0){
						byte[] myname = cur.getLOGsetName().getBytes();
						byte[] hisname = Tplayer.getLOGsetName().getBytes();

						byte[] fury1 = new byte[68];
						fury1[0] = (byte)0x44;
						fury1[4] = (byte)0x04;
						fury1[6] = (byte)0x2b;
						fury1[8] = (byte)0x01;
						for(int i=0;i<4;i++) {
						fury1[12+i] = Tpchid[i];
						fury1[20+i] = chid[i];
						fury1[44+i] = Tpchid[i];
						}
						fury1[17] = (byte)0xfa;
						fury1[18] = (byte)0x60; 
						fury1[19] = (byte)0x2a;
						
						for(int i=0;i<myname.length;i++) {//me
							fury1[24+i] = myname[i];
						}
						for(int i=0;i<hisname.length;i++) {//him
							fury1[48+i] = hisname[i];
						}
						
						fury1[65] = (byte)0x98;
						fury1[66] = (byte)0x0f; 
						fury1[67] = (byte)0xbf;	
				 cur.Duel = 0; 
				 cur.sendToMap(fury1);
				 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury1); 
				 }
				 
				 cur.sendToMap(pckt);
				 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt);
		   
	}
	
	

	// return external character packet
		public byte[] extCharPacket() {
			byte[] cedata = new byte[612];
			short length = (short)cedata.length;
			byte[] lengthbytes = BitTools.shortToByteArray(length);
			//System.out.println("RAZERNAGA:"+cur.Summonid);
			byte[] chID = BitTools.intToByteArray(cur.Summonid);
			byte[] chName;
			 chName = cur.getLOGsetName().getBytes();	

			byte[] xCoords = BitTools.floatToByteArray(cur.getLocation().getX());		
			byte[] yCoords = BitTools.floatToByteArray(cur.getLocation().getY());		
			cedata[0] = lengthbytes[0];
			cedata[1] = lengthbytes[1];
			cedata[4] = (byte)0x05;
			cedata[6] = (byte)0x01;
			cedata[8] = (byte)0x01;
			
			for(int i=0;i<4;i++) {
				cedata[i+12] = chID[i]; //character ID
				cedata[i+88] = xCoords[i]; //location x
				cedata[i+92] = yCoords[i]; //location y
			}
			
					for(int i=0;i<chName.length;i++) {
				cedata[i+20] = chName[i]; //characters Name
			}
			
			for(int i=0;i<16;i++) {
				cedata[37+i] = (byte)0x30; //character packets have 16 times 30(0 in ASCII) in row. Mysteries of CRS.
			}
			
			cedata[54] = (byte)cur.getFaction();//faction	
		    byte[] fame = BitTools.intToByteArray(cur.getFame());        for(int i=0;i<4;i++) { cedata[i+56] = fame[i]; }// fame 
			cedata[62] = (byte)cur.getFace(); // face
			
			if(cur.getCharacterClass() == 2) {
				cedata[60] = (byte)0x02; //gender byte
				cedata[68] = (byte)0x02; //class byte
			} else {
				cedata[60] = (byte)0x01; //gender byte
				cedata[68] = (byte)cur.getCharacterClass(); //class byte
			}
			cedata[70] = (byte)0x23;
			cedata[71] = (byte)0x07;
			cedata[72] = (byte)0x07;
			cedata[74] = (byte)cur.getLevel();
			   					
		
			//SHOW my ITEMS:Equipment TO OTHER PLAYERS				
			
			// Head		
			cedata[98] = (byte)0x0b; // <- somekind of item ( head ) indicator         
			byte[] head = BitTools.intToByteArray(cur.getequipSLOT(0));        for(int i=0;i<4;i++) { cedata[i+100] = head[i]; }                
			// neck       
			byte[] neck = BitTools.intToByteArray(cur.getequipSLOT(1));        for(int i=0;i<4;i++) { cedata[i+112] = neck[i]; }        
			cedata[111] = (byte)0x01;                        
			// cape <-- bugged ?        
			byte[] cape = BitTools.intToByteArray(cur.getequipSLOT(2));        for(int i=0;i<4;i++) { cedata[i+124] = cape[i]; }                
			// chest        
			cedata[134] = (byte)0x0a;         
			byte[] chest = BitTools.intToByteArray(cur.getequipSLOT(3));      for(int i=0;i<4;i++) { cedata[i+136] = chest[i]; }                 
			// pants        
			cedata[146] = (byte)0x0a;         
			cedata[147] = (byte)0x02;         
			byte[] pants = BitTools.intToByteArray(cur.getequipSLOT(4));      for(int i=0;i<4;i++) { cedata[i+148] = pants[i]; }                        
			// armor        
			cedata[158] = (byte)0x05;        
			byte[] armor = BitTools.intToByteArray(cur.getequipSLOT(5));      for(int i=0;i<4;i++) { cedata[i+160] = armor[i]; }                        
			// bracer        
			cedata[170] = (byte)0x03;        
			cedata[171] = (byte)0x07;        
			byte[] bracer = BitTools.intToByteArray(cur.getequipSLOT(6));    for(int i=0;i<4;i++) { cedata[i+172] = bracer[i]; }                         
			// primary weapon        
			byte[] primaryweapon = BitTools.intToByteArray(cur.getequipSLOT(7));  for(int i=0;i<4;i++) { cedata[i+184] = primaryweapon[i]; }  
			// second weapon        
			byte[] secondweapon = BitTools.intToByteArray(cur.getequipSLOT(8));  for(int i=0;i<4;i++) { cedata[i+196] = secondweapon[i]; } 
			// ring 1       
			byte[] ring1 = BitTools.intToByteArray(cur.getequipSLOT(9));      for(int i=0;i<4;i++) { cedata[i+208] = ring1[i]; }            
			// ring 2        
			byte[] ring2 = BitTools.intToByteArray(cur.getequipSLOT(10));      for(int i=0;i<4;i++) { cedata[i+220] = ring2[i]; }        
			// shoes      
			byte[] shoes = BitTools.intToByteArray(cur.getequipSLOT(11));      for(int i=0;i<4;i++) { cedata[i+232] = shoes[i]; }   
	    
			byte[] medal1 = BitTools.intToByteArray(cur.getequipSLOT(12));      for(int i=0;i<4;i++) { cedata[i+244] = medal1[i]; }  
			
			byte[] medal2 = BitTools.intToByteArray(cur.getequipSLOT(13));      for(int i=0;i<4;i++) { cedata[i+256] = medal2[i]; }  
			
			byte[] medal3 = BitTools.intToByteArray(cur.getequipSLOT(14));      for(int i=0;i<4;i++) { cedata[i+268] = medal3[i]; }  
			
			// mount        
			byte[] mount = BitTools.intToByteArray(cur.getequipSLOT(15));      for(int i=0;i<4;i++) { cedata[i+280] = mount[i]; }  
			
			// next to mount (DT)        
			byte[] bird = BitTools.intToByteArray(cur.getequipSLOT(16));      for(int i=0;i<4;i++) { cedata[i+292] = bird[i]; }  
			
			cedata[610] = (byte)0x50;        
			cedata[611] = (byte)0x2a;
			return cedata;
		}
	
}
