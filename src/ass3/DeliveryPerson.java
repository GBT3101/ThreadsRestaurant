package ass3;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeliveryPerson implements Runnable {

	private static final Logger logger = LogManager.getLogger(Statistics.class
			.getName());

	/********* Fields *********/
	private String _name;
	private int _speed;
	private Address _restaurantAddress;
	private LinkedBlockingQueue<Order> _DeliveryQueue;
	private CountDownLatch m_latchObject;

	/********* Constructors *********/
	public DeliveryPerson(String name, Address restaurantAddress, int speed) {
		this._name = name;
		this._speed = speed;
		this._restaurantAddress = restaurantAddress;
	}

	/********* Getters/Setters *********/
	private String getName() {
		return _name;
	}

	private int getSpeed() {
		return _speed;
	}

	public void setQueue(LinkedBlockingQueue<Order> que) {
		this._DeliveryQueue = que;
	}

	public void setLatch(CountDownLatch latchObject) {
		this.m_latchObject = latchObject;
	}
	
	/********* Methods *********/

	public double calculateDistance(Order order) { // Calculate the distance between the restaurant to the order's address.
		return _restaurantAddress.distanceBetween(order.getAddress());
	}

	@Override
	public void run() {
		while (!(Thread.currentThread().isInterrupted()) || _DeliveryQueue.size() != 0) {
			// Proceed as long as there was no shut down request OR there are still deliveries waiting in the queue.
			try {
				Order order = _DeliveryQueue.take(); // take an order from the delivery queue, or wait until there is one.
				long currentDistance = Math.round(calculateDistance(order)); // calculate distance.
				long deliveryTime = currentDistance / _speed; // calculate Expected delivery time.
				
				Date date = new Date(); //SAVE DATE_BeginDelivery
				long timeBefore = date.getTime();
				
				// Logger message for an order being delivered.
				logger.info("ORDER {} IS BEING DELIVERD", order.getID());
				Thread.sleep(deliveryTime); // deliver the order
				// Logger message for an order has completed delivery.
				logger.info("ORDER {} HAS BEEN DELIVERED", order.getID());
				
				long timeAfter = date.getTime(); //SAVE DATE_EndingDelivery
				
				order.setStatus(OrderStatus.DELIVERED);
				order.setExpectedDeliveryTime(deliveryTime);
				order.setTotalActualDeliveryTime(timeAfter - timeBefore);

				Statistics.addExpectedReward(new Double(order.getReward())); // add an expected reward
				Statistics.addDeliveredOrder(order); // add the order to the statistics.
				
				if (order.getTotalActualCookingTime()+ order.getTotalActualDeliveryTime() < 1.15 * (order.getExpectedCookingTime() + deliveryTime)) {
					// The order has been cooked and delivered on time, the reward is full.
					Statistics.addReward(order.getReward());
					Statistics.addActualReward(new Double(order.getReward()));
					order.setActualReward(order.getReward());
				} else { // the order has been cooked and delivered late, the reward has been cut in half.
					Statistics.addReward(order.getReward()/2);
					Statistics.addActualReward(new Double(order.getReward()/2));
					order.setActualReward(order.getReward()/2);
				}

				m_latchObject.countDown(); // The order has been delievered.
				Thread.sleep(deliveryTime); // go back to restaurant

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}
	}

	public String toString() {
		return "Delivery Person: [name=" + getName() + "][speed=" + getSpeed()+ "] \n";
	}

}