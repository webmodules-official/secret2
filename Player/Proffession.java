package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * Skilleffectsdata.class
 * Stores all Skilleffects data 
 */

public class Proffession implements Cloneable{
	public static Map<Integer, Integer> farmfishing = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> farmherbing = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> farmmining = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> farmgraveyard = new HashMap<Integer, Integer>();

	
	public static void setfarmfishing(int one, int two) {
		farmfishing.put(one, two); 
		//System.out.println("farmfishing: " +one+" - " +two);
	}
	public static int getfarmfishing(int one) {
		if(farmfishing.containsKey(one)){ 
			int two = farmfishing.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}
	
	
	public static void setfarmherbing(int one, int two) {
		farmherbing.put(one, two); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	public static int getfarmherbing(int one) {
		if(farmherbing.containsKey(one)){ 
			int two = farmherbing.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}
	
	
	public static void setfarmmining(int one, int two) {
		farmmining.put(one, two); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	public static int getfarmmining(int one) {
		if(farmmining.containsKey(one)){ 
			int two = farmmining.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}
	
	public static void setfarmgraveyard(int one, int two) {
		farmgraveyard.put(one, two); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	public static int getfarmgraveyard(int one) {
		if(farmgraveyard.containsKey(one)){ 
			int two = farmgraveyard.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}
	
}
