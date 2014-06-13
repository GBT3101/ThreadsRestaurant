package ass3;

public class OrderOfDish {

	/********* Fields *********/
	private Dish dish;
	private int quantityOfDish;
	private double reward;
	
	/********* Constructors *********/
	public OrderOfDish(Dish dish, int quantityOfDish) {
		this.dish = dish;
		this.quantityOfDish = quantityOfDish;
		calculateReward();
	}
	
	/********* Getters/Setters *********/
	public Dish getDish() {
		return dish;
	}
	
	public double getReward(){
		return reward;
	}
	
	public int getDifficulty(){
		return dish.getDifficultyRating();
	}
	
	public int getQuantityOfDish(){
		return quantityOfDish;
	}
	
	/********* Methods *********/
	public void calculateReward(){ // calculating the total reward, summing the dishes.
		reward = dish.getReward() * quantityOfDish;
	}
	
	public String toString(){
		String ans="";
		ans+="[Order Of Dish: [Quantity="+getQuantityOfDish()+"][Cost="+reward+"]";
		ans+=dish.toString();
		
		return ans;
	}
	
}