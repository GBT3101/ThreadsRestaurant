package ass3;

import java.util.concurrent.CountDownLatch;

public class CookOneDish implements Runnable {
	
	/********* Fields *********/
	
	private Dish _dish;
	private Warehouse _warehouse; // Reference to Warehouse.
	private long _cookTime;
	
	private CountDownLatch m_latchObject;
	
	/********* Constructors *********/
	
	public CookOneDish(Dish dish, Warehouse warehouse, Chef chef, CountDownLatch latchObject){
		this._dish = dish;
		this._warehouse = warehouse;
		this.m_latchObject = latchObject;
		_cookTime = Math.round(dish.getCookTime() * chef.getEfficiency()); // calculating the cooking time.
	}
	
	/********* Methods *********/
	
	public void acquireIngredients(){ // By using the warehouse reference, acquire the ingredients needed.
		for(int i=0; i<_dish.getIngSize(); i++){
			_warehouse.takeIngredient(_dish.getIng(i));
		}
	}
	
	public void acquireTools(){// By using the warehouse reference, acquire the tools needed.
		_dish.acquireTools(_warehouse); // to hide the implementation of the tools vector in Dish.
		// There is also a Re_Source ordering to avoid a dead lock when acquiring Tools.
	}
	
	public void returnTools(){ // Return the tools to the warehouse.
		for(int i=0; i<_dish.getToolSize(); i++){
			_warehouse.returnKitchenTool(_dish.getTool(i));
		}
		
	}
	
	@Override
	public void run() {
		acquireIngredients(); // Acquire the needed ingredients.
		acquireTools(); // Acquire the needed tools.
		try {
			Thread.sleep(_cookTime); // Cook.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		returnTools(); // Return the used tools.
		m_latchObject.countDown(); // Update the counter, the dish has been cooked.
		
	}

}
