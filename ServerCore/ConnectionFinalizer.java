package ServerCore;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import Player.PlayerConnection;
import logging.ServerLogger;
import Connections.Connection;
import Player.Player;
import Player.PlayerConnection;
import Player.Character;
import Player.exptable;
import Tools.StringTools;
import World.WMap;


public class ConnectionFinalizer {
	private LinkedBlockingQueue<Connection> finaList = new LinkedBlockingQueue<Connection>();
	private ServerFacade sf = ServerFacade.getInstance();
	private ServerLogger logging = ServerLogger.getInstance();
	private static ConnectionFinalizer instance = null;
	private WMap wmap = WMap.getInstance();
	
	private ConnectionFinalizer() {
		instance = this;
	}
	
	public static synchronized ConnectionFinalizer getInstance() {
		return (instance == null ) ? new ConnectionFinalizer() : instance;
	}
	
	public void addFinalizableConnection(Connection con) {
		PlayerConnection tmplc;
		try{
			if(con.isPlayerConnection()){
				tmplc = (PlayerConnection) con;
				if(tmplc.getPlayer().hasActiveCharacter()) {
					if(tmplc.getActiveCharacter().getHp() <= 0 && tmplc.getActiveCharacter().killedbyplayer == false){
					long MAXexp = exptable.getMAXexptable(tmplc.getActiveCharacter().getLevel());
					double num = MAXexp * .02; // is 2.00% lost
					long remexp = tmplc.getActiveCharacter().getexp() - (long)num; // current exp - 2.50%(from MAXEXP) = new exp
					tmplc.getActiveCharacter().setexp(remexp); // save it
					tmplc.getActiveCharacter().killedbyplayer = false;
					}
			  		if(wmap.Trade.containsKey(tmplc.getActiveCharacter().TradeUID)){
			  		wmap.Trade.get(tmplc.getActiveCharacter().TradeUID).CancelTrade();
			  		}
					tmplc.getActiveCharacter().savecharacter();
					ServerFacade.getInstance().getCon().removeAccounts(tmplc.getPl().getAccountID());
					tmplc.getActiveCharacter().BoothStance = 0;
					WMap.getInstance().vendinglist.remove(tmplc.getActiveCharacter().getCharID());
					tmplc.getActiveCharacter().leaveGameWorld();
					tmplc.getWriteBuffer().clear();
					tmplc.setActiveCharacter(null);
				}}
			this.finaList.offer(con);
		}catch (NullPointerException e){
			this.finaList.offer(con);
		}
	}
	
	public boolean isWaitingFinalization(Connection con) {
		return this.finaList.contains(con);
	}
	
	public LinkedBlockingQueue<Connection> getFinalizeList() {
		return this.finaList;
	}
	
	public void finalize() {
		Connection tmp;
		while(!this.finaList.isEmpty()) {
			tmp = this.finaList.poll();
			try{
			this.sf.removeConnection(tmp.getChan());
			tmp.disconnect();
			}catch (NullPointerException e){
			}
		}
	}
	
	public boolean isEmpty() {
		return this.finaList.isEmpty();
	}
}
