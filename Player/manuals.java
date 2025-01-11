package Player;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class manuals implements Cloneable{
	public static Map<Integer, String> manuals = new HashMap<Integer, String>();


	public static void setgrinditems(int one, String two) {
		manuals.put(Integer.valueOf(one), two); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	
	
	public static String getgrinditems(int one) {
		if(manuals.containsKey(one)){ 
			String two = manuals.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return null; 
		}
	}
	
	public static boolean trygrinditems(int one) {
		return manuals.containsKey(one);
	}

	
}
