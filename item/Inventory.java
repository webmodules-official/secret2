package item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
	private int pages;
	private Map<Integer, Item> inv = new HashMap<Integer, Item>();
	
	public Inventory(int pages){
		this.pages = pages;
	}
	// increase amount of pages in inventory
	public void addPages(int amount){
		if (amount > 0 || amount < 4) this.pages += amount;
	}
	// adds item to inventory only if all needed slots are empty, return true is success, false otherwise
	public boolean addItem(int line, int row, Item it){
		List<Item> ls = this.checkBlockingItems(line, row, it);
		if (ls == null){
			return false;
		}
		if (ls.size() == 0 ){
			this.put(line, row, it);
			return true;	
		}
		return false;
	}
	// adds item to inventory and return the item replaced if possible 
	public Item AddAndReturn(int line, int row, Item it){
		List<Item> ls = this.checkBlockingItems(line, row, it);
		Item  ret = null;
		if (ls == null){
			return ret;
		}
		else if (ls.size() == 1){
			this.put(line, row, it);
			ret = ls.get(0);
			this.removeAll(ret);
		}
		else if (ls.size() == 0){
			this.put(line, row, it);
		}
		return ret;
		
	}
	// removes item from inventory and returns true on success, false otherwise
	public boolean removeItem(int line, int row){
		Item it = this.inv.get((row*100)+line);
		return this.removeAll(it);
	}
	// removes item from inventory list and returns the deleted object
	public Item removeAndReturn(int line, int row){
		Item it = null;
		if (this.inv.containsKey((row*100)+line)){
			it = this.inv.get((row*100)+line);
			this.remove(line, row);
		}
		return it;
	}
	// return item on specific slot, null if no item present
	public Item getItem(int line, int row) {
		return this.inv.get((row*100)+line);
	}
	// remove item by iterating through all slots
	private void remove(int line, int row) {
		Item it = this.inv.get((row*100) + line);
		if (it != null){
			for (int i = line;  i < (line + it.getWidth()); i++){
				for (int u=row; u < (line + it.getHeight()); u++){
					this.inv.remove(Integer.valueOf((u*100)+i));
				}
			}
		}
	}
	// insert item to all the slots is requires, no boundary checks are performed
	private void put(int line, int row, Item it) {
		for (int i = line;  i < (line + it.getWidth()); i++){
			for (int u=row; u < (line + it.getHeight()); u++){
				this.inv.put(Integer.valueOf((u*100)+i), it);
			}
		}
		
	}
	// remove all instances of Item
	private boolean removeAll(Item it){
		if (this.inv.containsValue(it)){
			return this.inv.values().removeAll(Collections.singleton(it));
		}
		return false;
	}
	/* check if item will fit in inventory
	 *  Returns list containing all items that are using same slots as tmp
	 */
	private List<Item> checkBlockingItems(int line, int row, Item tmp){
		int width = tmp.getWidth();
		List<Item> ls = new ArrayList<Item>();
		int height = tmp.getHeight();
		Item it = null;
		int c = 0;
		 
		// boundary check
		if (!this.checkWlimit(row, width)) return null;
		if (!this.checkHlimit(line, height)) return null;
		
		// main loop
		for (int i=line; i < (line + height); i++){
			for (int u=row; u < (row + width); u++){
				// if item is in slot add it to list
				if (this.inv.containsKey((u*100)+i)){
					it = this.inv.get((u*100) +i);
					if (!ls.contains(it)) ls.add(c, it);
					c++;
				}
			}
			
		}
		return ls;
	}
	// check if item will fit to inventory by height
	private boolean checkHlimit(int line, int height) {
		if (line + height < 5) return true;
		return false;
	}
	// check if item will fit to inventory by width
	private boolean checkWlimit(int row, int width) {
		if ((this.pages*8) > (row + width)){
			int page = (int)Math.floor(row / 8);
			if ((row+width) <= (page*8+7) && row >= (page*8)){
				return true;
			}
		}
		return false;
	}
	
}
