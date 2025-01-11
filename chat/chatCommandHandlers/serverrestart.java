package chat.chatCommandHandlers;

import Connections.Connection;
import chat.ChatCommandExecutor;

public class serverrestart implements ChatCommandExecutor {


	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling server restart command");

		//TODO: save all characters that are in world ????? maybe or not.
		// run another java? then exit with restart?
		//Shellexecute;
		System.exit(0);
		
	}

}
