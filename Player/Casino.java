package Player;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
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

public class Casino implements Cloneable{
	private WMap wmap = WMap.getInstance();
	public ConcurrentMap<Integer, Integer> SLOT1CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT2CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT3CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT4CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT5CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT6CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT7CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT8CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT9CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT10CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT11CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT12CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT13CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT14CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT15CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT16CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT17CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public ConcurrentMap<Integer, Integer> SLOT18CharidtoGold = new ConcurrentHashMap<Integer, Integer>();
	public int SLOT1Total = 0;
	public int SLOT2Total = 0;
	public int SLOT3Total = 0;
	public int SLOT4Total = 0;
	public int SLOT5Total = 0;
	public int SLOT6Total = 0;
	public int SLOT7Total = 0;
	public int SLOT8Total = 0;
	public int SLOT9Total = 0;
	public int SLOT10Total = 0;
	public int SLOT11Total = 0;
	public int SLOT12Total = 0;
	public int SLOT13Total = 0;
	public int SLOT14Total = 0;
	public int SLOT15Total = 0;
	public int SLOT16Total = 0;
	public int SLOT17Total = 0;
	public int SLOT18Total = 0;
	public boolean AllowDonateFame = false;
	public int GlobalTimer = 35;
	public int DDResult1 = 0;
	public int DDResult2 = 0;
	public int DDResult3 = 0;
	public int DDResult4 = 0;
	public int DDResult5 = 0;
	public int DvTResult1 = 0;
	public int DvTResult2 = 0;
	public int DvTResult3 = 0;
	public int DvTResult4 = 0;
	public int DvTResult5 = 0;
	public int Header = 3;
	public int rolleddicevisual = 0;
	public int Markiplier = 0;
	
	/*
	 * Each individual put our own golds in the hashmaps + we calc a TOTAL of that slot.
	 * then when an interaction is made we update to ALL.
	 * when all has put their mojo, we do iterator trough all hashmaps and calculate each individuals cut by it.
	 * then we give everyone their cuts and update to ALL to notify em again.
	 * End, ClearALL, Next round.
	 */
	
	private	static Casino instance; 
    public synchronized static Casino getInstance(){
        if (instance == null){
                instance = new Casino();
        }
        return instance;
    }
    
	// startup casino for first time
	public Casino() {				
		
		CasinoTimer();
	}
	static boolean running = true;
	 public static synchronized void CasinoTimer() {
		    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
		      public void run() {
		while(running) {
		//clean all and run and awllow donate fame	
			getInstance().AllowDonateFame = true;
			
		//update to all
			try{Thread.sleep(1000);}catch(InterruptedException e){}
			getInstance().GlobalTimer = 34;
			try{Thread.sleep(1000);}catch(InterruptedException e){}
			getInstance().GlobalTimer = 33;
			try{Thread.sleep(1000);}catch(InterruptedException e){}
			getInstance().GlobalTimer = 32;
			try{Thread.sleep(1000);}catch(InterruptedException e){}
			getInstance().GlobalTimer = 31;
			try{Thread.sleep(1000);}catch(InterruptedException e){}
			getInstance().GlobalTimer = 30;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 29;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 28;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 27;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 26;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 25;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 24;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 23;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 22;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 21;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 20;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 19;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 18;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 17;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 16;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 15;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 14;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 13;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 12;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 11;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 10;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 9;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 8;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 7;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 6;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 5;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 4;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 3;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 2;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 1;
		try{Thread.sleep(1000);}catch(InterruptedException e){}
		getInstance().GlobalTimer = 0;
		// end round, block donate fame, return results 
		getInstance().AllowDonateFame = false;
		//calc all shit and Dices falling
		
		//Roll the dices
		Random rDice1 = new Random(); 
		Random rDice2 = new Random(); 
		Random rTiger1 = new Random(); 
		Random rTiger2 = new Random(); 
		int Dice1 = 1+rDice1.nextInt(6);
		int Dice2 = 1+rDice2.nextInt(6);
		int Tiger1 = 1+rTiger1.nextInt(6);
		int Tiger2 = 1+rTiger2.nextInt(6);
		int TotalDice = Dice1 + Dice2;
		int TotalTiger = Tiger1 + Tiger2;
		
		//Tiger & Dragons
		int DvsTwins = 0;// 1 = TotalTiger|2 = TotalDicewins|3 = Drawwins|4 = 180 wins
		if(TotalDice > TotalTiger){DvsTwins=15;}
		if(TotalDice < TotalTiger){DvsTwins=16;}
		if(TotalDice == TotalTiger){DvsTwins=17;}
		if(Dice1 == Dice2 && Tiger1 == Tiger2){DvsTwins=18;}
		
		//Double Dragons
		int DDwins = 0;
		int DDwins0 = 0;
		int DDwins1 = 0;
		int DDwins2 = 0;
		if(Dice1 == 1 && Dice2 == 1){DDwins = 1;}
		if(Dice1 == 2 && Dice2 == 2){DDwins = 2;}
		if(Dice1 == 3 && Dice2 == 3){DDwins = 3;}
		if(Dice1 == 4 && Dice2 == 4){DDwins = 4;}
		if(Dice1 == 5 && Dice2 == 5){DDwins = 5;}
		if(Dice1 == 6 && Dice2 == 6){DDwins = 6;}
		
		if(Dice1 == 1 && Dice2 == 2){DDwins0 = 7;}
		if(Dice1 == 5 && Dice2 == 6){DDwins0 = 7;}
		if(Dice1 == 2 && Dice2 == 3){DDwins0 = 8;}
		if(Dice1 == 4 && Dice2 == 6){DDwins0 = 8;}
		if(Dice1 == 1 && Dice2 == 4){DDwins0 = 9;}
		if(Dice1 == 3 && Dice2 == 6){DDwins0 = 9;}
		
		if(Dice1 == 2 && Dice2 == 1){DDwins0 = 7;}
		if(Dice1 == 6 && Dice2 == 5){DDwins0 = 7;}
		if(Dice1 == 3 && Dice2 == 2){DDwins0 = 8;}
		if(Dice1 == 6 && Dice2 == 4){DDwins0 = 8;}
		if(Dice1 == 4 && Dice2 == 1){DDwins0 = 9;}
		if(Dice1 == 6 && Dice2 == 3){DDwins0 = 9;}
		
		if(Dice1 == 1){DDwins1 = 10;}
		if(Dice1 == 3){DDwins1 = 10;}
		if(Dice1 == 5){DDwins1 = 10;}
		if(Dice2 == 2){DDwins1 = 11;}
		if(Dice2 == 4){DDwins1 = 11;}
		if(Dice2 == 6){DDwins1 = 11;}
		
		if(TotalDice >= 2 && TotalDice <= 6){DDwins2 = 12;}
		if(TotalDice >= 8 && TotalDice <= 12){DDwins2 = 13;}
		if(TotalDice == 7){DDwins2 = 14;}
		
		//System.out.println("TotalDice:"+TotalDice+" TotalTiger:"+TotalTiger);
		//System.out.println("DvsTwins:"+DvsTwins);
		
		getInstance().Header = 4;
		getInstance().Tigerdicesvisual(Tiger1, Tiger2);
		Iterator<Map.Entry<Integer, Character>> iter2 = WMap.getInstance().getCharacterMap().entrySet().iterator();
		while(iter2.hasNext()) {
			Map.Entry<Integer, Character> pairs2 = iter2.next();
			Character tmp = pairs2.getValue();
			if(tmp != null && tmp.getCurrentMap() == 100){
				getInstance().UpdateToMe(tmp);
			}
		}
		
		try{Thread.sleep(8000);}catch(InterruptedException e){}
		
		// set dices visaul
		getInstance().Header = 5;
		getInstance().dicesvisual(Dice1, Dice2, DvsTwins);
		
		Iterator<Map.Entry<Integer, Character>> iter = WMap.getInstance().getCharacterMap().entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<Integer, Character> pairs = iter.next();
			Character tmp = pairs.getValue();
			if(tmp != null && tmp.getCurrentMap() == 100){
			int newfame = tmp.getFame();
			int charid = tmp.getCharID();
			
				//x33
				if(getInstance().SLOT1CharidtoGold.containsKey(charid)){if(DDwins == 1){int tempfame = getInstance().SLOT1CharidtoGold.get(charid) * 33; newfame = newfame + tempfame;}}
				if(getInstance().SLOT2CharidtoGold.containsKey(charid)){if(DDwins == 2){int tempfame = getInstance().SLOT2CharidtoGold.get(charid) * 33; newfame = newfame + tempfame;}}
				if(getInstance().SLOT3CharidtoGold.containsKey(charid)){if(DDwins == 3){int tempfame = getInstance().SLOT3CharidtoGold.get(charid) * 33; newfame = newfame + tempfame;}}
				if(getInstance().SLOT4CharidtoGold.containsKey(charid)){if(DDwins == 4){int tempfame = getInstance().SLOT4CharidtoGold.get(charid) * 33; newfame = newfame + tempfame;}}
				if(getInstance().SLOT5CharidtoGold.containsKey(charid)){if(DDwins == 5){int tempfame = getInstance().SLOT5CharidtoGold.get(charid) * 33; newfame = newfame + tempfame;}}
				if(getInstance().SLOT6CharidtoGold.containsKey(charid)){if(DDwins == 6){int tempfame = getInstance().SLOT6CharidtoGold.get(charid) * 33; newfame = newfame + tempfame;}}
				
				//x8
				if(getInstance().SLOT7CharidtoGold.containsKey(charid)){if(DDwins0 == 7){int tempfame = getInstance().SLOT7CharidtoGold.get(charid) * 8; newfame = newfame + tempfame;}}
				if(getInstance().SLOT8CharidtoGold.containsKey(charid)){if(DDwins0 == 8){int tempfame = getInstance().SLOT8CharidtoGold.get(charid) * 8; newfame = newfame + tempfame;}}
				if(getInstance().SLOT9CharidtoGold.containsKey(charid)){if(DDwins0 == 9){int tempfame = getInstance().SLOT9CharidtoGold.get(charid) * 8; newfame = newfame + tempfame;}}
				
				//x4
				if(getInstance().SLOT10CharidtoGold.containsKey(charid)){if(DDwins1 == 10){int tempfame = getInstance().SLOT10CharidtoGold.get(charid) * 4; newfame = newfame + tempfame;}}
				if(getInstance().SLOT11CharidtoGold.containsKey(charid)){if(DDwins1 == 11){int tempfame = getInstance().SLOT11CharidtoGold.get(charid) * 4; newfame = newfame + tempfame;}}
				
				//x2
				if(getInstance().SLOT12CharidtoGold.containsKey(charid)){if(DDwins2 == 12){int tempfame = getInstance().SLOT12CharidtoGold.get(charid) * 2; newfame = newfame + tempfame;}}
				if(getInstance().SLOT13CharidtoGold.containsKey(charid)){if(DDwins2 == 13){int tempfame = getInstance().SLOT13CharidtoGold.get(charid) * 2; newfame = newfame + tempfame;}}
				//x5
				if(getInstance().SLOT14CharidtoGold.containsKey(charid)){if(DDwins2 == 14){int tempfame = getInstance().SLOT14CharidtoGold.get(charid) * 5; newfame = newfame + tempfame;}}
				
				//x2
				if(getInstance().SLOT15CharidtoGold.containsKey(charid)){if(DvsTwins == 15){int tempfame = getInstance().SLOT15CharidtoGold.get(charid) * 2; newfame = newfame + tempfame;}}
				if(getInstance().SLOT16CharidtoGold.containsKey(charid)){if(DvsTwins == 16){int tempfame = getInstance().SLOT16CharidtoGold.get(charid) * 2; newfame = newfame + tempfame;}}
				
				//x8
				if(getInstance().SLOT17CharidtoGold.containsKey(charid)){if(DvsTwins == 17){int tempfame = getInstance().SLOT17CharidtoGold.get(charid) * 8; newfame = newfame + tempfame;}}
				
				//x180
				if(getInstance().SLOT18CharidtoGold.containsKey(charid)){if(DvsTwins == 18){int tempfame = getInstance().SLOT18CharidtoGold.get(charid) * 180; newfame = newfame + tempfame;}}
				
				tmp.setFame(newfame);
				//execute it to the douchebag
				getInstance().UpdateToMe(tmp);
			}
		}
		try{Thread.sleep(6000);}catch(InterruptedException e){}
		// Next round.
		
		getInstance().ClearAll();
		getInstance().Header = 3;
		getInstance().GlobalTimer = 35;
		Iterator<Map.Entry<Integer, Character>> iter1 = WMap.getInstance().getCharacterMap().entrySet().iterator();
		while(iter1.hasNext()) {
			Map.Entry<Integer, Character> pairs1 = iter1.next();
			Character tmp = pairs1.getValue();
			if(tmp != null && tmp.getCurrentMap() == 100){
				getInstance().UpdateToMe(tmp);
			}
		}
		}
	  }}).start();
	}
	 
	 public void Tigerdicesvisual(int Dice1, int Dice2){
		 int Markiplier = 0; // 0 = 1,1 | 1 = 1,2 etc
		 
		 if(Dice1 == 1 && Dice2 == 1){Markiplier = 0;}
		 if(Dice1 == 1 && Dice2 == 2){Markiplier = 1;}
		 if(Dice1 == 1 && Dice2 == 3){Markiplier = 2;}
		 if(Dice1 == 1 && Dice2 == 4){Markiplier = 3;}
		 if(Dice1 == 1 && Dice2 == 5){Markiplier = 4;}
		 if(Dice1 == 1 && Dice2 == 6){Markiplier = 5;}
		 
		 if(Dice1 == 2 && Dice2 == 1){Markiplier = 10;}
		 if(Dice1 == 2 && Dice2 == 2){Markiplier = 11;}
		 if(Dice1 == 2 && Dice2 == 3){Markiplier = 12;}
		 if(Dice1 == 2 && Dice2 == 4){Markiplier = 13;}
		 if(Dice1 == 2 && Dice2 == 5){Markiplier = 14;}
		 if(Dice1 == 2 && Dice2 == 6){Markiplier = 15;}
		 
		 if(Dice1 == 3 && Dice2 == 1){Markiplier = 20;}
		 if(Dice1 == 3 && Dice2 == 2){Markiplier = 21;}
		 if(Dice1 == 3 && Dice2 == 3){Markiplier = 22;}
		 if(Dice1 == 3 && Dice2 == 4){Markiplier = 23;}
		 if(Dice1 == 3 && Dice2 == 5){Markiplier = 24;}
		 if(Dice1 == 3 && Dice2 == 6){Markiplier = 25;}
		 
		 if(Dice1 == 4 && Dice2 == 1){Markiplier = 30;}
		 if(Dice1 == 4 && Dice2 == 2){Markiplier = 31;}
		 if(Dice1 == 4 && Dice2 == 3){Markiplier = 32;}
		 if(Dice1 == 4 && Dice2 == 4){Markiplier = 33;}
		 if(Dice1 == 4 && Dice2 == 5){Markiplier = 34;}
		 if(Dice1 == 4 && Dice2 == 6){Markiplier = 35;}
		 
		 if(Dice1 == 5 && Dice2 == 1){Markiplier = 40;}
		 if(Dice1 == 5 && Dice2 == 2){Markiplier = 41;}
		 if(Dice1 == 5 && Dice2 == 3){Markiplier = 42;}
		 if(Dice1 == 5 && Dice2 == 4){Markiplier = 43;}
		 if(Dice1 == 5 && Dice2 == 5){Markiplier = 44;}
		 if(Dice1 == 5 && Dice2 == 6){Markiplier = 45;}
		 
		 if(Dice1 == 6 && Dice2 == 1){Markiplier = 50;}
		 if(Dice1 == 6 && Dice2 == 2){Markiplier = 51;}
		 if(Dice1 == 6 && Dice2 == 3){Markiplier = 52;}
		 if(Dice1 == 6 && Dice2 == 4){Markiplier = 53;}
		 if(Dice1 == 6 && Dice2 == 5){Markiplier = 54;}
		 if(Dice1 == 6 && Dice2 == 6){Markiplier = 55;}
		 
		 this.Markiplier = Markiplier;
	 }
	 
	 
	 //calc dice visual
	 public void dicesvisual(int Dice1, int Dice2, int DvTslot){
		 int rolleddicevisual = 0; // 0 = 1,1 | 1 = 1,2 etc
		 int TvDicon = 0;
		 
		 if(Dice1 == 1 && Dice2 == 1){rolleddicevisual = 0;}
		 if(Dice1 == 1 && Dice2 == 2){rolleddicevisual = 1;}
		 if(Dice1 == 1 && Dice2 == 3){rolleddicevisual = 2;}
		 if(Dice1 == 1 && Dice2 == 4){rolleddicevisual = 3;}
		 if(Dice1 == 1 && Dice2 == 5){rolleddicevisual = 4;}
		 if(Dice1 == 1 && Dice2 == 6){rolleddicevisual = 5;}
		 
		 if(Dice1 == 2 && Dice2 == 1){rolleddicevisual = 10;}
		 if(Dice1 == 2 && Dice2 == 2){rolleddicevisual = 11;}
		 if(Dice1 == 2 && Dice2 == 3){rolleddicevisual = 12;}
		 if(Dice1 == 2 && Dice2 == 4){rolleddicevisual = 13;}
		 if(Dice1 == 2 && Dice2 == 5){rolleddicevisual = 14;}
		 if(Dice1 == 2 && Dice2 == 6){rolleddicevisual = 15;}
		 
		 if(Dice1 == 3 && Dice2 == 1){rolleddicevisual = 20;}
		 if(Dice1 == 3 && Dice2 == 2){rolleddicevisual = 21;}
		 if(Dice1 == 3 && Dice2 == 3){rolleddicevisual = 22;}
		 if(Dice1 == 3 && Dice2 == 4){rolleddicevisual = 23;}
		 if(Dice1 == 3 && Dice2 == 5){rolleddicevisual = 24;}
		 if(Dice1 == 3 && Dice2 == 6){rolleddicevisual = 25;}
		 
		 if(Dice1 == 4 && Dice2 == 1){rolleddicevisual = 30;}
		 if(Dice1 == 4 && Dice2 == 2){rolleddicevisual = 31;}
		 if(Dice1 == 4 && Dice2 == 3){rolleddicevisual = 32;}
		 if(Dice1 == 4 && Dice2 == 4){rolleddicevisual = 33;}
		 if(Dice1 == 4 && Dice2 == 5){rolleddicevisual = 34;}
		 if(Dice1 == 4 && Dice2 == 6){rolleddicevisual = 35;}
		 
		 if(Dice1 == 5 && Dice2 == 1){rolleddicevisual = 40;}
		 if(Dice1 == 5 && Dice2 == 2){rolleddicevisual = 41;}
		 if(Dice1 == 5 && Dice2 == 3){rolleddicevisual = 42;}
		 if(Dice1 == 5 && Dice2 == 4){rolleddicevisual = 43;}
		 if(Dice1 == 5 && Dice2 == 5){rolleddicevisual = 44;}
		 if(Dice1 == 5 && Dice2 == 6){rolleddicevisual = 45;}
		 
		 if(Dice1 == 6 && Dice2 == 1){rolleddicevisual = 50;}
		 if(Dice1 == 6 && Dice2 == 2){rolleddicevisual = 51;}
		 if(Dice1 == 6 && Dice2 == 3){rolleddicevisual = 52;}
		 if(Dice1 == 6 && Dice2 == 4){rolleddicevisual = 53;}
		 if(Dice1 == 6 && Dice2 == 5){rolleddicevisual = 54;}
		 if(Dice1 == 6 && Dice2 == 6){rolleddicevisual = 55;}
		 
		 if(DvTslot == 15){TvDicon = 0;}
		 if(DvTslot == 16){TvDicon = 1;}
		 if(DvTslot == 17){TvDicon = 2;}
		 if(DvTslot == 18){TvDicon = 3;}
		 
		 
			this.DDResult5 = this.DDResult4;
			this.DDResult4 = this.DDResult3;
			this.DDResult3 = this.DDResult2;
			this.DDResult2 = this.DDResult1;
			this.DDResult1 = rolleddicevisual;
			
			this.DvTResult5 = this.DvTResult4;
			this.DvTResult4 = this.DvTResult3;
			this.DvTResult3 = this.DvTResult2;
			this.DvTResult2 = this.DvTResult1;
			this.DvTResult1 = TvDicon;
		 
		 this.rolleddicevisual = rolleddicevisual;
	 }
	 
	public void DicesFallign(Character cur, int Dice1, int Dice2){
		byte[] dice1 = BitTools.intToByteArray(901528);
		byte[] dice2 = BitTools.intToByteArray(901528);
		
		byte[] D1id = BitTools.intToByteArray(10000);
		byte[] D2id = BitTools.intToByteArray(10001);
		
		//901528 = ?
		byte[] fury = new byte[56];
		fury[0] = (byte)0x1c;
		fury[4] = (byte)0x05;
		fury[6] = (byte)0x34;
		fury[8] = (byte)0x02;
		//fury[12] = (byte)0x01;
		fury[16] = (byte)0x01;
		
		//fury[20] = (byte)0x99;
		//fury[21] = (byte)0xc1;
		//fury[22] = (byte)0x0d;
		
		//fury[48] = (byte)0xa2;
		//fury[49] = (byte)0xc1;
		//fury[50] = (byte)0x0d;
		
		for(int i=0;i<4;i++) {
		fury[20+i] = dice1[i];
		fury[48+i] = dice2[i];
		fury[12+i] = D1id[i];
		fury[40+i] = D2id[i];
		}

		fury[28] = (byte)0x1c;
		fury[32] = (byte)0x05;
		fury[34] = (byte)0x34;
		fury[36] = (byte)0x02;
		//fury[40] = (byte)0x02;
		fury[44] = (byte)0x01;
		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);
	}	 	
	
	//update only to me
	public void UpdateToMe(Character cur){
		
		byte[] chid = BitTools.intToByteArray(cur.getCharID());
		// check if player is withing the play rules
		
		//make gold casino.. maybe
		
		
		if(cur.getFame() > 2100000000){cur.respondguildTIMED("Too much Fame, can only play with less then 2100000000 Fame.",cur.GetChannel());return;}
		byte[] fame = BitTools.LongToByteArrayREVERSE(cur.getFame());
		byte[] SLOT1 = BitTools.shortToByteArray((short)this.SLOT1Total);
		byte[] SLOT2 = BitTools.shortToByteArray((short)this.SLOT2Total);
		byte[] SLOT3 = BitTools.shortToByteArray((short)this.SLOT3Total);
		byte[] SLOT4 = BitTools.shortToByteArray((short)this.SLOT4Total);
		byte[] SLOT5 = BitTools.shortToByteArray((short)this.SLOT5Total);
		byte[] SLOT6 = BitTools.shortToByteArray((short)this.SLOT6Total);
		byte[] SLOT7 = BitTools.shortToByteArray((short)this.SLOT7Total);
		byte[] SLOT8 = BitTools.shortToByteArray((short)this.SLOT8Total);
		byte[] SLOT9 = BitTools.shortToByteArray((short)this.SLOT9Total);
		byte[] SLOT10 = BitTools.shortToByteArray((short)this.SLOT10Total);
		byte[] SLOT11 = BitTools.shortToByteArray((short)this.SLOT11Total);
		byte[] SLOT12 = BitTools.shortToByteArray((short)this.SLOT12Total);
		byte[] SLOT13 = BitTools.shortToByteArray((short)this.SLOT13Total);
		byte[] SLOT14 = BitTools.shortToByteArray((short)this.SLOT14Total);
		byte[] SLOT15 = BitTools.shortToByteArray((short)this.SLOT15Total);
		byte[] SLOT16 = BitTools.shortToByteArray((short)this.SLOT16Total);
		byte[] SLOT17 = BitTools.shortToByteArray((short)this.SLOT17Total);
		byte[] SLOT18 = BitTools.shortToByteArray((short)this.SLOT18Total);
		
		byte[] fury = new byte[192];
		fury[0] = (byte)0xc0;
		fury[4] = (byte)0x04;
		fury[6] = (byte)0x64;
		fury[8] = (byte)0x01;
		for(int i=0;i<4;i++) {
		fury[12+i] = chid[i]; 
		fury[24+i] = fame[i]; 
		}
		
		// bet
		if(this.Header == 3){
		fury[16] = (byte)0x03; // 3 = open window
		fury[18] = (byte)0xff; // ?
		fury[20] = (byte)this.GlobalTimer; // Time in sec
		}
		
		//Mr asian said: fuck you rice!
		if(this.Header == 4){ // test shit shit bc it doesnt work, 1+5 = 2 + 3? IDK WEIRD test it with playcasino:?:?
		//System.out.println("this.Markiplier :"+this.Markiplier);	
		fury[16] = (byte)0x04;
		fury[18] = (byte)this.Markiplier;
		fury[20] = (byte)0x05;
		}
		
		// result
		if(this.Header == 5){ // test shit shit bc it doesnt work, 1+5 = 2 + 3? IDK WEIRD test it with playcasino:?:?
		//System.out.println("rolleddicevisual:"+this.rolleddicevisual);	
		fury[16] = (byte)0x05;
		fury[18] = (byte)this.rolleddicevisual;
		fury[20] = (byte)0x0f;
		}
		
		fury[28] = (byte)this.DDResult1;
		fury[29] = (byte)this.DDResult2;
		fury[30] = (byte)this.DDResult3;
		fury[31] = (byte)this.DDResult4;
		fury[32] = (byte)this.DDResult5;

		fury[33] = (byte)this.DvTResult1;
		fury[34] = (byte)this.DvTResult2;
		fury[35] = (byte)this.DvTResult3;
		fury[36] = (byte)this.DvTResult4;
		fury[37] = (byte)this.DvTResult5;
		
		for(int i=0;i<2;i++) {
		fury[48+i] = SLOT1[i];  
		fury[56+i] = SLOT2[i]; 
		fury[64+i] = SLOT3[i]; 
		fury[72+i] = SLOT4[i]; 
		fury[80+i] = SLOT5[i]; 
		fury[88+i] = SLOT6[i]; 
		fury[96+i] = SLOT7[i]; 
		fury[104+i] = SLOT8[i]; 
		fury[112+i] = SLOT9[i]; 
		fury[120+i] = SLOT10[i]; 
		fury[128+i] = SLOT11[i]; 
		fury[136+i] = SLOT12[i]; 
		fury[144+i] = SLOT13[i]; 
		fury[152+i] = SLOT14[i]; 
		fury[160+i] = SLOT15[i]; 
		fury[168+i] = SLOT16[i]; 
		fury[176+i] = SLOT17[i]; 
		fury[184+i] = SLOT18[i]; 
		}

		ServerFacade.getInstance().addWriteByChannel(cur.GetChannel(), fury);
	}
	
	
	// put gold
	public void addgold(int SLOT, int charid, int addgold) {		
		Character cur = wmap.getCharacter(charid);
		if(cur == null){return;}
		if(!this.AllowDonateFame){cur.respondguildTIMED("Round is over, wait for a new round to start.",cur.GetChannel());return;}
		if(addgold <= 0){return;}
		// check if player is withing the play rules
		if(cur.getFame() > 2100000000){cur.respondguildTIMED("Too much Fame, can only play with less then 2100000000 Fame.",cur.GetChannel());return;}
		if(cur.getFame() < 50){cur.respondguildTIMED("You need atleast 50 Fame to play.",cur.GetChannel());return;}	
		if(cur.getFame() <= addgold){cur.respondguildTIMED("You do not have enough fame to spend.",cur.GetChannel());return;}
		
		
		this.setSLOT(SLOT, charid, addgold);
		cur.setFame(cur.getFame() - addgold);
		Iterator<Map.Entry<Integer, Character>> iter1 = WMap.getInstance().getCharacterMap().entrySet().iterator();
		while(iter1.hasNext()) {
			Map.Entry<Integer, Character> pairs1 = iter1.next();
			Character tmp = pairs1.getValue();
			if(tmp != null && tmp.getCurrentMap() == 100){
				getInstance().UpdateToMe(tmp);
			}
		}
	}
	
	//set a SLOT
	public void setSLOT(int SLOT, int charid, int addgold){
		
		if(SLOT == 1){this.SLOT1Total = this.SLOT1Total + addgold; if(this.SLOT1CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT1CharidtoGold.get(charid); this.SLOT1CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT1CharidtoGold.put(charid, addgold);}}
		if(SLOT == 2){this.SLOT2Total = this.SLOT2Total + addgold; if(this.SLOT2CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT2CharidtoGold.get(charid); this.SLOT2CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT2CharidtoGold.put(charid, addgold);}}
		if(SLOT == 3){this.SLOT3Total = this.SLOT3Total + addgold; if(this.SLOT3CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT3CharidtoGold.get(charid); this.SLOT3CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT3CharidtoGold.put(charid, addgold);}}
		if(SLOT == 4){this.SLOT4Total = this.SLOT4Total + addgold; if(this.SLOT4CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT4CharidtoGold.get(charid); this.SLOT4CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT4CharidtoGold.put(charid, addgold);}}
		if(SLOT == 5){this.SLOT5Total = this.SLOT5Total + addgold; if(this.SLOT5CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT5CharidtoGold.get(charid); this.SLOT5CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT5CharidtoGold.put(charid, addgold);}}
		if(SLOT == 6){this.SLOT6Total = this.SLOT6Total + addgold; if(this.SLOT6CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT6CharidtoGold.get(charid); this.SLOT6CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT6CharidtoGold.put(charid, addgold);}}
		if(SLOT == 7){this.SLOT7Total = this.SLOT7Total + addgold; if(this.SLOT7CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT7CharidtoGold.get(charid); this.SLOT7CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT7CharidtoGold.put(charid, addgold);}}
		if(SLOT == 8){this.SLOT8Total = this.SLOT8Total + addgold; if(this.SLOT8CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT8CharidtoGold.get(charid); this.SLOT8CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT8CharidtoGold.put(charid, addgold);}}
		if(SLOT == 9){this.SLOT9Total = this.SLOT9Total + addgold; if(this.SLOT9CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT9CharidtoGold.get(charid); this.SLOT9CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT9CharidtoGold.put(charid, addgold);}}
		if(SLOT == 10){this.SLOT10Total = this.SLOT10Total + addgold; if(this.SLOT10CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT10CharidtoGold.get(charid); this.SLOT10CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT10CharidtoGold.put(charid, addgold);}}
		if(SLOT == 11){this.SLOT11Total = this.SLOT11Total + addgold; if(this.SLOT11CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT11CharidtoGold.get(charid); this.SLOT11CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT11CharidtoGold.put(charid, addgold);}}
		if(SLOT == 12){this.SLOT12Total = this.SLOT12Total + addgold; if(this.SLOT12CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT12CharidtoGold.get(charid); this.SLOT12CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT12CharidtoGold.put(charid, addgold);}}
		if(SLOT == 13){this.SLOT13Total = this.SLOT13Total + addgold; if(this.SLOT13CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT13CharidtoGold.get(charid); this.SLOT13CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT13CharidtoGold.put(charid, addgold);}}
		if(SLOT == 14){this.SLOT14Total = this.SLOT14Total + addgold; if(this.SLOT14CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT14CharidtoGold.get(charid); this.SLOT14CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT14CharidtoGold.put(charid, addgold);}}
		if(SLOT == 15){this.SLOT15Total = this.SLOT15Total + addgold; if(this.SLOT15CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT15CharidtoGold.get(charid); this.SLOT15CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT15CharidtoGold.put(charid, addgold);}}
		if(SLOT == 16){this.SLOT16Total = this.SLOT16Total + addgold; if(this.SLOT16CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT16CharidtoGold.get(charid); this.SLOT16CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT16CharidtoGold.put(charid, addgold);}}
		if(SLOT == 17){this.SLOT17Total = this.SLOT17Total + addgold; if(this.SLOT17CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT17CharidtoGold.get(charid); this.SLOT17CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT17CharidtoGold.put(charid, addgold);}}
		if(SLOT == 18){this.SLOT18Total = this.SLOT18Total + addgold; if(this.SLOT18CharidtoGold.containsKey(charid)){int mycurgoldforthisslot = this.SLOT18CharidtoGold.get(charid); this.SLOT18CharidtoGold.put(charid, mycurgoldforthisslot + addgold);}else{this.SLOT18CharidtoGold.put(charid, addgold);}}

		//System.out.println("SLOT:"+SLOT+" Charid:"+charid+" this.SLOT1Total:"+this.SLOT1Total);
	}
	
	// clear all, mainly used after a round is over...
	public void ClearAll(){
		this.SLOT1CharidtoGold.clear();
		this.SLOT2CharidtoGold.clear();
		this.SLOT3CharidtoGold.clear();
		this.SLOT4CharidtoGold.clear();
		this.SLOT5CharidtoGold.clear();
		this.SLOT6CharidtoGold.clear();
		this.SLOT7CharidtoGold.clear();
		this.SLOT8CharidtoGold.clear();
		this.SLOT9CharidtoGold.clear();
		this.SLOT10CharidtoGold.clear();
		this.SLOT11CharidtoGold.clear();
		this.SLOT12CharidtoGold.clear();
		this.SLOT13CharidtoGold.clear();
		this.SLOT14CharidtoGold.clear();
		this.SLOT15CharidtoGold.clear();
		this.SLOT16CharidtoGold.clear();
		this.SLOT17CharidtoGold.clear();
		this.SLOT18CharidtoGold.clear();
		this.SLOT1Total = 0;
		this.SLOT2Total = 0;
		this.SLOT3Total = 0;
		this.SLOT4Total = 0;
		this.SLOT5Total = 0;
		this.SLOT6Total = 0;
		this.SLOT7Total = 0;
		this.SLOT8Total = 0;
		this.SLOT9Total = 0;
		this.SLOT10Total = 0;
		this.SLOT11Total = 0;
		this.SLOT12Total = 0;
		this.SLOT13Total = 0;
		this.SLOT14Total = 0;
		this.SLOT15Total = 0;
		this.SLOT16Total = 0;
		this.SLOT17Total = 0;
		this.SLOT18Total = 0;
	}
		
}
