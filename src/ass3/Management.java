package ass3;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Management {
	
	private static final Logger logger = LogManager.getLogger(Statistics.class.getName());

	/********* Fields *********/
	private Warehouse _warehouse;
	private Vector<Chef> _chefs;
	private Vector<DeliveryPerson> _deliveryPersons;
	private LinkedBlockingQueue<Order> _deliveriesQueue;
	private List<Order> _cookingOrders;
	ExecutorService _cookingExecutor;
	ExecutorService _deliveryExecutor;
	private CountDownLatch m_latchObject;
	private final Object LOCK = new Object();
	private Statistics _statistics;

	/********* Constructors *********/
	public Management(Warehouse warehouse, Vector<Chef> chefs,
			Vector<DeliveryPerson> deliveryPersons, List<Order> cookingOrders,
			Menu menu) {
		m_latchObject = new CountDownLatch(cookingOrders.size());
		this._warehouse = warehouse;
		this._chefs = chefs;
		this._deliveryPersons = deliveryPersons;
		this._deliveriesQueue = new LinkedBlockingQueue<Order>();
		this._cookingOrders = cookingOrders;
		setUpChefs(); // give the chefs a reference to management.
		setUpDeliveryPersons(); // give the delivery persons a reference to the deilvery queue, and the counter.
		_cookingExecutor = Executors.newFixedThreadPool(chefs.size());
		_deliveryExecutor = Executors.newFixedThreadPool(deliveryPersons.size());
		executeChefs();
		executeDeliveryPersons();
		this._statistics = new Statistics();
		Statistics.setChefs(chefs); 
		Statistics.setDeiveryPersons(deliveryPersons); 

	}
	
	/********* Methods *********/

	public void setUpChefs() {
		for (Chef chef : _chefs) {
			chef.setManagement(this);
		}
	}

	public void setUpDeliveryPersons() {
		for (DeliveryPerson deliveryPerson : _deliveryPersons) {
			deliveryPerson.setQueue(_deliveriesQueue);
			deliveryPerson.setLatch(m_latchObject);
		}
	}

	public void executeChefs() { // start running the chefs.
		for (Chef chef : _chefs)
			_cookingExecutor.execute(chef);
	}

	public void executeDeliveryPersons() { // start running the delivery guys.
		for (DeliveryPerson deliveryPerson : _deliveryPersons)
			_deliveryExecutor.execute(deliveryPerson);
	}

	public void giveOrders() {
		synchronized (LOCK) {
			while (_cookingOrders.size() > 0) { // as long as there are orders to cook.
				Iterator<Order> iteratorOrder = _cookingOrders.iterator();
				Collections.sort(_chefs); // sort the chefs, to get the most suitable chef to cook.
				while (iteratorOrder.hasNext()) {
					Order order = iteratorOrder.next();
					for (Chef chef : _chefs) {
						if (chef.possibleToAcceptOrder(order)) { // make sure the best chef can get the order.
							chef.acceptOrder(order, _warehouse); // give the order to the chef.
							iteratorOrder.remove(); // remove the order from the cooking iterator.
							break; // a chef got the order, break the loop.
						}
					}
				}
				try {
					LOCK.wait(); // no chef can get the order, wait for one to open the lock.
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		try {
			m_latchObject.await(); // wait for the delivery guys to deliver all the orders.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		shutDownEverything(); // all the order have been cooked and delivered, shut down the program.
		_warehouse.giveCosumedIngredientToStatistics(); // update consumed ingredients.
		//Log the entire statistics.
		logger.info(_statistics.toString());
		
	}

	public void getOrder(Order order) { // Get a complete order from the chef, and add it to the delivery queue.
		_deliveriesQueue.add(order);

	}

	public void shutDownEverything() {
		// Logger message for Shutting down the program.
        logger.info("THE SYSTEM HAS BEEN SHUT DOWN !");
        
		_deliveryExecutor.shutdownNow(); // shut down the delivery guys.
		for (Chef chef : _chefs) { // shut down the chefs.
			chef.shutDown();
		}
		_cookingExecutor.shutdownNow(); // shut down the cooking executor.
	}

	public void notifyThis() { // Notify the lock, continue to looking for chefs.
		synchronized (LOCK) {
			LOCK.notify();
		}
	}

}