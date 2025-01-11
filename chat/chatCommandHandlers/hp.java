package chat.chatCommandHandlers;

import Player.Character;
import Player.PlayerConnection;
import Connections.Connection;
import chat.ChatCommandExecutor;

public class hp implements ChatCommandExecutor {

	
	public void execute(String[] parameters, Connection source) {
		//System.out.println("PUTTING POTS!");
		Character cur = ((PlayerConnection)source).getActiveCharacter();
		int setint = Integer.parseInt(parameters[0]);

		if(setint == 0){cur.FAD = 0;cur.FASR = 0; cur.FDD = 0;  cur.CASR = 0; cur.FD = 0; cur.SAP = 0; cur.NEW_STATUS_SUNDAN = 0;  
		cur.Exp_Tag_15 = 0; cur.Exp_Tag_30 = 0; cur.Exp_Tag_100 = 0;cur.JACKPOT_TAG  = 0;cur.DOUBLE_ITEM_DROP_TAG  = 0;
		
		}
		if(setint == 1){cur.FAD = 1;cur.FASR = 1;}
		if(setint == 2){cur.FDD = 1;cur.CASR = 1;}
		if(setint == 3){cur.FD = 1;}
		if(setint == 4){cur.SAP = 1;}
		if(setint == 5){cur.NEW_STATUS_SUNDAN = 1;}
		if(setint == 6){cur.JACKPOT_TAG  = 1;cur.DOUBLE_ITEM_DROP_TAG  = 1;}
		if(setint == 7){cur.Exp_Tag_15 = 1;}
		if(setint == 8){cur.Exp_Tag_30 = 1;}
		if(setint == 9){cur.Exp_Tag_100 = 1;}
		cur.statlist(); // refresh LIST
		//System.out.println("DONE");
	}
	
}
