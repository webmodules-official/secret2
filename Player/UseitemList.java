package Player;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;


/*
 * buffdata.class
 * Stores all buff data 
 */
interface Command {
    void runCommand();
}

public class UseitemList implements Cloneable{
	static Map<Integer, Command> UseItemLIST = new HashMap<Integer, Command>();

	public static void RegisterUseItemLIST(int one, Object two) {
		UseItemLIST.put(273000242, new Command() {
            public void runCommand() { 
            
            };
        });
	}
	
	
	public static Object GETUseItemLIST(int one) {
		if(UseItemLIST.containsKey(one)){ 
			Object two = UseItemLIST.get(one);
			System.out.println("UseItemLIST: " +one+" - " +two);
			return two;
		}else{ System.out.println(one+"- null "); 
		return null; 
		}
	}

	
}
