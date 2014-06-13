package ass3;
import java.util.Vector;

public class Dish {
	
	/********* Fields *********/
	
	private String _dishName;
	private long _dishCookTime;
	private Vector<Ingredient> _ingVector;
	private Vector<KitchenTool> _toolVector;
	private int _difficultyRating;
	private int _reward;
	
	/********* Constructors *********/
	
	public Dish(String dishName, long dishCookTime, Vector<Ingredient> ingVector, Vector<KitchenTool> toolVector, int difficultyRating, int reward){
		this._dishName = dishName;
		this._dishCookTime = dishCookTime;
		this._ingVector = ingVector;
		this._toolVector = toolVector;
		this._difficultyRating = difficultyRating;
		this._reward = reward;
	}
	
	/********* Getters/Setters *********/
	
	public String getName(){
		return this._dishName;
	}
	
	public int getReward(){
		return this._reward;
	}
	
	public int getDifficultyRating(){
		return this._difficultyRating;
	}
	
	public long getCookTime(){
		return this._dishCookTime;
	}
	
	public Ingredient getIng(int i){
		return _ingVector.elementAt(i);
	}
	
	public KitchenTool getTool(int i){
		return _toolVector.elementAt(i);
	}
	
	public int getIngSize(){
		return _ingVector.size();
	}
	
	public int getToolSize(){
		return _toolVector.size();
	}
	
	/********* Methods *********/
	
	public void acquireTools(Warehouse warehouse){ // Acquire tools from the warehouse.
		warehouse.takeKitchenTools(_toolVector);
	}
	
	public String toString(){
		String ans="";
		ans+="[Dish: [Name: " + getName() +"][Expected Cook Time="+_dishCookTime+"][Difficulty Rating="+getDifficultyRating()+"][Cost="+getReward()+"]";
		ans+="[Required Ingredients: ";
		for(Ingredient ing : _ingVector){
			ans+="["+ing.getName()+"="+ing.getQuantity()+"]";
		}
		ans+="][Required Tools: ";
		for(KitchenTool tool : _toolVector){
			ans+="["+tool.getName()+"="+tool.getQuantity()+"]";
		}
		ans+="]]";
		return ans;
	}
	
	
	
	
	
}
