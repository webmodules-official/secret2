package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */

public class ipbanlist implements Cloneable{
	public static Map<String, String> ipbanlist = new HashMap<String, String>();

	
	
	
	public static String getipbanlist(String ip) {
		if(ipbanlist.containsKey(ip)){ //System.out.println("FOUND:----- ");
			String ipz = ipbanlist.get(ip);
			//System.out.println("GETipbanlist: " +ip);
			return ipz;
		}else{ ////System.out.println(ip+"- null "); 
		return null; 
		}
	}

	public static void setipbanlist(String ip) {
		ipbanlist.put(ip, ip); 
		//System.out.println("Set Banned IPs: " +ip);
	}
	
	
	public static void clearipbanlist() {
		ipbanlist.clear();
	}
	
	
}
