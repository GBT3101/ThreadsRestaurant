package ass3;

import java.util.Vector;

public class Order {

	/********* Fields *********/
	private String _orderID;
	private int _difficultyRating;
	private Vector<OrderOfDish> _orderOfDishes;
	private Address _costumerAddress;
	private OrderStatus _status;
	private double _reward;
	private double _actualReward;
	private long _totalActualCookingTime;
	private long _totalActualDeliveryTime;
	private long _expectedCookingTime;
	private long _expectedDeliveryTime;

	/********* Constructors *********/
	public Order(String orderID, Vector<OrderOfDish> orderOfDishes,
			Address costumerAddress) {
		this._orderID = orderID;
		this._status = OrderStatus.INCOMPLETE;
		this._orderOfDishes = orderOfDishes;
		this._costumerAddress = costumerAddress;
		calculateDifficultyRating();
		calculateReward();
	}
	
	
	
	/********* Getters/Setters *********/
	
	public double getReward(){
		return _reward;
	}

	public void setStatus(OrderStatus status) {
		this._status = status;
	}
	
	public void setActualReward(double reward){
		this._actualReward = reward;
	}
	
	public void setExpectedDeliveryTime(long expectedDeliveryTime){
		this._expectedDeliveryTime = expectedDeliveryTime;
	}

	public int getDishesSize() {
		return _orderOfDishes.size();
	}

	public OrderOfDish getDish(int i) {
		return _orderOfDishes.elementAt(i);
	}

	public OrderStatus getStatus() {
		return _status;
	}

	public String getID() {
		return _orderID;
	}

	public int getDifficultyRating() {
		return _difficultyRating;
	}
	
	public long getExpectedCookingTime() {
		return _expectedCookingTime;
	}

	public Address getAddress() {
		return _costumerAddress;
	}
	
	public long getTotalActualCookingTime() {
		return _totalActualCookingTime;
	}

	public void setTotalActualCookingTime(long totalActualCookingTime) {
		this._totalActualCookingTime = totalActualCookingTime;
	}

	public long getTotalActualDeliveryTime() {
		return _totalActualDeliveryTime;
	}

	public void setTotalActualDeliveryTime(long totalActualDeliveryTime) {
		this._totalActualDeliveryTime = totalActualDeliveryTime;
	}
	
	/********* Methods *********/

	private void calculateReward(){ // calculating the reward by summing all the dishes.
		_reward = 0;
		for(OrderOfDish orderOfDish : _orderOfDishes){
			_reward+=orderOfDish.getReward();
		}
	}
	
	private void calculateDifficultyRating() { 
		// calculating the difficulty rating by choosing the dish with the highest difficulty rating.
		_difficultyRating = 0;
		int max = 0;
		for (OrderOfDish dish : _orderOfDishes) {
			if (max < dish.getDifficulty()) {
				max = dish.getDifficulty();
			}
		}
		_difficultyRating = max;
	}
	
	public void calculateExpectedCookingTime(){
		// calculating the expected cooking time by choosing the dish with the highest cooking time.
		long max = 0;
		for (OrderOfDish dish : _orderOfDishes) {
			if (max < dish.getDish().getCookTime()) {
				max = dish.getDish().getCookTime();
			}
		}
		_expectedCookingTime = max;
	}


	
	public String toString(){
		String ans="";
		ans+="[Order: "+"[id="+getID()+"]["+"status="+getStatus()+"][Order Difficulty="+getDifficultyRating()+"]"+_costumerAddress.toString()+
				"[Expected Delivery Time="+_expectedDeliveryTime+"][Delivery Time="+_totalActualDeliveryTime+"][Expected Cooking Time="+_expectedCookingTime+"]"
						+ "[Actual Cooking Time="+_totalActualCookingTime+"][Order Cost="+
				getReward()+"][Recieved Reward="+_actualReward+"] \n";
		for(OrderOfDish orderOfDish : _orderOfDishes){
			ans+=orderOfDish.toString()+"\n";
		}
		ans+="]";
		
		return ans;
	}

}
