package Lobby;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import npc.NpcController;

import Configuration.ConfigurationManager;
import Connections.Connection;
import Database.Queries;
import Database.SQLconnection;
import PacketHandler.PacketHandler;
import ServerCore.ServerFacade;
import Tools.BitTools;
import Tools.StringTools;

public class Lobby implements PacketHandler {
	private ServerFacade sface;
	
	public static final byte serverID = 0x02; //don't ask why 2
	
	public static final byte serverIsOffline = 0x00;
	public static final byte serverIsAvailable = 0x01;
	public static final byte serverIsBusy = 0x02;
	public static final byte serverIsVeryBusy = 0x03;
	public static final byte FULL = 0x04;
	
	//private byte[] lobbyPacket;
	
	public void processPacket(ByteBuffer buf, SocketChannel chan) {
		// NOP		
	}

	/*
	 * Upon new connection send em packet telling there's one server online
	 */
	public void newConnection(SocketChannel chan) {
		byte[] lobbyPacket = new byte[16];
		for(int i=0;i<lobbyPacket.length;i++) {
			lobbyPacket[i] = 0x00;
		}
		
		int Realmstatus = 48; //49(1) = online |48(0) = offline
		
		Path path = Paths.get("realmstatus.txt");
		try {
			 byte[] data = Files.readAllBytes(path);
			 Realmstatus = BitTools.byteToInt(data[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
		 
		lobbyPacket[0] = (byte)lobbyPacket.length; //packet length
		lobbyPacket[4] = 0x01; //amount of servers
		lobbyPacket[8] = serverID; //server names are hard coded in client itself. they are distinctable by this byte 
		if(Realmstatus == 48){lobbyPacket[10] = 0x03;} // = offline
		lobbyPacket[12] = (byte)(int)Integer.valueOf(ConfigurationManager.getConf("Lobby").getVar("status")); //server status
		this.sface.addWriteByChannel(chan,lobbyPacket);
	}


	public void initialize(ServerFacade sf) {
		this.sface = sf;
	}

	
	public void newConnection(Connection con) {
		byte[] lobbyPacket = new byte[16];
		for(int i=0;i<lobbyPacket.length;i++) {
			lobbyPacket[i] = 0x00;
		}
		
		int Realmstatus = 48; //49(1) = online |48(0) = offline
		
		Path path = Paths.get("realmstatus.txt");
		try {
			byte[] data = Files.readAllBytes(path);
			 Realmstatus = BitTools.byteToInt(data[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
		 
		lobbyPacket[0] = (byte)lobbyPacket.length; //packet length
		lobbyPacket[4] = 0x01; //amount of servers
		lobbyPacket[8] = serverID; //server names are hard coded in client itself. they are distinctable by this byte 
		if(Realmstatus == 48){lobbyPacket[10] = 0x03;} // = offline
		lobbyPacket[12] = (byte)(int)Integer.valueOf(ConfigurationManager.getConf("Lobby").getVar("status")); //server status
		con.addWrite(lobbyPacket);
	}

	@Override
	public ByteBuffer processPacket(ByteBuffer boss) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processPacket(ByteBuffer buf, Connection con) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processList(Connection con) {
		// TODO Auto-generated method stub
		
	}
	
}
