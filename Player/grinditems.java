package Player;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class grinditems implements Cloneable{
	public static Map<Integer, Integer> grinditems = new HashMap<Integer, Integer>();


	public static void setgrinditems(int one, int two) {
		grinditems.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	
	
	public static int getgrinditems(int one) {
		if(grinditems.containsKey(one)){ 
			int two = grinditems.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}
	
	public static boolean trygrinditems(int one) {
		return grinditems.containsKey(one);
	}

	
}
