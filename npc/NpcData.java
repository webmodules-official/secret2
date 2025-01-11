package npc;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Connections.Connection;
import ServerCore.ServerFacade;
import Player.Character;
import Player.PlayerConnection;
import Tools.BitTools;
import World.WMap;




public class NpcData {
	private int id;
	private int [] grid;
	private int groupID;
	private int gridID;
	
	public NpcData(){
	}
	
	public int[] getGrid() {
		return grid;
	}
	public void setGrid(int[] grid) {
		this.grid = grid;
	}
	
	public int getgroupID() {
		return groupID;
	}
	public void setgroupID(int groupID) {
		this.groupID = groupID;
	}
	public int getGridID() {
		return gridID;
	}
	public void setGridID(int gridID) {
		this.gridID = gridID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
		
		

