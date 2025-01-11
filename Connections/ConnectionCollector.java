package Connections;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.nio.channels.*;

import Player.Player;



public class ConnectionCollector {
	private volatile ConcurrentMap<SocketChannel, Connection> connections;
	public ConcurrentMap<Integer, Player> accounts = new ConcurrentHashMap<Integer, Player>();
	
	public static Selector selector;
	
	public ConnectionCollector(int size, float load, int cl) {
		this.connections = new ConcurrentHashMap<SocketChannel, Connection>(size, load, cl);
	}
	
	public void addConnection(SocketChannel sc, int rsize, int wsize) {
		this.connections.putIfAbsent(sc, new Connection(sc, rsize, wsize));
	}
	
	public void addaccount(int acntid, Player pl) {
		this.accounts.put(acntid, pl);
	}
	
	public Connection addAndReturnConnection(SocketChannel sc, int rsize, int wsize) {
		this.connections.putIfAbsent(sc, new Connection(sc, rsize, wsize));
		return this.connections.get(sc);
	}
	
	public synchronized Connection getConnection(SocketChannel sc) {	
		return this.connections.get(sc);
		
	}
	
	public synchronized Player getaccount(int sc) {	
		return this.accounts.get(sc);
	}
	
	public void removeConnection(SocketChannel sc) {
		this.connections.remove(sc);
	}
	
	public void removeAccounts(int acntid) {
		this.accounts.remove(acntid);
	}

	public int getConnectionCount() {
		return this.connections.size();
	}
		
	public SelectionKey getKeyByChannel(SocketChannel sc) {
		return sc.keyFor(selector);
	}

	public static Selector getSelector() {
		return selector;
	}

	public static void setSelector(Selector select) {
		selector = select;
	}
	
	public boolean isaccountsRegistered(int ch) {
		return this.accounts.containsKey(ch);
	}
	

	public boolean isChannelRegistered(SocketChannel ch) {
		return this.connections.containsKey(ch);
	}
	
	public ConcurrentMap<SocketChannel, Connection> getConnectionMap() {
		return this.connections;
	}
}
