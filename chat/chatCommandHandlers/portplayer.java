package chat.chatCommandHandlers;

import Connections.Connection;
import Database.CharacterDAO;
import chat.ChatCommandExecutor;


public class portplayer implements ChatCommandExecutor {

	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling teleport command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
		}	
	
				String gox = (parameters[0]);
				String goy = (parameters[1]);
				String gomap = (parameters[2]);
				String getcharnamewhomadethecommand = (parameters[3]);
			
				
				//---------tele player----------\\
				CharacterDAO.telexymap(gox, goy, gomap, getcharnamewhomadethecommand);		
				
				
				
				
				
				
				
				
				
				
				
				
				
		}	
	}
		
		
  





	