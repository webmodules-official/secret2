package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * Skilleffectsdata.class
 * Stores all Skilleffects data 
 */

public class skilleffects implements Cloneable{
	public static Map<Integer, String> skilleffects = new HashMap<Integer, String>();


	public static void setskilleffects(int one, String two) {
		skilleffects.put(Integer.valueOf(one), two); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	
	
	public static String getskilleffects(int one) {
		if(skilleffects.containsKey(one)){ 
			String two = skilleffects.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return null; 
		}
	}
	
	public static boolean tryskilleffects(int one) {
		return skilleffects.containsKey(one);
	}
	
}
