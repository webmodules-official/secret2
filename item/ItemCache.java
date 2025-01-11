package item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import Database.ItemDAO;

public class ItemCache {
	
	private volatile static ItemCache instance;
	private Map<Integer, Item> cache = Collections.synchronizedMap(new HashMap<Integer, Item>());
	private Map<Integer, Long> timestamp = Collections.synchronizedMap(new HashMap<Integer, Long>());
	private long lastCheck;
	private long checkInterval = 15000;
	
	public static ConcurrentMap<Integer, String> Items = new ConcurrentHashMap<Integer, String>();
	public static ConcurrentMap<Integer, String> Item4xsets = new ConcurrentHashMap<Integer, String>();
	
	
	
	
	
	

	
	
	
	
	
	private ItemCache(){}
	
	public static synchronized ItemCache getInstance(){
		if (instance == null){
			instance = new ItemCache();
		}
		return instance;
	}
	
	public Map<Integer, Item> getCache() {
		return cache;
	}
	private void putItem(int id, Item it) {
		this.cache.put(id, it);
		this.timestamp.put(id, System.currentTimeMillis());
	}
	public Item getItem(int id){
		Item it = null;
		if (this.cache.containsKey(id)){
			it = this.cache.get(id);
			this.timestamp.put(id, System.currentTimeMillis());
		}
		else{
			it = ItemDAO.getItem(id);
			this.putItem(id, it);
		}
		if ((this.lastCheck + this.checkInterval) < System.currentTimeMillis()){
			checkCache();
		}
		return it;
		
	}

	private void checkCache() {
		synchronized(this.timestamp){
			Iterator<Map.Entry<Integer, Long>> iter = this.timestamp.entrySet().iterator();
			long ts = 0;
			int i = 0;
			
			while(iter.hasNext()){
				 Map.Entry<Integer, Long> pairs = iter.next();
				i = pairs.getKey();
				ts = pairs.getValue();
				if ((ts + this.checkInterval ) < System.currentTimeMillis()){
					iter.remove();
					this.cache.remove(i);
				}
			}
		}
		this.lastCheck =  System.currentTimeMillis();
	}
	

}
