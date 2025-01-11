package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * skilldata.class
 * Stores all buff data 
 */

public class skilldata implements Cloneable{
	public static Map<Integer, Integer> skilldmg = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> skillcritchance = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> skillmanaconsume = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> skillcooldowns = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> skilllevel = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> skillcategory = new HashMap<Integer, Integer>();

	public static int getskillcategory(int skillid) {
		if(skillcategory.containsKey(skillid)){
			int skillcritchancez = skillcategory.get(skillid);
			//System.out.println("skillmanaconsume: " +skillid+" - " +skillcritchancez);
			return skillcritchancez;
		}else{ //System.out.println(skillid+" - null ( WARNING: HAS NO skillmanaconsume )"); 
		return 0; 
		}
	}

	public static void setskillcategory(int skillid, int skillmanaconsumez) {
		skillcategory.put(Integer.valueOf(skillid), Integer.valueOf(skillmanaconsumez)); 
		//System.out.println("skillmanaconsume: " +skillid+" - " +skillmanaconsumez);
	}
	
	public static int getskilllevel(int skillid) {
		if(skilllevel.containsKey(skillid)){
			int skillcritchancez = skilllevel.get(skillid);
			//System.out.println("skillmanaconsume: " +skillid+" - " +skillcritchancez);
			return skillcritchancez;
		}else{ //System.out.println(skillid+" - null ( WARNING: HAS NO skillmanaconsume )"); 
		return 0; 
		}
	}

	public static void setskilllevel(int skillid, int skillmanaconsumez) {
		skilllevel.put(Integer.valueOf(skillid), Integer.valueOf(skillmanaconsumez)); 
		//System.out.println("skillmanaconsume: " +skillid+" - " +skillmanaconsumez);
	}
	
	public static int getskillmanaconsume(int skillid) {
		if(skillmanaconsume.containsKey(skillid)){
			int skillcritchancez = skillmanaconsume.get(skillid);
			//System.out.println("skillmanaconsume: " +skillid+" - " +skillcritchancez);
			return skillcritchancez;
		}else{ //System.out.println(skillid+" - null ( WARNING: HAS NO skillmanaconsume )"); 
		return 0; 
		}
	}

	public static void setskillmanaconsume(int skillid, int skillmanaconsumez) {
		skillmanaconsume.put(Integer.valueOf(skillid), Integer.valueOf(skillmanaconsumez)); 
		//System.out.println("skillmanaconsume: " +skillid+" - " +skillmanaconsumez);
	}
	
	public static int getskillcritchance(int skillid) {
		if(skillcritchance.containsKey(skillid)){
			int skillcritchancez = skillcritchance.get(skillid);
			//System.out.println("skillcritchance: " +skillid+" - " +skillcritchancez);
			return skillcritchancez;
		}else{ //System.out.println(skillid+" - null ( WARNING: HAS NO skillcritchance )"); 
		return 0; 
		}
	}

	public static void setskillcritchance(int skillid, int skillcritchancez) {
		skillcritchance.put(Integer.valueOf(skillid), Integer.valueOf(skillcritchancez)); 
		//System.out.println("skillcritchance: " +skillid+" - " +skillcritchancez);
	}
	
	public static int getskilldmg(int skillid) {
		if(skilldmg.containsKey(skillid)){
			int skilldmgz = skilldmg.get(skillid);
			//System.out.println("skilldmg: " +skillid+" - " +skilldmgz);
			return skilldmgz;
		}else{ //System.out.println(skillid+" - null ( WARNING: HAS NO DAMAGE )"); 
		return 0; 
		}
	}

	public static void setskilldmg(int skillid, int skilldmgz) {
		skilldmg.put(Integer.valueOf(skillid), Integer.valueOf(skilldmgz)); 
		//System.out.println("skilldmg: " +skillid+" - " +skilldmgz);
	}
	
	public static int getskillcooldowns(int skillid) {
		if(skillcooldowns.containsKey(skillid)){
			int skillcooldownsz = skillcooldowns.get(skillid);
			//System.out.println("skillcooldown: " +skillid+" - " +skillcooldownsz);
			return skillcooldownsz;
		}else{ //System.out.println(skillid+" - null ( WARNING: HAS NO DAMAGE )"); 
		return 0; 
		}
	}

	public static void setskillcooldowns(int skillid, int skillcooldownsz) {
		skillcooldowns.put(Integer.valueOf(skillid), Integer.valueOf(skillcooldownsz)); 
		//System.out.println("skilldmg: " +skillid+" - " +skilldmgz);
	}

	
}
