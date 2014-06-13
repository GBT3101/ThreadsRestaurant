package ass3;

import java.io.File;
import java.util.Vector;


public class Driver {

	public static void main(String [] args){
		System.out.println(args[0]);
		File menuXML= new File(args[1]);
		Menu menu = InputParser.parseMenu(menuXML); // Parsing the menu.
		File orderListXML= new File(args[2]);
		Vector<Order> orders = InputParser.parseOrders(menu, orderListXML); // Parsing the orders
		File initialDataXML = new File(args[0]);
		Management management = InputParser.parseInitialData(initialDataXML, menu, orders); // parsing the data
		
		management.giveOrders(); // Initiate the program by telling the management to give orders to the chefs.

	}
	
	
}
