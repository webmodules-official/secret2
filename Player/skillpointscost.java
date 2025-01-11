package Player;

import java.util.HashMap;
import java.util.Map;


/*
 * skillpointscost.class
 * Stores all skillpointscost data 
 */

public class skillpointscost implements Cloneable{
	public static Map<Integer, Integer> skillpoints = new HashMap<Integer, Integer>();


	
	
	
	public static int getskillpointscost(int skillid) {
		if(skillpoints.containsKey(skillid)){ //System.out.println("FOUND:----- ");
			int skillpointscostz = skillpoints.get(skillid);
			////System.out.println("skillpointscost: " +skillid+" - " +skillpointscostz);
			return skillpointscostz;
		}else{ //System.out.println(skillid+"- null (DOESNT HAVE ANY SKILLPOINT COST)"); 
		return 0; 
		}
	}

	public static void setskillpointscost(int skillid, int skillpointscostz) {
		skillpoints.put(Integer.valueOf(skillid), Integer.valueOf(skillpointscostz)); 
		//System.out.println("skillpointscost: " +skillid+" - " +skillpointscostz);
	}

	
}
