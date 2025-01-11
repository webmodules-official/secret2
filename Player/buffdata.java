package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class buffdata implements Cloneable{
	public static Map<Integer, Integer> buffid = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> bufftime = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> buffvalue = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> buffslot = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> bufflist = new HashMap<Integer, Integer>();
	
	public static Map<Integer, Integer> Bedoong = new HashMap<Integer, Integer>();
	
	
	public static int getBedoonglist(int skillid) {
		if(Bedoong.containsKey(skillid)){
			int skillidz = Bedoong.get(skillid);
			//System.out.println("Bedoong: " +skillid+" - " +skillidz);
			return skillidz;
		}else{//System.out.println(skillid+" = null"); 
		return 0; 
		}
	}

	public static void setBedoonglist(int buff, int buffz) {
		Bedoong.put(Integer.valueOf(buff), Integer.valueOf(buffz)); 
		//System.out.println("Bedoong: " +buff+" - " +buffz);
	}
	
	
	public static int getbufflist(int skillid) {
		if(bufflist.containsKey(skillid)){
			int skillidz = bufflist.get(skillid);
			////System.out.println("buffid: " +skillid+" - " +skillidz);
			return skillidz;
		}else{// //System.out.println(skillid+" is not an buff"); 
		return 0; 
		}
	}

	public static void setbufflist(int buff, int buffz) {
		bufflist.put(Integer.valueOf(buff), Integer.valueOf(buffz)); 
		//System.out.println("bufflist: " +buff+" - " +buffz);
	}
	
	public static int getbuffid(int skillid) {
		if(buffid.containsKey(skillid)){
			int skillidz = buffid.get(skillid);
			////System.out.println("buffid: " +skillid+" - " +skillidz);
			return skillidz;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}

	public static void setbuffid(int skillid, int buffidz) {
		buffid.put(Integer.valueOf(skillid), Integer.valueOf(buffidz)); 
		//System.out.println("buffid: " +skillid+" - " +buffidz);
	}
	
	
	public static int getbufftime(int skillid) {
		if(bufftime.containsKey(skillid)){ 
			int skillidz = bufftime.get(skillid);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return skillidz;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}

	public static void setbufftime(int skillid, int bufftimez) {
		bufftime.put(Integer.valueOf(skillid), Integer.valueOf(bufftimez)); 
		//System.out.println("bufftime: " +skillid+" - " +bufftimez);
	}
	
	
	public static int getbuffvalue(int skillid) {
		if(buffvalue.containsKey(skillid)){
			int buffvaluez = buffvalue.get(skillid);
		//	//System.out.println("buffvalue: " +skillid+" - " +buffvaluez);
			return buffvaluez;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}

	public static void setbuffvalue(int skillid, int buffvaluez) {
		buffvalue.put(Integer.valueOf(skillid), Integer.valueOf(buffvaluez)); 
		//System.out.println("buffvalue: " +skillid+" - " +buffvaluez);
	}
	
	public static int getbuffslot(int skillid) {
		if(buffslot.containsKey(skillid)){ 
			int buffslotz = buffslot.get(skillid);
			////System.out.println("buffslot: " +skillid+" - " +buffslotz);
			return buffslotz;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}

	public static void setbuffslot(int skillid, int buffslotz) {
		buffslot.put(Integer.valueOf(skillid), Integer.valueOf(buffslotz)); 
		//System.out.println("buffvalue: " +skillid+" - " +buffslotz);
	}

	
}
