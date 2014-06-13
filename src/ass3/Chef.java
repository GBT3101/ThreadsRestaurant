package ass3;

import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chef implements Runnable, Comparable<Chef> {
	
	/********* Fields *********/
	
	private static final Logger logger = LogManager.getLogger(Statistics.class.getName());
	private String _chefName;
	private double _efficiencyRating;
	private int _enduranceRating;
	private int _currentPressure;
	private Vector<Future<Order>> _futureOrders; // Vector for the future orders.
	private ExecutorService _executor; // This is the executor for the Callable CookWholeDish.
	private ExecutorCompletionService<Order> _superExecutor; // This smart executor is to know when the future Object "is done".
	private Management _management; // Reference to the management.
	private boolean _shutDown;
	
	/********* Constructors *********/

	public Chef(String name, double efficiencyRating, int enduranceRating) {
		_shutDown = false;
		this._chefName = name;
		this._efficiencyRating = efficiencyRating;
		this._enduranceRating = enduranceRating;
		_currentPressure = 0;
		_executor = Executors.newCachedThreadPool();
		_futureOrders = new Vector<Future<Order>>();
		_superExecutor = new ExecutorCompletionService<Order>(_executor);
	}

	/********* Getters/Setters *********/
	
	public double getEfficiency() {
		return _efficiencyRating;
	}

	public String getName() {
		return _chefName;
	}

	public void setManagement(Management m) {
		this._management = m;
	}

	public int getEnduranceRating() {
		return _enduranceRating;
	}

	public int getCurrentPressure() {
		return _currentPressure;
	}

	/********* Methods *********/
	
	public void shutDown(){ // Request a shut down from the chef.
		_shutDown = !_shutDown;
	}
	
	public void raisePressure(int difficultyRating) {
		_currentPressure += difficultyRating;
	}

	public void decreasePressure(int difficultyRating) {
		_currentPressure -= difficultyRating;
	}

	public boolean possibleToAcceptOrder(Order order) { // Check if it is possible to accept the Order.
		return order.getDifficultyRating() <= _enduranceRating - _currentPressure;
	}

	public void acceptOrder(Order order, Warehouse warehouse) {
		CookWholeOrder cookingOrder = new CookWholeOrder(order, warehouse, this); // Creating the Callable.
		raisePressure(order.getDifficultyRating());
		_futureOrders.add(_superExecutor.submit(cookingOrder)); // Submitting the Callable, and adding the future object to the vector.
		// Logger message for accepting new order.
        logger.info("ORDER {} HAS BEEN ACCEPTED", order.getID());
	}

	public void releaseOrder(Order order) { // Release the order back to the management.
		_management.getOrder(order);
		decreasePressure(order.getDifficultyRating());
		// Logger message for completing an order.
        logger.info(" ORDER {} IS COMPLETED", order.getID());
	}

	@Override
	public void run() {

			while (!_shutDown || _futureOrders.size() > 0) { // Procceed if there's no shut down request OR there are still orders being cooked.

				try {
					Future<Order> futureOrder = _superExecutor.take(); // Waiting until the future object is done, then take it.

					_futureOrders.remove(futureOrder); // remove the future order from the vector.

					try {
						releaseOrder(futureOrder.get()); // Release the order back to the management.
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					_management.notifyThis(); // notify Management, so he knows a chef has done his work.

				} catch (InterruptedException e) {	}
			}
			_executor.shutdown(); // shut down the Callable Cook Whole Order.

		//}
	}

	@Override
	public int compareTo(Chef o) { // In order to sort Chefs.
		return (_enduranceRating - _currentPressure)
				- (o.getEnduranceRating() - o.getCurrentPressure());
	}
	
	public String toString() {
		return "Chef: [name=" + getName() + "][endurance rating=" +
	getEnduranceRating() + "][efficiency rating=" + getEfficiency() + "] \n"; 
	}
	
	



	

}