package chat.chatCommandHandlers;


import Tools.StringTools;
import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import ServerCore.ServerFacade;
import Database.CharacterDAO;
import chat.ChatCommandExecutor;



public class quitcommand implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("Handling quit command");
		for(int i=0;i<parameters.length;i++) {
			//System.out.println("Command param[" + (i+1) + "] : " + parameters[i]);
			if(StringTools.isInteger(parameters[0]) && parameters[i].length() <= 1) {
				//System.out.println("Param[" + (i+1) + "] is of integer type length lower then 1!");
				//int CharID = Integer.parseInt(parameters[0]);
				Character curwa = ((PlayerConnection)source).getActiveCharacter();
				//System.out.println(" =====>Char: " + curwa.getLOGsetName() +" Quited the game with command" );
				
				
				// ---  save char location in db --\\
				Character current = ((PlayerConnection)source).getActiveCharacter();
				float gox = current.getlastknownX();
				float goy = current.getlastknownY();
				int gomap = current.getCurrentMap();
				String getcharnamewhomadethecommand = current.getLOGsetName();
				CharacterDAO.telexymapfloat(gox, goy, gomap, getcharnamewhomadethecommand);		
				CharacterDAO.savehpmanastam(current.getHp(), current.getMana(), current.getStamina(), getcharnamewhomadethecommand);
				//---------Disconnect the player----------\\	
				 ServerFacade.getInstance().finalizeConnection(current.GetChannel()); 
			
			}
		}
		

		
	}
}