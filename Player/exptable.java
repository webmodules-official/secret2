package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * MAXexptable.class
 * Stores all buff data 
 */

public class exptable implements Cloneable{
	public static Map<Integer, Long> MAXexptable = new HashMap<Integer, Long>();
	public static Map<Integer, Integer> furyV = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> furyT = new HashMap<Integer, Integer>();
	
	public static int getfuryV(int level) {
		if(furyV.containsKey(level)){
			int exp = furyV.get(level);
			////System.out.println("MAXexptable: " +level+" - " +exp);
			return exp;
		}else{ 
		return 0; 
		}
	}

	public static void setfuryV(int level, int furyVz) {
		furyV.put(Integer.valueOf(level), Integer.valueOf(furyVz)); 
		//System.out.println("setfuryV: " +level+" - " +furyVz);
	}
	
	public static int getfuryT(int level) {
		if(furyT.containsKey(level)){
			int exp = furyT.get(level);
			////System.out.println("MAXexptable: " +level+" - " +exp);
			return exp;
		}else{ 
		return 0; 
		}
	}

	public static void setfuryT(int levelz, int furyTz) {
		int newfuryt = furyTz * 1000;
		furyT.put(Integer.valueOf(levelz), Integer.valueOf(newfuryt)); 
		//System.out.println("setfuryT: " +levelz+" - " +newfuryt);
	}
	
	public static long getMAXexptable(int level) {
		if(MAXexptable.containsKey(level)){
			long exp = MAXexptable.get(level);
			////System.out.println("MAXexptable: " +level+" - " +exp);
			return exp;
		}else{ 
		return 0; 
		}
	}

	public static void setMAXexptable(int levelz, long exp) {
		MAXexptable.put(Integer.valueOf(levelz), Long.valueOf(exp)); 
		//System.out.println("MAXexptable: " +levelz+" - " +exp);
	}
	
	

	
}
