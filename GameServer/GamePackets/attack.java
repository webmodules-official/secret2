package GameServer.GamePackets;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import chat.ChatParser;


import Mob.Mob;
import Player.Character;
import Player.Charstuff;
import Player.Guildwar;
import Player.Party;
import Player.PlayerConnection;
import Player.Summon;
import Player.buffdata;
import Player.skilldata;
import Player.skilleffects;
import Player.skillpasives;
import Tools.BitTools;
import World.WMap;

import Connections.Connection;
import ServerCore.ServerFacade;
import Encryption.Decryptor;

public class attack implements Packet {
	private  int critdmg = 0, HidingCritChance = 0, HidingDamage = 0;
	private  int zerocheck = 1;
	private  int greencrit = 1;
	private  int fury = 1; // normal
	private  int Crit = 1; // put it to 1
	private  double Bytecritchance = 1;
	private  double TargetPlayerdefence = 0; // Target player DEF
	private  double PlayerAttack = 0; // player DEF
	private  double Mobexp = 0; // player DEF
	private  double inc = 0;
	private  long FINALEXP = 0;
	private  int attacksuccesrate = 0;
	private WMap wmap = WMap.getInstance();
	
	@Override
	public void execute(ByteBuffer buff) {
		// TODO Auto-generated method stub
	}

	public byte[] returnWritableByteBuffer(byte[] buffyTheVampireSlayer, Connection con) {
		//System.out.println("Handling combat ");
		byte[] decrypted = new byte[(buffyTheVampireSlayer[0] & 0xFF)-8];
		
		for(int i=0;i<decrypted.length;i++) {
			decrypted[i] = (byte)(buffyTheVampireSlayer[i+8] & 0xFF);
		}
		
				decrypted = Decryptor.Decrypt(decrypted);
			
			//PERFECT WAY TO KNOW WHATS WHAT !
			//int one1 = 0;
			//for(int i=0;i<decrypted.length;i++) {System.out.print(" "+one1+":"+decrypted[i]); one1++;}
			//System.out.println("");
			  /*
			  
			  Put DoOption.ini -> OPTION_EFFECT=1 <- so others can see eachoter skill effects.
			  
			  */
			  /*
			  decrypted[16] if = 1 then its a player
			  decrypted[17] if = 1 then its a summon ???
			  decrypted[18] if = 1 then its an mob
			  */
				 Character cur = ((PlayerConnection)con).getActiveCharacter();
				 if(cur.getFaction() == 0 && cur.getLevel() >= 36){Charstuff.getInstance().respondguild("Please select a faction first on the website before using skills. ", cur.GetChannel()); Charstuff.getInstance().respondguild("www.globalmartialheroes.com -> Member Login -> "+cur.getLOGsetName(), cur.GetChannel());return null;}							 
				 if(cur.getCurrentMap() == 100){return null;}
				 byte[] chid = BitTools.intToByteArray(cur.getCharID());
				 int seqway;
				 seqway = cur.getlearnedskill(decrypted[0]);
				 
				 int skilllevel = skilldata.getskilllevel(seqway);
				 if(skilllevel > cur.getLevel()){/*Charstuff.getInstance().respondguild("attack: " +skilllevel+" is smaller then " +cur.getLevel(), cur.GetChannel());*/return null;}
		
					
				 //System.out.println("seqway : "+seqway);
				 //byte[] skid = BitTools.intToByteArray(seqway); // skill id
				 byte[] target1 = new byte[4];
				 byte[] target2 = new byte[4];
				 byte[] target3 = new byte[4];
				 byte[] target4 = new byte[4];
				 byte[] target5 = new byte[4];
				 byte[] target6 = new byte[4];
				 byte[] target7 = new byte[4];
				 byte[] target8 = new byte[4];
				 
				 
				 // get skill cooldowns by skillid  (clientsided skill cooldowns might be in skills.scr)
				    int skillcooldown = skilldata.getskillcooldowns(seqway); 
				
				 	// check if skill is on cooldown 
					if (System.currentTimeMillis() - cur.getskillcooldowns(seqway) > skillcooldown ){ // skill attack cooldown
						cur.setskillcooldowns(seqway, System.currentTimeMillis());
					}
					else if(seqway == 121103060||seqway == 122206060||seqway == 121309060||seqway == 121413050){
						if (System.currentTimeMillis() - cur.getskillcooldowns(seqway) > skillcooldown){
								cur.setskillcooldowns(seqway, System.currentTimeMillis());
						}else{return null;}
					}else if (System.currentTimeMillis() - cur.getskillcooldowns(cur.getlearnedskill(0)) > skilldata.getskillcooldowns(cur.getlearnedskill(0))){ // basic attack cooldown
						if(seqway != buffdata.getBedoonglist(seqway)){
						for(int i=0;i<4;i++) {target1[i] = (byte)decrypted[i+20];} int one = BitTools.byteArrayToInt(target1);
						if(cur.getCharID() != one && seqway != buffdata.getbufflist(seqway)){
						seqway = cur.getlearnedskill(0);
						
						if(skillcooldown > 4000){ // if is bigger then 4 seconds then notify the cd.
							long lol = System.currentTimeMillis() - cur.getskillcooldowns(seqway);
							long lol1 = skillcooldown - lol;
							long lol2 = lol1 / 1000;
							Charstuff.getInstance().respondyellowshout("Cooldown: "+lol2+" Seconds.",cur.GetChannel());
							cur.setskillcooldowns(cur.getlearnedskill(0), System.currentTimeMillis());
						}
						cur.setskillcooldowns(cur.getlearnedskill(0), System.currentTimeMillis());
						}else{
						if(skillcooldown > 4000){  // if is bigger then 4 seconds then notify the cd.
							long lol = System.currentTimeMillis() - cur.getskillcooldowns(seqway);
							long lol1 = skillcooldown - lol;
							long lol2 = lol1 / 1000;
							Charstuff.getInstance().respondyellowshout("Cooldown: "+lol2+" Seconds.",cur.GetChannel());
							cur.setskillcooldowns(cur.getlearnedskill(0), System.currentTimeMillis());
							}
							return null;}
						}else{
							if(cur.Budoong != 0){
								// let it pass when its on 1 and 2
							}else{
							if(skillcooldown > 4000){ // if is bigger then 4 seconds then notify the cd.
								long lol = System.currentTimeMillis() - cur.getskillcooldowns(seqway);
								long lol1 = skillcooldown - lol;
								long lol2 = lol1 / 1000;
								Charstuff.getInstance().respondyellowshout("Cooldown: "+lol2+" Seconds.",cur.GetChannel());
								cur.setskillcooldowns(cur.getlearnedskill(0), System.currentTimeMillis());
							  }
							return null;
							}
						}
					}else{return null;}
					
					
				//check if access is granted by cds
				if(System.currentTimeMillis() - cur.attackskillcd > 875){
				if(cur.getLevel() >= 10){cur.recfury(1);}
				cur.attackskillcd = System.currentTimeMillis();	 
					
				// get skill Damage by skillid
				 int skilldmg = skilldata.getskilldmg(seqway); 
				 
				// get skill Crit by skillid
				 int skillcritchance = skilldata.getskillcritchance(seqway); 
				 
				// get PASSIVE skill crit
				 int passiveskillcritchance = cur.getTempPassives(6); 
				 
				// get crit bonus damage
				 critdmg = cur.getTempPassives(8);
				 
				 //Hiding crit bonuschance
				 HidingCritChance = cur.gettempstore(22);
				 
				 //Hiding % bonus damage
				 HidingDamage = cur.gettempstore(45);
				 
				// get skill attacksuccesrate by skillid ( Warrior buff )
				 int meow = 0;
				 if(cur.bufficon1 == 100|| cur.bufficon2 == 100){/*System.out.println("attacksuccesrate");*/ meow = 5;}
				 attacksuccesrate = cur.gettempstore(21)/*WarrriorATTACKRATEbuff*/ + cur.getTempPassives(11) + meow;
				
				// get skill Mana consume by skillid
				 int skilldmanaconsume = skilldata.getskillmanaconsume(seqway); 
				 int finalmana = cur.mana - skilldmanaconsume;	
				 if(cur.getMana() <= 0){return null;}
				 cur.setMana(finalmana); // put new mana
				 PlayerAttack = cur.attack + skilldmg; 
				 if(cur.bufficon1 == 101|| cur.bufficon2 == 101){/*System.out.println("final attack points");*/double ATK = PlayerAttack * 1.05;  PlayerAttack = (int)ATK;}
					
				 
				 double Damage;
				 if(cur.Budoong == 2 && seqway == buffdata.getBedoonglist(seqway)){
				  double times = 3;
				  int Targetlevel = 0;
					if(decrypted[16] == 1 ||decrypted[16] == 2 ||decrypted[16] == 3 ||decrypted[16] == 4 ||decrypted[16] == 5 ||decrypted[16] == 6 ||decrypted[16] == 7 ||decrypted[16] == 8){	// if 16 = 1 then its a player	 
						for(int i=0;i<4;i++) {target1[i] = (byte)(decrypted[i+20]);}
						int TP1 = BitTools.byteArrayToInt(target1);
						Character Tplayer = WMap.getInstance().getCharacter(TP1);
						if(Tplayer != null){
						Targetlevel = Tplayer.getLevel();
						}
					}else
					if(decrypted[18] == 1 ||decrypted[18] == 2 ||decrypted[18] == 3 ||decrypted[18] == 4 ||decrypted[18] == 5 ||decrypted[18] == 6 ||decrypted[18] == 7 ||decrypted[18] == 8){	// if 18 = 1 then its a mob	 
						for(int i=0;i<4;i++) {target1[i] = (byte)(decrypted[i+20]);}
						int TP1 = BitTools.byteArrayToInt(target1);
						 Mob TMob1 = WMap.getInstance().getMob(TP1, cur.getCurrentMap()); 
						if(TMob1 != null){
						Targetlevel = TMob1.getData().getLvl();
						}
					}else{
						return null;
					}

					if(cur.getLevel() < Targetlevel){
						int btween = Targetlevel - cur.getLevel();
					   for(int i=0;i<btween;i++) {
						   times = times - 0.010;
					   }
					}else
					if(Targetlevel < cur.getLevel()){
						int btween = cur.getLevel() - Targetlevel;
						for(int i=0;i<btween;i++) {
						 times = times + 0.010;
						}
					}
				 Damage = PlayerAttack * times; // TOTAL DAMAGE 
				 }else{Damage = PlayerAttack;} 
		
				 // Check if fury is ON or OFF
				 if (cur.furycheck == 1){ fury = 2;}// is * 2 
				 int checkfury = (int)Damage * fury;//if fury is on then *20 else *1
				 PlayerAttack = 0; // reset to 0;
				 fury = 1;
				 //System.out.println("<=== Attack target ===>");
	//----------------------------- Attack Target MOB -------------------------------------------\\
	// Movement Speed Boost && Budoong AOE -------144 basic -> 195 skill -> 587 budoong
	if( decrypted[16] == 0 && decrypted[17] == 0 && decrypted[18] == 0){	
		byte[] skils = BitTools.intToByteArray(seqway); 
		 byte[] buff1 = new byte[28];
		   buff1[0] = (byte)0x1c; 
		   buff1[4] = (byte)0x05;
		   buff1[6] = (byte)0x34;
		   buff1[8] = (byte)0x01;
		   for(int i=0;i<4;i++) {
			  buff1[12+i] = chid[i];	
			  buff1[20+i] = skils[i];
		   }
		   buff1[16] = (byte)0x01;
		   
		   if(seqway == buffdata.getBedoonglist(seqway) && cur.Budoong == 0){ // if is budoong then cast
		   cur.Budoong = 1;
		   cur.BudoongSkill = seqway;
		   if(cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    cur.HIDING = 0;
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }
		   }else if(seqway == buffdata.getBedoonglist(seqway) && cur.Budoong == 1){//end cast budoong
		   cur.Budoong = 2;	// 2 = ready for attack
		   cur.BudoongSkill = seqway;
		   buff1[24] = (byte)0x01;  
		   if(cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    cur.HIDING = 0;
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		   }
		   }else{// else its speed running skill
		   if(cur.Running == 0){
		   buff1[24] = (byte)0xca;
		   buff1[25] = (byte)0xa0;
		   buff1[26] = (byte)0x04;
		   cur.Running = 1;
		   }
		   else{
		   buff1[24] = (byte)0xcb;
		   buff1[25] = (byte)0x8e;
		   buff1[26] = (byte)0x0d;
		   cur.Running = 0;
		   }
		   cur.Runskill = seqway;
		   }
		 cur.sendToMap(buff1);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), buff1); 	
		 return null;
	 }
	 
     if(cur.Budoong == 2){
	 cur.RemoveBudoong(cur.BudoongSkill);
	 }
	
     
     
     //monk healing
 	if(decrypted[16] == 1  && seqway == 410171 ||decrypted[16] == 1  && seqway == 410175 || seqway == 410173|| seqway == 42100021|| seqway == 41100021){
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
		}
		 int T1 = BitTools.byteArrayToInt(target1);
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target = BitTools.intToByteArray(T1);
		
		   byte[] pckt = new byte[52];
				 pckt[0] = (byte)0x34;
				 pckt[4] = (byte)0x05;
				 pckt[6] = (byte)0x34;
				 pckt[8] = (byte)0x01;
				 
				 for(int i=0;i<4;i++) {
						pckt[i+12] = chid[i];						// char ID
						pckt[i+32] = target[i]; 					// target id SERVER
						pckt[i+20] = skid[i]; 						// skill ID
				 }
				 pckt[16] = (byte)0x01;
				 if(seqway == buffdata.getBedoonglist(seqway)){
				 pckt[24] = (byte)0x02;
				 }
				 pckt[25] = (byte)0x07;
				 
				 pckt[27] = (byte)0x01;	// how many targets
				 pckt[28] = (byte)0x01; // player 1 ( 0x01 is player | 0x02 is mob )
				 
				 
				 
				 	// Player 1
				 	Character Tplayer = WMap.getInstance().getCharacter(BitTools.byteArrayToInt(target));

				 	if(seqway == 410171){inc = 300;}
				 	else
				 	if(seqway == 410175){inc = 600;}
				 	else
				 	if(seqway == 410173){inc = 0;}
					else
					if(seqway == 42100021){inc = 0;}
					else
					if(seqway == 41100021){inc = 0;}
					else
				 	{return null;}
		
				 	pckt[36] = (byte)0x01; // blue 
				 
				 	int newhp; int newmana;
					if(Tplayer.MANA_SHIELD_PROTECTION == 1){newmana = Tplayer.getMana() + (int)inc;}
					else{newmana = Tplayer.getHp() + (int)inc;}
					
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
					 if(inc > 0){Tplayer.setMana(newmana);}
				 }else{
					 newhpz = BitTools.intToByteArray(newhp);  
					 newmanaz = BitTools.intToByteArray(Tplayer.getMana());
					 if(inc > 0){Tplayer.setHp(newhp);}
				 }
				 for(int i=0;i<2;i++) {
						pckt[i+40] = newhpz[i];		
						pckt[i+48] = newmanaz[i];
				 }
				 
				 cur.sendToMap(pckt);
				 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt);
		   
		
	 }else
	 // 1 Target
	if(decrypted[18] == 1  && seqway == cur.getlearnedskill(0)||decrypted[18] == 2  && seqway == cur.getlearnedskill(0)||decrypted[18] == 3  && seqway == cur.getlearnedskill(0)||decrypted[18] == 4  && seqway == cur.getlearnedskill(0)||decrypted[18] == 5  && seqway == cur.getlearnedskill(0)||decrypted[18] == 6  && seqway == cur.getlearnedskill(0)||decrypted[18] == 7  && seqway == cur.getlearnedskill(0)||decrypted[18] == 8 && seqway == cur.getlearnedskill(0)){
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
		}
		int T1 = BitTools.byteArrayToInt(target1);
		this.AttackMOB1(seqway, checkfury, skillcritchance, passiveskillcritchance, T1, con);
	 }
	else
		if(decrypted[16] == 1  && seqway == cur.getlearnedskill(0)||decrypted[16] == 2  && seqway == cur.getlearnedskill(0)||decrypted[16] == 3  && seqway == cur.getlearnedskill(0)||decrypted[16] == 4  && seqway == cur.getlearnedskill(0)||decrypted[16] == 5  && seqway == cur.getlearnedskill(0)||decrypted[16] == 6  && seqway == cur.getlearnedskill(0)||decrypted[16] == 7  && seqway == cur.getlearnedskill(0)||decrypted[16] == 8 && seqway == cur.getlearnedskill(0)){
			for(int i=0;i<4;i++) {
				target1[i] = (byte)(decrypted[i+20]); 	
			}
			int T1 = BitTools.byteArrayToInt(target1);
			this.AttackPLAYER1(seqway, checkfury, skillcritchance, passiveskillcritchance, T1, con);
		 }
		else
	//1 Target player
	if(decrypted[16] == 1 ||decrypted[16] == 2 ||decrypted[16] == 3 ||decrypted[16] == 4 ||decrypted[16] == 5 ||decrypted[16] == 6 ||decrypted[16] == 7 ||decrypted[16] == 8){	// if 16 = 1 then its a player	 
			for(int i=0;i<4;i++) {
				target1[i] = (byte)(decrypted[i+20]); 	
			}
			int TP1 = BitTools.byteArrayToInt(target1);
	   this.AttackPLAYER1(seqway, checkfury, skillcritchance, passiveskillcritchance, TP1, con);
	 }			 
	else			 
	 // 1 Target
	if( decrypted[18] == 1 ){
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
		}
		int T1 = BitTools.byteArrayToInt(target1);
		this.AttackMOB1(seqway, checkfury, skillcritchance, passiveskillcritchance, T1, con);
	 }
	else
	 // 2 Targets
	if( decrypted[18] == 2 ){ 
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
			target2[i] = (byte)(decrypted[i+24]); 	
		}
		int T1 = BitTools.byteArrayToInt(target1);
		int T2 = BitTools.byteArrayToInt(target2);
		this.AttackMOB2(seqway, checkfury, skillcritchance, passiveskillcritchance, T1,T2, con);
	}		    		    
	else
	 // 3 Targets
	if( decrypted[18] == 3 ){  
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
			target2[i] = (byte)(decrypted[i+24]); 	
			target3[i] = (byte)(decrypted[i+28]); 	
		}
		int T1 = BitTools.byteArrayToInt(target1);
		int T2 = BitTools.byteArrayToInt(target2);
		int T3 = BitTools.byteArrayToInt(target3);
		this.AttackMOB3(seqway, checkfury, skillcritchance, passiveskillcritchance, T1,T2,T3, con);
	}
	else
	 // 4 Targets
	if( decrypted[18] == 4 ){ 	
		for(int i=0;i<4;i++) {
		target1[i] = (byte)(decrypted[i+20]); 	
		target2[i] = (byte)(decrypted[i+24]); 	
		target3[i] = (byte)(decrypted[i+28]); 	
		target4[i] = (byte)(decrypted[i+32]); 	
			
	}
	int T1 = BitTools.byteArrayToInt(target1);
	int T2 = BitTools.byteArrayToInt(target2);
	int T3 = BitTools.byteArrayToInt(target3);
	int T4 = BitTools.byteArrayToInt(target4);
		this.AttackMOB4(seqway, checkfury, skillcritchance, passiveskillcritchance, T1,T2,T3,T4, con);
	}
	else
	// 5 Targets
	if( decrypted[18] == 5 ){ 
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
			target2[i] = (byte)(decrypted[i+24]); 	
			target3[i] = (byte)(decrypted[i+28]); 	
			target4[i] = (byte)(decrypted[i+32]); 	
			target5[i] = (byte)(decrypted[i+36]); 	
		}
		int T1 = BitTools.byteArrayToInt(target1);
		int T2 = BitTools.byteArrayToInt(target2);
		int T3 = BitTools.byteArrayToInt(target3);
		int T4 = BitTools.byteArrayToInt(target4);
		int T5 = BitTools.byteArrayToInt(target5);
		this.AttackMOB5(seqway, checkfury, skillcritchance, passiveskillcritchance, T1,T2,T3,T4,T5, con);
	}

	// 6 Targets
	if( decrypted[18] == 6 ){  
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
			target2[i] = (byte)(decrypted[i+24]); 	
			target3[i] = (byte)(decrypted[i+28]); 	
			target4[i] = (byte)(decrypted[i+32]); 	
			target5[i] = (byte)(decrypted[i+36]); 	
			target6[i] = (byte)(decrypted[i+40]); 	
		}
		int T1 = BitTools.byteArrayToInt(target1);
		int T2 = BitTools.byteArrayToInt(target2);
		int T3 = BitTools.byteArrayToInt(target3);
		int T4 = BitTools.byteArrayToInt(target4);
		int T5 = BitTools.byteArrayToInt(target5);
		int T6 = BitTools.byteArrayToInt(target6);
		this.AttackMOB6(seqway, checkfury, skillcritchance, passiveskillcritchance, T1,T2,T3,T4,T5,T6, con);			   
	}
				 
	// 7 Targets
	if( decrypted[18] == 7 ){ 
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
			target2[i] = (byte)(decrypted[i+24]); 	
			target3[i] = (byte)(decrypted[i+28]); 	
			target4[i] = (byte)(decrypted[i+32]); 	
			target5[i] = (byte)(decrypted[i+36]); 	
			target6[i] = (byte)(decrypted[i+40]); 	
			target7[i] = (byte)(decrypted[i+44]); 	
		}
		int T1 = BitTools.byteArrayToInt(target1);
		int T2 = BitTools.byteArrayToInt(target2);
		int T3 = BitTools.byteArrayToInt(target3);
		int T4 = BitTools.byteArrayToInt(target4);
		int T5 = BitTools.byteArrayToInt(target5);
		int T6 = BitTools.byteArrayToInt(target6);
		int T7 = BitTools.byteArrayToInt(target7);
		this.AttackMOB7(seqway, checkfury, skillcritchance, passiveskillcritchance, T1,T2,T3,T4,T5,T6,T7 , con);		   
	}
	 		 
	// 8 Targets
	if( decrypted[18] == 8 ){  
		for(int i=0;i<4;i++) {
			target1[i] = (byte)(decrypted[i+20]); 	
			target2[i] = (byte)(decrypted[i+24]); 	
			target3[i] = (byte)(decrypted[i+28]); 	
			target4[i] = (byte)(decrypted[i+32]); 	
			target5[i] = (byte)(decrypted[i+36]); 	
			target6[i] = (byte)(decrypted[i+40]); 	
			target7[i] = (byte)(decrypted[i+44]); 	
			target8[i] = (byte)(decrypted[i+48]); 	
		}

		int T1 = BitTools.byteArrayToInt(target1);
		int T2 = BitTools.byteArrayToInt(target2);
		int T3 = BitTools.byteArrayToInt(target3);
		int T4 = BitTools.byteArrayToInt(target4);
		int T5 = BitTools.byteArrayToInt(target5);
		int T6 = BitTools.byteArrayToInt(target6);
		int T7 = BitTools.byteArrayToInt(target7);
		int T8 = BitTools.byteArrayToInt(target8);
		this.AttackMOB8(seqway, checkfury, skillcritchance, passiveskillcritchance, T1,T2,T3,T4,T5,T6,T7,T8, con);			   
	}
	
	cur.attackskillcd = System.currentTimeMillis();
	}
	   return null;
}
	
	//----------------------------- Attack / Buff Target PLAYER -------------------------------------------\\
	public void AttackPLAYER1(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1 ,Connection con) {
		 Character cur = ((PlayerConnection)con).getActiveCharacter();
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target = BitTools.intToByteArray(target1);

		   if (seqway == buffdata.getbufflist(seqway)) { // if skill is in bufflist then buff target player
				String gm = cur.getChargm();
			   if(gm.equals("az") && cur.getCharacterClass() == 4){
				   byte[] buff1 = new byte[44];
				   buff1[0] = (byte)0x2c; 
				   buff1[4] = (byte)0x05;
				   buff1[6] = (byte)0x1f;
				   buff1[8] = (byte)0x01;
				   buff1[9] = (byte)0x99;
				   buff1[10] = (byte)0x0f;
				   buff1[11] = (byte)0xbf;
					 
					for(int i=0;i<4;i++) {
						buff1[12+i] = target[i]; // target id 4 bytes
					}	
					 	
					 int buffid1 = 15; 		// buff id -> 07 = hp | 15 = offence 
					 int bufftime1 = 1800; 	// time  Btw: 1800 = 2 hours.
					 int buffvalue1 = 50;	// value
					 int buffslot1 = 8; 	// buffslot
					 
					 

					 byte[] buffidz1 = BitTools.intToByteArray(buffid1); 
					 byte[] bufftimez1 = BitTools.intToByteArray(bufftime1);
					 byte[] buffvaluez1 = BitTools.intToByteArray(buffvalue1);
					 byte[] buffslotz1 = BitTools.intToByteArray(buffslot1);
					 
					 Character Tplayer = WMap.getInstance().getCharacter(BitTools.byteArrayToInt(target)); 
				     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 41){}
				     else
				     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 143){}
				     else
					 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 137){}
					 else
					 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 132){}
					 else
					 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 129){}
					 else
				     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 125){}
				     else
					 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 115){}
				     else
					 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 83){}
					 else
					 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 74){}
					 else
					 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 72){}
					 else
					 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 66){}
					 else
					 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 62){}
					 else
				     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 56){}
				     else
				     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 82){}
				     else{return;}
					 Tplayer.setDots(buffslot1, buffid1, bufftime1, buffvalue1);
		
						 for(int i=0;i<2;i++) {
							 buff1[i+16] = buffslotz1[i];	 // buffslot
							 buff1[i+20] = buffidz1[i];	 // buff id
							 buff1[i+22] = bufftimez1[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
							 buff1[i+24] = buffvaluez1[i]; // Value XXXXX
						 }
						 buff1[26] = (byte)0x01; 
						
						 buff1[28] = (byte)0x87; 
						 buff1[29] = (byte)0x01; 
						 buff1[32] = (byte)0x87; 
						 buff1[33] = (byte)0x01; 
						
						 buff1[36] = (byte)0x45; 
						 buff1[37] = (byte)0x01; 
						 buff1[38] = (byte)0x45; 
						 buff1[39] = (byte)0x01; 
						 buff1[40] = (byte)0xf2; 
						 buff1[42] = (byte)0xf2; 
						 ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), buff1); 	
				   
				   
				   
				   byte[] buff2 = new byte[44];
				   buff2[0] = (byte)0x2c; 
				   buff2[4] = (byte)0x05;
				   buff2[6] = (byte)0x1f;
				   buff2[8] = (byte)0x01;
				   buff2[9] = (byte)0x99;
				   buff2[10] = (byte)0x0f;
				   buff2[11] = (byte)0xbf;
					 
					for(int i=0;i<4;i++) {
						buff2[12+i] = target[i]; // target id 4 bytes
					}	
					 	
					 int buffid2 = 7; 		// buff id -> 07 = hp | 15 = offence 
					 int bufftime2 = 1800; 	// time  Btw: 1800 = 2 hours.
					 int buffvalue2 = 250;	// value
					 int buffslot2 = 9; 	// buffslot
					 

					 byte[] buffidz2 = BitTools.intToByteArray(buffid2); 
					 byte[] bufftimez2 = BitTools.intToByteArray(bufftime2);
					 byte[] buffvaluez2 = BitTools.intToByteArray(buffvalue2);
					 byte[] buffslotz2 = BitTools.intToByteArray(buffslot2);
					 Tplayer.setDots(buffslot2, buffid2, bufftime2, buffvalue2);
					 
						 for(int i=0;i<2;i++) {
							 buff2[i+16] = buffslotz2[i];	 // buffslot
							 buff2[i+20] = buffidz2[i];	 // buff id
							 buff2[i+22] = bufftimez2[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
							 buff2[i+24] = buffvaluez2[i]; // Value XXXXX
						 }
						 buff2[26] = (byte)0x01; 
						
						 buff2[28] = (byte)0x87; 
						 buff2[29] = (byte)0x01; 
						 buff2[32] = (byte)0x87; 
						 buff2[33] = (byte)0x01; 
						
						 buff2[36] = (byte)0x45; 
						 buff2[37] = (byte)0x01; 
						 buff2[38] = (byte)0x45; 
						 buff2[39] = (byte)0x01; 
						 buff2[40] = (byte)0xf2; 
						 buff2[42] = (byte)0xf2; 
						 ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), buff2); 	
				   
				   byte[] buff = new byte[96];
					 buff[0] = (byte)0x2c; 
					 buff[4] = (byte)0x05;
					 buff[6] = (byte)0x1f;
					 buff[8] = (byte)0x01;
					 

					 
					for(int i=0;i<4;i++) {
						buff[12+i] = target[i]; // target id 4 bytes
						buff[56+i] = chid[i]; 	
						buff[64+i] = skid[i]; 
						buff[76+i] = target[i]; // target id 4 bytes
					}	
					 	
					 int buffid = 16;	// buff id -> 07 = hp | 15 = offence 
					 int bufftime = 1800; 	// time  Btw: 1800 = 2 hours.
					 int buffvalue = 50;		// value
					 int buffslot = 11; 	// buffslot
					 

					 byte[] buffidz = BitTools.intToByteArray(buffid); 
					 byte[] bufftimez = BitTools.intToByteArray(bufftime);
					 byte[] buffvaluez = BitTools.intToByteArray(buffvalue);
					 byte[] buffslotz = BitTools.intToByteArray(buffslot);
					 //	public void setDots(int DotsSLOTz, int DotsIconIDz, int DotsTimez, int DotsValuez) {
					 Tplayer.setDots(buffslot, buffid, bufftime, buffvalue);
					 
						 for(int i=0;i<2;i++) {
							 buff[i+16] = buffslotz[i];	 // buffslot
							 buff[i+20] = buffidz[i];	 // buff id
							 buff[i+22] = bufftimez[i];  // Time XX Mins XX Secs (Time in mh = EXAMPLE: 192 / 4 = 48 -> 48 is deci  = 30 Hex)
							 buff[i+24] = buffvaluez[i]; // Value XXXXX
						 }
						 buff[26] = (byte)0x01; 
						
						 buff[28] = (byte)0x87; 
						 buff[29] = (byte)0x01; 
						 buff[32] = (byte)0x87; 
						 buff[33] = (byte)0x01; 
						
						 buff[36] = (byte)0x45; 
						 buff[37] = (byte)0x01; 
						 buff[38] = (byte)0x45; 
						 buff[39] = (byte)0x01; 
						 buff[40] = (byte)0xf2; 
						 buff[42] = (byte)0xf2; 
						 
						 buff[44] = (byte)0x34; // 2nd packet contains buff animation
						 buff[48] = (byte)0x05; 
						 buff[50] = (byte)0x34; 
						 buff[52] = (byte)0x01; 
						
					   	 buff[60] = (byte)0x01; 

						 buff[69] = (byte)0x07; 
						
						 buff[71] = (byte)0x01; 
						 buff[72] = (byte)0x01; 

						 buff[80] = (byte)0x03;
						
						/* buff[84] = (byte)0xb2; // hp 
						 buff[85] = (byte)0xb2; 
						
						 buff[92] = (byte)0x45; // mana
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
				   
				   
			   }else{
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
				 
				 int bufftime = buffdata.getbufftime(seqway); 	// time  Btw: 1800 = 2 hours.
				 byte[] bufftimez = BitTools.intToByteArray(bufftime);
				 
				 int buffvalue = buffdata.getbuffvalue(seqway); // value
				 byte[] buffvaluez; 
				 if(skilldata.getskillcategory(seqway) == 2){
				 buffvaluez = BitTools.intToByteArray(buffvalue + 100);
				 }else{
				 buffvaluez = BitTools.intToByteArray(buffvalue);	 
				 }
				 
				 int buffslot = buffdata.getbuffslot(seqway); 	// buffslot
				 byte[] buffslotz = BitTools.intToByteArray(buffslot);
				 
				 Character Tplayer;
				 Tplayer = WMap.getInstance().getCharacter(BitTools.byteArrayToInt(target)); 
			     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 41){}
			     else
			     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 143){}
			     else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 137){}
				 else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 132){}
				 else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 129){}
				 else
			     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 125){}
			     else
				 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 115){}
			     else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 83){}
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 74){}
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 72){}
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 66){}
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 62){}
				 else
			     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 56){}
			     else
			     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 82){}
			     else{return;}
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
				 }else if(skilldata.getskillcategory(seqway) != 2){// if is  not % buff
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
						
						//boolean SlicenDice = this.Dice(Rate, Limit);1
						
						this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);
						if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
						if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
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
					 
				 // if this buff is category 2 which is Defence buff % which stacks
				 if(skilldata.getskillcategory(seqway) == 2){Tplayer.AddDeffpercentagebuff(buffid, buffvalue);}	 
				 Tplayer.statlist(); // refresh dat statlist of target when i buffed the shit out of him!
				 cur.sendToMap(buff);
				 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), buff);
			   }
		   } else { //< ===== Attack target player ===== >
				 //
			  if(cur.charID == BitTools.byteArrayToInt(target)){return;}
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
				 if(seqway == buffdata.getBedoonglist(seqway)){
				 pckt[24] = (byte)0x02;
				 }
				 pckt[25] = (byte)0x07;
				 
				 pckt[27] = (byte)0x01;	// how many targets
				 pckt[28] = (byte)0x01; // player 1 ( 0x01 is player | 0x02 is mob )
				 
				 
				 
				 // Player 1
				 Character Tplayer = WMap.getInstance().getCharacter(BitTools.byteArrayToInt(target));
				 
				 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.getlastknownX(), tmpmob.getlastknownY()));
			     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 41){}
			     else
			     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 143){}
			     else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 137){}
				 else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 132){}
				 else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 129){}
				 else
			     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 125){}
			     else
				 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 115){}
			     else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 83){}
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 74){}
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 72){}
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 66){}
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 62){}
				 else
			     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 56){}
			     else
			     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), Tplayer.getlastknownX(), Tplayer.getlastknownY()) < 82){}
			     else{return;}
				 
				  	// if both guild is at least grade 10 or lower
					if(Guildwar.getInstance().isguildwar()){// if guildwar is on
				}else
				 	if(cur.guild != null && Tplayer.guild != null && Tplayer.guild.guildtype != 1){ 
				 		if(cur.guild.War.containsKey(Tplayer.guild.guildname)){	
				 		}else{return;}
				}else // if both is not in guild
					if(cur.guild == null && Tplayer.guild == null){
				}else
					if(cur.Duel == 1){// if is in duel skip
				}else{
					return;	
				}
				 byte[] Tpchid = BitTools.intToByteArray(Tplayer.getCharID());
				 
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
				 
				 int newskillcritchance = skillcritchance + cur.FPCritRate;
				 if(newskillcritchance >= 3 && newskillcritchance <= 7){  
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 } 
				 if(newskillcritchance >= 8 && newskillcritchance <= 12){  
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(newskillcritchance >= 13 && newskillcritchance <= 17){ 
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(newskillcritchance >= 18 && newskillcritchance <= 22){ 
					 if (random == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
					 if (random == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
					 if (random == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 } 
				 if(newskillcritchance >= 23){ 
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
				 AFdmg = Tplayer.Calcdmgtroughdefbuffpercent(AFdmg);
				 TargetPlayerdefence = 0;
				 double Fdmg = AFdmg * Bytecritchance; // FINAL DAMAGE
				 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
				 int Fdmgzerochck = (int)Fdmg * zerocheck;
				 
				 Random greendice = new Random();	 
				 int randomgreencrit;
				 randomgreencrit = 1+greendice.nextInt(40);  // RANDOMIZER devided by 3.7% each!
				 if (randomgreencrit == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
				 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
				 double luza = (double)HidingDamage / 100;
				 double FinalDamage; 
				 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
				 else{FinalDamage = luca;}
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
				 
				 if(Tplayer.FPDefRate != 0){double substract = inc * Tplayer.FPDefRate; inc = inc - substract;}
				 //System.out.println("Attack Final DMG = "+ (int)inc);
				 if(Tplayer.FPCritAbsorb != 0 && Crit != 1 && Crit != 0){double substractt = inc * Tplayer.FPCritAbsorb; inc = inc - substractt;}
				 //System.out.println("Attack Final DMG on crit = "+ (int)inc);
				 
				 
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
				 
				 
					if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
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
						int Rate = Integer.valueOf(Passive[12]); 
						int Limit = Integer.valueOf(Passive[13]); 
						
						boolean SlicenDice = false;
						
					    if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(Tplayer.getLevel() - cur.getLevel() <= 50){
							 SlicenDice = this.Dice(Rate, Limit);	    	
						}}
					    }else
					    if(DotsIconID == 46){
					    if((int)inc > 0){	
						if(Tplayer.getLevel() - cur.getLevel() <= 50){
							 SlicenDice = this.Dice(Rate, Limit);	    	
						}}
					    }else
					 	if(DotsIconID == 49){
					 	if((int)inc > 0){	
						if(Tplayer.getLevel() - cur.getLevel() <= 50){
							 SlicenDice = this.Dice(Rate, Limit);	    	
						}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
						
						if(SlicenDice){
						if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
						else{
						this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
						if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
						if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
						}
					}
					
				   if(Crit != 0 && cur.HIDING == 1){  
					cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
				    
				    cur.HIDING = 0;
				    
				    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
				    
				     
				    }
				   
			  		if(cur.Summonid != 0){	
						Summon sum = wmap.getsummons(cur.Summonid);
						sum.Attack(Tplayer);
					}
				
				 
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
				 }else
				 if(Tplayer.Duel == 1){
					 // skip n continue
				 }else
			     if(Guildwar.getInstance().isguildwar()){// if guildwar is on
				 }else
				 // if not in the same guild and tlpayer is in a guild and his hp is below 0 and is not in a duel then gg
				 if(cur.guild != null &&  Tplayer.guild != null && Tplayer.guild != cur.guild && newhp <= 0){
						if(cur.guild != null && cur.guild.guildgold >= 500 && cur.guild.getGuildfame() >= 25000){ 
							if(Tplayer.guild != null && Tplayer.guild.guildgold >= 500 && Tplayer.guild.getGuildfame() >= 25000){	
					Tplayer.guild.guilddeaths = Tplayer.guild.guilddeaths + 1;
					cur.guild.guildkills = cur.guild.guildkills + 1;
					
					//TODO:must prevent them from swapping 
					
					//cur rating add gain
					int AddRating = this.CalcPvprating(cur.guild.getGuildpvprating());
					cur.guild.setGuildpvprating(cur.guild.getGuildpvprating() + AddRating);
					
					//tplayer rating substract half of gain
					double DecRating = this.CalcPvprating(Tplayer.guild.getGuildpvprating()) * 0.450;
					Tplayer.guild.setGuildpvprating(Tplayer.guild.getGuildpvprating() - (int)DecRating);
				    
					
					// Guild News FOR ME
					byte[] iniguild5 = new byte[212];
					iniguild5[0] = (byte)0xd4; 
					iniguild5[4] = (byte)0x04; 
					iniguild5[6] = (byte)0x67; 
					iniguild5[8] = (byte)0x01; 
					for(int a=0;a<4;a++) {iniguild5[12+a] = chid[a];}
					byte[] guildtext = cur.guild.getguildnews();
					for(int a=0;a<guildtext.length;a++) {iniguild5[16+a] = guildtext[a];}
					 
					// Guild News FOR TARGETPLAYER
					byte[] iniguild55 = new byte[212];
					iniguild55[0] = (byte)0xd4; 
					iniguild55[4] = (byte)0x04; 
					iniguild55[6] = (byte)0x67; 
					iniguild55[8] = (byte)0x01; 
					for(int a=0;a<4;a++) {iniguild55[12+a] = target[a];}
					byte[] guildtextt = Tplayer.guild.getguildnews();
					for(int a=0;a<guildtextt.length;a++) {iniguild55[16+a] = guildtextt[a];}
					
					 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), iniguild5);
					 ServerFacade.getInstance().addWriteByChannel(Tplayer.GetChannel(), iniguild55);
						 }
					 }
				 Tplayer.killedbyplayer = true;
				 }else if(newhp <= 0){Tplayer.killedbyplayer = true;}
					 
				 
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
				 
				 cur.sendToMap(pckt);
				 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt);
		   }
	}
	
	public int CalcPvprating(int PvPRating){
		int AddRating = 0;
		if(PvPRating >= 2500){AddRating = 1;}
		else
		if(PvPRating >= 2300){AddRating = 2;}
		else
		if(PvPRating >= 2200){AddRating = 3;}
		else
		if(PvPRating >= 2000){AddRating = 4;}
		else
		if(PvPRating >= 1900){AddRating = 5;}
		else
		if(PvPRating >= 1800){AddRating = 6;}
		else
		if(PvPRating >= 1700){AddRating = 7;}
		else
		if(PvPRating >= 1600){AddRating = 8;}
		else
		if(PvPRating >= 1500){AddRating = 10;}
		else
		if(PvPRating >= 1400){AddRating = 12;}
		else
	    if(PvPRating >= 1300){AddRating = 13;}
	    else
		if(PvPRating >= 1200){AddRating = 14;}
		else
		if(PvPRating >= 1100){AddRating = 15;}
		else
		if(PvPRating >= 1000){AddRating = 16;}
		else
		if(PvPRating >= 900){AddRating = 17;}
		else
		if(PvPRating >= 700){AddRating = 18;}
		else
		if(PvPRating >= 500){AddRating = 19;}
		else
		if(PvPRating >= 300){AddRating = 20;}
		else
	    if(PvPRating >= 100){AddRating = 21;}
	    else
		if(PvPRating >= 0){AddRating = 25;}
		
		return AddRating;
	}
	
	//----------------------------- Attack Target MOB -------------------------------------------\\
	public void AttackMOB1(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1 ,Connection con) {

				 Character cur = ((PlayerConnection)con).getActiveCharacter();
				 //
				 byte[] chid = BitTools.intToByteArray(cur.getCharID());
				 byte[] skid = BitTools.intToByteArray(seqway); // skill id
				 byte[] target1_ = BitTools.intToByteArray(target1);

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
				 
				 if(seqway == buffdata.getBedoonglist(seqway)){
				 pckt[24] = (byte)0x02;
				 pckt[25] = (byte)0x07;
				 }
				 
				 pckt[27] = (byte)0x01;	// how many targets
				 pckt[28] = (byte)0x02; // 0x02 = mob
				 
				// Mob 1
				 Mob TMob1 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target1_), cur.getCurrentMap()); 
				 //War
				 //40
				 //
				 //Assassin
				 //113 normal bow
				 //123 lvl 1 skill
				 //128 lvl 2
				 //131 lvl 3
				 //136 lvl 4
				 //142 lvl 5
				 //
				 //Mage
				 //51 normal
				 //61
				 //65
				 //71
				 //75
				 //81
				 //
				 //Monk
				 //81
				 Mob tmpmob = TMob1;
				 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
			     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 92){}//92
			     else
			     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 190){}//190
			     else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 186){}//186
				 else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 180){}//180
				 else
				 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 176){}//176
				 else
			     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 168){}//168
			     else
				 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 162){}//162
			     else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}//165
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 154){}//154
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 136){}//136
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 126){}//126
				 else
				 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 120){}//120
				 else
			     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 110){}//110
			     else
			     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}//165
			     else{return;}
				 
				 
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
				 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
				 double luza = (double)HidingDamage / 100;
				 double FinalDamage; 
				 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
				 else{FinalDamage = luca;}
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
				 
				 
					if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
						String e = skilleffects.getskilleffects(seqway);
						String[] Passive = e.split(",");	
						int DETERMINER = 2;//mob
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
						int Rate = Integer.valueOf(Passive[12]); 
						int Limit = Integer.valueOf(Passive[13]); 
						
						boolean SlicenDice = false;
						
					    if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
					    
						if(SlicenDice){
							if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
							else{
							this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
							if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
							if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
						}
					}
					
				   if(Crit != 0 && cur.HIDING == 1){  
					cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
				    
				    cur.HIDING = 0;
				    
				    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
				    
				     
				    }
				
				   
					  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;   if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
					  else
					  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
					  else
					  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
					  else
					  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
					  else
					  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
					  else
					  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
				 
				 Crit = 1;			 // reset to *1 ( Normal Damage )
				 greencrit = 1;		 // reset to *1 ( Normal Damage )
				 zerocheck = 1;		 // reset to *1 ( Normal Damage )
				 Bytecritchance = 1; // reset to *1 ( Normal Damage )
				 int newhp = TMob1.hp - (int)inc;
				 
				 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
				 for(int i=0;i<4;i++) {
						pckt[i+44] = finaldmg[i];						
				 }
				 
				 byte[] newhpz = BitTools.intToByteArray(newhp); 
				 byte[] newmanaz = BitTools.intToByteArray(5000); 
				 for(int i=0;i<2;i++) {
						pckt[i+40] = newhpz[i];		
						pckt[i+48] = newmanaz[i];
				 }
				 
			  		if(cur.Summonid != 0){	
						Summon sum = wmap.getsummons(cur.Summonid);
						sum.Attack(TMob1);
					}
					
			  	/*String meowisGM = cur.getChargm();   		
					// Administrator \\
				if(meowisGM.equals ("az"))
				{
					////System.out.print(" =====>Character: isgm = " + meowisGM +" : ");
					Charstuff.getInstance().respondguild("AreaTrigger: uid: "+TMob1.getUid()+" - isAlive: "+TMob1.isAlive(), cur.GetChannel());
				} */
				 TMob1.setHp(cur.charID,(int)inc, newhp);
				 inc = 0;
				 cur.sendToMap(pckt);
				 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt);
				//System.out.println("DONE");
		
				 
}		
	public void AttackMOB2(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1, int target2 ,Connection con) {	
		 Character cur = ((PlayerConnection)con).getActiveCharacter();
		 
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target1_ = BitTools.intToByteArray(target1);
		 byte[] target2_ = BitTools.intToByteArray(target2);
		 byte[] pckt1 = new byte[76];
		 pckt1[0] = (byte)0x4c;
		 pckt1[4] = (byte)0x05;
		 pckt1[6] = (byte)0x34;
		 pckt1[8] = (byte)0x01;
		 
		 
		 for(int i=0;i<4;i++) {
				pckt1[i+12] = chid[i];						// char ID
				pckt1[i+32] = target1_[i]; 
				pckt1[i+56] = target2_[i]; 		// target id SERVER
				pckt1[i+20] = skid[i]; 						// skill ID
		 }
		
		 pckt1[16] = (byte)0x01;
		 
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[24] = (byte)0x02;
		 pckt1[25] = (byte)0x07;
		 }
		 pckt1[27] = (byte)0x02;	// how many mobs ?
		 
		 pckt1[28] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;} if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;}    // mob 1
		 
			// Mob 1
		 Mob TMob1 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target1_), cur.getCurrentMap()); 
		 
		 Mob tmpmob = TMob1;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
	     else{return;}

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

		 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza = (double)HidingDamage / 100;
		 double FinalDamage; 
		 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
		 else{FinalDamage = luca;}
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
		 
		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		 
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
		 if(Crit == 0){pckt1[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[36] = (byte)0x05;} // * 2 green crit
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }

			  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp = TMob1.hp - (int)inc;
		 
		 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+44] = finaldmg[i];						
		 }

		 byte[] newhpz = BitTools.intToByteArray(newhp); 
		 byte[] newmanaz = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+40] = newhpz[i];		
			 pckt1[i+48] = newmanaz[i];
		 }
		 
		 TMob1.setHp(cur.charID,(int)inc, newhp);
		 inc = 0;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[48] = (byte)0x02;
		 pckt1[49] = (byte)0x07;
		 }
		 pckt1[52] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[48] = (byte)0x02;pckt1[49] = (byte)0x07;}   // mob 2
		
		 
		 
		 //<=== Mob 2 ===>
		 Mob TMob2 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target2_), cur.getCurrentMap()); 
		 
		 Mob tmpmob2 = TMob2;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
	     else{return;}
		
		 //===  RANDOMIZER  ===\\
		 Random dice2 = new Random();	 
		 int random2; // RANDOMIZER devided by 5% each!
		 random2 = 1+dice2.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random2 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random2 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random2 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random2 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random2 == 6) {} // just normal
		 if (random2 == 7) {} 
		 if (random2 == 8) {}
		 if (random2 == 9) {} 
		 if (random2 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random2 == 16){} 
		 if (random2 == 17){} 
		 if (random2 == 18){}
		 if (random2 == 19){} 
		 if (random2 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob2.FDD_FASR == 1 | TMob2.FDD == 1){double DEF = TMob2.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob2.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg2 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg2 = AFdmg2 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg2 <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck2 = (int)Fdmg2 * zerocheck;
		 
		 Random greendice2 = new Random();	 
		int randomgreencrit2 = 1+greendice2.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit2 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca2 = Fdmgzerochck2 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza2 = (double)HidingDamage / 100;
		 double FinalDamage2; 
		 if(luza2 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage2 = luca2 * luza2;}
		 else{FinalDamage2 = luca2;}
		 Random pdmgdice2 = new Random();	 
		 int randomdmg2 = 1+pdmgdice2.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg2 == 1){inc = FinalDamage2 * 1.050;} 
		 if (randomdmg2 == 2){inc = FinalDamage2 * 1.040;}
		 if (randomdmg2 == 3){inc = FinalDamage2 * 1.035;} 
		 if (randomdmg2 == 4){inc = FinalDamage2 * 1.025;}
		 if (randomdmg2 == 5){inc = FinalDamage2 * 1.000;} 
		 if (randomdmg2 == 6){inc = FinalDamage2 * 0.990;}
		 if (randomdmg2 == 7){inc = FinalDamage2 * 0.985;} 
		 if (randomdmg2 == 8){inc = FinalDamage2 * 0.975;}
		 if (randomdmg2 == 9){inc = FinalDamage2 * 0.960;} 
		 if (randomdmg2 == 10){inc = FinalDamage2 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob2.getMobdata().getData().getLvl()){ 
		 Random missdice2 = new Random();
		 int randommiss2 = 1+missdice2.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){inc = 0; Crit = 0;}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){}
		 }else{
		 if (randommiss2 == 6){inc = 0; Crit = 0;}
		 if (randommiss2 == 7){inc = 0; Crit = 0;}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[60] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[60] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[60] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[60] = (byte)0x05;} // * 2 green crit
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target2, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target2, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target2, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }
	
			  if(TMob2.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp2 = TMob2.hp - (int)inc;
		 
		 byte[] finaldmg2 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+68] = finaldmg2[i];						
		 }

		 
		 byte[] newhpz2 = BitTools.intToByteArray(newhp2); 
		 byte[] newmanaz2 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+64] = newhpz2[i];		
			 pckt1[i+72] = newmanaz2[i];
		 }
	
		 TMob2.setHp(cur.charID,(int)inc, newhp2);
		 inc = 0;
		 cur.sendToMap(pckt1);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt1);
	}	
	public void AttackMOB3(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1,int target2,int target3 ,Connection con) {
		 Character cur = ((PlayerConnection)con).getActiveCharacter();
		 
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target1_ = BitTools.intToByteArray(target1);
		 byte[] target2_ = BitTools.intToByteArray(target2);
		 byte[] target3_ = BitTools.intToByteArray(target3);
		 byte[] pckt1 = new byte[100];
		 pckt1[0] = (byte)0x64;
		 pckt1[4] = (byte)0x05;
		 pckt1[6] = (byte)0x34;
		 pckt1[8] = (byte)0x01;
		 
		 for(int i=0;i<4;i++) {
				pckt1[i+12] = chid[i];						// char ID
				pckt1[i+32] = target1_[i];  
				pckt1[i+56] = target2_[i]; 
				pckt1[i+80] = target3_[i];		// target id SERVER
				pckt1[i+20] = skid[i]; 						// skill ID
		 }
		
		 pckt1[16] = (byte)0x01;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[24] = (byte)0x02;
		 pckt1[25] = (byte)0x07;
		 }
		 pckt1[27] = (byte)0x03;  // how many mobs ?
		 
		 pckt1[28] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;} if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;}  // mob 1
		 
			// Mob 1
		 Mob TMob1 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target1_), cur.getCurrentMap()); 
		 
		 Mob tmpmob = TMob1;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
	     else{return;}

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

		 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza = (double)HidingDamage / 100;
		 double FinalDamage; 
		 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
		 else{FinalDamage = luca;}
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

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
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
		 if(Crit == 0){pckt1[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[36] = (byte)0x05;} // * 2 green crit
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }

			  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp = TMob1.hp - (int)inc;
		 
		 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+44] = finaldmg[i];						
		 }
	
		 byte[] newhpz = BitTools.intToByteArray(newhp); 
		 byte[] newmanaz = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+40] = newhpz[i];		
			 pckt1[i+48] = newmanaz[i];
		 }
		 
		 TMob1.setHp(cur.charID,(int)inc, newhp);
		 inc = 0;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[48] = (byte)0x02;
		 pckt1[49] = (byte)0x07;
		 }
		 pckt1[52] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[48] = (byte)0x02;pckt1[49] = (byte)0x07;}   // mob 2
		
		 
		 
		 //<=== Mob 2 ===>
		 Mob TMob2 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target2_), cur.getCurrentMap());
		 
		 Mob tmpmob2 = TMob2;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
	     else{return;}

		 //===  RANDOMIZER  ===\\
		 Random dice2 = new Random();	 
		 int random2; // RANDOMIZER devided by 5% each!
		 random2 = 1+dice2.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random2 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random2 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random2 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random2 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random2 == 6) {} // just normal
		 if (random2 == 7) {} 
		 if (random2 == 8) {}
		 if (random2 == 9) {} 
		 if (random2 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random2 == 16){} 
		 if (random2 == 17){} 
		 if (random2 == 18){}
		 if (random2 == 19){} 
		 if (random2 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob2.FDD_FASR == 1 | TMob2.FDD == 1){double DEF = TMob2.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob2.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg2 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg2 = AFdmg2 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck2 = (int)Fdmg2 * zerocheck;
		 
		 Random greendice2 = new Random();	 
		int randomgreencrit2 = 1+greendice2.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit2 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca2 = Fdmgzerochck2 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza2 = (double)HidingDamage / 100;
		 double FinalDamage2; 
		 if(luza2 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage2 = luca2 * luza2;}
		 else{FinalDamage2 = luca2;}
		 Random pdmgdice2 = new Random();	 
		 int randomdmg2 = 1+pdmgdice2.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg2 == 1){inc = FinalDamage2 * 1.050;} 
		 if (randomdmg2 == 2){inc = FinalDamage2 * 1.040;}
		 if (randomdmg2 == 3){inc = FinalDamage2 * 1.035;} 
		 if (randomdmg2 == 4){inc = FinalDamage2 * 1.025;}
		 if (randomdmg2 == 5){inc = FinalDamage2 * 1.000;} 
		 if (randomdmg2 == 6){inc = FinalDamage2 * 0.990;}
		 if (randomdmg2 == 7){inc = FinalDamage2 * 0.985;} 
		 if (randomdmg2 == 8){inc = FinalDamage2 * 0.975;}
		 if (randomdmg2 == 9){inc = FinalDamage2 * 0.960;} 
		 if (randomdmg2 == 10){inc = FinalDamage2 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob2.getMobdata().getData().getLvl()){ 
		 Random missdice2 = new Random();
		 int randommiss2 = 1+missdice2.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){inc = 0; Crit = 0;}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){}
		 }else{
		 if (randommiss2 == 6){inc = 0; Crit = 0;}
		 if (randommiss2 == 7){inc = 0; Crit = 0;}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[60] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[60] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[60] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[60] = (byte)0x05;} // * 2 green crit
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target2, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target2, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target2, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }

			  if(TMob2.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob2.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp2 = TMob2.hp - (int)inc;
		 
		 byte[] finaldmg2 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+68] = finaldmg2[i];						
		 }
		
		 byte[] newhpz2 = BitTools.intToByteArray(newhp2); 
		 byte[] newmanaz2 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+64] = newhpz2[i];		
			 pckt1[i+72] = newmanaz2[i];
		 }
		 
		 TMob2.setHp(cur.charID,(int)inc, newhp2);
		 inc = 0;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[72] = (byte)0x02;
		 pckt1[73] = (byte)0x07;
		 }
		 pckt1[76] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[72] = (byte)0x02;pckt1[73] = (byte)0x07;} // mob 3
		 
		 //<=== Mob 3 ===>
		 Mob TMob3 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target3_), cur.getCurrentMap());
		 Mob tmpmob3 = TMob3;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
	     else{return;}

		 //===  RANDOMIZER  ===\\
		 Random dice3 = new Random();	 
		 int random3; // RANDOMIZER devided by 5% each!
		 random3 = 1+dice3.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random3 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random3 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random3 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random3 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random3 == 6) {} // just normal
		 if (random3 == 7) {} 
		 if (random3 == 8) {}
		 if (random3 == 9) {} 
		 if (random3 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random3 == 16){} 
		 if (random3 == 17){} 
		 if (random3 == 18){}
		 if (random3 == 19){} 
		 if (random3 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob3.FDD_FASR == 1 | TMob3.FDD == 1){double DEF = TMob3.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob3.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg3 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg3 = AFdmg3 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck3 = (int)Fdmg3 * zerocheck;
		 
		 Random greendice3 = new Random();	 
		int randomgreencrit3 = 1+greendice3.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit3 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca3 = Fdmgzerochck3 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza3 = (double)HidingDamage / 100;
		 double FinalDamage3; 
		 if(luza3 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage3 = luca3 * luza3;}
		 else{FinalDamage3 = luca3;}
		 Random pdmgdice3 = new Random();	 
		 int randomdmg3 = 1+pdmgdice3.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg3 == 1){inc = FinalDamage3 * 1.050;} 
		 if (randomdmg3 == 2){inc = FinalDamage3 * 1.040;}
		 if (randomdmg3 == 3){inc = FinalDamage3 * 1.035;} 
		 if (randomdmg3 == 4){inc = FinalDamage3 * 1.025;}
		 if (randomdmg3 == 5){inc = FinalDamage3 * 1.000;} 
		 if (randomdmg3 == 6){inc = FinalDamage3 * 0.990;}
		 if (randomdmg3 == 7){inc = FinalDamage3 * 0.985;} 
		 if (randomdmg3 == 8){inc = FinalDamage3 * 0.975;}
		 if (randomdmg3 == 9){inc = FinalDamage3 * 0.960;} 
		 if (randomdmg3 == 10){inc = FinalDamage3 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		 
		 if(cur.getLevel() < TMob3.getMobdata().getData().getLvl()){ 
		 Random missdice3 = new Random();
		 int randommiss3 = 1+missdice3.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){inc = 0; Crit = 0;}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){}
		 }else{
		 if (randommiss3 == 6){inc = 0; Crit = 0;}
		 if (randommiss3 == 7){inc = 0; Crit = 0;}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[84] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[84] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[84] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[84] = (byte)0x05;} // * 2 green crit
		 
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	} 
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target3, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target3, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target3, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }
	
			  if(TMob3.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob3.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob3.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob3.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob3.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob3.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp3 = TMob3.hp - (int)inc;
		 
		 byte[] finaldmg3 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+92] = finaldmg3[i];						
		 }

		 byte[] newhpz3 = BitTools.intToByteArray(newhp3); 
		 byte[] newmanaz3 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+88] = newhpz3[i];		
			 pckt1[i+96] = newmanaz3[i];
		 }
		 
		 TMob3.setHp(cur.charID,(int)inc, newhp3);
		 inc = 0;
		 cur.sendToMap(pckt1);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt1);
	}	
	public void AttackMOB4(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1,int target2,int target3,int target4 ,Connection con) {
		 Character cur = ((PlayerConnection)con).getActiveCharacter();
		 
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target1_ = BitTools.intToByteArray(target1);
		 byte[] target2_ = BitTools.intToByteArray(target2);
		 byte[] target3_ = BitTools.intToByteArray(target3);
		 byte[] target4_ = BitTools.intToByteArray(target4);
		 byte[] pckt1 = new byte[124];
		 pckt1[0] = (byte)0x7c;
		 pckt1[4] = (byte)0x05;
		 pckt1[6] = (byte)0x34;
		 pckt1[8] = (byte)0x01;
		 
		 for(int i=0;i<4;i++) {
				pckt1[i+12] = chid[i];						// char ID
				pckt1[i+32] = target1_[i];  
				pckt1[i+56] = target2_[i];
				pckt1[i+80] = target3_[i]; 
				pckt1[i+104] = target4_[i]; // target id SERVER
				pckt1[i+20] = skid[i]; 						// skill ID
		 }
		
		 pckt1[16] = (byte)0x01;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[24] = (byte)0x02;
		 pckt1[25] = (byte)0x07;
		 }
		 pckt1[27] = (byte)0x04;  // how many mobs ?
		 
		 pckt1[28] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;} if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;}  // mob 1
		 
			// Mob 1
		 Mob TMob1 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target1_), cur.getCurrentMap()); 
		 Mob tmpmob = TMob1;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
	     else{return;}
		 
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
		 
		 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza = (double)HidingDamage / 100;
		 double FinalDamage; 
		 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
		 else{FinalDamage = luca;}
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

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
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
		 if(Crit == 0){pckt1[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[36] = (byte)0x05;} // * 2 green crit
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }

			  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp = TMob1.hp - (int)inc;
		 
		 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+44] = finaldmg[i];						
		 }
	
		 byte[] newhpz = BitTools.intToByteArray(newhp); 
		 byte[] newmanaz = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+40] = newhpz[i];		
			 pckt1[i+48] = newmanaz[i];
		 }
		 
		 TMob1.setHp(cur.charID,(int)inc, newhp);
		 inc = 0;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[48] = (byte)0x02;
		 pckt1[49] = (byte)0x07;
		 }
		 pckt1[52] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[48] = (byte)0x02;pckt1[49] = (byte)0x07;}   // mob 2
		
		 
		 
		 //<=== Mob 2 ===>
		 Mob TMob2 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target2_), cur.getCurrentMap()); 
		 
		 Mob tmpmob2 = TMob2;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
	     else{return;}
	
		 //===  RANDOMIZER  ===\\
		 Random dice2 = new Random();	 
		 int random2; // RANDOMIZER devided by 5% each!
		 random2 = 1+dice2.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random2 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random2 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random2 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random2 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random2 == 6) {} // just normal
		 if (random2 == 7) {} 
		 if (random2 == 8) {}
		 if (random2 == 9) {} 
		 if (random2 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random2 == 16){} 
		 if (random2 == 17){} 
		 if (random2 == 18){}
		 if (random2 == 19){} 
		 if (random2 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob2.FDD_FASR == 1 | TMob2.FDD == 1){double DEF = TMob2.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob2.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg2 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg2 = AFdmg2 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck2 = (int)Fdmg2 * zerocheck;
		 
		 Random greendice2 = new Random();	 
		int randomgreencrit2 = 1+greendice2.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit2 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca2 = Fdmgzerochck2 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza2 = (double)HidingDamage / 100;
		 double FinalDamage2; 
		 if(luza2 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage2 = luca2 * luza2;}
		 else{FinalDamage2 = luca2;}
		 Random pdmgdice2 = new Random();	 
		 int randomdmg2 = 1+pdmgdice2.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg2 == 1){inc = FinalDamage2 * 1.050;} 
		 if (randomdmg2 == 2){inc = FinalDamage2 * 1.040;}
		 if (randomdmg2 == 3){inc = FinalDamage2 * 1.035;} 
		 if (randomdmg2 == 4){inc = FinalDamage2 * 1.025;}
		 if (randomdmg2 == 5){inc = FinalDamage2 * 1.000;} 
		 if (randomdmg2 == 6){inc = FinalDamage2 * 0.990;}
		 if (randomdmg2 == 7){inc = FinalDamage2 * 0.985;} 
		 if (randomdmg2 == 8){inc = FinalDamage2 * 0.975;}
		 if (randomdmg2 == 9){inc = FinalDamage2 * 0.960;} 
		 if (randomdmg2 == 10){inc = FinalDamage2 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob2.getMobdata().getData().getLvl()){ 
		 Random missdice4 = new Random();
		 int randommiss4 = 1+missdice4.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){inc = 0; Crit = 0;}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){}
		 }else{
		 if (randommiss4 == 6){inc = 0; Crit = 0;}
		 if (randommiss4 == 7){inc = 0; Crit = 0;}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[60] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[60] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[60] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[60] = (byte)0x05;} // * 2 green crit
	
		  if(TMob2.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp2 = TMob2.hp - (int)inc;
		 
		 byte[] finaldmg2 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+68] = finaldmg2[i];						
		 }
	
		 byte[] newhpz2 = BitTools.intToByteArray(newhp2); 
		 byte[] newmanaz2 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+64] = newhpz2[i];		
			 pckt1[i+72] = newmanaz2[i];
		 }
		 
		 TMob2.setHp(cur.charID,(int)inc, newhp2);
		 inc = 0;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[72] = (byte)0x02;
		 pckt1[73] = (byte)0x07;
		 }
		 pckt1[76] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[72] = (byte)0x02;pckt1[73] = (byte)0x07;} // mob 3
		 
		 //<=== Mob 3 ===>
		 Mob TMob3 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target3_), cur.getCurrentMap()); 
		 
		 Mob tmpmob3 = TMob3;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
	     else{return;}
		
		 //===  RANDOMIZER  ===\\
		 Random dice3 = new Random();	 
		 int random3; // RANDOMIZER devided by 5% each!
		 random3 = 1+dice3.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random3 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random3 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random3 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random3 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random3 == 6) {} // just normal
		 if (random3 == 7) {} 
		 if (random3 == 8) {}
		 if (random3 == 9) {} 
		 if (random3 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random3 == 16){} 
		 if (random3 == 17){} 
		 if (random3 == 18){}
		 if (random3 == 19){} 
		 if (random3 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob3.FDD_FASR == 1 | TMob3.FDD == 1){double DEF = TMob3.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob3.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg3 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg3 = AFdmg3 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck3 = (int)Fdmg3 * zerocheck;
		 
		 Random greendice3 = new Random();	 
		int randomgreencrit3 = 1+greendice3.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit3 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca3 = Fdmgzerochck3 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza3 = (double)HidingDamage / 100;
		 double FinalDamage3; 
		 if(luza3 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage3 = luca3 * luza3;}
		 else{FinalDamage3 = luca3;}
		 Random pdmgdice3 = new Random();	 
		 int randomdmg3 = 1+pdmgdice3.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg3 == 1){inc = FinalDamage3 * 1.050;} 
		 if (randomdmg3 == 2){inc = FinalDamage3 * 1.040;}
		 if (randomdmg3 == 3){inc = FinalDamage3 * 1.035;} 
		 if (randomdmg3 == 4){inc = FinalDamage3 * 1.025;}
		 if (randomdmg3 == 5){inc = FinalDamage3 * 1.000;} 
		 if (randomdmg3 == 6){inc = FinalDamage3 * 0.990;}
		 if (randomdmg3 == 7){inc = FinalDamage3 * 0.985;} 
		 if (randomdmg3 == 8){inc = FinalDamage3 * 0.975;}
		 if (randomdmg3 == 9){inc = FinalDamage3 * 0.960;} 
		 if (randomdmg3 == 10){inc = FinalDamage3 *0.950;} 

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob3.getMobdata().getData().getLvl()){ 
		 Random missdice3 = new Random();
		 int randommiss3 = 1+missdice3.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){inc = 0; Crit = 0;}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){}
		 }else{
		 if (randommiss3 == 6){inc = 0; Crit = 0;}
		 if (randommiss3 == 7){inc = 0; Crit = 0;}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[84] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[84] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[84] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[84] = (byte)0x05;} // * 2 green crit

		  if(TMob3.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp3 = TMob3.hp - (int)inc;
		 
		 byte[] finaldmg3 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+92] = finaldmg3[i];						
		 }

		 byte[] newhpz3 = BitTools.intToByteArray(newhp3); 
		 byte[] newmanaz3 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+88] = newhpz3[i];		
			 pckt1[i+96] = newmanaz3[i];
		 }
		 

		 TMob3.setHp(cur.charID,(int)inc, newhp3);
		 inc = 0;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[96] = (byte)0x02;
		 pckt1[97] = (byte)0x07;
		 }
		 pckt1[100] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[96] = (byte)0x02;pckt1[97] = (byte)0x07;} // mob 4
		 
		 //<=== Mob 4 ===>
		 Mob TMob4 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target4_), cur.getCurrentMap()); 
		 
		 Mob tmpmob4 = TMob4;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
	     else{return;}

		 //===  RANDOMIZER  ===\\
		 Random dice4 = new Random();	  // RANDOMIZER devided by 5% each!
		int random4 = 1+dice4.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random4 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random4 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random4 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random4 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random4 == 6) {} // just normal
		 if (random4 == 7) {} 
		 if (random4 == 8) {}
		 if (random4 == 9) {} 
		 if (random4 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if (random4 == 16){} 
		 if (random4 == 17){} 
		 if (random4 == 18){}
		 if (random4 == 19){} 
		 if (random4 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob4.FDD_FASR == 1 | TMob4.FDD == 1){double DEF = TMob4.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob4.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg4 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg4 = AFdmg4 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck4 = (int)Fdmg4 * zerocheck;
		 
		 Random greendice4 = new Random();	 
		 int randomgreencrit4 = 1+greendice4.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit4 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca4 = Fdmgzerochck4 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza4 = (double)HidingDamage / 100;
		 double FinalDamage4; 
		 if(luza4 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage4 = luca4 * luza4;}
		 else{FinalDamage4 = luca4;}
		 Random pdmgdice4 = new Random();	 
		 int randomdmg4 = 1+pdmgdice4.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg4 == 1){inc = FinalDamage4 * 1.050;} 
		 if (randomdmg4 == 2){inc = FinalDamage4 * 1.040;}
		 if (randomdmg4 == 3){inc = FinalDamage4 * 1.035;} 
		 if (randomdmg4 == 4){inc = FinalDamage4 * 1.025;}
		 if (randomdmg4 == 5){inc = FinalDamage4 * 1.000;} 
		 if (randomdmg4 == 6){inc = FinalDamage4 * 0.990;}
		 if (randomdmg4 == 7){inc = FinalDamage4 * 0.985;} 
		 if (randomdmg4 == 8){inc = FinalDamage4 * 0.975;}
		 if (randomdmg4 == 9){inc = FinalDamage4 * 0.960;} 
		 if (randomdmg4 == 10){inc = FinalDamage4 *0.950;}
		 

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob4.getMobdata().getData().getLvl()){ 
		 Random missdice5 = new Random();
		 int randommiss5 = 1+missdice5.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){inc = 0; Crit = 0;}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){}
		 }else{
		 if (randommiss5 == 6){inc = 0; Crit = 0;}
		 if (randommiss5 == 7){inc = 0; Crit = 0;}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[108] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[108] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[108] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[108] = (byte)0x05;} // * 2 green crit

		  if(TMob4.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp4 = TMob4.hp - (int)inc;
		 
		 byte[] finaldmg4 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+116] = finaldmg4[i];						
		 }

		 byte[] newhpz4 = BitTools.intToByteArray(newhp4); 
		 byte[] newmanaz4 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+112] = newhpz4[i];		
			 pckt1[i+120] = newmanaz4[i];
		 }
 
		 TMob4.setHp(cur.charID,(int)inc, newhp4);
		 inc = 0;
		 cur.sendToMap(pckt1);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt1);
	}	
	public void AttackMOB5(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1,int target2,int target3,int target4,int target5 ,Connection con) {	
		 Character cur = ((PlayerConnection)con).getActiveCharacter();
		 
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target1_ = BitTools.intToByteArray(target1);
		 byte[] target2_ = BitTools.intToByteArray(target2);
		 byte[] target3_ = BitTools.intToByteArray(target3);
		 byte[] target4_ = BitTools.intToByteArray(target4);
		 byte[] target5_ = BitTools.intToByteArray(target5);
		 byte[] pckt1 = new byte[148];
		 pckt1[0] = (byte)0x94;
		 pckt1[4] = (byte)0x05;
		 pckt1[6] = (byte)0x34;
		 pckt1[8] = (byte)0x01;
		 
		 for(int i=0;i<4;i++) {
				pckt1[i+12] = chid[i];						// char ID
				pckt1[i+32] = target1_[i];
				pckt1[i+56] = target2_[i];
				pckt1[i+80] = target3_[i];
				pckt1[i+104] = target4_[i];
				pckt1[i+128] = target5_[i]; // target id SERVER
				pckt1[i+20] = skid[i]; 						// skill ID
		 }
		
		 pckt1[16] = (byte)0x01;
		 if(seqway == buffdata.getBedoonglist(seqway)){
		 pckt1[24] = (byte)0x02;
		 pckt1[25] = (byte)0x07;
		 }
		 pckt1[27] = (byte)0x05;  // how many mobs ?
		 
		 pckt1[28] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;} if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;}  // mob 1
		 
			// Mob 1
		 Mob TMob1 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target1_), cur.getCurrentMap()); 
		 Mob tmpmob = TMob1;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
	     else{return;}
	
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
		 
		 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza = (double)HidingDamage / 100;
		 double FinalDamage; 
		 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
		 else{FinalDamage = luca;}
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

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 
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
		 if(Crit == 0){pckt1[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[36] = (byte)0x05;} // * 2 green crit
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }
	
			  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp = TMob1.hp - (int)inc;
		 
		 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+44] = finaldmg[i];						
		 }
		 
		 byte[] newhpz = BitTools.intToByteArray(newhp); 
		 byte[] newmanaz = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+40] = newhpz[i];		
			 pckt1[i+48] = newmanaz[i];
		 }

		 TMob1.setHp(cur.charID,(int)inc, newhp);
		 inc = 0;
		 pckt1[52] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[48] = (byte)0x02;pckt1[49] = (byte)0x07;}   // mob 2
		
		 
		 
		 //<=== Mob 2 ===>
		 Mob TMob2 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target2_), cur.getCurrentMap());
		 Mob tmpmob2 = TMob2;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
	     else{return;}
		
		 //===  RANDOMIZER  ===\\
		 Random dice2 = new Random();	 
		 int random2; // RANDOMIZER devided by 5% each!
		 random2 = 1+dice2.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random2 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random2 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random2 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random2 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random2 == 6) {} // just normal
		 if (random2 == 7) {} 
		 if (random2 == 8) {}
		 if (random2 == 9) {} 
		 if (random2 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random2 == 16){} 
		 if (random2 == 17){} 
		 if (random2 == 18){}
		 if (random2 == 19){} 
		 if (random2 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob2.FDD_FASR == 1 | TMob2.FDD == 1){double DEF = TMob2.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob2.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg2 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg2 = AFdmg2 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck2 = (int)Fdmg2 * zerocheck;
		 
		 Random greendice2 = new Random();	 
		int randomgreencrit2 = 1+greendice2.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit2 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca2 = Fdmgzerochck2 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza2 = (double)HidingDamage / 100;
		 double FinalDamage2; 
		 if(luza2 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage2 = luca2 * luza2;}
		 else{FinalDamage2 = luca2;}
		 Random pdmgdice2 = new Random();	 
		 int randomdmg2 = 1+pdmgdice2.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg2 == 1){inc = FinalDamage2 * 1.050;} 
		 if (randomdmg2 == 2){inc = FinalDamage2 * 1.040;}
		 if (randomdmg2 == 3){inc = FinalDamage2 * 1.035;} 
		 if (randomdmg2 == 4){inc = FinalDamage2 * 1.025;}
		 if (randomdmg2 == 5){inc = FinalDamage2 * 1.000;} 
		 if (randomdmg2 == 6){inc = FinalDamage2 * 0.990;}
		 if (randomdmg2 == 7){inc = FinalDamage2 * 0.985;} 
		 if (randomdmg2 == 8){inc = FinalDamage2 * 0.975;}
		 if (randomdmg2 == 9){inc = FinalDamage2 * 0.960;} 
		 if (randomdmg2 == 10){inc = FinalDamage2 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob2.getMobdata().getData().getLvl()){ 
		 Random missdice6 = new Random();
		 int randommiss6 = 1+missdice6.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){inc = 0; Crit = 0;}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){}
		 }else{
		 if (randommiss6 == 6){inc = 0; Crit = 0;}
		 if (randommiss6 == 7){inc = 0; Crit = 0;}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[60] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[60] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[60] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[60] = (byte)0x05;} // * 2 green crit
	
		  if(TMob2.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp2 = TMob2.hp - (int)inc;
		 
		 byte[] finaldmg2 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+68] = finaldmg2[i];						
		 }
		 
		 byte[] newhpz2 = BitTools.intToByteArray(newhp2); 
		 byte[] newmanaz2 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+64] = newhpz2[i];		
			 pckt1[i+72] = newmanaz2[i];
		 }
		
		
		 TMob2.setHp(cur.charID,(int)inc, newhp2);
		 inc = 0;
		 pckt1[76] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[72] = (byte)0x02;pckt1[73] = (byte)0x07;} // mob 3
		 
		 //<=== Mob 3 ===>
		 Mob TMob3 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target3_), cur.getCurrentMap()); 
		 Mob tmpmob3 = TMob3;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
	     else{return;}
	
		 //===  RANDOMIZER  ===\\
		 Random dice3 = new Random();	 
		 int random3; // RANDOMIZER devided by 5% each!
		 random3 = 1+dice3.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random3 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random3 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random3 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random3 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random3 == 6) {} // just normal
		 if (random3 == 7) {} 
		 if (random3 == 8) {}
		 if (random3 == 9) {} 
		 if (random3 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random3 == 16){} 
		 if (random3 == 17){} 
		 if (random3 == 18){}
		 if (random3 == 19){} 
		 if (random3 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob3.FDD_FASR == 1 | TMob3.FDD == 1){double DEF = TMob3.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob3.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg3 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg3 = AFdmg3 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck3 = (int)Fdmg3 * zerocheck;
		 
		 Random greendice3 = new Random();	 
		int randomgreencrit3 = 1+greendice3.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit3 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca3 = Fdmgzerochck3 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza3 = (double)HidingDamage / 100;
		 double FinalDamage3; 
		 if(luza3 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage3 = luca3 * luza3;}
		 else{FinalDamage3 = luca3;}
		 Random pdmgdice3 = new Random();	 
		 int randomdmg3 = 1+pdmgdice3.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg3 == 1){inc = FinalDamage3 * 1.050;} 
		 if (randomdmg3 == 2){inc = FinalDamage3 * 1.040;}
		 if (randomdmg3 == 3){inc = FinalDamage3 * 1.035;} 
		 if (randomdmg3 == 4){inc = FinalDamage3 * 1.025;}
		 if (randomdmg3 == 5){inc = FinalDamage3 * 1.000;} 
		 if (randomdmg3 == 6){inc = FinalDamage3 * 0.990;}
		 if (randomdmg3 == 7){inc = FinalDamage3 * 0.985;} 
		 if (randomdmg3 == 8){inc = FinalDamage3 * 0.975;}
		 if (randomdmg3 == 9){inc = FinalDamage3 * 0.960;} 
		 if (randomdmg3 == 10){inc = FinalDamage3 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob3.getMobdata().getData().getLvl()){ 
		 Random missdice3 = new Random();
		 int randommiss3 = 1+missdice3.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){inc = 0; Crit = 0;}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){}
		 }else{
		 if (randommiss3 == 6){inc = 0; Crit = 0;}
		 if (randommiss3 == 7){inc = 0; Crit = 0;}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[84] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[84] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[84] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[84] = (byte)0x05;} // * 2 green crit

		  if(TMob3.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp3 = TMob3.hp - (int)inc;
		 
		 byte[] finaldmg3 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+92] = finaldmg3[i];						
		 }

		 byte[] newhpz3 = BitTools.intToByteArray(newhp3); 
		 byte[] newmanaz3 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+88] = newhpz3[i];		
			 pckt1[i+96] = newmanaz3[i];
		 }
		 
		
		 TMob3.setHp(cur.charID,(int)inc, newhp3);
		 inc = 0;
		 pckt1[100] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[96] = (byte)0x02;pckt1[97] = (byte)0x07;} // mob 4
		 
		 //<=== Mob 4 ===>
		 Mob TMob4 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target4_), cur.getCurrentMap()); 
		 Mob tmpmob4 = TMob4;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
	     else{return;}
	
		 //===  RANDOMIZER  ===\\
		 Random dice4 = new Random();	  // RANDOMIZER devided by 5% each!
		int random4 = 1+dice4.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random4 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random4 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random4 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random4 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random4 == 6) {} // just normal
		 if (random4 == 7) {} 
		 if (random4 == 8) {}
		 if (random4 == 9) {} 
		 if (random4 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random4 == 16){} 
		 if (random4 == 17){} 
		 if (random4 == 18){}
		 if (random4 == 19){} 
		 if (random4 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob4.FDD_FASR == 1 | TMob4.FDD == 1){double DEF = TMob4.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob4.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg4 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg4 = AFdmg4 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck4 = (int)Fdmg4 * zerocheck;
		 
		 Random greendice4 = new Random();	 
		int randomgreencrit4 = 1+greendice4.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit4 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca4 = Fdmgzerochck4 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza4 = (double)HidingDamage / 100;
		 double FinalDamage4; 
		 if(luza4 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage4 = luca4 * luza4;}
		 else{FinalDamage4 = luca4;}
		 Random pdmgdice4 = new Random();	 
		 int randomdmg4 = 1+pdmgdice4.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg4 == 1){inc = FinalDamage4 * 1.050;} 
		 if (randomdmg4 == 2){inc = FinalDamage4 * 1.040;}
		 if (randomdmg4 == 3){inc = FinalDamage4 * 1.035;} 
		 if (randomdmg4 == 4){inc = FinalDamage4 * 1.025;}
		 if (randomdmg4 == 5){inc = FinalDamage4 * 1.000;} 
		 if (randomdmg4 == 6){inc = FinalDamage4 * 0.990;}
		 if (randomdmg4 == 7){inc = FinalDamage4 * 0.985;} 
		 if (randomdmg4 == 8){inc = FinalDamage4 * 0.975;}
		 if (randomdmg4 == 9){inc = FinalDamage4 * 0.960;} 
		 if (randomdmg4 == 10){inc = FinalDamage4 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 
		 if(cur.getLevel() < TMob4.getMobdata().getData().getLvl()){ 
		 Random missdice4 = new Random();
		 int randommiss4 = 1+missdice4.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){inc = 0; Crit = 0;}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){}
		 }else{
		 if (randommiss4 == 6){inc = 0; Crit = 0;}
		 if (randommiss4 == 7){inc = 0; Crit = 0;}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[108] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[108] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[108] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[108] = (byte)0x05;} // * 2 green crit
	
		  if(TMob4.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp4 = TMob4.hp - (int)inc;
		 
		 byte[] finaldmg4 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+116] = finaldmg4[i];						
		 }
		 
		 byte[] newhpz4 = BitTools.intToByteArray(newhp4); 
		 byte[] newmanaz4 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+112] = newhpz4[i];		
			 pckt1[i+120] = newmanaz4[i];
		 }
		 
		 TMob4.setHp(cur.charID,(int)inc, newhp4);
		 inc = 0;
		 pckt1[124] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[120] = (byte)0x02;pckt1[121] = (byte)0x07;} // mob 5
		 
		 //<=== Mob 5 ===>
		 Mob TMob5 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target5_), cur.getCurrentMap()); 
		 Mob tmpmob5 = TMob5;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 165){}
	     else{return;}
	
		 //===  RANDOMIZER  ===\\
		 Random dice5 = new Random();	  // RANDOMIZER devided by 5% each!
		int random5 = 1+dice5.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random5 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random5 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random5 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random5 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random5 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random5 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random5 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random5 == 6) {} // just normal
		 if (random5 == 7) {} 
		 if (random5 == 8) {}
		 if (random5 == 9) {} 
		 if (random5 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random5 == 16){} 
		 if (random5 == 17){} 
		 if (random5 == 18){}
		 if (random5 == 19){} 
		 if (random5 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob5.FDD_FASR == 1 | TMob5.FDD == 1){double DEF = TMob5.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob5.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg5 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg5 = AFdmg5 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck5 = (int)Fdmg5 * zerocheck;
		 
		 Random greendice5 = new Random();	 
		int randomgreencrit5 = 1+greendice5.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit5 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca5 = Fdmgzerochck5 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza5 = (double)HidingDamage / 100;
		 double FinalDamage5; 
		 if(luza5 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage5 = luca5 * luza5;}
		 else{FinalDamage5 = luca5;}
		 Random pdmgdice5 = new Random();	 
		 int randomdmg5 = 1+pdmgdice5.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg5 == 1){inc = FinalDamage5 * 1.050;} 
		 if (randomdmg5 == 2){inc = FinalDamage5 * 1.040;}
		 if (randomdmg5 == 3){inc = FinalDamage5 * 1.035;} 
		 if (randomdmg5 == 4){inc = FinalDamage5 * 1.025;}
		 if (randomdmg5 == 5){inc = FinalDamage5 * 1.000;} 
		 if (randomdmg5 == 6){inc = FinalDamage5 * 0.990;}
		 if (randomdmg5 == 7){inc = FinalDamage5 * 0.985;} 
		 if (randomdmg5 == 8){inc = FinalDamage5 * 0.975;}
		 if (randomdmg5 == 9){inc = FinalDamage5 * 0.960;} 
		 if (randomdmg5 == 10){inc = FinalDamage5 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob5.getMobdata().getData().getLvl()){ 
		 Random missdice5 = new Random();
		 int randommiss5 = 1+missdice5.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){inc = 0; Crit = 0;}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){}
		 }else{
		 if (randommiss5 == 6){inc = 0; Crit = 0;}
		 if (randommiss5 == 7){inc = 0; Crit = 0;}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[132] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[132] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[132] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[132] = (byte)0x05;} // * 2 green crit

		  if(TMob5.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp5 = TMob5.hp - (int)inc;
		 
		 byte[] finaldmg5 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+140] = finaldmg5[i];						
		 } 
		 
		 
		 byte[] newhpz5 = BitTools.intToByteArray(newhp5); 
		 byte[] newmanaz5 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+136] = newhpz5[i];		
			 pckt1[i+144] = newmanaz5[i];
		 }
		 
		 TMob5.setHp(cur.charID,(int)inc, newhp5);
		 inc = 0;
		 cur.sendToMap(pckt1);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt1);
	}	
	public void AttackMOB6(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1,int target2,int target3,int target4,int target5,int target6 ,Connection con) {	
		 Character cur = ((PlayerConnection)con).getActiveCharacter();
		 
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target1_ = BitTools.intToByteArray(target1);
		 byte[] target2_ = BitTools.intToByteArray(target2);
		 byte[] target3_ = BitTools.intToByteArray(target3);
		 byte[] target4_ = BitTools.intToByteArray(target4);
		 byte[] target5_ = BitTools.intToByteArray(target5);
		 byte[] target6_ = BitTools.intToByteArray(target6);
		 
		 byte[] pckt1 = new byte[172];
		 pckt1[0] = (byte)0xac;
		 pckt1[4] = (byte)0x05;
		 pckt1[6] = (byte)0x34;
		 pckt1[8] = (byte)0x01;
		 
		 for(int i=0;i<4;i++) {
				pckt1[i+12] = chid[i];						// char ID
				pckt1[i+32] = target1_[i];
				pckt1[i+56] = target2_[i];
				pckt1[i+80] = target3_[i];
				pckt1[i+104] = target4_[i];
				pckt1[i+128] = target5_[i];// target id SERVER
				pckt1[i+152] = target6_[i];
				pckt1[i+20] = skid[i]; 						// skill ID
		 }
		
		 pckt1[16] = (byte)0x01;

		 pckt1[27] = (byte)0x06;  // how many mobs ?
		 
		 pckt1[28] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;} if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;}  // mob 1
		 
			// Mob 1
		 Mob TMob1 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target1_), cur.getCurrentMap()); 
		 Mob tmpmob = TMob1;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
	     else{return;}
	
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
		 
		 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza = (double)HidingDamage / 100;
		 double FinalDamage; 
		 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
		 else{FinalDamage = luca;}
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
		 

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
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
		 if(Crit == 0){pckt1[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[36] = (byte)0x05;} // * 2 green crit
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }
		
			  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp = TMob1.hp - (int)inc;
		 
		 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+44] = finaldmg[i];						
		 }
		 
		 byte[] newhpz = BitTools.intToByteArray(newhp); 
		 byte[] newmanaz = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+40] = newhpz[i];		
			 pckt1[i+48] = newmanaz[i];
		 }
		 
		 TMob1.setHp(cur.charID,(int)inc, newhp);
		 inc = 0;
		 pckt1[52] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[48] = (byte)0x02;pckt1[49] = (byte)0x07;}   // mob 2
		
		 
		 
		 //<=== Mob 2 ===>
		 Mob TMob2 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target2_), cur.getCurrentMap());
		 Mob tmpmob2 = TMob2;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
	     else{return;}
	
		 //===  RANDOMIZER  ===\\
		 Random dice2 = new Random();	 
		 int random2; // RANDOMIZER devided by 5% each!
		 random2 = 1+dice2.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random2 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random2 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random2 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random2 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random2 == 6) {} // just normal
		 if (random2 == 7) {} 
		 if (random2 == 8) {}
		 if (random2 == 9) {} 
		 if (random2 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random2 == 16){} 
		 if (random2 == 17){} 
		 if (random2 == 18){}
		 if (random2 == 19){} 
		 if (random2 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob2.FDD_FASR == 1 | TMob2.FDD == 1){double DEF = TMob2.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob2.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg2 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg2 = AFdmg2 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck2 = (int)Fdmg2 * zerocheck;
		 
		 Random greendice2 = new Random();	 
		int randomgreencrit2 = 1+greendice2.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit2 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca2 = Fdmgzerochck2 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza2 = (double)HidingDamage / 100;
		 double FinalDamage2; 
		 if(luza2 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage2 = luca2 * luza2;}
		 else{FinalDamage2 = luca2;}
		 Random pdmgdice2 = new Random();	 
		 int randomdmg2 = 1+pdmgdice2.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg2 == 1){inc = FinalDamage2 * 1.050;} 
		 if (randomdmg2 == 2){inc = FinalDamage2 * 1.040;}
		 if (randomdmg2 == 3){inc = FinalDamage2 * 1.035;} 
		 if (randomdmg2 == 4){inc = FinalDamage2 * 1.025;}
		 if (randomdmg2 == 5){inc = FinalDamage2 * 1.000;} 
		 if (randomdmg2 == 6){inc = FinalDamage2 * 0.990;}
		 if (randomdmg2 == 7){inc = FinalDamage2 * 0.985;} 
		 if (randomdmg2 == 8){inc = FinalDamage2 * 0.975;}
		 if (randomdmg2 == 9){inc = FinalDamage2 * 0.960;} 
		 if (randomdmg2 == 10){inc = FinalDamage2 *0.950;}
		 

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob2.getMobdata().getData().getLvl()){ 
		 Random missdice2 = new Random();
		 int randommiss2 = 1+missdice2.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){inc = 0; Crit = 0;}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){}
		 }else{
		 if (randommiss2 == 6){inc = 0; Crit = 0;}
		 if (randommiss2 == 7){inc = 0; Crit = 0;}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[60] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[60] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[60] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[60] = (byte)0x05;} // * 2 green crit
		
		  if(TMob2.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp2 = TMob2.hp - (int)inc;
		 
		 byte[] finaldmg2 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+68] = finaldmg2[i];						
		 }
		 
		 byte[] newhpz2 = BitTools.intToByteArray(newhp2); 
		 byte[] newmanaz2 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+64] = newhpz2[i];		
			 pckt1[i+72] = newmanaz2[i];
		 }


		 TMob2.setHp(cur.charID,(int)inc, newhp2);
		 inc = 0;
		 pckt1[76] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[72] = (byte)0x02;pckt1[73] = (byte)0x07;} // mob 3
		 
		 //<=== Mob 3 ===>
		 Mob TMob3 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target3_), cur.getCurrentMap()); 
		 Mob tmpmob3 = TMob3;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
	     else{return;}
		 
		 //===  RANDOMIZER  ===\\
		 Random dice3 = new Random();	 
		 int random3; // RANDOMIZER devided by 5% each!
		 random3 = 1+dice3.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random3 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 }  
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random3 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random3 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random3 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random3 == 6) {} // just normal
		 if (random3 == 7) {} 
		 if (random3 == 8) {}
		 if (random3 == 9) {} 
		 if (random3 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random3 == 16){} 
		 if (random3 == 17){} 
		 if (random3 == 18){}
		 if (random3 == 19){} 
		 if (random3 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob3.FDD_FASR == 1 | TMob3.FDD == 1){double DEF = TMob3.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob3.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg3 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg3 = AFdmg3 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck3 = (int)Fdmg3 * zerocheck;
		 
		 Random greendice3 = new Random();	 
		int randomgreencrit3 = 1+greendice3.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit3 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca3 = Fdmgzerochck3 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza3 = (double)HidingDamage / 100;
		 double FinalDamage3; 
		 if(luza3 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage3 = luca3 * luza3;}
		 else{FinalDamage3 = luca3;}
		 Random pdmgdice3 = new Random();	 
		 int randomdmg3 = 1+pdmgdice3.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg3 == 1){inc = FinalDamage3 * 1.050;} 
		 if (randomdmg3 == 2){inc = FinalDamage3 * 1.040;}
		 if (randomdmg3 == 3){inc = FinalDamage3 * 1.035;} 
		 if (randomdmg3 == 4){inc = FinalDamage3 * 1.025;}
		 if (randomdmg3 == 5){inc = FinalDamage3 * 1.000;} 
		 if (randomdmg3 == 6){inc = FinalDamage3 * 0.990;}
		 if (randomdmg3 == 7){inc = FinalDamage3 * 0.985;} 
		 if (randomdmg3 == 8){inc = FinalDamage3 * 0.975;}
		 if (randomdmg3 == 9){inc = FinalDamage3 * 0.960;} 
		 if (randomdmg3 == 10){inc = FinalDamage3 *0.950;}
		 

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob3.getMobdata().getData().getLvl()){ 
		 Random missdice1 = new Random();
		 int randommiss1 = 1+missdice1.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){}
		 if (randommiss1 == 9){}
		 if (randommiss1 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){inc = 0; Crit = 0;}
		 if (randommiss1 == 8){inc = 0; Crit = 0;}
		 if (randommiss1 == 9){inc = 0; Crit = 0;}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){inc = 0; Crit = 0;}
		 if (randommiss1 == 9){inc = 0; Crit = 0;}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){}
		 if (randommiss1 == 9){inc = 0; Crit = 0;}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){}
		 if (randommiss1 == 9){}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){}
		 if (randommiss1 == 9){}
		 if (randommiss1 == 10){}
		 }else{
		 if (randommiss1 == 6){inc = 0; Crit = 0;}
		 if (randommiss1 == 7){inc = 0; Crit = 0;}
		 if (randommiss1 == 8){inc = 0; Crit = 0;}
		 if (randommiss1 == 9){inc = 0; Crit = 0;}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[84] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[84] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[84] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[84] = (byte)0x05;} // * 2 green crit
	
		  if(TMob3.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp3 = TMob3.hp - (int)inc;
		 
		 byte[] finaldmg3 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+92] = finaldmg3[i];						
		 }
	
		 byte[] newhpz3 = BitTools.intToByteArray(newhp3); 
		 byte[] newmanaz3 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+88] = newhpz3[i];		
			 pckt1[i+96] = newmanaz3[i];
		 }
		 

		 TMob3.setHp(cur.charID,(int)inc, newhp3);
		 inc = 0;
		 pckt1[100] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[96] = (byte)0x02;pckt1[97] = (byte)0x07;} // mob 4
		 
		 //<=== Mob 4 ===>
		 Mob TMob4 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target4_), cur.getCurrentMap()); 
		 Mob tmpmob4 = TMob4;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
	     else{return;}
		 //===  RANDOMIZER  ===\\
		 Random dice4 = new Random();	  // RANDOMIZER devided by 5% each!
		int random4 = 1+dice4.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random4 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random4 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random4 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random4 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random4 == 6) {} // just normal
		 if (random4 == 7) {} 
		 if (random4 == 8) {}
		 if (random4 == 9) {} 
		 if (random4 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random4 == 16){} 
		 if (random4 == 17){} 
		 if (random4 == 18){}
		 if (random4 == 19){} 
		 if (random4 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob4.FDD_FASR == 1 | TMob4.FDD == 1){double DEF = TMob4.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob4.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg4 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg4 = AFdmg4 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck4 = (int)Fdmg4 * zerocheck;
		 
		 Random greendice4 = new Random();	 
		int randomgreencrit4 = 1+greendice4.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit4 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca4 = Fdmgzerochck4 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza4 = (double)HidingDamage / 100;
		 double FinalDamage4; 
		 if(luza4 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage4 = luca4 * luza4;}
		 else{FinalDamage4 = luca4;}
		 Random pdmgdice4 = new Random();	 
		 int randomdmg4 = 1+pdmgdice4.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg4 == 1){inc = FinalDamage4 * 1.050;} 
		 if (randomdmg4 == 2){inc = FinalDamage4 * 1.040;}
		 if (randomdmg4 == 3){inc = FinalDamage4 * 1.035;} 
		 if (randomdmg4 == 4){inc = FinalDamage4 * 1.025;}
		 if (randomdmg4 == 5){inc = FinalDamage4 * 1.000;} 
		 if (randomdmg4 == 6){inc = FinalDamage4 * 0.990;}
		 if (randomdmg4 == 7){inc = FinalDamage4 * 0.985;} 
		 if (randomdmg4 == 8){inc = FinalDamage4 * 0.975;}
		 if (randomdmg4 == 9){inc = FinalDamage4 * 0.960;} 
		 if (randomdmg4 == 10){inc = FinalDamage4 *0.950;}
		 

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob4.getMobdata().getData().getLvl()){ 
		 Random missdice0 = new Random();
		 int randommiss0 = 1+missdice0.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss0 == 6){}
		 if (randommiss0 == 7){}
		 if (randommiss0 == 8){}
		 if (randommiss0 == 9){}
		 if (randommiss0 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss0 == 6){}
		 if (randommiss0 == 7){inc = 0; Crit = 0;}
		 if (randommiss0 == 8){inc = 0; Crit = 0;}
		 if (randommiss0 == 9){inc = 0; Crit = 0;}
		 if (randommiss0 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss0 == 6){}
		 if (randommiss0 == 7){}
		 if (randommiss0 == 8){inc = 0; Crit = 0;}
		 if (randommiss0 == 9){inc = 0; Crit = 0;}
		 if (randommiss0 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss0 == 6){}
		 if (randommiss0 == 7){}
		 if (randommiss0 == 8){}
		 if (randommiss0 == 9){inc = 0; Crit = 0;}
		 if (randommiss0 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss0 == 6){}
		 if (randommiss0 == 7){}
		 if (randommiss0 == 8){}
		 if (randommiss0 == 9){}
		 if (randommiss0 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss0 == 6){}
		 if (randommiss0 == 7){}
		 if (randommiss0 == 8){}
		 if (randommiss0 == 9){}
		 if (randommiss0 == 10){}
		 }else{
		 if (randommiss0 == 6){inc = 0; Crit = 0;}
		 if (randommiss0 == 7){inc = 0; Crit = 0;}
		 if (randommiss0 == 8){inc = 0; Crit = 0;}
		 if (randommiss0 == 9){inc = 0; Crit = 0;}
		 if (randommiss0 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[108] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[108] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[108] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[108] = (byte)0x05;} // * 2 green crit
	
		  if(TMob4.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}} 
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp4 = TMob4.hp - (int)inc;
		 
		 byte[] finaldmg4 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+116] = finaldmg4[i];						
		 }
		
		 byte[] newhpz4 = BitTools.intToByteArray(newhp4); 
		 byte[] newmanaz4 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+112] = newhpz4[i];		
			 pckt1[i+120] = newmanaz4[i];
		 }
		 
		 TMob4.setHp(cur.charID,(int)inc, newhp4);
		 inc = 0;
		 pckt1[124] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[120] = (byte)0x02;pckt1[121] = (byte)0x07;} // mob 5
		 
		 //<=== Mob 5 ===>
		 Mob TMob5 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target5_), cur.getCurrentMap()); 
		 Mob tmpmob5 = TMob5;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 165){}
	     else{return;}
		
		 //===  RANDOMIZER  ===\\
		 Random dice5 = new Random();	  // RANDOMIZER devided by 5% each!
		int random5 = 1+dice5.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random5 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random5 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random5 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random5 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random5 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random5 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random5 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random5 == 6) {} // just normal
		 if (random5 == 7) {} 
		 if (random5 == 8) {}
		 if (random5 == 9) {} 
		 if (random5 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random5 == 16){} 
		 if (random5 == 17){} 
		 if (random5 == 18){}
		 if (random5 == 19){} 
		 if (random5 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob5.FDD_FASR == 1 | TMob5.FDD == 1){double DEF = TMob5.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob5.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg5 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg5 = AFdmg5 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck5 = (int)Fdmg5 * zerocheck;
		 
		 Random greendice5 = new Random();	 
		int randomgreencrit5 = 1+greendice5.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit5 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca5 = Fdmgzerochck5 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza5 = (double)HidingDamage / 100;
		 double FinalDamage5; 
		 if(luza5 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage5 = luca5 * luza5;}
		 else{FinalDamage5 = luca5;}
		 Random pdmgdice5 = new Random();	 
		 int randomdmg5 = 1+pdmgdice5.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg5 == 1){inc = FinalDamage5 * 1.050;} 
		 if (randomdmg5 == 2){inc = FinalDamage5 * 1.040;}
		 if (randomdmg5 == 3){inc = FinalDamage5 * 1.035;} 
		 if (randomdmg5 == 4){inc = FinalDamage5 * 1.025;}
		 if (randomdmg5 == 5){inc = FinalDamage5 * 1.000;} 
		 if (randomdmg5 == 6){inc = FinalDamage5 * 0.990;}
		 if (randomdmg5 == 7){inc = FinalDamage5 * 0.985;} 
		 if (randomdmg5 == 8){inc = FinalDamage5 * 0.975;}
		 if (randomdmg5 == 9){inc = FinalDamage5 * 0.960;} 
		 if (randomdmg5 == 10){inc = FinalDamage5 *0.950;}
		 

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob5.getMobdata().getData().getLvl()){ 
		 Random missdice4 = new Random();
		 int randommiss4 = 1+missdice4.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){inc = 0; Crit = 0;}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){}
		 }else{
		 if (randommiss4 == 6){inc = 0; Crit = 0;}
		 if (randommiss4 == 7){inc = 0; Crit = 0;}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[132] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[132] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[132] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[132] = (byte)0x05;} // * 2 green crit
	
		  if(TMob5.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp5 = TMob5.hp - (int)inc;
		 
		 byte[] finaldmg5 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+140] = finaldmg5[i];						
		 } 
		
		 
		 byte[] newhpz5 = BitTools.intToByteArray(newhp5); 
		 byte[] newmanaz5 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+136] = newhpz5[i];		
			 pckt1[i+144] = newmanaz5[i];
		 }
		 
		 TMob5.setHp(cur.charID,(int)inc, newhp5);
		 inc = 0;
		 
		 pckt1[148] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[144] = (byte)0x02;pckt1[145] = (byte)0x07;} // mob 6
		 
		 //<=== Mob 6 ===>
		 Mob TMob6 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target6_), cur.getCurrentMap()); 
		 Mob tmpmob6 = TMob6;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 165){}
	     else{return;}
		
		 //===  RANDOMIZER  ===\\
		 Random dice6 = new Random();	  // RANDOMIZER devided by 5% each!
		int random6 = 1+dice6.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random6 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random6 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random6 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random6 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random6 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random6 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random6 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random6 == 6) {} // just normal
		 if (random6 == 7) {} 
		 if (random6 == 8) {}
		 if (random6 == 9) {} 
		 if (random6 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random6 == 16){} 
		 if (random6 == 17){} 
		 if (random6 == 18){}
		 if (random6 == 19){} 
		 if (random6 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob6.FDD_FASR == 1 | TMob6.FDD == 1){double DEF = TMob6.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob6.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg6 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg6 = AFdmg6 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck6 = (int)Fdmg6 * zerocheck;
		 
		 Random greendice6 = new Random();	 
		int randomgreencrit6 = 1+greendice6.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit6 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca6 = Fdmgzerochck6 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza6 = (double)HidingDamage / 100;
		 double FinalDamage6; 
		 if(luza6 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage6 = luca6 * luza6;}
		 else{FinalDamage6 = luca6;}
		 Random pdmgdice6 = new Random();	 
		 int randomdmg6 = 1+pdmgdice6.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg6 == 1){inc = FinalDamage6 * 1.050;} 
		 if (randomdmg6 == 2){inc = FinalDamage6 * 1.040;}
		 if (randomdmg6 == 3){inc = FinalDamage6 * 1.035;} 
		 if (randomdmg6 == 4){inc = FinalDamage6 * 1.025;}
		 if (randomdmg6 == 5){inc = FinalDamage6 * 1.000;} 
		 if (randomdmg6 == 6){inc = FinalDamage6 * 0.990;}
		 if (randomdmg6 == 7){inc = FinalDamage6 * 0.985;} 
		 if (randomdmg6 == 8){inc = FinalDamage6 * 0.975;}
		 if (randomdmg6 == 9){inc = FinalDamage6 * 0.960;} 
		 if (randomdmg6 == 10){inc = FinalDamage6 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 if(cur.getLevel() < TMob6.getMobdata().getData().getLvl()){ 
		 Random missdice6 = new Random();
		 int randommiss6 = 1+missdice6.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){inc = 0; Crit = 0;}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){}
		 }else{
		 if (randommiss6 == 6){inc = 0; Crit = 0;}
		 if (randommiss6 == 7){inc = 0; Crit = 0;}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }}
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[156] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[156] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[156] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[156] = (byte)0x05;} // * 2 green crit
	
		  if(TMob6.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp6 = TMob6.hp - (int)inc;
		 
		 byte[] finaldmg6 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+164] = finaldmg6[i];						
		 } 
		
		 
		 byte[] newhpz6 = BitTools.intToByteArray(newhp6); 
		 byte[] newmanaz6 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+160] = newhpz6[i];		
			 pckt1[i+168] = newmanaz6[i];
		 }

		 TMob6.setHp(cur.charID,(int)inc, newhp6);
		 inc = 0;
		 cur.sendToMap(pckt1);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt1);
	}	
	public void AttackMOB7(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1,int target2,int target3,int target4,int target5,int target6,int target7 ,Connection con) {	
		 Character cur = ((PlayerConnection)con).getActiveCharacter();
		 
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target1_ = BitTools.intToByteArray(target1);
		 byte[] target2_ = BitTools.intToByteArray(target2);
		 byte[] target3_ = BitTools.intToByteArray(target3);
		 byte[] target4_ = BitTools.intToByteArray(target4);
		 byte[] target5_ = BitTools.intToByteArray(target5);
		 byte[] target6_ = BitTools.intToByteArray(target6);
		 byte[] target7_ = BitTools.intToByteArray(target7);
		 byte[] pckt1 = new byte[196];
		 pckt1[0] = (byte)0xc4;
		 pckt1[4] = (byte)0x05;
		 pckt1[6] = (byte)0x34;
		 pckt1[8] = (byte)0x01;
		 
		 for(int i=0;i<4;i++) {
				pckt1[i+12] = chid[i];						// char ID
				pckt1[i+32] = target1_[i]; 
				pckt1[i+56] = target2_[i];
				pckt1[i+80] = target3_[i];
				pckt1[i+104] = target4_[i];
				pckt1[i+128] = target5_[i];// target id SERVER
				pckt1[i+152] = target6_[i];
				pckt1[i+176] = target7_[i];
				pckt1[i+20] = skid[i]; 						// skill ID
		 }
		
		 pckt1[16] = (byte)0x01;

		 pckt1[27] = (byte)0x07;  // how many mobs ?
		 
		 pckt1[28] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;} if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;}  // mob 1
		 
			// Mob 1
		 Mob TMob1 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target1_), cur.getCurrentMap()); 
		 Mob tmpmob = TMob1;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
	     else{return;}
		
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
		 
		 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza = (double)HidingDamage / 100;
		 double FinalDamage; 
		 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
		 else{FinalDamage = luca;}
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

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
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
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[36] = (byte)0x05;} // * 2 green crit
		
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }
		
			  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp = TMob1.hp - (int)inc;
	 
		 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+44] = finaldmg[i];						
		 }
		 
		 byte[] newhpz = BitTools.intToByteArray(newhp); 
		 byte[] newmanaz = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+40] = newhpz[i];		
			 pckt1[i+48] = newmanaz[i];
		 }

		 TMob1.setHp(cur.charID,(int)inc, newhp);
		 inc = 0;
		 pckt1[52] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[48] = (byte)0x02;pckt1[49] = (byte)0x07;}   // mob 2
		
		 
		 
		 //<=== Mob 2 ===>
		 Mob TMob2 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target2_), cur.getCurrentMap());
		 Mob tmpmob2 = TMob2;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
	     else{return;}
		
		 //===  RANDOMIZER  ===\\
		 Random dice2 = new Random();	 
		 int random2; // RANDOMIZER devided by 5% each!
		 random2 = 1+dice2.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random2 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random2 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random2 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random2 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random2 == 6) {} // just normal
		 if (random2 == 7) {} 
		 if (random2 == 8) {}
		 if (random2 == 9) {} 
		 if (random2 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random2 == 16){} 
		 if (random2 == 17){} 
		 if (random2 == 18){}
		 if (random2 == 19){} 
		 if (random2 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob2.FDD_FASR == 1 | TMob2.FDD == 1){double DEF = TMob2.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob2.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg2 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg2 = AFdmg2 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck2 = (int)Fdmg2 * zerocheck;
		 
		 Random greendice2 = new Random();	 
		int randomgreencrit2 = 1+greendice2.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit2 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca2 = Fdmgzerochck2 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza2 = (double)HidingDamage / 100;
		 double FinalDamage2; 
		 if(luza2 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage2 = luca2 * luza2;}
		 else{FinalDamage2 = luca2;}
		 Random pdmgdice2 = new Random();	 
		 int randomdmg2 = 1+pdmgdice2.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg2 == 1){inc = FinalDamage2 * 1.050;} 
		 if (randomdmg2 == 2){inc = FinalDamage2 * 1.040;}
		 if (randomdmg2 == 3){inc = FinalDamage2 * 1.035;} 
		 if (randomdmg2 == 4){inc = FinalDamage2 * 1.025;}
		 if (randomdmg2 == 5){inc = FinalDamage2 * 1.000;} 
		 if (randomdmg2 == 6){inc = FinalDamage2 * 0.990;}
		 if (randomdmg2 == 7){inc = FinalDamage2 * 0.985;} 
		 if (randomdmg2 == 8){inc = FinalDamage2 * 0.975;}
		 if (randomdmg2 == 9){inc = FinalDamage2 * 0.960;} 
		 if (randomdmg2 == 10){inc = FinalDamage2 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice7 = new Random();
		 int randommiss7 = 1+missdice7.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){}
		 if (randommiss7 == 9){}
		 if (randommiss7 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){inc = 0; Crit = 0;}
		 if (randommiss7 == 8){inc = 0; Crit = 0;}
		 if (randommiss7 == 9){inc = 0; Crit = 0;}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){inc = 0; Crit = 0;}
		 if (randommiss7 == 9){inc = 0; Crit = 0;}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){}
		 if (randommiss7 == 9){inc = 0; Crit = 0;}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){}
		 if (randommiss7 == 9){}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){}
		 if (randommiss7 == 9){}
		 if (randommiss7 == 10){}
		 }else{
		 if (randommiss7 == 6){inc = 0; Crit = 0;}
		 if (randommiss7 == 7){inc = 0; Crit = 0;}
		 if (randommiss7 == 8){inc = 0; Crit = 0;}
		 if (randommiss7 == 9){inc = 0; Crit = 0;}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[60] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[60] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[60] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[60] = (byte)0x05;} // * 2 green crit
		
		 
		  if(TMob2.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp2 = TMob2.hp - (int)inc;

		 
		 byte[] finaldmg2 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+68] = finaldmg2[i];						
		 }
		 
		 byte[] newhpz2 = BitTools.intToByteArray(newhp2); 
		 byte[] newmanaz2 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+64] = newhpz2[i];		
			 pckt1[i+72] = newmanaz2[i];
		 }
	
		 TMob2.setHp(cur.charID,(int)inc, newhp2);
		 inc = 0;
		 pckt1[76] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[72] = (byte)0x02;pckt1[73] = (byte)0x07;} // mob 3
		 
		 //<=== Mob 3 ===>
		 Mob TMob3 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target3_), cur.getCurrentMap()); 
		 Mob tmpmob3 = TMob3;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
	     else{return;}
		
		 //===  RANDOMIZER  ===\\
		 Random dice3 = new Random();	 
		 int random3; // RANDOMIZER devided by 5% each!
		 random3 = 1+dice3.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random3 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random3 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random3 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random3 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random3 == 6) {} // just normal
		 if (random3 == 7) {} 
		 if (random3 == 8) {}
		 if (random3 == 9) {} 
		 if (random3 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random3 == 16){} 
		 if (random3 == 17){} 
		 if (random3 == 18){}
		 if (random3 == 19){} 
		 if (random3 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob3.FDD_FASR == 1 | TMob3.FDD == 1){double DEF = TMob3.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob3.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg3 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg3 = AFdmg3 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck3 = (int)Fdmg3 * zerocheck;
		 
		 Random greendice3 = new Random();	 
		int randomgreencrit3 = 1+greendice3.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit3 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca3 = Fdmgzerochck3 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza3 = (double)HidingDamage / 100;
		 double FinalDamage3; 
		 if(luza3 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage3 = luca3 * luza3;}
		 else{FinalDamage3 = luca3;}
		 Random pdmgdice3 = new Random();	 
		 int randomdmg3 = 1+pdmgdice3.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg3 == 1){inc = FinalDamage3 * 1.050;} 
		 if (randomdmg3 == 2){inc = FinalDamage3 * 1.040;}
		 if (randomdmg3 == 3){inc = FinalDamage3 * 1.035;} 
		 if (randomdmg3 == 4){inc = FinalDamage3 * 1.025;}
		 if (randomdmg3 == 5){inc = FinalDamage3 * 1.000;} 
		 if (randomdmg3 == 6){inc = FinalDamage3 * 0.990;}
		 if (randomdmg3 == 7){inc = FinalDamage3 * 0.985;} 
		 if (randomdmg3 == 8){inc = FinalDamage3 * 0.975;}
		 if (randomdmg3 == 9){inc = FinalDamage3 * 0.960;} 
		 if (randomdmg3 == 10){inc = FinalDamage3 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice8 = new Random();
		 int randommiss8 = 1+missdice8.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){}
		 if (randommiss8 == 9){}
		 if (randommiss8 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){inc = 0; Crit = 0;}
		 if (randommiss8 == 8){inc = 0; Crit = 0;}
		 if (randommiss8 == 9){inc = 0; Crit = 0;}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){inc = 0; Crit = 0;}
		 if (randommiss8 == 9){inc = 0; Crit = 0;}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){}
		 if (randommiss8 == 9){inc = 0; Crit = 0;}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){}
		 if (randommiss8 == 9){}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){}
		 if (randommiss8 == 9){}
		 if (randommiss8 == 10){}
		 }else{
		 if (randommiss8 == 6){inc = 0; Crit = 0;}
		 if (randommiss8 == 7){inc = 0; Crit = 0;}
		 if (randommiss8 == 8){inc = 0; Crit = 0;}
		 if (randommiss8 == 9){inc = 0; Crit = 0;}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[84] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[84] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[84] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[84] = (byte)0x05;} // * 2 green crit
		
	
		  if(TMob3.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp3 = TMob3.hp - (int)inc;
		 
		 byte[] finaldmg3 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+92] = finaldmg3[i];						
		 }
		 
		 byte[] newhpz3 = BitTools.intToByteArray(newhp3); 
		 byte[] newmanaz3 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+88] = newhpz3[i];		
			 pckt1[i+96] = newmanaz3[i];
		 }
		 
		 TMob3.setHp(cur.charID,(int)inc, newhp3);
		 inc = 0;
		 pckt1[100] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[96] = (byte)0x02;pckt1[97] = (byte)0x07;} // mob 4
		 
		 //<=== Mob 4 ===>
		 Mob TMob4 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target4_), cur.getCurrentMap()); 
		 Mob tmpmob4 = TMob4;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
	     else{return;}
		
		 //===  RANDOMIZER  ===\\
		 Random dice4 = new Random();	  // RANDOMIZER devided by 5% each!
		int random4 = 1+dice4.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random4 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random4 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random4 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random4 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random4 == 6) {} // just normal
		 if (random4 == 7) {} 
		 if (random4 == 8) {}
		 if (random4 == 9) {} 
		 if (random4 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random4 == 16){} 
		 if (random4 == 17){} 
		 if (random4 == 18){}
		 if (random4 == 19){} 
		 if (random4 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob4.FDD_FASR == 1 | TMob4.FDD == 1){double DEF = TMob4.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob4.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg4 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg4 = AFdmg4 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck4 = (int)Fdmg4 * zerocheck;
		 
		 Random greendice4 = new Random();	 
		int randomgreencrit4 = 1+greendice4.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit4 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca4 = Fdmgzerochck4 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza4 = (double)HidingDamage / 100;
		 double FinalDamage4; 
		 if(luza4 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage4 = luca4 * luza4;}
		 else{FinalDamage4 = luca4;}
		 Random pdmgdice4 = new Random();	 
		 int randomdmg4 = 1+pdmgdice4.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg4 == 1){inc = FinalDamage4 * 1.050;} 
		 if (randomdmg4 == 2){inc = FinalDamage4 * 1.040;}
		 if (randomdmg4 == 3){inc = FinalDamage4 * 1.035;} 
		 if (randomdmg4 == 4){inc = FinalDamage4 * 1.025;}
		 if (randomdmg4 == 5){inc = FinalDamage4 * 1.000;} 
		 if (randomdmg4 == 6){inc = FinalDamage4 * 0.990;}
		 if (randomdmg4 == 7){inc = FinalDamage4 * 0.985;} 
		 if (randomdmg4 == 8){inc = FinalDamage4 * 0.975;}
		 if (randomdmg4 == 9){inc = FinalDamage4 * 0.960;} 
		 if (randomdmg4 == 10){inc = FinalDamage4 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice9 = new Random();
		 int randommiss9 = 1+missdice9.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){}
		 if (randommiss9 == 9){}
		 if (randommiss9 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){inc = 0; Crit = 0;}
		 if (randommiss9 == 8){inc = 0; Crit = 0;}
		 if (randommiss9 == 9){inc = 0; Crit = 0;}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){inc = 0; Crit = 0;}
		 if (randommiss9 == 9){inc = 0; Crit = 0;}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){}
		 if (randommiss9 == 9){inc = 0; Crit = 0;}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){}
		 if (randommiss9 == 9){}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){}
		 if (randommiss9 == 9){}
		 if (randommiss9 == 10){}
		 }else{
		 if (randommiss9 == 6){inc = 0; Crit = 0;}
		 if (randommiss9 == 7){inc = 0; Crit = 0;}
		 if (randommiss9 == 8){inc = 0; Crit = 0;}
		 if (randommiss9 == 9){inc = 0; Crit = 0;}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[108] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[108] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[108] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[108] = (byte)0x05;} // * 2 green crit
		
		 
		  if(TMob4.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1;  // reset to *1 ( Normal Damage )
		 int newhp4 = TMob4.hp - (int)inc;
		 
		 byte[] finaldmg4 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+116] = finaldmg4[i];						
		 }
		
		 byte[] newhpz4 = BitTools.intToByteArray(newhp4); 
		 byte[] newmanaz4 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+112] = newhpz4[i];		
			 pckt1[i+120] = newmanaz4[i];
		 }
		 
		 TMob4.setHp(cur.charID,(int)inc, newhp4);
		 inc = 0;
		 pckt1[124] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[120] = (byte)0x02;pckt1[121] = (byte)0x07;} // mob 5
		 
		 //<=== Mob 5 ===>
		 Mob TMob5 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target5_), cur.getCurrentMap()); 
		 Mob tmpmob5 = TMob5;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 165){}
	     else{return;}
		 
		 //===  RANDOMIZER  ===\\
		 Random dice5 = new Random();	  // RANDOMIZER devided by 5% each!
		int random5 = 1+dice5.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random5 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random5 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random5 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random5 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random5 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random5 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random5 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random5 == 6) {} // just normal
		 if (random5 == 7) {} 
		 if (random5 == 8) {}
		 if (random5 == 9) {} 
		 if (random5 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random5 == 16){} 
		 if (random5 == 17){} 
		 if (random5 == 18){}
		 if (random5 == 19){} 
		 if (random5 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob5.FDD_FASR == 1 | TMob5.FDD == 1){double DEF = TMob5.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob5.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg5 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg5 = AFdmg5 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck5 = (int)Fdmg5 * zerocheck;
		 
		 Random greendice5 = new Random();	 
		int randomgreencrit5 = 1+greendice5.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit5 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca5 = Fdmgzerochck5 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza5 = (double)HidingDamage / 100;
		 double FinalDamage5; 
		 if(luza5 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage5 = luca5 * luza5;}
		 else{FinalDamage5 = luca5;}
		 Random pdmgdice5 = new Random();	 
		 int randomdmg5 = 1+pdmgdice5.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg5 == 1){inc = FinalDamage5 * 1.050;} 
		 if (randomdmg5 == 2){inc = FinalDamage5 * 1.040;}
		 if (randomdmg5 == 3){inc = FinalDamage5 * 1.035;} 
		 if (randomdmg5 == 4){inc = FinalDamage5 * 1.025;}
		 if (randomdmg5 == 5){inc = FinalDamage5 * 1.000;} 
		 if (randomdmg5 == 6){inc = FinalDamage5 * 0.990;}
		 if (randomdmg5 == 7){inc = FinalDamage5 * 0.985;} 
		 if (randomdmg5 == 8){inc = FinalDamage5 * 0.975;}
		 if (randomdmg5 == 9){inc = FinalDamage5 * 0.960;} 
		 if (randomdmg5 == 10){inc = FinalDamage5 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice1 = new Random();
		 int randommiss1 = 1+missdice1.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){}
		 if (randommiss1 == 9){}
		 if (randommiss1 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){inc = 0; Crit = 0;}
		 if (randommiss1 == 8){inc = 0; Crit = 0;}
		 if (randommiss1 == 9){inc = 0; Crit = 0;}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){inc = 0; Crit = 0;}
		 if (randommiss1 == 9){inc = 0; Crit = 0;}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){}
		 if (randommiss1 == 9){inc = 0; Crit = 0;}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){}
		 if (randommiss1 == 9){}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss1 == 6){}
		 if (randommiss1 == 7){}
		 if (randommiss1 == 8){}
		 if (randommiss1 == 9){}
		 if (randommiss1 == 10){}
		 }else{
		 if (randommiss1 == 6){inc = 0; Crit = 0;}
		 if (randommiss1 == 7){inc = 0; Crit = 0;}
		 if (randommiss1 == 8){inc = 0; Crit = 0;}
		 if (randommiss1 == 9){inc = 0; Crit = 0;}
		 if (randommiss1 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[132] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[132] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[132] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[132] = (byte)0x05;} // * 2 green crit
		
		 
		
		  if(TMob5.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp5 = TMob5.hp - (int)inc;
		 
		 byte[] finaldmg5 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+140] = finaldmg5[i];						
		 } 
		
		 
		 byte[] newhpz5 = BitTools.intToByteArray(newhp5); 
		 byte[] newmanaz5 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+136] = newhpz5[i];		
			 pckt1[i+144] = newmanaz5[i];
		 }
		 
		 TMob5.setHp(cur.charID,(int)inc, newhp5);
		 inc = 0;
		 
		 pckt1[148] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[144] = (byte)0x02;pckt1[145] = (byte)0x07;} // mob 6
		 
		 //<=== Mob 6 ===>
		 Mob TMob6 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target6_), cur.getCurrentMap()); 
		 Mob tmpmob6 = TMob6;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 165){}
	     else{return;}
		 //===  RANDOMIZER  ===\\
		 Random dice6 = new Random();	  // RANDOMIZER devided by 5% each!
		int random6 = 1+dice6.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random6 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random6 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random6 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random6 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random6 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random6 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random6 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random6 == 6) {} // just normal
		 if (random6 == 7) {} 
		 if (random6 == 8) {}
		 if (random6 == 9) {} 
		 if (random6 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random6 == 16){} 
		 if (random6 == 17){} 
		 if (random6 == 18){}
		 if (random6 == 19){} 
		 if (random6 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob6.FDD_FASR == 1 | TMob6.FDD == 1){double DEF = TMob6.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob6.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg6 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg6 = AFdmg6 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck6 = (int)Fdmg6 * zerocheck;
		 
		 Random greendice6 = new Random();	 
		int randomgreencrit6 = 1+greendice6.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit6 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca6 = Fdmgzerochck6 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza6 = (double)HidingDamage / 100;
		 double FinalDamage6; 
		 if(luza6 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage6 = luca6 * luza6;}
		 else{FinalDamage6 = luca6;}
		 Random pdmgdice6 = new Random();	 
		 int randomdmg6 = 1+pdmgdice6.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg6 == 1){inc = FinalDamage6 * 1.050;} 
		 if (randomdmg6 == 2){inc = FinalDamage6 * 1.040;}
		 if (randomdmg6 == 3){inc = FinalDamage6 * 1.035;} 
		 if (randomdmg6 == 4){inc = FinalDamage6 * 1.025;}
		 if (randomdmg6 == 5){inc = FinalDamage6 * 1.000;} 
		 if (randomdmg6 == 6){inc = FinalDamage6 * 0.990;}
		 if (randomdmg6 == 7){inc = FinalDamage6 * 0.985;} 
		 if (randomdmg6 == 8){inc = FinalDamage6 * 0.975;}
		 if (randomdmg6 == 9){inc = FinalDamage6 * 0.960;} 
		 if (randomdmg6 == 10){inc = FinalDamage6 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice2 = new Random();
		 int randommiss2 = 1+missdice2.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){inc = 0; Crit = 0;}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss2 == 6){}
		 if (randommiss2 == 7){}
		 if (randommiss2 == 8){}
		 if (randommiss2 == 9){}
		 if (randommiss2 == 10){}
		 }else{
		 if (randommiss2 == 6){inc = 0; Crit = 0;}
		 if (randommiss2 == 7){inc = 0; Crit = 0;}
		 if (randommiss2 == 8){inc = 0; Crit = 0;}
		 if (randommiss2 == 9){inc = 0; Crit = 0;}
		 if (randommiss2 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[156] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[156] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[156] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[156] = (byte)0x05;} // * 2 green crit
		
	
		  if(TMob6.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp6 = TMob6.hp - (int)inc;

		 
		 byte[] finaldmg6 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+164] = finaldmg6[i];						
		 } 
		
		 
		 byte[] newhpz6 = BitTools.intToByteArray(newhp6); 
		 byte[] newmanaz6 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+160] = newhpz6[i];		
			 pckt1[i+168] = newmanaz6[i];
		 }
		 
		 TMob6.setHp(cur.charID,(int)inc, newhp6);
		 inc = 0;
		 
		 pckt1[174] = (byte)0x02; // mob 7
		 
		 //<=== Mob 7 ===>
		 Mob TMob7 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target7_), cur.getCurrentMap()); 
		 Mob tmpmob7 = TMob7;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 165){}
	     else{return;}

		 //===  RANDOMIZER  ===\\
		 Random dice7 = new Random();	  // RANDOMIZER devided by 5% each!
		int random7 = 1+dice7.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random7 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random7 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random7 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random7 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random7 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random7 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random7 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random7 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random7 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random7 == 6) {} // just normal
		 if (random7 == 7) {} 
		 if (random7 == 8) {}
		 if (random7 == 9) {} 
		 if (random7 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random7 == 16){} 
		 if (random7 == 17){} 
		 if (random7 == 18){}
		 if (random7 == 19){} 
		 if (random7 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob7.FDD_FASR == 1 | TMob7.FDD == 1){double DEF = TMob7.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob7.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg7 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg7 = AFdmg7 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck7 = (int)Fdmg7 * zerocheck;
		 
		 Random greendice7 = new Random();	 
		int randomgreencrit7 = 1+greendice7.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit7 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca7 = Fdmgzerochck7 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza7 = (double)HidingDamage / 100;
		 double FinalDamage7; 
		 if(luza7 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage7 = luca7 * luza7;}
		 else{FinalDamage7 = luca7;}
		 Random pdmgdice7 = new Random();	 
		 int randomdmg7 = 1+pdmgdice7.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg7 == 1){inc = FinalDamage7 * 1.050;} 
		 if (randomdmg7 == 2){inc = FinalDamage7 * 1.040;}
		 if (randomdmg7 == 3){inc = FinalDamage7 * 1.035;} 
		 if (randomdmg7 == 4){inc = FinalDamage7 * 1.025;}
		 if (randomdmg7 == 5){inc = FinalDamage7 * 1.000;} 
		 if (randomdmg7 == 6){inc = FinalDamage7 * 0.990;}
		 if (randomdmg7 == 7){inc = FinalDamage7 * 0.985;} 
		 if (randomdmg7 == 8){inc = FinalDamage7 * 0.975;}
		 if (randomdmg7 == 9){inc = FinalDamage7 * 0.960;} 
		 if (randomdmg7 == 10){inc = FinalDamage7 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice3 = new Random();
		 int randommiss3 = 1+missdice3.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){inc = 0; Crit = 0;}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss3 == 6){}
		 if (randommiss3 == 7){}
		 if (randommiss3 == 8){}
		 if (randommiss3 == 9){}
		 if (randommiss3 == 10){}
		 }else{
		 if (randommiss3 == 6){inc = 0; Crit = 0;}
		 if (randommiss3 == 7){inc = 0; Crit = 0;}
		 if (randommiss3 == 8){inc = 0; Crit = 0;}
		 if (randommiss3 == 9){inc = 0; Crit = 0;}
		 if (randommiss3 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[180] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[180] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[180] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[180] = (byte)0x05;} // * 2 green crit
		
		
		  if(TMob7.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp7 = TMob7.hp - (int)inc;
		 
		 byte[] finaldmg7 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+188] = finaldmg7[i];						
		 } 
		 
		 
		 byte[] newhpz7 = BitTools.intToByteArray(newhp7); 
		 byte[] newmanaz7 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+184] = newhpz7[i];		
			 pckt1[i+192] = newmanaz7[i];
		 }
		 
		 TMob7.setHp(cur.charID,(int)inc, newhp7);
		 inc = 0;
	    //con.addWrite(pckt);
		 cur.sendToMap(pckt1);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt1);
	   
	}
	public void AttackMOB8(int seqway ,int checkfury, int skillcritchance, int passiveskillcritchance, int target1,int target2,int target3,int target4,int target5,int target6,int target7,int target8 ,Connection con) {		
		 Character cur = ((PlayerConnection)con).getActiveCharacter();
		 
		 byte[] chid = BitTools.intToByteArray(cur.getCharID());
		 byte[] skid = BitTools.intToByteArray(seqway); // skill id
		 byte[] target1_ = BitTools.intToByteArray(target1);
		 byte[] target2_ = BitTools.intToByteArray(target2);
		 byte[] target3_ = BitTools.intToByteArray(target3);
		 byte[] target4_ = BitTools.intToByteArray(target4);
		 byte[] target5_ = BitTools.intToByteArray(target5);
		 byte[] target6_ = BitTools.intToByteArray(target6);
		 byte[] target7_ = BitTools.intToByteArray(target7);
		 byte[] target8_ = BitTools.intToByteArray(target8);
		 byte[] pckt1 = new byte[220];
		 pckt1[0] = (byte)0xdc;
		 pckt1[4] = (byte)0x05;
		 pckt1[6] = (byte)0x34;
		 pckt1[8] = (byte)0x01;
		 
		 for(int i=0;i<4;i++) {
				pckt1[i+12] = chid[i];						// char ID
				pckt1[i+32] = target1_[i];
				pckt1[i+56] = target2_[i];
				pckt1[i+80] = target3_[i];
				pckt1[i+104] = target4_[i];
				pckt1[i+128] = target5_[i];// target id SERVER
				pckt1[i+152] = target6_[i];
				pckt1[i+176] = target7_[i];
				pckt1[i+200] = target8_[i];
				pckt1[i+20] = skid[i]; 						// skill ID
		 }
		
		 pckt1[16] = (byte)0x01;
		 pckt1[27] = (byte)0x08;  // how many mobs ?
		 
		 pckt1[28] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;} if(seqway == buffdata.getBedoonglist(seqway)){pckt1[24] = (byte)0x02;pckt1[25] = (byte)0x07;}  // mob 1
		 
			// Mob 1
		 Mob TMob1 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target1_), cur.getCurrentMap()); 
		 Mob tmpmob = TMob1;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()) < 165){}
	     else{return;}

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
		 
		 int luca = Fdmgzerochck * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza = (double)HidingDamage / 100;
		 double FinalDamage; 
		 if(luza != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage = luca * luza;}
		 else{FinalDamage = luca;}
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

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
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
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[36] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[36] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[36] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[36] = (byte)0x05;} // * 2 green crit
		
		 
			if(Crit != 0 && skilleffects.tryskilleffects(seqway)){
				String e = skilleffects.getskilleffects(seqway);
				String[] Passive = e.split(",");	
				int DETERMINER = 2;//mob
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
				int Rate = Integer.valueOf(Passive[12]); 
				int Limit = Integer.valueOf(Passive[13]); 
				
				boolean SlicenDice = false;
				
				 if(DotsIconID == 43){
					    if((int)inc > 0){	
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
						    SlicenDice = this.Dice(1, 1);
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
						    SlicenDice = this.Dice(1, 2);    	
						}else
						if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
						    SlicenDice = this.Dice(1, 3);		    	
						}}
					    }else
					    if(DotsIconID == 46){
					    	if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					    }else
					 	if(DotsIconID == 49){
					 		if((int)inc > 0){	
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 15){
							    SlicenDice = this.Dice(1, 1);
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 30){
							    SlicenDice = this.Dice(1, 2);    	
							}else
							if(TMob1.getControl().getData().getLvl() - cur.getLevel() <= 50){
							    SlicenDice = this.Dice(1, 3);		    	
							}}
					 	}else{
						 SlicenDice = this.Dice(Rate, Limit);
					 	}
				
				if(SlicenDice){
					if(DotsIconID == 58){this.AddDot(cur.charID, DotsIconID, DotsValue, DotsTime, DotsSLOT, 1, cur);}
					else{
					this.AddDot(target1, DotsIconID, DotsValue, DotsTime, DotsSLOT, DETERMINER, cur);}
					if(DotsIconID2 != 0){this.AddDot(target1, DotsIconID2, DotsValue2, DotsTime2, DotsSLOT2, DETERMINER, cur);}
					if(DotsIconID3 != 0){this.AddDot(target1, DotsIconID3, DotsValue3, DotsTime3, DotsSLOT3, DETERMINER, cur);}
				}
			}
			
		   if(Crit != 0 && cur.HIDING == 1){  
			cur.sendToMap_ADMIN_CHECK_ON_extCharPacket();
		    
		    cur.HIDING = 0;
		    
		    cur.RemoveDot(cur.getDotsIconID(6), cur.getDotsSLOT(6));
		    
		     
		    }
		  
			  if(TMob1.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob1.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp = TMob1.hp - (int)inc;
		 
		 byte[] finaldmg = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+44] = finaldmg[i];						
		 }
		
		 byte[] newhpz = BitTools.intToByteArray(newhp); 
		 byte[] newmanaz = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+40] = newhpz[i];		
			 pckt1[i+48] = newmanaz[i];
		 }

		 TMob1.setHp(cur.charID,(int)inc, newhp);
		 inc = 0;
		 
		 pckt1[52] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[48] = (byte)0x02;pckt1[49] = (byte)0x07;}   // mob 2
		
		 
		 
		 //<=== Mob 2 ===>
		 Mob TMob2 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target2_), cur.getCurrentMap());
		 Mob tmpmob2 = TMob2;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob2.location.getX(), tmpmob2.location.getY()) < 165){}
	     else{return;}
	
		 //===  RANDOMIZER  ===\\
		 Random dice2 = new Random();	 
		 int random2; // RANDOMIZER devided by 5% each!
		 random2 = 1+dice2.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random2 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random2 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random2 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random2 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random2 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random2 == 6) {} // just normal
		 if (random2 == 7) {} 
		 if (random2 == 8) {}
		 if (random2 == 9) {} 
		 if (random2 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random2 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random2 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random2 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random2 == 16){} 
		 if (random2 == 17){} 
		 if (random2 == 18){}
		 if (random2 == 19){} 
		 if (random2 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob2.FDD_FASR == 1 | TMob2.FDD == 1){double DEF = TMob2.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob2.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg2 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg2 = AFdmg2 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck2 = (int)Fdmg2 * zerocheck;
		 
		 Random greendice2 = new Random();	 
		int randomgreencrit2 = 1+greendice2.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit2 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca2 = Fdmgzerochck2 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza2 = (double)HidingDamage / 100;
		 double FinalDamage2; 
		 if(luza2 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage2 = luca2 * luza2;}
		 else{FinalDamage2 = luca2;}
		 Random pdmgdice2 = new Random();	 
		 int randomdmg2 = 1+pdmgdice2.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg2 == 1){inc = FinalDamage2 * 1.050;} 
		 if (randomdmg2 == 2){inc = FinalDamage2 * 1.040;}
		 if (randomdmg2 == 3){inc = FinalDamage2 * 1.035;} 
		 if (randomdmg2 == 4){inc = FinalDamage2 * 1.025;}
		 if (randomdmg2 == 5){inc = FinalDamage2 * 1.000;} 
		 if (randomdmg2 == 6){inc = FinalDamage2 * 0.990;}
		 if (randomdmg2 == 7){inc = FinalDamage2 * 0.985;} 
		 if (randomdmg2 == 8){inc = FinalDamage2 * 0.975;}
		 if (randomdmg2 == 9){inc = FinalDamage2 * 0.960;} 
		 if (randomdmg2 == 10){inc = FinalDamage2 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice4 = new Random();
		 int randommiss4 = 1+missdice4.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){inc = 0; Crit = 0;}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss4 == 6){}
		 if (randommiss4 == 7){}
		 if (randommiss4 == 8){}
		 if (randommiss4 == 9){}
		 if (randommiss4 == 10){}
		 }else{
		 if (randommiss4 == 6){inc = 0; Crit = 0;}
		 if (randommiss4 == 7){inc = 0; Crit = 0;}
		 if (randommiss4 == 8){inc = 0; Crit = 0;}
		 if (randommiss4 == 9){inc = 0; Crit = 0;}
		 if (randommiss4 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[60] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[60] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[60] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[60] = (byte)0x05;} // * 2 green crit
		
		
		  if(TMob2.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob2.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp2 = TMob2.hp - (int)inc;
		 
		 byte[] finaldmg2 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+68] = finaldmg2[i];						
		 }
		 
		 byte[] newhpz2 = BitTools.intToByteArray(newhp2); 
		 byte[] newmanaz2 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+64] = newhpz2[i];		
			 pckt1[i+72] = newmanaz2[i];
		 }
		
		 
		 TMob2.setHp(cur.charID,(int)inc, newhp2);
		 inc = 0;
		 pckt1[76] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[72] = (byte)0x02;pckt1[73] = (byte)0x07;} // mob 3
		 
		 //<=== Mob 3 ===>
		 Mob TMob3 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target3_), cur.getCurrentMap()); 
		 Mob tmpmob3 = TMob3;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob3.location.getX(), tmpmob3.location.getY()) < 165){}
	     else{return;}
	
		 //===  RANDOMIZER  ===\\
		 Random dice3 = new Random();	 
		 int random3; // RANDOMIZER devided by 5% each!
		 random3 = 1+dice3.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random3 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random3 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random3 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random3 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random3 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random3 == 6) {} // just normal
		 if (random3 == 7) {} 
		 if (random3 == 8) {}
		 if (random3 == 9) {} 
		 if (random3 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random3 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random3 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random3 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random3 == 16){} 
		 if (random3 == 17){} 
		 if (random3 == 18){}
		 if (random3 == 19){} 
		 if (random3 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob3.FDD_FASR == 1 | TMob3.FDD == 1){double DEF = TMob3.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob3.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg3 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg3 = AFdmg3 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck3 = (int)Fdmg3 * zerocheck;
		 
		 Random greendice3 = new Random();	 
		int randomgreencrit3 = 1+greendice3.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit3 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca3 = Fdmgzerochck3 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza3 = (double)HidingDamage / 100;
		 double FinalDamage3; 
		 if(luza3 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage3 = luca3 * luza3;}
		 else{FinalDamage3 = luca3;}
		 Random pdmgdice3 = new Random();	 
		 int randomdmg3 = 1+pdmgdice3.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg3 == 1){inc = FinalDamage3 * 1.050;} 
		 if (randomdmg3 == 2){inc = FinalDamage3 * 1.040;}
		 if (randomdmg3 == 3){inc = FinalDamage3 * 1.035;} 
		 if (randomdmg3 == 4){inc = FinalDamage3 * 1.025;}
		 if (randomdmg3 == 5){inc = FinalDamage3 * 1.000;} 
		 if (randomdmg3 == 6){inc = FinalDamage3 * 0.990;}
		 if (randomdmg3 == 7){inc = FinalDamage3 * 0.985;} 
		 if (randomdmg3 == 8){inc = FinalDamage3 * 0.975;}
		 if (randomdmg3 == 9){inc = FinalDamage3 * 0.960;} 
		 if (randomdmg3 == 10){inc = FinalDamage3 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice5 = new Random();
		 int randommiss5 = 1+missdice5.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){inc = 0; Crit = 0;}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss5 == 6){}
		 if (randommiss5 == 7){}
		 if (randommiss5 == 8){}
		 if (randommiss5 == 9){}
		 if (randommiss5 == 10){}
		 }else{
		 if (randommiss5 == 6){inc = 0; Crit = 0;}
		 if (randommiss5 == 7){inc = 0; Crit = 0;}
		 if (randommiss5 == 8){inc = 0; Crit = 0;}
		 if (randommiss5 == 9){inc = 0; Crit = 0;}
		 if (randommiss5 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[84] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[84] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[84] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[84] = (byte)0x05;} // * 2 green crit
		
		 
		  if(TMob3.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob3.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp3 = TMob3.hp - (int)inc;
		 
		 byte[] finaldmg3 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+92] = finaldmg3[i];						
		 }
	
		 byte[] newhpz3 = BitTools.intToByteArray(newhp3); 
		 byte[] newmanaz3 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+88] = newhpz3[i];		
			 pckt1[i+96] = newmanaz3[i];
		 }
		 
		 TMob3.setHp(cur.charID,(int)inc, newhp3);
		 inc = 0;
		 pckt1[100] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[96] = (byte)0x02;pckt1[97] = (byte)0x07;} // mob 4
		 
		 //<=== Mob 4 ===>
		 Mob TMob4 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target4_), cur.getCurrentMap()); 
		 Mob tmpmob4 = TMob4;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob.location.getX(), tmpmob.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob4.location.getX(), tmpmob4.location.getY()) < 165){}
	     else{return;}
		 //===  RANDOMIZER  ===\\
		 Random dice4 = new Random();	  // RANDOMIZER devided by 5% each!
		int random4 = 1+dice4.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random4 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 }  
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random4 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random4 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random4 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random4 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random4 == 6) {} // just normal
		 if (random4 == 7) {} 
		 if (random4 == 8) {}
		 if (random4 == 9) {} 
		 if (random4 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random4 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random4 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random4 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random4 == 16){} 
		 if (random4 == 17){} 
		 if (random4 == 18){}
		 if (random4 == 19){} 
		 if (random4 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob4.FDD_FASR == 1 | TMob4.FDD == 1){double DEF = TMob4.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob4.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg4 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg4 = AFdmg4 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck4 = (int)Fdmg4 * zerocheck;
		 
		 Random greendice4 = new Random();	 
		int randomgreencrit4 = 1+greendice4.nextInt(40);  // RANDOMIZER devided by 3.7% each!
		 if (randomgreencrit4 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca4 = Fdmgzerochck4 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza4 = (double)HidingDamage / 100;
		 double FinalDamage4; 
		 if(luza4 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage4 = luca4 * luza4;}
		 else{FinalDamage4 = luca4;}
		 Random pdmgdice4 = new Random();	 
		 int randomdmg4 = 1+pdmgdice4.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg4 == 1){inc = FinalDamage4 * 1.050;} 
		 if (randomdmg4 == 2){inc = FinalDamage4 * 1.040;}
		 if (randomdmg4 == 3){inc = FinalDamage4 * 1.035;} 
		 if (randomdmg4 == 4){inc = FinalDamage4 * 1.025;}
		 if (randomdmg4 == 5){inc = FinalDamage4 * 1.000;} 
		 if (randomdmg4 == 6){inc = FinalDamage4 * 0.990;}
		 if (randomdmg4 == 7){inc = FinalDamage4 * 0.985;} 
		 if (randomdmg4 == 8){inc = FinalDamage4 * 0.975;}
		 if (randomdmg4 == 9){inc = FinalDamage4 * 0.960;} 
		 if (randomdmg4 == 10){inc = FinalDamage4 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice6 = new Random();
		 int randommiss6 = 1+missdice6.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){inc = 0; Crit = 0;}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss6 == 6){}
		 if (randommiss6 == 7){}
		 if (randommiss6 == 8){}
		 if (randommiss6 == 9){}
		 if (randommiss6 == 10){}
		 }else{
		 if (randommiss6 == 6){inc = 0; Crit = 0;}
		 if (randommiss6 == 7){inc = 0; Crit = 0;}
		 if (randommiss6 == 8){inc = 0; Crit = 0;}
		 if (randommiss6 == 9){inc = 0; Crit = 0;}
		 if (randommiss6 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[108] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[108] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[108] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[108] = (byte)0x05;} // * 2 green crit
		
		
		  if(TMob4.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob4.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp4 = TMob4.hp - (int)inc;
		 
		 byte[] finaldmg4 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+116] = finaldmg4[i];						
		 }
		
		 byte[] newhpz4 = BitTools.intToByteArray(newhp4); 
		 byte[] newmanaz4 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+112] = newhpz4[i];		
			 pckt1[i+120] = newmanaz4[i];
		 }
		 
		 TMob4.setHp(cur.charID,(int)inc, newhp4);
		 inc = 0;
		 pckt1[124] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[120] = (byte)0x02;pckt1[121] = (byte)0x07;} // mob 5
		 
		 //<=== Mob 5 ===>
		 Mob TMob5 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target5_), cur.getCurrentMap()); 
		 Mob tmpmob5 = TMob5;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob5.location.getX(), tmpmob5.location.getY()) < 165){}
	     else{return;}

		 //===  RANDOMIZER  ===\\
		 Random dice5 = new Random();	  // RANDOMIZER devided by 5% each!
		int random5 = 1+dice5.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random5 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random5 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random5 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random5 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random5 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random5 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random5 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random5 == 6) {} // just normal
		 if (random5 == 7) {} 
		 if (random5 == 8) {}
		 if (random5 == 9) {} 
		 if (random5 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random5 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random5 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random5 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random5 == 16){} 
		 if (random5 == 17){} 
		 if (random5 == 18){}
		 if (random5 == 19){} 
		 if (random5 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob5.FDD_FASR == 1 | TMob5.FDD == 1){double DEF = TMob5.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob5.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg5 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg5 = AFdmg5 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck5 = (int)Fdmg5 * zerocheck;
		 
		 Random greendice5 = new Random();	 
		int randomgreencrit5 = 1+greendice5.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit5 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca5 = Fdmgzerochck5 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza5 = (double)HidingDamage / 100;
		 double FinalDamage5; 
		 if(luza5 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage5 = luca5 * luza5;}
		 else{FinalDamage5 = luca5;}
		 Random pdmgdice5 = new Random();	 
		 int randomdmg5 = 1+pdmgdice5.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg5 == 1){inc = FinalDamage5 * 1.050;} 
		 if (randomdmg5 == 2){inc = FinalDamage5 * 1.040;}
		 if (randomdmg5 == 3){inc = FinalDamage5 * 1.035;} 
		 if (randomdmg5 == 4){inc = FinalDamage5 * 1.025;}
		 if (randomdmg5 == 5){inc = FinalDamage5 * 1.000;} 
		 if (randomdmg5 == 6){inc = FinalDamage5 * 0.990;}
		 if (randomdmg5 == 7){inc = FinalDamage5 * 0.985;} 
		 if (randomdmg5 == 8){inc = FinalDamage5 * 0.975;}
		 if (randomdmg5 == 9){inc = FinalDamage5 * 0.960;} 
		 if (randomdmg5 == 10){inc = FinalDamage5 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice7 = new Random();
		 int randommiss7 = 1+missdice7.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){}
		 if (randommiss7 == 9){}
		 if (randommiss7 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){inc = 0; Crit = 0;}
		 if (randommiss7 == 8){inc = 0; Crit = 0;}
		 if (randommiss7 == 9){inc = 0; Crit = 0;}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){inc = 0; Crit = 0;}
		 if (randommiss7 == 9){inc = 0; Crit = 0;}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){}
		 if (randommiss7 == 9){inc = 0; Crit = 0;}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){}
		 if (randommiss7 == 9){}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss7 == 6){}
		 if (randommiss7 == 7){}
		 if (randommiss7 == 8){}
		 if (randommiss7 == 9){}
		 if (randommiss7 == 10){}
		 }else{
		 if (randommiss7 == 6){inc = 0; Crit = 0;}
		 if (randommiss7 == 7){inc = 0; Crit = 0;}
		 if (randommiss7 == 8){inc = 0; Crit = 0;}
		 if (randommiss7 == 9){inc = 0; Crit = 0;}
		 if (randommiss7 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[132] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[132] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[132] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[132] = (byte)0x05;} // * 2 green crit
		
		
		  if(TMob5.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob5.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp5 = TMob5.hp - (int)inc;
		 
		 byte[] finaldmg5 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+140] = finaldmg5[i];						
		 } 
	
		 
		 byte[] newhpz5 = BitTools.intToByteArray(newhp5); 
		 byte[] newmanaz5 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+136] = newhpz5[i];		
			 pckt1[i+144] = newmanaz5[i];
		 }
		 
		 TMob5.setHp(cur.charID,(int)inc, newhp5);
		 inc = 0;
		 pckt1[148] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[144] = (byte)0x02;pckt1[145] = (byte)0x07;} // mob 6
		 //<=== Mob 6 ===>
		 Mob TMob6 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target6_), cur.getCurrentMap()); 
		 Mob tmpmob6 = TMob6;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob6.location.getX(), tmpmob6.location.getY()) < 165){}
	     else{return;}

		 //===  RANDOMIZER  ===\\
		 Random dice6 = new Random();	  // RANDOMIZER devided by 5% each!
		int random6 = 1+dice6.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random6 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random6 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random6 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random6 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random6 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random6 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random6 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random6 == 6) {} // just normal
		 if (random6 == 7) {} 
		 if (random6 == 8) {}
		 if (random6 == 9) {} 
		 if (random6 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random6 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random6 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random6 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 }  
		 	  
		 if (random6 == 16){} 
		 if (random6 == 17){} 
		 if (random6 == 18){}
		 if (random6 == 19){} 
		 if (random6 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob6.FDD_FASR == 1 | TMob6.FDD == 1){double DEF = TMob6.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob6.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg6 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg6 = AFdmg6 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck6 = (int)Fdmg6 * zerocheck;
		 
		 Random greendice6 = new Random();	 
		int randomgreencrit6 = 1+greendice6.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit6 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca6 = Fdmgzerochck6 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza6 = (double)HidingDamage / 100;
		 double FinalDamage6; 
		 if(luza6 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage6 = luca6 * luza6;}
		 else{FinalDamage6 = luca6;}
		 Random pdmgdice6 = new Random();	 
		 int randomdmg6 = 1+pdmgdice6.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg6 == 1){inc = FinalDamage6 * 1.050;} 
		 if (randomdmg6 == 2){inc = FinalDamage6 * 1.040;}
		 if (randomdmg6 == 3){inc = FinalDamage6 * 1.035;} 
		 if (randomdmg6 == 4){inc = FinalDamage6 * 1.025;}
		 if (randomdmg6 == 5){inc = FinalDamage6 * 1.000;} 
		 if (randomdmg6 == 6){inc = FinalDamage6 * 0.990;}
		 if (randomdmg6 == 7){inc = FinalDamage6 * 0.985;} 
		 if (randomdmg6 == 8){inc = FinalDamage6 * 0.975;}
		 if (randomdmg6 == 9){inc = FinalDamage6 * 0.960;} 
		 if (randomdmg6 == 10){inc = FinalDamage6 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice8 = new Random();
		 int randommiss8 = 1+missdice8.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){}
		 if (randommiss8 == 9){}
		 if (randommiss8 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){inc = 0; Crit = 0;}
		 if (randommiss8 == 8){inc = 0; Crit = 0;}
		 if (randommiss8 == 9){inc = 0; Crit = 0;}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){inc = 0; Crit = 0;}
		 if (randommiss8 == 9){inc = 0; Crit = 0;}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){}
		 if (randommiss8 == 9){inc = 0; Crit = 0;}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){}
		 if (randommiss8 == 9){}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss8 == 6){}
		 if (randommiss8 == 7){}
		 if (randommiss8 == 8){}
		 if (randommiss8 == 9){}
		 if (randommiss8 == 10){}
		 }else{
		 if (randommiss8 == 6){inc = 0; Crit = 0;}
		 if (randommiss8 == 7){inc = 0; Crit = 0;}
		 if (randommiss8 == 8){inc = 0; Crit = 0;}
		 if (randommiss8 == 9){inc = 0; Crit = 0;}
		 if (randommiss8 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[156] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[156] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[156] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[156] = (byte)0x05;} // * 2 green crit
		
	
		  if(TMob6.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob6.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp6 = TMob6.hp - (int)inc;
		 
		 byte[] finaldmg6 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+164] = finaldmg6[i];						
		 } 
	
		 
		 byte[] newhpz6 = BitTools.intToByteArray(newhp6); 
		 byte[] newmanaz6 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+160] = newhpz6[i];		
			 pckt1[i+168] = newmanaz6[i];
		 }
		 
		 TMob6.setHp(cur.charID,(int)inc, newhp6);
		 inc = 0;
		 pckt1[172] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[168] = (byte)0x02;pckt1[169] = (byte)0x07;} // mob 7
		 //<=== Mob 7 ===>
		 Mob TMob7 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target7_), cur.getCurrentMap()); 
		 Mob tmpmob7 = TMob7;
		 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()));
	     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 92){}
	     else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 190){}
	     else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 186){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 180){}
		 else
		 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 176){}
		 else
	     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 168){}
	     else
		 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 162){}
	     else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 165){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 154){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 136){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 126){}
		 else
		 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 120){}
		 else
	     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 110){}
	     else
	     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob7.location.getX(), tmpmob7.location.getY()) < 165){}
	     else{return;}
	
		 //===  RANDOMIZER  ===\\
		 Random dice7 = new Random();	  // RANDOMIZER devided by 5% each!
		int random7 = 1+dice7.nextInt(20);
		 //System.out.println("Random number = "+ random);
		 
		 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
			 if (random7 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
			 if (random7 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
			 if (random7 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(passiveskillcritchance >= 23){ 
			 if (random7 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 
		 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
		 if (random7 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 if (random7 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random7 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 if (random7 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
		 if (random7 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
		 }else{
		 if (random7 == 6) {} // just normal
		 if (random7 == 7) {} 
		 if (random7 == 8) {}
		 if (random7 == 9) {} 
		 if (random7 == 10){}  
		 }
		 
		 if(skillcritchance >= 3 && skillcritchance <= 7){  
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
		 } 
		 if(skillcritchance >= 8 && skillcritchance <= 12){  
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 13 && skillcritchance <= 17){ 
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 18 && skillcritchance <= 22){ 
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 if(skillcritchance >= 23){ 
			 if (random7 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random7 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random7 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
		 } 
		 	  
		 if (random7 == 16){} 
		 if (random7 == 17){} 
		 if (random7 == 18){}
		 if (random7 == 19){} 
		 if (random7 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
		 
		 if(TMob7.FDD_FASR == 1 | TMob7.FDD == 1){double DEF = TMob7.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
		 else { TargetPlayerdefence = TMob7.getDefence();} // normal
		 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
		 int AFdmg7 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
		 TargetPlayerdefence = 0;
		 double Fdmg7 = AFdmg7 * Bytecritchance; // FINAL DAMAGE
		 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
		 int Fdmgzerochck7 = (int)Fdmg7 * zerocheck;
		 
		 Random greendice7 = new Random();	 
		int randomgreencrit7 = 1+greendice7.nextInt(40);  // RANDOMIZER devided by 2.5% each!
		 if (randomgreencrit7 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
		 
		 int luca7 = Fdmgzerochck7 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
		 double luza7 = (double)HidingDamage / 100;
		 double FinalDamage7; 
		 if(luza7 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage7 = luca7 * luza7;}
		 else{FinalDamage7 = luca7;}
		 Random pdmgdice7 = new Random();	 
		 int randomdmg7 = 1+pdmgdice7.nextInt(10);  // RANDOMIZER devided by 20% each!
		 if (randomdmg7 == 1){inc = FinalDamage7 * 1.050;} 
		 if (randomdmg7 == 2){inc = FinalDamage7 * 1.040;}
		 if (randomdmg7 == 3){inc = FinalDamage7 * 1.035;} 
		 if (randomdmg7 == 4){inc = FinalDamage7 * 1.025;}
		 if (randomdmg7 == 5){inc = FinalDamage7 * 1.000;} 
		 if (randomdmg7 == 6){inc = FinalDamage7 * 0.990;}
		 if (randomdmg7 == 7){inc = FinalDamage7 * 0.985;} 
		 if (randomdmg7 == 8){inc = FinalDamage7 * 0.975;}
		 if (randomdmg7 == 9){inc = FinalDamage7 * 0.960;} 
		 if (randomdmg7 == 10){inc = FinalDamage7 *0.950;}

		 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
		
		 Random missdice9 = new Random();
		 int randommiss9 = 1+missdice9.nextInt(20);  // 1/4 on miss
		 if(cur.FASR == 1){
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){}
		 if (randommiss9 == 9){}
		 if (randommiss9 == 10){}
		 }
		 else
		 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){inc = 0; Crit = 0;}
		 if (randommiss9 == 8){inc = 0; Crit = 0;}
		 if (randommiss9 == 9){inc = 0; Crit = 0;}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){inc = 0; Crit = 0;}
		 if (randommiss9 == 9){inc = 0; Crit = 0;}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){}
		 if (randommiss9 == 9){inc = 0; Crit = 0;}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){}
		 if (randommiss9 == 9){}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		 else
		 if(attacksuccesrate >= 23){ 
		 if (randommiss9 == 6){}
		 if (randommiss9 == 7){}
		 if (randommiss9 == 8){}
		 if (randommiss9 == 9){}
		 if (randommiss9 == 10){}
		 }else{
		 if (randommiss9 == 6){inc = 0; Crit = 0;}
		 if (randommiss9 == 7){inc = 0; Crit = 0;}
		 if (randommiss9 == 8){inc = 0; Crit = 0;}
		 if (randommiss9 == 9){inc = 0; Crit = 0;}
		 if (randommiss9 == 10){inc = 0; Crit = 0;}
		 }
		// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
		 if(Crit == 0){pckt1[180] = (byte)0x00;} // miss
		 if(Crit == 1){pckt1[180] = (byte)0x01;} // * 1 normal damage ( STANDARD)
		 if(Crit == 2){pckt1[180] = (byte)0x02;} // * 1.5 & * 2 white crit
		 if(Crit == 3){pckt1[180] = (byte)0x05;} // * 2 green crit
		

		  if(TMob7.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		  else
		  if(TMob7.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
		 Crit = 1;			 // reset to *1 ( Normal Damage )
		 greencrit = 1;		 // reset to *1 ( Normal Damage )
		 zerocheck = 1;		 // reset to *1 ( Normal Damage )
		 Bytecritchance = 1; // reset to *1 ( Normal Damage )
		 int newhp7 = TMob7.hp - (int)inc;
		 
		 byte[] finaldmg7 = BitTools.intToByteArray((int)inc); 
		 for(int i=0;i<4;i++) {
			 pckt1[i+188] = finaldmg7[i];						
		 } 
		
		 
		 byte[] newhpz7 = BitTools.intToByteArray(newhp7); 
		 byte[] newmanaz7 = BitTools.intToByteArray(5000); 
		 for(int i=0;i<2;i++) {
			 pckt1[i+184] = newhpz7[i];		
			 pckt1[i+192] = newmanaz7[i];
		 }
		 
		 TMob7.setHp(cur.charID,(int)inc, newhp7);
		 inc = 0;
		 pckt1[196] = (byte)0x02; if(seqway == buffdata.getBedoonglist(seqway)){pckt1[192] = (byte)0x02;pckt1[193] = (byte)0x07;} // mob 8
			//<=== Mob 8 ===>
			 Mob TMob8 = WMap.getInstance().getMob(BitTools.byteArrayToInt(target8_), cur.getCurrentMap()); 
			 Mob tmpmob8 = TMob8;
			 //System.out.println("===> Between: x:"+WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()));
		     if(cur.getCharacterClass() == 1 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 92){}
		     else
		     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 190){}
		     else
			 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 186){}
			 else
			 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 180){}
			 else
			 if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 176){}
			 else
		     if(cur.getCharacterClass() == 2 && cur.skills.containsValue(200141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 168){}
		     else
			 if(cur.getCharacterClass() == 2 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 162){}
		     else
			 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300145) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 165){}
			 else
			 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300144) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 154){}
			 else
			 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300143) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 136){}
			 else
			 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300142) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 126){}
			 else
			 if(cur.getCharacterClass() == 3 && cur.skills.containsValue(300141) && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 120){}
			 else
		     if(cur.getCharacterClass() == 3 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 110){}
		     else
		     if(cur.getCharacterClass() == 4 && WMap.distance(cur.getlastknownX(), cur.getlastknownY(), tmpmob8.location.getX(), tmpmob8.location.getY()) < 165){}
		     else{return;}
		
			 //===  RANDOMIZER  ===\\
			 Random dice8 = new Random();	  // RANDOMIZER devided by 5% each!
			int random8 = 1+dice8.nextInt(20);
			 //System.out.println("Random number = "+ random);
			 
			 if(passiveskillcritchance >= 3 && passiveskillcritchance <= 7){  
				 if (random8 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 } 
			 if(passiveskillcritchance >= 8 && passiveskillcritchance <= 12){  
				 if (random8 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random8 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 } 
			 if(passiveskillcritchance >= 13 &&passiveskillcritchance <= 17){ 
				 if (random8 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random8 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 } 
			 if(passiveskillcritchance >= 18 && passiveskillcritchance <= 22){ 
				 if (random8 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random8 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 } 
			 if(passiveskillcritchance >= 23){ 
				 if (random8 == 1) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random8 == 2) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 3) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 4) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 5) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 }  
			 
			 if(cur.CASR == 1){ // if player has FDD FASR or FADR ( +25% crit chance)  then: 
			 if (random8 == 6) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 if (random8 == 7) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random8 == 8) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 if (random8 == 9) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} 
			 if (random8 == 10){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}  
			 }else{
			 if (random8 == 6) {} // just normal
			 if (random8 == 7) {} 
			 if (random8 == 8) {}
			 if (random8 == 9) {} 
			 if (random8 == 10){}  
			 }
			 
			 if(skillcritchance >= 3 && skillcritchance <= 7){  
				 if (random8 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
			 } 
			 if(skillcritchance >= 8 && skillcritchance <= 12){  
				 if (random8 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random8 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 } 
			 if(skillcritchance >= 13 && skillcritchance <= 17){ 
				 if (random8 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random8 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 } 
			 if(skillcritchance >= 18 && skillcritchance <= 22){ 
				 if (random8 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random8 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 } 
			 if(skillcritchance >= 23){ 
				 if (random8 == 11) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} // if player has FD or FD CCSR pot (increase crit damage 50%) then * 2 else * 1.5 
				 if (random8 == 12) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 13) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 14) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
				 if (random8 == 15) {if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}}
			 } 
			 	  
			 if (random8 == 16){} 
			 if (random8 == 17){} 
			 if (random8 == 18){}
			 if (random8 == 19){} 
			 if (random8 == 20){if(cur.FD == 1 || cur.FD_CCSR == 1){Bytecritchance = 2.00; Crit = 2;}else{Bytecritchance = 1.50;  Crit = 2;}} //DIT IS * 1.50 WHITE CRIT CHANCE! & 5% crit chance = Standard!
			 
			 if(TMob8.FDD_FASR == 1 | TMob8.FDD == 1){double DEF = TMob8.getDefence() * 1.25; TargetPlayerdefence = (int)DEF;} // if target has FDD then increase his defence HERE by 25% ( * 1.25 ) 
			 else { TargetPlayerdefence = TMob8.getDefence();} // normal
			 //System.out.println("TargetPlayerdefence = "+ (int)TargetPlayerdefence);
			 int AFdmg8 = checkfury - (int)TargetPlayerdefence;// TOTAL DAMAGE - target defence!
			 TargetPlayerdefence = 0;
			 double Fdmg8 = AFdmg8 * Bytecritchance; // FINAL DAMAGE
			 if (Fdmg <= 0 ){zerocheck = 0;} // if its STILL 0 or below then just hit 0 ( by * 0 = 0 )
			 int Fdmgzerochck8 = (int)Fdmg8 * zerocheck;
			 
			 Random greendice8 = new Random();	 
			int randomgreencrit8 = 1+greendice8.nextInt(40);  // RANDOMIZER devided by 2.5% each!
			 if (randomgreencrit8 == 10) {if(cur.FD == 1 || cur.FD_CCSR == 1){greencrit = 3; Crit = 3;}else{greencrit = 2; Crit = 3;}} 
			 
			 int luca8 = Fdmgzerochck8 * greencrit; // TOTAL FINAL DMG * 2 ( = Green Crit )
			 double luza8 = (double)HidingDamage / 100;
			 double FinalDamage8; 
			 if(luza8 != 0 && seqway != buffdata.getBedoonglist(seqway)){FinalDamage8 = luca8 * luza8;}
			 else{FinalDamage8 = luca8;}
			 Random pdmgdice8 = new Random();	 
			 int randomdmg8 = 1+pdmgdice8.nextInt(10);  // RANDOMIZER devided by 20% each!
			 if (randomdmg8 == 1){inc = FinalDamage8 * 1.050;} 
			 if (randomdmg8 == 2){inc = FinalDamage8 * 1.040;}
			 if (randomdmg8 == 3){inc = FinalDamage8 * 1.035;} 
			 if (randomdmg8 == 4){inc = FinalDamage8 * 1.025;}
			 if (randomdmg8 == 5){inc = FinalDamage8 * 1.000;} 
			 if (randomdmg8 == 6){inc = FinalDamage8 * 0.990;}
			 if (randomdmg8 == 7){inc = FinalDamage8 * 0.985;} 
			 if (randomdmg8 == 8){inc = FinalDamage8 * 0.975;}
			 if (randomdmg8 == 9){inc = FinalDamage8 * 0.960;} 
			 if (randomdmg8 == 10){inc = FinalDamage8 *0.950;}

			 if(Crit == 2||Crit == 3){inc = inc + critdmg;}
			
			 Random missdice1 = new Random();
			 int randommiss1 = 1+missdice1.nextInt(20);  // 1/4 on miss
			 if(cur.FASR == 1){
			 if (randommiss1 == 6){}
			 if (randommiss1 == 7){}
			 if (randommiss1 == 8){}
			 if (randommiss1 == 9){}
			 if (randommiss1 == 10){}
			 }
			 else
			 if(attacksuccesrate >= 1 && attacksuccesrate <= 7){ 
			 if (randommiss1 == 6){}
			 if (randommiss1 == 7){inc = 0; Crit = 0;}
			 if (randommiss1 == 8){inc = 0; Crit = 0;}
			 if (randommiss1 == 9){inc = 0; Crit = 0;}
			 if (randommiss1 == 10){inc = 0; Crit = 0;}
			 }
			 else
			 if(attacksuccesrate >= 8 && attacksuccesrate <= 12){ 
			 if (randommiss1 == 6){}
			 if (randommiss1 == 7){}
			 if (randommiss1 == 8){inc = 0; Crit = 0;}
			 if (randommiss1 == 9){inc = 0; Crit = 0;}
			 if (randommiss1 == 10){inc = 0; Crit = 0;}
			 }
			 else
			 if(attacksuccesrate >= 13 && attacksuccesrate <= 17){ 
			 if (randommiss1 == 6){}
			 if (randommiss1 == 7){}
			 if (randommiss1 == 8){}
			 if (randommiss1 == 9){inc = 0; Crit = 0;}
			 if (randommiss1 == 10){inc = 0; Crit = 0;}
			 }
			 else
			 if(attacksuccesrate >= 18 && attacksuccesrate <= 22){ 
			 if (randommiss1 == 6){}
			 if (randommiss1 == 7){}
			 if (randommiss1 == 8){}
			 if (randommiss1 == 9){}
			 if (randommiss1 == 10){inc = 0; Crit = 0;}
			 }
			 else
			 if(attacksuccesrate >= 23){ 
			 if (randommiss1 == 6){}
			 if (randommiss1 == 7){}
			 if (randommiss1 == 8){}
			 if (randommiss1 == 9){}
			 if (randommiss1 == 10){}
			 }else{
			 if (randommiss1 == 6){inc = 0; Crit = 0;}
			 if (randommiss1 == 7){inc = 0; Crit = 0;}
			 if (randommiss1 == 8){inc = 0; Crit = 0;}
			 if (randommiss1 == 9){inc = 0; Crit = 0;}
			 if (randommiss1 == 10){inc = 0; Crit = 0;}
			 }
			// 0x01 = normal | 0x02 = white crit | 0x05 = green crit
			 if(Crit == 0){pckt1[204] = (byte)0x00;} // miss
			 if(Crit == 1){pckt1[204] = (byte)0x01;} // * 1 normal damage ( STANDARD)
			 if(Crit == 2){pckt1[204] = (byte)0x02;} // * 1.5 & * 2 white crit
			 if(Crit == 3){pckt1[204] = (byte)0x05;} // * 2 green crit
			
			  if(TMob8.getMobID() == 29501){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob8.getMobID() == 29502){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob8.getMobID() == 29503){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob8.getMobID() == 29504){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob8.getMobID() == 29505){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			  else
			  if(TMob8.getMobID() == 29506){inc = (int)inc / 10;  if(Charstuff.getInstance().getguildbuffs(cur.guild.GuildID) != 0){cur.respondguildTIMED("Cant attack stone when already have a Guild Buff.", cur.GetChannel()); return;}}
			 Crit = 1;			 // reset to *1 ( Normal Damage )
			 greencrit = 1;		 // reset to *1 ( Normal Damage )
			 zerocheck = 1;		 // reset to *1 ( Normal Damage )
			 Bytecritchance = 1; // reset to *1 ( Normal Damage )
			 int newhp8 = TMob8.hp - (int)inc;
			 
			 byte[] finaldmg8 = BitTools.intToByteArray((int)inc); 
			 for(int i=0;i<4;i++) {
				 pckt1[i+212] = finaldmg8[i];						
			 } 
		
			 
			 byte[] newhpz8 = BitTools.intToByteArray(newhp8); 
			 byte[] newmanaz8 = BitTools.intToByteArray(5000); 
			 for(int i=0;i<2;i++) {
				 pckt1[i+208] = newhpz8[i];		
				 pckt1[i+216] = newmanaz8[i];
			 }
		 
		 TMob8.setHp(cur.charID,(int)inc, newhp8);
		 inc = 0;
		 cur.sendToMap(pckt1);
		 ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), pckt1);
	   
	}	
	
	public boolean Dice(int rate, int limit){
		Random dice = new Random();
		 int Hit = 1+dice.nextInt(limit);  // 1/4 on miss
		 if(rate == 1){
		 if(Hit == 1){return true;}
		 }else
		 if(rate == 2){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 }else
		 if(rate == 3){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
	     }else
		 if(rate == 4){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
	     }	else
		 if(rate == 5){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
	     }else
		 if(rate == 6){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
	     }else
		 if(rate == 7){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
	     }else
		 if(rate == 8){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
	     }else
		 if(rate == 9){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
	     }else
		 if(rate == 10){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
	     }else
		 if(rate == 11){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
	     }else
		 if(rate == 12){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
	     }else
		 if(rate == 13){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
		 if(Hit == 13){return true;}
	     }else
		 if(rate == 14){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
		 if(Hit == 13){return true;}
		 if(Hit == 14){return true;}
	     }else
		 if(rate == 15){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
		 if(Hit == 13){return true;}
		 if(Hit == 14){return true;}
		 if(Hit == 15){return true;}
	     }else
		 if(rate == 16){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
		 if(Hit == 13){return true;}
		 if(Hit == 14){return true;}
		 if(Hit == 15){return true;}
		 if(Hit == 16){return true;}
	     }else
		 if(rate == 17){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
		 if(Hit == 13){return true;}
		 if(Hit == 14){return true;}
		 if(Hit == 15){return true;}
		 if(Hit == 16){return true;}
		 if(Hit == 17){return true;}
	     }else
		 if(rate == 18){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
		 if(Hit == 13){return true;}
		 if(Hit == 14){return true;}
		 if(Hit == 15){return true;}
		 if(Hit == 16){return true;}
		 if(Hit == 17){return true;}
		 if(Hit == 18){return true;}
	     }else
		 if(rate == 19){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
		 if(Hit == 13){return true;}
		 if(Hit == 14){return true;}
		 if(Hit == 15){return true;}
		 if(Hit == 16){return true;}
		 if(Hit == 17){return true;}
		 if(Hit == 18){return true;}
	     if(Hit == 19){return true;}
	     }else
		 if(rate == 20){
		 if(Hit == 1){return true;}
		 if(Hit == 2){return true;}
		 if(Hit == 3){return true;}
		 if(Hit == 4){return true;}
		 if(Hit == 5){return true;}
		 if(Hit == 6){return true;}
		 if(Hit == 7){return true;}
		 if(Hit == 8){return true;}
		 if(Hit == 9){return true;}
		 if(Hit == 10){return true;}
		 if(Hit == 11){return true;}
		 if(Hit == 12){return true;}
		 if(Hit == 13){return true;}
		 if(Hit == 14){return true;}
		 if(Hit == 15){return true;}
		 if(Hit == 16){return true;}
		 if(Hit == 17){return true;}
		 if(Hit == 18){return true;}
		 if(Hit == 19){return true;}
		 if(Hit == 20){return true;}
	     }
	return false;
	}
	
	public void AddDot(int Target, int DotsIconID, int DotsValue, int DotsTime, int DotsSLOT, int DETERMINER, Character cur){//DETERMINE: 1 = player | 2 = mob
		if(DETERMINER == 1){
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
		}}}else{
		    Mob TMob = WMap.getInstance().getMob(Target, cur.getCurrentMap()); 
			if(TMob != null){
			if(TMob.getMobdata().getData().getfamerate100() != 1)
			{
			byte[] chid = BitTools.intToByteArray(TMob.getUid());
			byte[] buff = new byte[44];
			 buff[0] = (byte)0x2c; 
			 buff[4] = (byte)0x05;
			 buff[6] = (byte)0x1f;
			 buff[8] = (byte)0x02;// 1 = player | 2 = mob
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
			 
			 TMob.setDots(DotsSLOT, DotsIconID, DotsTime, DotsValue);
			 
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
			 TMob.send(buff);
			}
		  }
		 }
		}
	
	
}
