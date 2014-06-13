package ass3;

import java.util.concurrent.Semaphore;

public class KitchenTool implements Comparable<KitchenTool> {
	
	/********* Fields *********/
	private String name;
	private int quantity;
	private Semaphore s; // Semaphore is our supervisor to make sure chefs will wait for a tool to be available.

	/********* Constructors *********/
	public KitchenTool(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
		s = new Semaphore(quantity); // initialize semaphore and give him keys as the number of the tool.
	}

	/********* Getters/Setters *********/
	public String getName(){
		return this.name;
	}
	
	public int getQuantity(){
		return this.quantity;
	}

	/********* Methods *********/
	public void takeTool(int quantity) { //acquire a tool.
		try {
			s.acquire(quantity);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void returnTool(int quantity) { // return a tool.
		s.release(quantity);
	}

	@Override
	public int compareTo(KitchenTool k) { // to sort the tools, it is a RE_SOURCE ordering solution to the dead lock.
		return this.hashCode() - k.hashCode();
	}
}
