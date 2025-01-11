package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class itemprice implements Cloneable{
	public static Map<Integer, Long> Buyprice = new HashMap<Integer, Long>();

	

	public static void setBuyprice(int one, int two) {
		Buyprice.put(Integer.valueOf(one), Long.valueOf(two)); 
		//System.out.println("setBuyprice: " +one+" - " +two);
	}
	
	
	public static long getBuyprice(int one) {
		if(Buyprice.containsKey(one)){ 
			long two = Buyprice.get(one);
			//System.out.println("getBuyprice: " +one+" - " +two);
			return two;
		}else{ //System.out.println(one+"- null "); 
		return 0; 
		}
	}

	
}
