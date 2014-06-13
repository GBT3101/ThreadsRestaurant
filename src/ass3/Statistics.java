package ass3;

import java.util.Vector;

public class Statistics {
	
	/********* Fields *********/
	private static double _moneyGained;
	private static Vector<Double> _actualRewards;
	private static Vector<Double> _expectedRewards;
	private static Vector<Order> _deliveredOrders;
	private static Vector<Ingredient> _cosumedIngredients;
	private static Vector<Chef> _chefs;
	private static Vector<DeliveryPerson> _deliveryPersons;
	
	/********* Constructors *********/
	public Statistics(){
		_moneyGained=0;
		_actualRewards = new Vector<Double>();
		_expectedRewards = new Vector<Double>();
		_deliveredOrders = new Vector<Order>();
		_cosumedIngredients = new Vector<Ingredient>();
	}
	
	/********* Getters/Setters *********/
	public static void setDeiveryPersons(Vector<DeliveryPerson> deliveryPersons){
		_deliveryPersons = deliveryPersons;
	}
	
	public static void setChefs(Vector<Chef> chefs){
		_chefs = chefs;
	}
	
	/********* Methods *********/
	public static void printExpectedRewards(){
		System.out.println(_expectedRewards.toString());
	}
	
	public static void printActualRewards(){
		System.out.println(_actualRewards.toString());
	}

	public static void addReward(double reward) {
		_moneyGained+=reward;
	}
	
	public static void addActualReward(Double reward) {
		_actualRewards.add(reward);
	}
	
	public static void addExpectedReward(Double reward) {
		_expectedRewards.add(reward);
	}
	
	public static void addDeliveredOrder(Order order){
		_deliveredOrders.add(order);
	}
	
	public static void addConsumedIngredient(Ingredient ing){
		_cosumedIngredients.add(ing);
	}
	
	public String toString(){
		String ans="";
		ans="Statistics: Money Gained="+_moneyGained+"\n";
		
		ans+="Expected Rewards: ";
		for(Double expectedReward : _expectedRewards){
			ans+="["+expectedReward+"]";
		}
		
		ans+="\n Received Rewards: ";
		for(Double actualReward : _actualRewards){
			ans+="["+actualReward+"]";
		}
		
		
		ans+="\n Delivered Orders: \n";
		for(Order order : _deliveredOrders){
			ans+=order.toString();
		}
		
		ans+="\n Consumed Ingredient: \n";
		for(Ingredient ing : _cosumedIngredients){
			ans+=ing.toString();
		}
		
		ans+="\n The Chefs: \n";
		for(Chef chef : _chefs){
			ans+=chef.toString();
		}
		
		ans+="\n The Delivery Guys: \n";
		for(DeliveryPerson deliveryPerson : _deliveryPersons){
			ans+=deliveryPerson.toString();
		}
		
		return ans;
	}
	
	
	
	
}


