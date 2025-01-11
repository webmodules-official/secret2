package timer;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import Player.Character;

import World.Waypoint;

public class MoveSyncTimer extends TimerTask{
	
	private List<Waypoint> wpoints;
	@SuppressWarnings("unused")
	private Character owner;
	private int currentWaypoint;

	public MoveSyncTimer(List<Waypoint> waypoints, Character current){
		this.wpoints = new ArrayList<Waypoint>();
		this.wpoints.addAll(waypoints);
		this.owner = current;
		this.currentWaypoint = 0;
	}
	
	@Override
	public void run() {
		if (this.currentWaypoint < this.wpoints.size()){
			@SuppressWarnings("unused")
			float x = this.wpoints.get(this.currentWaypoint).getX();
			@SuppressWarnings("unused")
			float y = this.wpoints.get(this.currentWaypoint).getY();
			// this.owner.syncLocation(x, y);
			this.currentWaypoint++;
			//System.out.println("MoveSync executed at:" +System.currentTimeMillis());
		}
		else {
			this.cancel();
		}
	}
	public boolean isCompleted(){
		boolean b = false;
		if (this.currentWaypoint == this.wpoints.size()) b = true; 
		return b;
	}
}
