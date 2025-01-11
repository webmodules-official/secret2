package Player;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class upgradelist implements Cloneable{
	public static Map<Integer, Integer> IDtoNewID = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> Upgradelist = new HashMap<Integer, Integer>();

	
	public static void setUpgradelist(int one, int two) {
		Upgradelist.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	
	public static boolean tryUpgradelist(int one) {
		return Upgradelist.containsKey(one);
	}
	
	public static int getUpgradelist(int one) {
		if(Upgradelist.containsKey(one)){ 
			int two = Upgradelist.get(one);
			//System.out.println("bufftime: " +one+" - " +two);
			return two;
		}else{ //System.out.println(one+"- null "); 
		return 0; 
		}
	}


	public static void setIDtoNewID(int one, int two) {
		IDtoNewID.put(Integer.valueOf(one), Integer.valueOf(two)); 
		//System.out.println("IDtoName: " +one+" - " +two);
	}
	
	
	public static int getIDtoNewID(int one) {
		if(IDtoNewID.containsKey(one)){ 
			int two = IDtoNewID.get(one);
			////System.out.println("bufftime: " +skillid+" - " +skillidz);
			return two;
		}else{ //System.out.println(skillid+"- null "); 
		return 0; 
		}
	}
	
	public static boolean tryIDtoNewID(int one) {
		return IDtoNewID.containsKey(one);
	}

	
}
