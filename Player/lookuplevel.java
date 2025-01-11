package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class lookuplevel implements Cloneable{
	public static Map<Integer, Integer> statpoints = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> skillpoints = new HashMap<Integer, Integer>();
	public static Map<Integer, String> lookupitems = new HashMap<Integer, String>();

	
	public static String getlookupitems(int level) {
		if(lookupitems.containsKey(level)){ 
			String statp = lookupitems.get(level);
			//System.out.println("statpoints: " +level+" - " +statp);
			return statp;
		}else{ //System.out.println(level+"- a special "); 
		return "a rare"; 
		}
	}

	public static void setlookupitems(int level ,String itname) {
		lookupitems.put(Integer.valueOf(level), itname); 
		//System.out.println("statpoints: " +level+" - " +statp);
	}
	
	
	public static int getstatP(int level) {
		if(statpoints.containsKey(level)){ 
			int statp = statpoints.get(level);
			////System.out.println("statpoints: " +level+" - " +statp);
			return statp;
		}else{ //System.out.println(level+"- null "); 
		return 0; 
		}
	}

	public static void setstatP(int level ,int statp) {
		statpoints.put(Integer.valueOf(level), Integer.valueOf(statp)); 
		//System.out.println("statpoints: " +level+" - " +statp);
	}
	
	public static int getskillP(int level) {
		if(skillpoints.containsKey(level)){ 
			int skillp = skillpoints.get(level);
			////System.out.println("skillpoints: " +level+" - " +skillp);
			return skillp;
		}else{ //System.out.println(level+"- null "); 
		return 0; 
		}
	}

	public static void setskillP(int level , int skillp) {
		skillpoints.put(Integer.valueOf(level), Integer.valueOf(skillp)); 
		//System.out.println("skillpoints: " +level+" - " +skillp);
	}
	
	



	
}
