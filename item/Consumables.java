package item;

public class Consumables extends Item{
	
	private int maxStackSize;

	public Consumables(){
		super();
	}
	public int getMaxStackSize() {
		return maxStackSize;
	}
	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}
	public Consumables getConsumable(){
		return this;
	}
	
}
