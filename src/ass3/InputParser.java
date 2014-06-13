package ass3;

import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

/*
 * Parser - a class that the driver class uses to parse and construct initial information and start the simulation
 * @author Gil Yitzhak & 	Guy Ben-Moshe.
 */
public class InputParser {

	public static Management parseInitialData(File initialDataXML, Menu menu,
			Vector<Order> orders) {
		try {
			// Creating a Java DOM XML Parser
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(initialDataXML);
			doc.getDocumentElement().normalize();
			// Calling the root of the document
			Element restaurantElement = doc.getDocumentElement();

			return new Management(warehouseParsing(restaurantElement),
					chefsParsing(restaurantElement), deliveryPersonsParsing(
							restaurantElement,
							addressParsing(restaurantElement)), orders, menu);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Address addressParsing(Element restaurantElement) {
		// Address Parsing Section
		NodeList addressList = restaurantElement
				.getElementsByTagName("Address");
		Node addressNode = addressList.item(0);
		if (addressNode.getNodeType() == Node.ELEMENT_NODE) {
			Element addressElement = (Element) addressNode;
			int tempX = Integer.parseInt(addressElement
					.getElementsByTagName("x").item(0).getTextContent());
			int tempY = Integer.parseInt(addressElement
					.getElementsByTagName("y").item(0).getTextContent());

			// System.out.println("Restaurant Address: [" + tempX + "," + tempY
			// + "]");

			return new Address(tempX, tempY);
		} // End of Parsing Section.
		return null;
	}

	private static Warehouse warehouseParsing(Element restaurantElement) {
		// Repository Parsing Section
		NodeList repositoryList = restaurantElement
				.getElementsByTagName("Repository");
		Node repositoryNode = repositoryList.item(0);
		ArrayList<KitchenTool> kitchenTools = new ArrayList<KitchenTool>();
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		if (repositoryNode.getNodeType() == Node.ELEMENT_NODE) {
			Element repositoryElement = (Element) repositoryNode;

			// Kitchen Tools Parsing Section
			NodeList toolsList = repositoryElement
					.getElementsByTagName("KitchenTool");
			for (int i = 0; i < toolsList.getLength(); i++) {
				Node tempToolNode = toolsList.item(i);
				if (tempToolNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tempToolElement = (Element) tempToolNode;
					String tempKitchenToolName = tempToolElement
							.getElementsByTagName("name").item(0)
							.getTextContent();
					int tempKitchenToolQuantity = Integer
							.parseInt(tempToolElement
									.getElementsByTagName("quantity").item(0)
									.getTextContent());
					kitchenTools.add(new KitchenTool(tempKitchenToolName,
							tempKitchenToolQuantity));
				}
			} // End of Kitchen Tools Parsing Section

			// Ingredients Parsing Section
			NodeList ingredientsList = repositoryElement
					.getElementsByTagName("Ingredient");
			for (int i = 0; i < ingredientsList.getLength(); i++) {
				Node tempIngredientNode = ingredientsList.item(i);
				if (tempIngredientNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tempIngredientElement = (Element) tempIngredientNode;
					String tempIngredientName = tempIngredientElement
							.getElementsByTagName("name").item(0)
							.getTextContent();
					int tempIngredientQuantity = Integer
							.parseInt(tempIngredientElement
									.getElementsByTagName("quantity").item(0)
									.getTextContent());
					ingredients.add(new Ingredient(tempIngredientName,
							tempIngredientQuantity));
				}
			} // End of Ingredients Parsing Section.
		} // End of Repository Parsing Section.

		return new Warehouse(ingredients, kitchenTools);
	}

	private static Vector<Chef> chefsParsing(Element restaurantElement) {
		// Staff Parsing Section
		NodeList staffList = restaurantElement.getElementsByTagName("Staff");
		Node staffNode = staffList.item(0);
		Vector<Chef> chefs = new Vector<Chef>();
		if (staffNode.getNodeType() == Node.ELEMENT_NODE) {
			Element staffElement = (Element) staffNode;

			// Chefs Parsing Section
			NodeList chefsList = staffElement.getElementsByTagName("Chef");
			for (int i = 0; i < chefsList.getLength(); i++) {
				Node tempChefNode = chefsList.item(i);
				if (tempChefNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tempChefElement = (Element) tempChefNode;
					String tempChefName = tempChefElement
							.getElementsByTagName("name").item(0)
							.getTextContent();
					double tempEfficiencyRating = Double
							.parseDouble(tempChefElement
									.getElementsByTagName("efficiencyRating")
									.item(0).getTextContent());
					int tempEnduranceRating = Integer.parseInt(tempChefElement
							.getElementsByTagName("enduranceRating").item(0)
							.getTextContent());
					chefs.add(new Chef(tempChefName, tempEfficiencyRating,
							tempEnduranceRating));
				}
			} // End of Chefs Parsing Section
		}// End of Staff Parsing Section.

		return chefs;
	}

	private static Vector<DeliveryPerson> deliveryPersonsParsing(
			Element restaurantElement, Address restaurantAddress) {
		// Staff Parsing Section
		NodeList staffList = restaurantElement.getElementsByTagName("Staff");
		Node staffNode = staffList.item(0);
		Vector<DeliveryPerson> deliveryPersons = new Vector<DeliveryPerson>();
		if (staffNode.getNodeType() == Node.ELEMENT_NODE) {
			Element staffElement = (Element) staffNode;
			// Delivery Persons Parsing Section
			NodeList deliveryPersonsList = staffElement
					.getElementsByTagName("DeliveryPerson");
			for (int i = 0; i < deliveryPersonsList.getLength(); i++) {
				Node tempDeliveryPersonNode = deliveryPersonsList.item(i);
				if (tempDeliveryPersonNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tempDeliveryPersonElement = (Element) tempDeliveryPersonNode;
					String tempDeliveryPersonName = tempDeliveryPersonElement
							.getElementsByTagName("name").item(0)
							.getTextContent();
					int tempDeliveryPersonSpeed = Integer
							.parseInt(tempDeliveryPersonElement
									.getElementsByTagName("speed").item(0)
									.getTextContent());
					deliveryPersons.add(new DeliveryPerson(
							tempDeliveryPersonName, restaurantAddress,
							tempDeliveryPersonSpeed));
				}
			} // End of Delivery Persons Parsing Section.
		}// End of Staff Parsing Section.

		return deliveryPersons;
	}

	public static Menu parseMenu(File menuXML) {
		try {
			ArrayList<Dish> dishes = new ArrayList<Dish>();
			// Creating a Java DOM XML Parser
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(menuXML);
			doc.getDocumentElement().normalize();
			// Calling the root of the document
			Element menuElement = doc.getDocumentElement();
			NodeList dishesList = menuElement.getElementsByTagName("Dish");
			for (int i = 0; i < dishesList.getLength(); i++) {
				Node dishNode = dishesList.item(i);
				if (dishNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tempDishElement = (Element) dishNode;
					String tempDishName = tempDishElement
							.getElementsByTagName("name").item(0)
							.getTextContent();
					int tempDifficultyRating = Integer.parseInt(tempDishElement
							.getElementsByTagName("difficultyRating").item(0)
							.getTextContent());
					long tempExpectedCookTime = Long.parseLong(tempDishElement
							.getElementsByTagName("expectedCookTime").item(0)
							.getTextContent());
					int tempReward = Integer.parseInt(tempDishElement
							.getElementsByTagName("reward").item(0)
							.getTextContent());

					// Kitchen Tools Parsing Section
					Vector<KitchenTool> tempKitchenTools = new Vector<KitchenTool>();
					NodeList toolsList = tempDishElement
							.getElementsByTagName("KitchenTool");
					for (int j = 0; j < toolsList.getLength(); j++) {
						Node tempToolNode = toolsList.item(j);
						if (tempToolNode.getNodeType() == Node.ELEMENT_NODE) {
							Element tempToolElement = (Element) tempToolNode;
							String tempKitchenToolName = tempToolElement
									.getElementsByTagName("name").item(0)
									.getTextContent();
							int tempKitchenToolQuantity = Integer
									.parseInt(tempToolElement
											.getElementsByTagName("quantity")
											.item(0).getTextContent());
							tempKitchenTools.add(new KitchenTool(
									tempKitchenToolName,
									tempKitchenToolQuantity));
						}
					} // End of Kitchen Tools Parsing Section

					// Ingredients Parsing Section
					Vector<Ingredient> tempIngredients = new Vector<Ingredient>();
					NodeList ingredientsList = tempDishElement
							.getElementsByTagName("Ingredient");
					for (int j = 0; j < ingredientsList.getLength(); j++) {
						Node tempIngredientNode = ingredientsList.item(j);
						if (tempIngredientNode.getNodeType() == Node.ELEMENT_NODE) {
							Element tempIngredientElement = (Element) tempIngredientNode;
							String tempIngredientName = tempIngredientElement
									.getElementsByTagName("name").item(0)
									.getTextContent();
							int tempIngredientQuantity = Integer
									.parseInt(tempIngredientElement
											.getElementsByTagName("quantity")
											.item(0).getTextContent());
							tempIngredients
									.add(new Ingredient(tempIngredientName,
											tempIngredientQuantity));
						}
					} // End of Ingredients Parsing Section.

					dishes.add(new Dish(tempDishName, tempExpectedCookTime,
							tempIngredients, tempKitchenTools,
							tempDifficultyRating, tempReward));
				}
			} // End of Chefs Parsing Section.

			return new Menu(dishes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Vector<Order> parseOrders(Menu menu, File orderListXML) {
		try {
			Vector<Order> orders = new Vector<Order>();

			// Creating a Java DOM XML Parser
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(orderListXML);
			doc.getDocumentElement().normalize();

			// Calling the root of the document
			Element ordersListElement = doc.getDocumentElement();
			NodeList ordersList = ordersListElement
					.getElementsByTagName("Order");
			Node ordersNode = ordersList.item(0);
			if (ordersNode.getNodeType() == Node.ELEMENT_NODE) {

				for (int i = 0; i < ordersList.getLength(); i++) {
					Node orderNode = ordersList.item(i);
					if (orderNode.getNodeType() == Node.ELEMENT_NODE) {
						Element tempOrderElement = (Element) orderNode;
						String tempOrderName = tempOrderElement
								.getAttribute("id");
						Address tempDeliveryAddress = new Address();
						// Address Parsing Section
						NodeList addressList = tempOrderElement
								.getElementsByTagName("DeliveryAddress");
						Node addressNode = addressList.item(0);
						if (addressNode.getNodeType() == Node.ELEMENT_NODE) {
							Element addressElement = (Element) addressNode;
							int tempX = Integer.parseInt(addressElement
									.getElementsByTagName("x").item(0)
									.getTextContent());
							int tempY = Integer.parseInt(addressElement
									.getElementsByTagName("y").item(0)
									.getTextContent());
							tempDeliveryAddress = new Address(tempX, tempY);
						} // End of Parsing Section.
							// Order of Dishes Parsing Section.
						NodeList dishesList = tempOrderElement
								.getElementsByTagName("Dish");
						Vector<OrderOfDish> tempDishesOrder = new Vector<OrderOfDish>();
						for (int j = 0; j < dishesList.getLength(); j++) {
							Node dishNode = dishesList.item(j);
							if (dishNode.getNodeType() == Node.ELEMENT_NODE) {
								Element tempDishElement = (Element) dishNode;
								String tempDishName = tempDishElement
										.getElementsByTagName("name").item(0)
										.getTextContent();
								int tempDishQuantity = Integer
										.parseInt(tempDishElement
												.getElementsByTagName(
														"quantity").item(0)
												.getTextContent());
								tempDishesOrder.add(new OrderOfDish(menu
										.getDish(tempDishName),
										tempDishQuantity));
							}
						}// End of Order of Dishes Parsing Section.
						orders.add(new Order(tempOrderName, tempDishesOrder,
								tempDeliveryAddress));
					}
				} // End of Chefs Parsing Section.

			}

			return orders;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
