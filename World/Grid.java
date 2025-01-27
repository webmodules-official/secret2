package World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import logging.ServerLogger;

import Mob.Mob;
import ThreadPool.MobThreadPool;

// Grid.class
// Each grid represents greater area, usually a in-game map
// Grid consists of multiple Area objects. Grid is always assumed to be square and therefore it should have gridsize * gridsize of Area objects stored.
// Grid is unaware of what objects are within each Area, it merely serves as means for the Objects to retrieve proper Area object

public class Grid implements Runnable
{
    private ConcurrentMap<Object, Area> grid = new ConcurrentHashMap<Object, Area>(250, 10, 250);	
	private int uid;
	private int areaSize = 300;
	private int gridSize = 500;
	private float[] dimenssions;
	private String name;
	private float mx, my;
	private float ex, ey;
	private MobThreadPool gridPool;
	private ServerLogger log = ServerLogger.getInstance();
	private volatile boolean active;
	
	public boolean isActive() {
		return active;
	}
	private void setActive(boolean active) {
		this.active = active;
	}

	// Initializes new grid
	// id  -  unique id of this grid
	// gsize - size of one side of the grid
	// areasize - size of one side of each Area
	// name  - name of the grid
	// x  - x-coordinate in-game from which the grid begins
	// y  - y-coordinate in-game from which the grid begins
	// poolSize - number of threads allocated for MobControllers in this grid
	public Grid(int id, int gsize, int areasize, String name, float x, float y, int poolSize)
    {
	  //this.log.info(this, "Creating Grid for map " + name + " id " + id + " ... ");
      this.uid = id;
	  this.setName(name);
	  this.gridSize = gsize;
	  this.mx = x;
	  this.my = y;
	  this.ex = x + (gsize * areasize);
	  this.ey = y + (gsize * areasize);
	  this.areaSize = areasize;
	  this.gridPool = new MobThreadPool(poolSize);
	  this.initGrid();
    }
	// add new area to the grid
    private void addArea(Area a)
	{
	 int tmp[] = a.getcoords();
	 int foo = ((tmp[0] *this.gridSize) + tmp[1] );
	 // System.out.println("Addin coords ("+tmp[0]+","+tmp[1]+") uid:" + foo);
	 if(this.areaExists(tmp)){ this.log.warning(this,"Warning duplicate unique id's formed for area"); }
	 grid.put(foo, a);
	}
	// returns true if area in coordinates coords[] does exists, otherwise returns false
	public boolean areaExists(int []coords)
	{
	  int foo = (coords[0] * this.gridSize) + coords[1];
	  return grid.containsKey(foo);
	}
	// returns area that is in coordinates coords[]
	public Area getArea(int [] coords)
	{
	  int foo = (coords[0] * this.gridSize) + coords[1];
	  return grid.get(foo);
	}
	// return thread pool associated to this grid
	public MobThreadPool getThreadPool(){
		return this.gridPool;
	}
	// returns the unique ID of this grid
	public int getuid()
	{
	  return this.uid;
	}
	public float getStartX(){
		return this.mx;
	}
	public float getStartY(){
		return this.my;
	}
	public float getEndX(){
		return this.ex;
	}
	public float getEndY(){
		return this.ey;
	}
	
	// initializes all the necessary Area objects needed for functionality
	private void initGrid()
	{
	  Area tmp;
	  int count = 0;
	  for (int i =0; i < this.gridSize; i++)
	  {
	    for (int u =0; u < this.gridSize; u++)
		{
	      tmp = new Area(u, i, this);
		  tmp.setuid((u * this.gridSize) + i);
		  this.addArea(tmp);
		  tmp = null;
		  count++;
		}
	  }
	  //this.log.info(this,"Done " + count + " Areas successfully created");
	  
	}
	public float[] getDimenssions() {
		return dimenssions;
	}
	public void setDimenssions(float[] dimenssions) {
		this.dimenssions = dimenssions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Area update(Location loc){
		  int []ret = new int[]{-1,-1};
		  float tx = loc.getlastknownX();
		  float ty = loc.getlastknownY();
		  Area ar = null;
		  if (tx >= this.getStartX() && tx <= this.getEndX() && ty >= this.getStartY() && ty <= this.getEndY()){
			  
			  float gx = WMap.distance(this.getStartX(), tx);
			  float gy = WMap.distance(this.getStartY(), ty);

			  ret[0] = (int)(Math.floor(gx / this.areaSize));
			  ret[1] = (int)(Math.floor(gy / this.areaSize));
			  ar = this.getArea(ret);
		  }else{
			  //this.log.warning(this, "ERROR: player "+ loc.getuid()+" out of grid");
		  }
		  return ar;
	  }
	public boolean areaExists(int x, int y) {
		return this.areaExists(new int[]{x,y});
	}

	public void run(){
		if(!WMap.getInstance().getMobs(uid).isEmpty()){
			if (!this.active){
				this.setActive(true);
				while(this.active){
						Iterator<Map.Entry<Integer, Mob>> iter = WMap.getInstance().getMobs(uid).entrySet().iterator();
						while(iter.hasNext()) {
							Map.Entry<Integer, Mob> pairs = iter.next();
							Mob mob = pairs.getValue(); 
							if(!mob.iniPackets.isEmpty()){
								mob.run();	
							}
						}
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						////e.printStackTrace();
					}
				}
				this.active = false;
			}
		}
	}




}
