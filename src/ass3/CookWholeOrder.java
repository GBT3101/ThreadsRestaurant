package ass3;

import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CookWholeOrder implements Callable<Order> {
	
	/********* Fields *********/
	
	private static final Logger logger = LogManager.getLogger(Statistics.class.getName());
	private Warehouse _warehouse;
	private Order _order;
	private Vector<CookOneDish> _RunnableDishes;
	private ExecutorService _executor;
	private Chef _responsibleChef;
	private CountDownLatch m_latchObject;
	
	/********* Constructors *********/
	
	public CookWholeOrder(Order order, Warehouse warehouse, Chef chef){
		this._warehouse = warehouse;
		this._order = order;
		this._responsibleChef = chef;
		_executor = Executors.newCachedThreadPool();
		initLatch(); // Initial the CountDownLatch
		_RunnableDishes = new Vector<CookOneDish>();
	}
	
	/********* Methods *********/
	
	public void initLatch(){ // To initial the counter by the number of dishes.
		int counter=0;
		for(int i=0; i<_order.getDishesSize(); i++){
			counter+= _order.getDish(i).getQuantityOfDish();
		}
		m_latchObject = new CountDownLatch (counter);
	}
	
	public void createThreads(){ // Creating threads to each dish, and adds it to the vector.
		for(int i=0; i<_order.getDishesSize(); i++){
			for(int j=0; j<_order.getDish(i).getQuantityOfDish(); j++){
				_RunnableDishes.add(new CookOneDish(_order.getDish(i).getDish(), _warehouse, _responsibleChef, m_latchObject ));
			}
		}
	}
	
	public void executeDishes(){ // Executing all the dishes.
		for(CookOneDish dish : _RunnableDishes){
			_executor.execute(dish);
		}
	}
	
	

	@Override
	public Order call() throws Exception {
		_order.setStatus(OrderStatus.IN_PROGRESS);
		//logger message for an order being cooked.
        logger.info("ORDER {} IS NOW BEING COOKED", _order.getID());
		createThreads(); // Prepare the threads.
		executeDishes(); // Execute the threads (dishes).
		
		Date date = new Date(); //SAVE DATE_BeginCooking
		long timeBefore = date.getTime();
		
		m_latchObject.await(); // The Order is now being Cooked.

		long timeAfter = date.getTime(); //SAVE DATE_EndingCooking
		
		_order.setStatus(OrderStatus.COMPLETE);
		//logger message for an order being completed.
        logger.info("ORDER {} HAS BEEN COMPLETED", _order.getID());
		
        _order.setTotalActualCookingTime(timeAfter - timeBefore); // sets the actual cooking time to the order.
		_executor.shutdown(); // shut down all the dishes.
		
		return _order;
	}

}
