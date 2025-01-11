package Player;

import java.nio.channels.SocketChannel;

import ServerCore.SelectorThread;


import Connections.Connection;

/**
 * TODO: Utilize this class as special occurance of Connection
 */

public class PlayerConnection extends Connection {
	private Player pl;
	
	
	public PlayerConnection(SocketChannel sc, int rsize, int wsize, SelectorThread sp) {
		super(sc, rsize, wsize, sp);
		// TODO Auto-generated constructor stub
	}

	public Player getPlayer() {
		return this.getPl();
	}

	public void setPlayer(Player pl) {
		this.setPl(pl);
	}
	
	public Character getActiveCharacter() {
		return this.getPl().getActiveCharacter();
	}
	
	public void setActiveCharacter(Character ch) {
		this.getPl().setActiveCharacter(ch);
	}
	
	@Override
	public boolean isPlayerConnection() {
		return true;
	}

	public Player getPl() {
		return pl;
	}

	public void setPl(Player pl) {
		this.pl = pl;
	}
}
