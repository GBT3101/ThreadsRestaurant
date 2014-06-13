package ass3;

public class Ingredient {
	
	/********* Fields *********/
	private int _consumed; // represents how many has been consumed.
	private int _quantity; // current quantity.
	private String _name;
	
	/********* Constructors *********/
	public Ingredient(String name, int quantity) {
		this._consumed = 0;
		this._quantity = quantity;
		this._name = name;
	}
	
	/********* Getters/Setters *********/
	
	public String getName(){
		return this._name;
	}
	
	public int getQuantity(){
		return this._quantity;
	}
	
	public int getConsumed(){
		return this._consumed;
	}
	
	/********* Methods *********/
	public synchronized void consume(int consumed) { // consume ingredients affect it's current quantity.
		this._quantity -= consumed;
		this._consumed += consumed;
	}
	
	public String toString() {
		return "Ingredient: [name=" + getName() + "][quantity=" + (getQuantity()+getConsumed()) + 
				"][consumed=" + getConsumed() + "][left=" + getQuantity() + "]] \n";
	}
	
	
}
