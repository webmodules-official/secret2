package Player;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class skillpasives implements Cloneable{
	public static Map<Integer, String> skillpassives = new HashMap<Integer, String>();


	public static void setskillpassives(int one, String two) {
		skillpassives.put(Integer.valueOf(one), two); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	
	
	public static String getskillpassives(int one) {
		if(skillpassives.containsKey(one)){ 
			String two = skillpassives.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return null; 
		}
	}
	
	public static boolean tryskillpassives(int one) {
		return skillpassives.containsKey(one);
	}

	
}
