package timer;

import java.util.TimerTask;

import logging.ServerLogger;

import Connections.Connection;
import Player.Character;
import Player.PlayerConnection;
import Tools.StringTools;

public class globalsave extends TimerTask{
	private ServerLogger logging = ServerLogger.getInstance();
	private Connection con;
	
	public globalsave(Connection con){
		this.con = con;
	}

	@Override
	public void run() {
		if(con.getChan().isOpen()) {
		try{
			Character cur = ((PlayerConnection)con).getActiveCharacter();
			if(cur != null){
			cur.savecharacter();
			//Charstuff.getInstance().respond("Character saved!", con);
			}
			}catch (NullPointerException e){
	        this.logging.warning(this, StringTools.readStackTrace(e));
			}
	}else{this.cancel();}}
}
