package ass3;

import java.util.ArrayList;

public class Menu {

	/********* Fields *********/
	private ArrayList<Dish> _dishes;

	/********* Constructors *********/
	public Menu(ArrayList<Dish> dishes) {
		this._dishes = dishes;
	}

	/********* Getters/Setters *********/
	public Dish getDish(String dishName) { // get a dish from the menu.
		for (int i = 0; i < _dishes.size(); i++) {
			if (_dishes.get(i).getName().equals(dishName)) {
				return _dishes.get(i);
			}
		}

		return null;

	}

}
